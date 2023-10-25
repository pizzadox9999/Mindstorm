package marvin.ms.application;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;


import javax.servlet.Servlet;
import javax.servlet.ServletException;

public class TomcatExecuter implements Runnable {
    private ExecutorService executorService;
    private Tomcat tomcat = null;
    private StandardContext tomcatContext = null;
    private int tomcatServletCounter = 0;
    private boolean run = true;
    private boolean recovery = true;
    private int recoveryCounter;
    
    
    public TomcatExecuter(String contextPath, String webappDirLocation) {
        this(contextPath, webappDirLocation, true, 5);
    }
    
    public TomcatExecuter(String contextPath, String webappDirLocation, boolean recovery, int recoveryCounter) {
        this.recovery = recovery;
        this.recoveryCounter = recoveryCounter;
        tomcat = new Tomcat(); // src/main/webapp/
        
        String webPort = System.getenv("PORT"); // Look for that variable and default to 8080 if it isn't there.
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }

        tomcat.setPort(Integer.valueOf(webPort));

        try {
			tomcatContext = (StandardContext) tomcat.addWebapp(
			        contextPath,
			        new File(webappDirLocation).getAbsolutePath()
			    );
		} catch (ServletException e) {
			e.printStackTrace();
		}
        
        // Servlet 3.0 annotation will work
        /*File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(tomcatContext);
        resources.addPreResources(
                new DirResourceSet(
                        resources,
                        "/WEB-INF/classes",
                        additionWebInfClasses.getAbsolutePath(),
                        "/"
                )
        );
        
        tomcatContext.setResources(resources);*/
    }
    
    public void addServlet(String mapping, Servlet servlet) {
        String name = mapping.substring(mapping.lastIndexOf("/")).toUpperCase() + "_" + (tomcatServletCounter++);
        Tomcat.addServlet(tomcatContext, name, servlet);
        tomcatContext.addServletMapping(mapping, name);
    }

    @Override
    public void run() {
        while (recovery && recoveryCounter > 0 && run) {
            try {
                tomcat.start();
                tomcat.getConnector();
                tomcat.getServer().await();
            } catch (LifecycleException e) {
                System.out.println("Tomcat has experienced an error!");
            }
            recoveryCounter--;
        }
    }
    
    public void stop() {
        run = false;
        try {
            tomcat.stop();
            executorService.shutdownNow();
        } catch (LifecycleException e) {
            //e.printStackTrace();
        }
    }
    
    public void destroy() {
        run = false;
        try {
            tomcat.destroy();
            executorService.shutdownNow();
        } catch (LifecycleException e) {
            //e.printStackTrace();
        }
    }
    
    public void start() {
        run = true;
        executorService = Executors.newFixedThreadPool(1);
        executorService.execute(this);
    }
    
    public void restart() {
        stop();
        start();
    }
}
