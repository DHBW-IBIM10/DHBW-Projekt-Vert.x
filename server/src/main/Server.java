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
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.core.json.DecodeException;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;

public class Server extends BusModBase {
	private String webRoot;
	private int port;
	private EventBus eBus;
	private Logger logger; 
	

	private Handler<ServerWebSocket> socketHandler = new Handler<ServerWebSocket>() {
	    public void handle(final ServerWebSocket ws) {
	    	ws.dataHandler(new Handler<Buffer>() {
	            public void handle(Buffer buffer) {
	            	logger.info("socketHandler:");
	            	logger.info(buffer.toString());
	                ws.writeBuffer(buffer);
	            }
	        });
	}};
    

    /**
     * Registers client in an address room.
     */
    private Handler<Message> formCreateHandler = new Handler<Message>() {
        public void handle(Message message) {
            //TODO: assign "room" for communication / register client in room
        	logger.info("not yet implemented");
        	logger.info(message.body);
        }
    };
    

    /**
     * Forwards form data to the websocket clients.
     */
    private Handler<Message> formDataHandler = new Handler<Message>() {
        public void handle(Message message) {
        	logger.info("message body: " +  message.body);
        	JsonObject json = new JsonObject(message.body.toString());
        	String address = "form.data.client." + json.getString("formid");
        	logger.info("Address: " + address);
        	eBus.publish(address, json);
        }
    };
	
	
	/**
	 * Triggers the fee calculation based on the given form data.
	 */
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
									req.response.headers().put("Content-Type", "application/json; charset=utf-8");
									req.response.end(message.body.encode());
								}
							});
				}
			});

		}
	};
	
	/**
	 * Triggers the evaluation of the form instance.
	 */
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
    
	/**
	 * Interprets the request path as file request and returns the file if present in the UI folder.
	 */
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
    	logger = container.getLogger();
    	eBus = vertx.eventBus();
        RouteMatcher rm = new RouteMatcher();
        EventBus eb = vertx.eventBus();
        
        //read configuration file if present
         port = 8080;//getOptionalIntConfig("port", 8080);
         webRoot = "../UI";//getOptionalStringConfig("web_root", "web");


        //normal HTTP handlers
        rm.post("/pricecalculation", calcHandler);
        rm.post("/insurances", formHandler);
        rm.noMatch(fileHandler);
        
        eb.registerHandler("form.create", this.formCreateHandler);
        eb.registerHandler("form.data", this.formDataHandler);
        
        
        HttpServer server = vertx.createHttpServer()
        .requestHandler(rm)
        .websocketHandler(socketHandler);
        //for SSL uncomment lines below
        //.setSSL(true)
        //.setKeyStorePath("/path/to/your/keystore/server-keystore.jks")
        //.setKeyStorePassword("password")
        
        JsonObject bridgeConfig = new JsonObject().putString("prefix", "/eventbus");
        
        JsonArray inboundPermitted = new JsonArray();
        JsonArray outboundPermitted = new JsonArray();
        // Let through any messages related to form creation and updates
        JsonObject addressData = new JsonObject().putString("address", "form.data");
        JsonObject addressCreate = new JsonObject().putString("address", "form.create");
        inboundPermitted.add(addressData);
        inboundPermitted.add(addressCreate);
        outboundPermitted.add(new JsonObject().putString("address_re", "form.data.client\\.\\d+"));
        //all allowed: new JsonArray().add(new JsonObject()
        vertx.createSockJSServer(server).bridge(bridgeConfig, inboundPermitted,  outboundPermitted);
        server.listen(port, "127.0.0.1");
    }
}