package be.stib.maas.reconciliation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Michael Couck
 * @since 14-07-2022
 */
@Configuration
@EnableScheduling
@SpringBootApplication
public class ReconciliationApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ReconciliationApplication.class, args);
    }

}
