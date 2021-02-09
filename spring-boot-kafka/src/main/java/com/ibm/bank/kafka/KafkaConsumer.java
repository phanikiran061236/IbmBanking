package com.ibm.bank.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.bank.model.Balance;
import com.ibm.bank.model.Transaction;
import com.ibm.bank.service.IbmBankService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.ibm.bank.constants.Constants.BALANCES_TOPIC;
import static com.ibm.bank.constants.Constants.TRANSACTIONS_TOPIC;

@Component
@Slf4j
public class KafkaConsumer {
    @Autowired
    IbmBankService ibmBankService;

    @Autowired
    ObjectMapper objectMapper;

    @KafkaListener(topics = TRANSACTIONS_TOPIC)
    public void consumeTransactions(ConsumerRecord<String,String> consumerRecord) throws JsonProcessingException {
        log.info("Consuming transaction message:"+consumerRecord.toString());
        ibmBankService.saveTransaction(objectMapper.readValue(consumerRecord.value(), Transaction.class));
    }

    @KafkaListener(topics = BALANCES_TOPIC)
    public void consumeBalances(ConsumerRecord<String,String> consumerRecord) throws JsonProcessingException {
        log.info("consuming balance message:"+consumerRecord.toString());
        ibmBankService.saveBalance(objectMapper.readValue(consumerRecord.value(),Balance.class));
    }

}
