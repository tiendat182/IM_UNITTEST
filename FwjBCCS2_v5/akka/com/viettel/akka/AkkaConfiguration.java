package com.viettel.akka;

import akka.actor.ActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The application configuration.
 */
@Configuration
public class AkkaConfiguration {
    // the application context is needed to initialize the Akka Spring Extension
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Actor system singleton for this application.
     */
    @Bean
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create("AkkaSystem");
        // initialize the application context in the Akka Spring Extension
        SpringExtension.SpringExtProvider.get(system).initialize(applicationContext);
        return system;
    }
}
