package marvin.application;

import marvin.ms.application.DefaultLanguagePack;
import marvin.ms.application.Language;

public class MarvinLanguagePack extends DefaultLanguagePack {
    public MarvinLanguagePack() {
        addLanguage(new GermanLanguage());
        addLanguage(new EnglishLanguage());

        setKey("color.red", "rot", "red");
    }

    class GermanLanguage extends Language {
        public GermanLanguage() {
            super("Deutsch");
        }
    }

    class EnglishLanguage extends Language {
        public EnglishLanguage() {
            super("English");
        }
    }

}
