package com.banking.cards.application.service.impl;

import com.banking.cards.application.avro.ApplicationDataAvro;
import com.banking.cards.application.avro.ApplicationValidationResultEvent;
import com.banking.cards.application.kafka.publisher.CardsApplicationValidationResultEventPublisher;
import com.banking.cards.application.service.CardsApplicationValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardsApplicationValidatorServiceImpl implements CardsApplicationValidatorService {

    private final KieContainer kieContainer;
    private final CardsApplicationValidationResultEventPublisher cardsApplicationValidationResultEventPublisher;

    @Override
    public void validateCardsApplicationData(ApplicationDataAvro applicationDataAvro) {
        String correlationId = applicationDataAvro.getCorrelationID().toString();
        log.info("Started validating application-submitted-event for correlation ID: {} ", correlationId);
        List<String> validationMessages = performValidation(applicationDataAvro);
        ApplicationValidationResultEvent applicationValidationResultEvent = createApplicationValidatedResultEvent(applicationDataAvro, validationMessages);
        this.cardsApplicationValidationResultEventPublisher.publishApplicationValidationResultEvent(applicationValidationResultEvent);
    }

    private List<String> performValidation(ApplicationDataAvro applicationDataAvro){
        KieSession kieSession = null;
        List<String> validationMessages = new ArrayList<>();
        try{
            kieSession = kieContainer.newKieSession();
            kieSession.setGlobal("validationMessages", validationMessages);
            kieSession.insert(applicationDataAvro);
            kieSession.fireAllRules();
        }catch(Exception e){
            log.error("Error during drools validation for the correlation id:{} ", applicationDataAvro.getCorrelationID());
        }finally{
            if(kieSession != null){
                kieSession.dispose();
            }
        }
        return validationMessages;
    }

    private ApplicationValidationResultEvent createApplicationValidatedResultEvent(ApplicationDataAvro applicationDataAvro,
                                                                                   List<String> validationMessages){
        ApplicationValidationResultEvent applicationValidationResultEvent = new ApplicationValidationResultEvent();
        applicationValidationResultEvent.setCorrelationID(applicationDataAvro.getCorrelationID());
        if(!validationMessages.isEmpty()){
            applicationValidationResultEvent.setApplicationStatus("REJECTED");
            applicationValidationResultEvent.setValidationMessages(validationMessages);
        }else{
            applicationValidationResultEvent.setApplicationStatus("APPROVED");
        }
        return applicationValidationResultEvent;
    }
}
