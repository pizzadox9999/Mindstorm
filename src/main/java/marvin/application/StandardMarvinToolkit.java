package marvin.application;

import marvin.ms.application.LanguagePack;
import marvin.ms.application.MarvinToolkit;

public class StandardMarvinToolkit extends MarvinToolkit {
    
    public static void initMarvinToolkit() {
        defaultMarvinToolkit = new StandardMarvinToolkit();
    }
    
    private MarvinLanguagePack marvinLanguagePack;
    
    private StandardMarvinToolkit() {
        marvinLanguagePack = new MarvinLanguagePack();
    }
    
    @Override
    public LanguagePack getLanguagePack() {
        return marvinLanguagePack;
    }

}
