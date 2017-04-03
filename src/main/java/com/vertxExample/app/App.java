package com.vertxExample.app;

import com.vertxExample.services.Service1;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class App  {
	public static void main(String[] args) {
   
        final DeploymentOptions deploymentOptions = new DeploymentOptions()
                .setInstances(5);
        
        final ClusterManager mgr = new HazelcastClusterManager();
        final VertxOptions options = new VertxOptions().setClusterManager(mgr);
        												
        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                
                final Vertx vertx = res.result();
                vertx.deployVerticle(Service1.class.getName());
            } else {
                System.out.println("FAIL !!!");
            }
        });
    }
}