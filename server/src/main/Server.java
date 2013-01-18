package main;
/**
 * Class to demonstrate request handling / matching of parameters.
 *
 * @author Rocco Schulz
 */

import org.vertx.java.busmods.BusModBase;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.JsonObject;

public class Server extends BusModBase {
	private String webRoot;
	private int port;
	private EventBus eBus;
	
	private Handler<HttpServerRequest> formHandler = new Handler<HttpServerRequest>() {
        public void handle(final HttpServerRequest req) {
        	JsonObject form = null; //TODO: get form as JSON from request
        	boolean valid = true; //TODO: validate form
        	System.out.println("Got post request.");
        	if(valid){
        		// trigger calculation in worker verticle
        		eBus.send("form.calculate", form, new Handler<Message<JsonObject>>() {
                    public void handle(Message<JsonObject> message) {
                        req.response.end(message.toString());
                    }
                });
        	} else{
        		req.response.end("Form invalid"); //TODO: json with all form fields
        	}
            
        }
    };
    
    
    private Handler<HttpServerRequest> fileHandler = new Handler<HttpServerRequest>() {
        public void handle(HttpServerRequest req) {
        	System.out.println("nomatch");
        	if (req.path.equals("/")) {
                req.response.sendFile(webRoot + "index.html");
              } else {
                //FIXME: This is clearly a security issue.
            	//		 Kept simple for demonstration purposes.
                req.response.sendFile(webRoot + req.path);
              }
        }
    };

    
    public void start() {
    	eBus = vertx.eventBus();
        RouteMatcher rm = new RouteMatcher();
        
        //read configuration file if present
         port = 8080;//getOptionalIntConfig("port", 8080);
         webRoot = "../UI";//getOptionalStringConfig("web_root", "web");


        rm.post("/insurances", formHandler);
        rm.noMatch(fileHandler);
        
        vertx.createHttpServer().requestHandler(rm).listen(port);
        
        System.out.println("http server started");
    }
}