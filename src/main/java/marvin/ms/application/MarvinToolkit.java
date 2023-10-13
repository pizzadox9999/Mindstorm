package marvin.ms.application;

public abstract class MarvinToolkit {

    protected static MarvinToolkit defaultMarvinToolkit = null;

    public static MarvinToolkit getDefaulToolkit() {
        return defaultMarvinToolkit;
    }

    abstract public LanguagePack getLanguagePack();
}
