/**
 * 
 */
package form;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Verticle;

/**
 * @author rocco
 *
 */
public class Calculator extends Verticle {
	

	/* (non-Javadoc)
	 * @see org.vertx.java.deploy.Verticle#start()
	 */
	@Override
	public void start() throws Exception {
		System.out.println("start called");
		final EventBus eBus = vertx.eventBus();
		System.out.println("bus referenced.");
        eBus.registerHandler("form.calculate", new Handler<Message<JsonObject>>() {

			public void handle(Message<JsonObject> message) {
            	
            	//TODO: calculate insurance fee
            	
                try {
                	//simulate long operation
                    Thread.sleep(1000);
                } catch (Exception ex) {
                	//nothing to do.
                }
                
                System.out.println("I received a message " + message.body);

                // Do some stuff

                // Now reply to it
                JsonObject reply = new JsonObject();
                reply.putString("key", "value");
                message.reply(reply);
                
                
            }
        });
	}

}
