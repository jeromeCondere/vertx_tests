package com.vertxExample.services;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.*;
import io.vertx.servicediscovery.types.*;

public class Service1 extends AbstractVerticle 
{
	private ServiceDiscovery discovery;
	
	@Override
	final public void start() throws Exception 
	{
		discovery = ServiceDiscovery.create(vertx,
			    new ServiceDiscoveryOptions()
			        .setAnnounceAddress("service-announce")
			        .setName("service1"));
		
		Record httpRecord = HttpEndpoint.createRecord(
				"serviceRest1", // The service name
				"localhost", // The host
				8080, // the port
				"/api" // the root of the service
		);
		
		discovery.publish(httpRecord, ar -> {
			 System.out.println(ar.result().ENDPOINT);
		});
		
		
		discovery.getRecord(new JsonObject().put("name", "serviceRest1"), ar -> {
			  if (ar.succeeded() && ar.result() != null) {
			    // Retrieve the service reference
			    ServiceReference reference = discovery.getReference(ar.result());
			    // Retrieve the service object
			    HttpClient client = reference.get();

			    // You need to path the complete path
			    client.getNow("/api/persons", response -> {
			    	response.bodyHandler(b->{
			    		System.out.println("fceed");
			    	});
			      

			      // Dont' forget to release the service
			      reference.release();

			    });
			  }
			});
		
	}
	
	@Override
	final public void stop() throws Exception 
	{
		discovery.close();
	}
}
