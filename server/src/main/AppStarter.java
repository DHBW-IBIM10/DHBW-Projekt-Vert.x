/**
 * 
 */
package main;

import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Verticle;

/**
 * @author rocco
 * 
 */
public class AppStarter extends Verticle {
	
	/** Key for the web config in the conf file */
	private static final String WEB_CONF = "web_conf";

	/* (non-Javadoc)
	 * @see org.vertx.java.deploy.Verticle#start()
	 */
	/**
	 * Reads the config and starts all verticles that belong to this project.
	 */
	@Override
	public void start() throws Exception {
		JsonObject appConfig = container.getConfig();
		JsonObject serverConfig = appConfig.getObject(WEB_CONF);
		//JsonObject verticle2Config = appConfig.getObject("verticle2_conf");
		
		//Run web server for static files
		container.deployModule("vertx.web-server-v1.0", serverConfig);
		System.out.println("Webserver started: http://127.0.0.1:8080");
		//container.deployVerticle("main.Server");
	}

}
