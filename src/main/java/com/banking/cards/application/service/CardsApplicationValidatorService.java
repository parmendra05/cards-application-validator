package com.banking.cards.application.service;

import com.banking.cards.application.avro.ApplicationDataAvro;

public interface CardsApplicationValidatorService {
    void validateCardsApplicationData(ApplicationDataAvro applicationDataAvro);
}
