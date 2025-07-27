package com.banking.cards.application.kafka.listener;

import com.banking.cards.application.avro.ApplicationDataAvro;
import com.banking.cards.application.service.CardsApplicationValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardsApplicationSubmittedEventListener {

    private final CardsApplicationValidatorService cardsApplicationValidatorService;

    @KafkaListener(topics = "vnd_cards_application_submitted_event", groupId = "application-submitted-event-group")
    public void getData(ConsumerRecord<String, ApplicationDataAvro> consumerRecord){
        log.info("Received Application Submitted Event: {}", consumerRecord.value().getCorrelationID());
        ApplicationDataAvro applicationDataAvro = consumerRecord.value();
        this.cardsApplicationValidatorService.validateCardsApplicationData(applicationDataAvro);
    }

}
