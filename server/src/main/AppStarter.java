/**
 * 
 */
package main;

import org.vertx.java.core.SimpleHandler;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Verticle;

/**
 * @author rocco
 * 
 */
public class AppStarter extends Verticle {
	
	/** Key for the web config in the conf file */
	private static final String WEB_CONF = "web_conf";
	/** Key for http verticle config. */
	private static final String HTTP_CONF = "http_conf";
	/** number of available processors */
	private static final int NUM_PROCESSORS = Runtime.getRuntime().availableProcessors();

	/* (non-Javadoc)
	 * @see org.vertx.java.deploy.Verticle#start()
	 */
	/**
	 * Reads the config and starts all verticles that belong to this project.
	 */
	@Override
	public void start() throws Exception {
		JsonObject appConfig = container.getConfig();
		JsonObject httpConfig = appConfig.getObject(HTTP_CONF);
		
		container.deployVerticle("main.Server", httpConfig, 6);
		// run calculations in multiple instances to avoid bottleneck.
		container.deployWorkerVerticle("form.Calculator", NUM_PROCESSORS - 1);
		//container.deployWorkerVerticle("form.Calculator", 1);
	}

}
