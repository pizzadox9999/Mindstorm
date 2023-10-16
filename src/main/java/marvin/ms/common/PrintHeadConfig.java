package marvin.ms.common;

public class PrintHeadConfig {
    public boolean smooth;
    public boolean blackAndWhite;
    
    public PrintHeadConfig() {
        this(true, true);
    }
    
    public PrintHeadConfig(boolean smooth, boolean blackAndWhite) {
        this.smooth = smooth;
        this.blackAndWhite = blackAndWhite;
    }
}
