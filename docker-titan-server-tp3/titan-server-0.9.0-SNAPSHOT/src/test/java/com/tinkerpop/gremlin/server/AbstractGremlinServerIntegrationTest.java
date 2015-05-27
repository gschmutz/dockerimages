package com.tinkerpop.gremlin.server;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Starts and stops an instance for each executed test.
 *
 * @author Stephen Mallette (http://stephen.genoprime.com)
 */
public abstract class AbstractGremlinServerIntegrationTest {
    private static final Logger logger = LoggerFactory.getLogger(AbstractGremlinServerIntegrationTest.class);

    private Thread thread;
    private String host;
    private String port;

    public Settings overrideSettings(final Settings settings) {
        return settings;
    }

    public InputStream getSettingsInputStream() {
        return AbstractGremlinServerIntegrationTest.class.getResourceAsStream("gremlin-server-integration.yaml");
    }

    @Before
    public void setUp() throws Exception {
        final InputStream stream = getSettingsInputStream();
        final Settings settings = Settings.read(stream);
        final CompletableFuture<Void> serverReadyFuture = new CompletableFuture<>();

        thread = new Thread(() -> {
            try {
                final Settings overridenSettings = overrideSettings(settings);
                new GremlinServer(overridenSettings, serverReadyFuture).run();
            } catch (InterruptedException ie) {
                logger.info("Shutting down Gremlin Server");
            } catch (Exception ex) {
                logger.error("Could not start Gremlin Server for integration tests", ex);
            }
        });
        thread.start();

        // make sure gremlin server gets off the ground - longer than 30 seconds means that this didn't work somehow
        try {
            serverReadyFuture.get(30000, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            logger.error("Server did not start in the expected time or was otherwise interrupted.", ex);
            return;
        }

        host = System.getProperty("host", "localhost");
        port = System.getProperty("port", "8182");
    }

    @After
    public void tearDown() throws Exception {
        stopServer();
    }

    public void stopServer() throws Exception {
        if (!thread.isInterrupted())
            thread.interrupt();

        while (thread.isAlive()) {
            Thread.sleep(250);
        }
    }

    protected String getHostPort() {
        return host + ":" + port;
    }

    protected String getWebSocketBaseUri() {
        return "ws://" + getHostPort() + "/gremlin";
    }

    public static boolean deleteDirectory(final File directory) {
        if(directory.exists()){
            final File[] files = directory.listFiles();
            if(null != files){
                for(int i=0; i<files.length; i++) {
                    if(files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    }
                    else {
                        files[i].delete();
                    }
                }
            }
        }

        return(directory.delete());
    }
}
