package marvin.ms.application;

import marvin.ms.common.test.Tests;

abstract public class Application implements Runnable {
    
    protected void startup() {}
    protected void shutudown() {};

    public void start() {
        if(Tests.runTests) {
            Tests.start();
            if(Tests.runTestsExclusive) {
                return;
            }
        }
        System.out.println("Application startup.");
        startup();
        run();
        shutudown();
        System.out.println("Application shutdown.");
    }
}
