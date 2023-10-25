package marvin.ms.application;

import java.util.ArrayList;

import javax.servlet.Servlet;

public class ServletMappingBuilder {
    private ArrayList<String> mappings;
    private ArrayList<Servlet> servlets;
    
    public ServletMappingBuilder() {
        mappings = new ArrayList<>();
        servlets = new ArrayList<>();
    }
    
    public void add(String mapping, Servlet servlet) {
        mappings.add(mapping);
        servlets.add(servlet);
    }
    
    public String[] getMappings() {
        return mappings.toArray(new String[mappings.size()]);
    }
    
    public Servlet[] getServlets() {
        return servlets.toArray(new Servlet[servlets.size()]);
    }
}
