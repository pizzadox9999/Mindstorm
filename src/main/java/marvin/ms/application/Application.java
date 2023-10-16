package marvin.ms.application;

import jakarta.servlet.Servlet;
import marvin.ms.common.test.Tests;

abstract public class Application implements Runnable {
    private TomcatExecuter tomcatExecuter;
    private String tomcatContext = "/";
    
    protected Application(String webappDir) {
        tomcatExecuter = new TomcatExecuter(tomcatContext, webappDir);
    }

    protected void tomcatStart() {
        tomcatExecuter.start();
    }
    
    protected void tomcatStop() {
        tomcatExecuter.stop();
    }
    
    protected void tomcatRestart() {
        tomcatExecuter.restart();
    }
    
    protected void tomcatDestroy() {
        tomcatExecuter.destroy();
    }
    
    protected void startup() {}
    protected void shutudown() {};

    public void start() {
        if(Tests.runTests) {
            Tests.start();
            if(Tests.runTestsExclusive) {
                return;
            }
        }
        System.out.println("Application startup.");
        tomcatStart();
        startup();
        run();
        shutudown();
        tomcatStop();
        tomcatDestroy();
        System.out.println("Application shutdown.");
    }
    
    public void registerServlets(String[] mappings, Servlet[] servlets) {
        if(mappings.length != servlets.length) {
            throw new RuntimeException("Couldn't register servlets, no equals sizes.");
        }
        
        for(int i=0; i<mappings.length; i++) {
            tomcatExecuter.addServlet(mappings[i], servlets[i]);
        }
    }
    
    public void registerServlets(ServletMappingBuilder servletMappingBuilder) {
        registerServlets(servletMappingBuilder.getMappings(), servletMappingBuilder.getServlets());
    }
}
