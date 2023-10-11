package marvin.ms.common;

import java.util.List;

/**
 * a canvas represents the piece of paper we are using. It can be presented by
 * the following interface. A canvas is also able to draw with a print head. The
 * head should support how hard it presses against the canvas.
 * 
 * @author pizzadox9999
 */
public interface Canvas {

    /**
     * The unit should roughly describe the scale of the canvas and more importantly
     * how precise the drawing is handled.
     * 
     * @return The unit used by this canvas.
     */
    public Unit getUnit();

    /**
     * It returns the width of the canvas.
     * 
     * @return The width of the canvas.
     */
    public float getWidth();

    /**
     * It returns the height of the canvas.
     * 
     * @return The height of the canvas.
     */
    public float getHeight();

    /**
     * Retrieves the x position relative to the origin.
     * 
     * @return The current X position of the canvas.
     */
    public float getX();

    /**
     * Sets the x position in relation to the origin.
     * 
     * @param The new X position;
     */
    public void setX(float x);

    /**
     * Retrieves the y position relative to the origin.
     * 
     * @return The current Y position of the canvas.
     */
    public float getY();

    /**
     * Sets the y position in relation to the origin.
     * 
     * @param The new Y position;
     */
    public void setY(float y);

    /**
     * Creates a subcanvas which can be moved around.
     * 
     * @return Returns the new subcanvas;
     */
    public Canvas createSubCanvas();

    /**
     * This methods returns with how much pressure/force the print head pushes
     * against the canvas.
     * 
     * @return It returns a value between 1 and 0. 1 for the highest available
     *         pressure and 0 for the lowest amount possible. This means no drawing
     *         in that time.
     */
    public float getPressure();

    /**
     * This function checks if the request color is supported by this canvas.
     * 
     * @param color The color to check.
     * @return Returns if the color is supported.
     */
    public boolean isColorSupport(Color color);

    /**
     * This function delivers all the available colors on this canvas.
     * 
     * @return The available colors.
     */
    public List<Color> getAvaiableColors();

    /**
     * Sets the color the print head is currently using.
     * 
     * @param color The color which should be used.
     */
    public void chooseColor(Color color);
    
    /**
     * Draws the drawable to the canvas.
     * @param drawable
     */
    public void draw(Drawable drawable);
}
