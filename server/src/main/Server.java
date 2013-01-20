package main;
/**
 * Class to demonstrate request handling / matching of parameters.
 *
 * @author Rocco Schulz
 */

import org.vertx.java.busmods.BusModBase;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.DecodeException;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

public class Server extends BusModBase {
	private String webRoot;
	private int port;
	private EventBus eBus;
	
	private Handler<HttpServerRequest> calcHandler = new Handler<HttpServerRequest>() {
		public void handle(final HttpServerRequest req) {
			// read body of request
			req.bodyHandler(new Handler<Buffer>() {
				@Override
				public void handle(Buffer buff) {
					JsonObject form;
					try{
						form = new JsonObject(buff.toString());
					} catch(DecodeException ex){
						form = new JsonObject();					
					}
					
					// trigger calculation in worker verticle
					eBus.send("form.calculate", form,
							new Handler<Message<JsonObject>>() {
								public void handle(Message<JsonObject> message) {
									req.response.end(message.body.encode());
								}
							});
				}
			});

		}
	};
	
	private Handler<HttpServerRequest> formHandler = new Handler<HttpServerRequest>() {
		public void handle(final HttpServerRequest req) {
			// read body of request
			req.bodyHandler(new Handler<Buffer>() {
				@Override
				public void handle(Buffer buff) {
					JsonArray form = new JsonArray(buff.toString());
					// trigger calculation in worker verticle
					eBus.send("form.submit", form,
							new Handler<Message<JsonArray>>() {
								public void handle(Message<JsonArray> message) {
									req.response.end(message.body.encode());
								}
							});
				}
			});

		}
	};
    
    private Handler<HttpServerRequest> fileHandler = new Handler<HttpServerRequest>() {
        public void handle(HttpServerRequest req) {
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


        rm.post("/pricecalculation", calcHandler);
        rm.post("/insurances", formHandler);
        rm.noMatch(fileHandler);
        
        vertx.createHttpServer().requestHandler(rm).listen(port);
        
        System.out.println("http server started");
    }
}