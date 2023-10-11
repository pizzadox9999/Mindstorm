package marvin.ms.application;

import java.util.HashMap;

abstract public class Language {
    private static int languageCounter = 0;
    private HashMap<String, String> keyMap;

    private int id;
    private String name;

    protected Language(String name) {
        this.name = name;
        id = languageCounter;
        
        keyMap = new HashMap<>(); 
    }

    public void setKey(String key, String value) {
        keyMap.put(key, value);
    }

    public String getKey(String key) {
        return keyMap.get(key);
    }

    public int getID() {
        return id;
    }

    public boolean equals(Language language) {
        return id == language.id;
    }

    public String toString() {
        return name;
    }
}
