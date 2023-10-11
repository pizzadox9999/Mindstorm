package marvin.application;

import marvin.ms.application.LanguagePack;
import marvin.ms.common.MarvinToolkit;

public class DefaultMarvinToolkit extends MarvinToolkit {
    
    public static void initMarvinToolkit() {
        defaultMarvinToolkit = new DefaultMarvinToolkit();
    }
    
    private MarvinLanguagePack marvinLanguagePack;
    
    private DefaultMarvinToolkit() {
        marvinLanguagePack = new MarvinLanguagePack();
    }
    
    @Override
    public LanguagePack getLanguagePack() {
        return marvinLanguagePack;
    }

}
