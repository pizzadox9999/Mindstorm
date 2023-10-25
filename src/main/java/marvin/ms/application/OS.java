package marvin.ms.application;

import java.util.Arrays;

public class OS {
    public static final OS WINDOWS = new OS("Microsoft Windows", "win");
    public static final OS MACOS = new OS("Macintosh Operating System", "mac");
    public static final OS LINUX = new OS("Linux", "nix", "nux", "aix");
    public static final OS LEJOS = new OS("LeJOS", "");
    public static final OS CURRENT_OS = new OS("current os", System.getProperty("os.name"));
    
    private String name;
    private String[] handles;
    
    public OS(String... os) {
        if(os.length <= 1) {
            throw new RuntimeException("No OS handles declared."); 
        }
        
        this.name = os[0];
        this.handles = new String[os.length - 1];
        for(int i=1; i<os.length; i++) {
            handles[i - 1] = os[i];
        }
        Arrays.sort(handles);
    }

    public boolean equals(OS os) {
        if(handles.length != os.handles.length) {
            return false;
        }
        
        for(int i=1; i<handles.length; i++) {
            if(!handles[i].equals(os.handles[i])) {
                return false;
            }
        }
        
        return true;
    }
    
    public String toString() {
        return name + Arrays.asList(handles).toString();
    }
}
