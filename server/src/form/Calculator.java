/**
 * 
 */
package form;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
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
        eBus.registerHandler("form.calculate", new Handler<Message<JsonArray>>() {

			public void handle(Message<JsonArray> message) {
				
                InsuranceForm form = new InsuranceForm(message.body);
                if(form.isValid()){
                    try {
                    	//simulate long operation
                        Thread.sleep(1000);
                    } catch (Exception ex) {
                    	//nothing to do.
                    }
                    JsonArray reply = new JsonArray();
                    message.reply(reply);
                    
                } else {
                	//TODO: 
                    JsonArray reply = new JsonArray();
                    message.reply(reply);                	
                }

                
                
            }
        });
	}

}
