/**
 * 
 */
package form;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Verticle;

/**
 * @author rocco
 *
 */
public class Calculator extends Verticle {
	
	
	/**
	 * Instantiates an insuranceForm and calculates the fee based on the forms data.
	 * The result is sent back via the event bus.
	 */
	private Handler<Message<JsonObject>> calculationHandler = new Handler<Message<JsonObject>>() {
		public void handle(Message<JsonObject> message) {
			long start = System.currentTimeMillis();
            InsuranceForm form = new InsuranceForm();
            JsonObject fee = new JsonObject();
            fee.putString("price", String.valueOf(form.getInsuranceFee()));
            message.reply(fee);
            long stop = System.currentTimeMillis();
            System.out.println("Calculation handler completed after " + (stop - start) + "ms");
        }
    };
    
	/**
	 * Instantiates an insuranceForm and calculates the fee based on the forms data.
	 * The result is sent back via the event bus.
	 */
	private Handler<Message<JsonArray>> submissionHandler = new Handler<Message<JsonArray>>() {
		public void handle(Message<JsonArray> message) {				
            InsuranceForm form = new InsuranceForm(message.body);
            if(form.isValid()){
                JsonArray reply = new JsonArray();
                message.reply(reply);
                
            } else {
                JsonArray reply = form.getInvalidFields();
                message.reply(reply);                	
            }  
        }
    };
	

	/* (non-Javadoc)
	 * @see org.vertx.java.deploy.Verticle#start()
	 */
	@Override
	public void start() throws Exception {
		final EventBus eBus = vertx.eventBus();
        eBus.registerHandler("form.calculate", calculationHandler);
        eBus.registerHandler("form.submit", submissionHandler);
	}

}
