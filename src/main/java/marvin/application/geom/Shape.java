package marvin.application.geom;

import marvin.application.Printer;

public interface Shape {
    public void draw(Printer printer);
    
    public String toString();
}
