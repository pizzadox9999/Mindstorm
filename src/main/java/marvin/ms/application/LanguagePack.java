package marvin.ms.application;

public interface LanguagePack {

    public String get(String key);

    public String get(Language language, String key);

    public void addLanguage(Language language);

    public void chooseLanguage(Language language);
}
