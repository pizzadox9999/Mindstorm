package marvin.ms.common;

import marvin.ms.application.LanguagePack;
import marvin.ms.application.MarvinToolkit;

/**
 * A simple unit representation which features a to string methods for later
 * debug purposes. This class also features the available standard units we
 * should able to choose from for our canvas. It was chosen to support multiple
 * debug languages.
 * 
 * @author pizzadox9999
 */
public class Unit {
    private static LanguagePack languagePack = MarvinToolkit.getDefaulToolkit().getLanguagePack();
    public static final Unit MILLI_METER = new Unit(languagePack.get("unit.millimeter"));
    public static final Unit CENTI_METER = new Unit(languagePack.get("unit.centimeter"));
    public static final Unit DECI_METER = new Unit(languagePack.get("unit.decimeter"));
    public static final Unit METER = new Unit(languagePack.get("unit.meter"));

    private static int unitCounter = 0;

    private int id;
    private String name;

    public Unit(String name) {
        this.name = name;
        id = unitCounter++;
    }

    public boolean equals(Unit unit) {
        return id == unit.id;
    }

    public String toString() {
        return name;
    }
}
