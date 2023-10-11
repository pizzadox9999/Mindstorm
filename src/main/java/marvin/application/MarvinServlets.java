package marvin.application;

import marvin.application.servlet.MarvinPrintServlet;
import marvin.ms.application.Application;
import marvin.ms.application.ServletMappingBuilder;

public class MarvinServlets {
    public MarvinServlets(Application application) {
        ServletMappingBuilder srb = new ServletMappingBuilder();
        srb.add("/marvin/print", new MarvinPrintServlet());
        application.registerServlets(srb);
    }
}
