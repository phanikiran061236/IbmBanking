package com.ibm.bank.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.bank.kafka.KafkaProducer;
import com.ibm.bank.model.Balance;
import com.ibm.bank.model.Transaction;
import com.ibm.bank.service.IbmBankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;

import static com.ibm.bank.constants.Constants.BALANCES_TOPIC;
import static com.ibm.bank.constants.Constants.TRANSACTIONS_TOPIC;

@RestController
@RequestMapping("/banking")
@Validated
@Slf4j
public class IbmBankController {

    @Autowired
    KafkaProducer kafkaProducer;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    IbmBankService ibmBankService;

    @PostMapping("/transactions")
    public void postTransaction(@RequestBody Transaction transaction) throws JsonProcessingException {
        log.info("Transaction request is: "+objectMapper.writeValueAsString(transaction));
        kafkaProducer.publish(TRANSACTIONS_TOPIC, objectMapper.writeValueAsString(transaction));
    }

    @GetMapping("/transactions")
    public List<Transaction> getTransactions(String accountNumber, String type, @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate, @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") @Min(value = 1, message = "Page size cannot be less than 1") @Max(value = 20, message = "Page size cannot be greater than 20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ibmBankService.getTransactions(accountNumber, type, fromDate, toDate, pageable);
    }

    @PostMapping("/balances")
    public void postBalance(@RequestBody Balance balance) throws JsonProcessingException {
        log.info("Balance request is: "+objectMapper.writeValueAsString(balance));
        kafkaProducer.publish(BALANCES_TOPIC, objectMapper.writeValueAsString(balance));
    }

    @GetMapping("/balances")
    public Double getBalance(String accountNumber){
        return ibmBankService.getBalance(accountNumber);
    }

}
