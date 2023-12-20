package marvin.web.application.rest.printer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class RetrieveWaitingPosition extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt((String) req.getParameter("id"));
        System.out.println("id for waiting position: " + id);
        
        try {
            System.out.println("waiting for Print.waitingPositionLock");
            synchronized (Print.waitingPositionLock) {
                Print.waitingPositionLock.wait();
            }
            System.out.println("Print.waitingPositionLock notified");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        JSONObject returnData = new JSONObject();
        returnData.put("status", "ok");
        
        returnData.put("position", Print.printQueue.indexOf(id));
        
        resp.getWriter().println(returnData.toString());
    }
}
