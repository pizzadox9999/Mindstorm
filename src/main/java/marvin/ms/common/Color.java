package marvin.ms.common;

import java.util.ArrayList;
import java.util.Arrays;

import marvin.ms.application.LanguagePack;
import marvin.ms.application.MarvinToolkit;

/**
 * This is a simple color representation. This includes red green blue, but no
 * transparency.
 * 
 * @author pizzadox9999
 */
public class Color {
    private static LanguagePack languagePack = MarvinToolkit.getDefaulToolkit().getLanguagePack();
    public static final Color RED = new Color(255, 0, 0, languagePack.get("color.red"));
    public static final Color GREEN = new Color(0, 255, 0, languagePack.get("color.green"));
    public static final Color BLUE = new Color(0, 0, 255, languagePack.get("color.blue"));
    public static final Color BLACK = new Color(255, 255, 255, languagePack.get("color.black"));

    private static int unitCounter = 0;

    private int id;

    private int red;
    private int green;
    private int blue;

    private String name;

    public Color(int red, int green, int blue) {
        this(red, green, blue, null);
    }

    public Color(int red, int green, int blue, String name) {
        this.name = name;
        id = unitCounter++;
    }
    
    public int getRed() {
        return red;
    }
    
    public int getGreen() {
        return green;
    }
    
    public int getBlue() {
        return blue;
    }

    public boolean equals(Color color) {
        return id == color.id;
    }

    public boolean hasName() {
        return name != null || !name.isEmpty();
    }

    public String toString() {
        if (!hasName()) {
            name = "Color";
        }

        return name + "(" + red + "|" + green + "|" + blue + ")";
    }
    
    public static ArrayList<Color> createSupportList(Color... colors) {
        return (ArrayList<Color>) Arrays.asList(colors);
    }
}
