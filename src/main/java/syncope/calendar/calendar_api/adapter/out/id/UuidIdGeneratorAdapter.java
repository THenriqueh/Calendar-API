package syncope.calendar.calendar_api.adapter.out.id;

import org.springframework.stereotype.Component;
import syncope.calendar.calendar_api.application.port.out.IdGeneratorPort;

import java.util.UUID;

@Component
public class UuidIdGeneratorAdapter implements IdGeneratorPort {

    @Override
    public String nextSeriesId() {
        return UUID.randomUUID().toString();
    }
}