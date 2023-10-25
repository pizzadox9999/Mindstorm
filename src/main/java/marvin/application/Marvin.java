package marvin.application;

import java.util.Scanner;

import marvin.ms.application.Application;
import marvin.ms.application.MarvinToolkit;

public class Marvin extends Application {
    private boolean run = true;

    public Marvin(String webappDir) {
        super(webappDir);
    }

    @Override
    protected void startup() {
        new MarvinServlets(this);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (run) {
            System.out.println("Console: ");
            String nextLine = scanner.nextLine().toLowerCase();
            if (nextLine.equals("stop")) {
                run = false;
            } else if (nextLine.equals("restart")) {
                tomcatRestart();
            } else {
                System.out.println("Your input was not recognized");
            }
        }
    }
    
    private String getLanguageValue(String key) {
        return MarvinToolkit.getDefaulToolkit().getLanguagePack().get(key);
    }

    public static void main(String[] args) {
        StandardMarvinToolkit.initMarvinToolkit();
        Marvin marvin = new Marvin("src/main/webapp/");
        marvin.start();
    }
}
