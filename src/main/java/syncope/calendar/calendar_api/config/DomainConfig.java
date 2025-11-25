package syncope.calendar.calendar_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import syncope.calendar.calendar_api.domain.service.TransactionDomainService;

@Configuration
public class DomainConfig {

    @Bean
    public TransactionDomainService transactionDomainService() {
        return new TransactionDomainService();
    }
}
