package marvin.ms.common.test;

import java.util.ArrayList;

public class Tests implements Runnable {
    //test configuration
    public static final boolean runTests = true;
    public static final boolean runTestsExclusive = true;
    
    private ArrayList<Testable> tests = new ArrayList<>();
    
    public Tests() {
        //register all standard tests
        registerTest(new DrawTest());
    }
    
    public void registerTest(Testable testable) {
        tests.add(testable);
    }
    
    /**
     * Executes all the registered test.
     */
    public void run() {
        for(Testable testable : tests) {
            testable.run();
        }
    }
    
    /**
     * Runs the standard tests.
     */
    public static void start() {
        new Tests().run();
    }
}
