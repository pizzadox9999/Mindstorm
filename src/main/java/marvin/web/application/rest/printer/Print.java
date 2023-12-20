package marvin.web.application.rest.printer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import marvin.application.Marvin;
import marvin.application.geom.Shape;
import marvin.application.geom.ShapeFactory;

class ShapeContainer {
    public String id;
    public double[] dataPoints;
    
    public ShapeContainer(String id, double[] dataPoints) {
        this.id = id;
        this.dataPoints = dataPoints;
    }
}

public class Print extends HttpServlet {
    public static final int PRINT_PROTOCOL_VERSION = 1;
    private static int printIDCounter = 0;
    public static ArrayList<Integer> printQueue = new ArrayList<>();
    private static HashMap<Integer, ArrayList<ShapeContainer>> idContainerMap = new HashMap<>();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp); // just for support main is post
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        String printData = req.getParameter("printData");
        //decode json data
        JSONObject jsonPrintData = new JSONObject(printData);
        
        if(jsonPrintData.getInt("version") != PRINT_PROTOCOL_VERSION) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        JSONArray shapeArray = jsonPrintData.getJSONArray("shapeArray");
        
        ArrayList<ShapeContainer> shapeList = new ArrayList<>();
        
        //collect shapes to draw
        for(int i=0; i < shapeArray.length(); i++) {
            JSONObject shape = shapeArray.getJSONObject(i);
            String shapeID = shape.getString("shapeID");
            
            JSONArray jsonDataPoints = shape.getJSONArray("dataPoints");
            double[] dataPoints = new double[jsonDataPoints.length()];

            //collect data points
            for(int j=0; j<jsonDataPoints.length(); j++) {
                dataPoints[j] = jsonDataPoints.getDouble(j);
            }
            
            shapeList.add(new ShapeContainer(shapeID, dataPoints));
        }
        
        int id = printIDCounter ++;
        printQueue.add(id);
        idContainerMap.put(id, shapeList);
        
        synchronized (lock) {
            lock.notify();
        }
        
        
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        
        JSONObject returnData = new JSONObject();
        returnData.put("status", "ok");
        returnData.put("position", printQueue.size() - 1);
        returnData.put("id", id);
        
        System.out.println("print returnData: " + returnData.toString());
        
        resp.getWriter().println(returnData.toString());
    }
    
    
    private static boolean print = true;
    private static Object  lock = new Object();
    public static Thread printThread = new Thread(new Runnable() {
        
        @Override
        public void run() {
            mainLoop: while(print) {
                //if printQueue is empty wait for something to print
                if(printQueue.isEmpty()) {
                    try {
                        System.out.println("waiting for notifiy");
                        synchronized (lock) {
                            lock.wait();
                        }
                        System.out.println("was notified");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //if still empty after awakening stop thread execution
                    if(printQueue.isEmpty()) {
                        break mainLoop;
                    }
                }
                
                
                ArrayList<ShapeContainer> shapeList = null;
                synchronized (printQueue) {
                    int id = printQueue.remove(0);
                    shapeList = idContainerMap.remove(id);
                }
                
                //setup printer
                Marvin.printer.setup();
                Marvin.printer.needConversion(true);
                
                //draw shapes
                for(ShapeContainer shapeContainer : shapeList) {
                    Shape shape = ShapeFactory.createShape(shapeContainer.id, shapeContainer.dataPoints);
                    shape.draw(Marvin.printer);
                }
                
                Marvin.printer.finish();
                System.out.println("Printer should have been finsihed by now!");
                
                //refresh waiting clients
                synchronized (waitingPositionLock) {
                   waitingPositionLock.notifyAll();
                }
                
            }
        }
    });
    
    public static Object waitingPositionLock = new Object();
    
    static {
        printThread.start();
    }
}
