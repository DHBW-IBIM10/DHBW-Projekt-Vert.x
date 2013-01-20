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
	            	logger.info(buffer.toString());
	                ws.writeBuffer(buffer);
	            }
	        });
	        //actions:
	    	// 'createForm' - new form, sends data
	    	// data used for socket.room = data
	    	// socket.join(data)
	    	
	    	// 'liveform' - data
	    	// emit data on all sockets in the socket.room with key 'liveform'
	}};
	
    //event bus handlers for the sockjs server
    private Handler<Message> echoHandler = new Handler<Message>() {
        public void handle(Message message) {
            System.out.println("I received a message on " + message.replyAddress + ": " + message.body);
        }
    };
    
    private Handler<Message> formCreateHandler = new Handler<Message>() {
        public void handle(Message message) {
            //TODO: assign "room" for communication / register client in room
        	logger.info("not yet implemented");
        }
    };
    
    private Handler<Message> formDataHandler = new Handler<Message>() {
        public void handle(Message message) {
        	System.out.println("message body: " +  message.body);
        	eBus.send("form.client.data", new JsonObject(message.body.toString()));
        }
    };
	
	
	
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
        outboundPermitted.add(new JsonObject().putString("address", "form.client.data"));
        vertx.createSockJSServer(server).bridge(bridgeConfig, inboundPermitted, outboundPermitted);
        server.listen(port, "127.0.0.1");
    }
}