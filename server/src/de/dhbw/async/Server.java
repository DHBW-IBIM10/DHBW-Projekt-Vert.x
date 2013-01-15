package de.dhbw.async; /**
 * Class to demonstrate request handling / matching of parameters.
 *
 * @author Rocco Schulz
 */

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.deploy.Verticle;


import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.deploy.Verticle;

public class Server extends Verticle {

    public void start() {

        RouteMatcher rm = new RouteMatcher();

        //return list of customers
        rm.get("/customers", new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                req.response.end("List of customers");
            }
        });

        //return specific customer details
        rm.get("/customers/:id", new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                req.response.end(" ID: " + req.params().get("id"));
            }
        });


        //return list of insurances
        rm.get("/insurances", new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                req.response.end("List of insurances");
            }
        });

        //return insurance
        rm.get("/insurances/:id", new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                req.response.end(" ID: " + req.params().get("id"));
            }
        });

        // Catch all
        rm.getWithRegEx(".*", new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                req.response.headers().put("Content-Type", "text/html; charset=UTF-8");
                req.response.end("<html><body><p>This is our API, implemented in vert.x</p></body></html>");
            }
        });

        vertx.createHttpServer().requestHandler(rm).listen(8080);
    }
}