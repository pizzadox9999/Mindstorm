package marvin.ms.common;

import marvin.ms.application.LanguagePack;

public abstract class MarvinToolkit {

    protected static MarvinToolkit defaultMarvinToolkit = null;

    public static MarvinToolkit getDefaulToolkit() {
        return defaultMarvinToolkit;
    }

    abstract public LanguagePack getLanguagePack();
}
