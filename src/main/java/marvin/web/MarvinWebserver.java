package marvin.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletHandler;

import marvin.web.application.rest.printer.Print;
import marvin.web.application.rest.printer.RetrieveWaitingPosition;
import marvin.web.application.rest.printer.Shutdown;

public class MarvinWebserver {
    public static final String WEBSITE_ZIP_PATH = "./MindstormControl.zip";
    public static final String WEBSERVER_PATH = "/webserver";
    public static final String WWW_PATH = WEBSERVER_PATH + "/www";
    public static final String DATA_PATH = WEBSERVER_PATH + "/data";

    public static Server server;
    public static ServletHandler servletHandler;

    public static void startWebServer(String[] args) {
        server = new Server();
        ServerConnector serverConnector = new ServerConnector(server);
        serverConnector.setPort(80);
        server.setConnectors(new Connector[] { serverConnector });

        servletHandler = new ServletHandler();

        ResourceHandler resourceHanndler = new ResourceHandler();
        resourceHanndler.setDirectoriesListed(true);
        resourceHanndler.setWelcomeFiles(new String[] { "index.html" });

        resourceHanndler.setResourceBase(WWW_PATH);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resourceHanndler, servletHandler }); // new DefaultHandler()

        server.setHandler(handlers);

        addServletWithMapping(TestLet.class, "/test");

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopWebserver() {
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addServletWithMapping(Class clazz, String path) {
        servletHandler.addServletWithMapping(clazz, path);
    }

    public static void main(String[] args) {
        /*System.out.println("root list");
        for (String s : new File("/").list()) {
            System.out.println(s);
        }

        System.out.println("./ list");
        for (String s : new File("./").list()) {
            System.out.println(s);
        }*/

        // check paths
        File webserverDirectory = new File(WEBSERVER_PATH);
        File wwwDirectory = new File(WWW_PATH);
        File dataDirectory = new File(DATA_PATH);

        if (!webserverDirectory.isDirectory()) {
            createDirectory(webserverDirectory);
        }

        if (!wwwDirectory.isDirectory()) {
            createDirectory(wwwDirectory);
        }

        if (!dataDirectory.isDirectory()) {
            createDirectory(dataDirectory);
        }

        File websiteZipFile = new File(WEBSITE_ZIP_PATH);

        if (websiteZipFile.isFile()) {
            System.out.println("Website zip file located.");

            // delete www folder content
            deleteFoldersContent(wwwDirectory);

            // unzip into www folder
            try {
                unzip(new FileInputStream(websiteZipFile), wwwDirectory.toPath());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("error while unzipping www content");
            }

            // delete it because update should have been successful
            websiteZipFile.delete();
        } else {
            System.out.println("No website zip file found.");
        }

        // System.out.println(FileAssert.printDirectoryTree(new File(WEBSERVER_PATH)));

        if (wwwDirectory.listFiles() == null || wwwDirectory.listFiles().length == 0) {
            System.out.println("www  folder is empty shutting down.");
            System.exit(0);
        }

        startWebServer(args);

        loadMappings();

        // wait for server
        try {
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void loadMappings() {
        addServletWithMapping(Print.class, "/api/print");
        addServletWithMapping(RetrieveWaitingPosition.class, "/api/waitingPosition");
        addServletWithMapping(Shutdown.class, "/api/shutdown");
        
    }

    static boolean createDirectory(File f) {
        try {
            return f.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    static void deleteFoldersContent(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { // some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFoldersContent(f);

                }

                f.delete();
            }
        }
        // folder.delete();
    }

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { // some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    public static void unzip(InputStream is, Path targetDir) throws IOException {
        targetDir = targetDir.toAbsolutePath();
        try (ZipInputStream zip = new ZipInputStream(is)) {
            for (ZipEntry ze; (ze = zip.getNextEntry()) != null;) {
                Path resolvedPath = targetDir.resolve(ze.getName()).normalize();
                if (!resolvedPath.startsWith(targetDir)) {
                    // see: https://snyk.io/research/zip-slip-vulnerability
                    throw new RuntimeException("Entry with an illegal path: " + ze.getName());
                }
                if (ze.isDirectory()) {
                    Files.createDirectories(resolvedPath);
                } else {
                    Files.createDirectories(resolvedPath.getParent());
                    Files.copy(zip, resolvedPath);
                }
            }
        }
    }
}
