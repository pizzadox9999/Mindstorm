package marvin.application;

import java.util.Scanner;

import marvin.ms.application.Application;
import marvin.ms.application.MarvinToolkit;

public class Marvin extends Application {
    private boolean run = true;

    @Override
    protected void startup() {
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (run) {
            System.out.println("Console: ");
            String nextLine = scanner.nextLine().toLowerCase();
            if (nextLine.equals("stop")) {
                run = false;
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
        Marvin marvin = new Marvin();
        marvin.start();
    }
}
