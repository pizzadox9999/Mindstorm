package marvin.ms.application;

import java.util.ArrayList;

abstract public class DefaultLanguagePack implements LanguagePack {

    private ArrayList<Language> languages = new ArrayList<>();
    private int currentLanguageIndex;
    private int currentLanguageID;

    @Override
    public String get(Language language, String key) {
        if (currentLanguageID != language.getID()) {
            chooseLanguage(language);
        }
        return get(key);
    }

    @Override
    public String get(String key) {
        try {
            return languages.get(currentLanguageIndex).getKey(key);
        } catch (Exception e) {
        }
        return "";
    }

    @Override
    public void addLanguage(Language language) {
        languages.add(language);
    }

    @Override
    public void chooseLanguage(Language language) {
        currentLanguageID = language.getID();
        currentLanguageIndex = languages.indexOf(language);
    }

    /**
     * Adds the values off the key to the corresponding language in their order
     * after they were added.
     * 
     * @param key
     * @param values
     */
    public void setKey(String key, String... values) {
        if (languages.size() != values.length) {
            throw new RuntimeException("The amount of languages is not matching the amount of values.");
        }

        for (int i = 0; i < values.length; i++) {
            languages.get(i).setKey(key, values[i]);
        }
    }
}
