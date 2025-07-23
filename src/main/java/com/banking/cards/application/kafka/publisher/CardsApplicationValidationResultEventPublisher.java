package com.banking.cards.application.kafka.publisher;

import com.banking.cards.application.avro.ApplicationValidationResultEvent;
import com.banking.cards.application.config.Topics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardsApplicationValidationResultEventPublisher {
    private final Topics topics;
    private final KafkaTemplate<String, ApplicationValidationResultEvent> kafkaTemplate;

    public void publishApplicationValidationResultEvent(ApplicationValidationResultEvent applicationValidationResultEvent){
        log.info("Preparing to publish application-validated-event to topic {} for the correlationId {}",
                topics.getApplicationValidatedEvent(),applicationValidationResultEvent.getCorrelationID());
        try{
            this.kafkaTemplate.send(topics.getApplicationValidatedEvent(), applicationValidationResultEvent);
            log.info("Successfully published application-validated-event to topic {} for the correlationId {}",
                    topics.getApplicationValidatedEvent(),applicationValidationResultEvent.getCorrelationID());
        }catch(KafkaException kafkaException){
            log.error("Error while publishing application-validated-event to topic {} for the correlationId {}",
                    topics.getApplicationSubmittedEvent(),applicationValidationResultEvent.getCorrelationID());
        }catch (Exception e){
            log.error("Unexpected error while publishing application-validated-event to topic {} for the correlationId {}",
                    topics.getApplicationSubmittedEvent(),applicationValidationResultEvent.getCorrelationID());
        }
    }
}
