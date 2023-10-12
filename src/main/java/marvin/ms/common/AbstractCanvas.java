package marvin.ms.common;

import java.util.ArrayList;
import java.util.List;

abstract public class AbstractCanvas implements Canvas {
    private Unit unit;
    private float width;
    private float height;
    private float x;
    private float y;
    private float pressure;
    private Color color;
    private ArrayList<Color> supportedColors;
    
    protected AbstractCanvas(ArrayList<Color> supportedColors) {
        this.supportedColors = supportedColors;
    }

    @Override
    public Unit getUnit() {
        return unit;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    @Override
    public Canvas createSubCanvas(float x, float y, float width, float height) {
        return null;
    }

    @Override
    public float getPressure() {
        return pressure;
    }

    @Override
    public boolean isColorSupport(Color color) {
        return supportedColors.contains(color);
    }

    @Override
    public List<Color> getSupportedColors() {
        return supportedColors;
    }

    @Override
    public void chooseColor(Color color) {
        this.color = color;
    }
    
    @Override
    public Color getColor() {
        return color;
    }
    
    protected float getColorIntensity() {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        
        int colors = 3;
        if(red <= 0) {
            colors--;
        }
        if(green <= 0) {
            colors--;
        }
        if(blue <= 0) {
            colors--;
        }
        
        if(colors <= 0) {
            return 0;
        }
        
        return (red + green + blue) / (colors * 255);
    }
}
