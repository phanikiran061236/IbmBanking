package com.ibm.bank.service.impl;

import antlr.StringUtils;
import com.ibm.bank.model.Balance;
import com.ibm.bank.model.Transaction;
import com.ibm.bank.repository.BalanceRepository;
import com.ibm.bank.repository.TransactionRepository;
import com.ibm.bank.service.IbmBankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class IbmBankServcieImpl implements IbmBankService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BalanceRepository balanceRepository;


    public void saveBalance(Balance balance){
        Balance balanceExists=balanceRepository.findByAccountNumber(balance.getAccountNumber());
        if (balanceExists != null) {
            balanceExists.setBalance(balance.getBalance());
            balanceExists.setLastUpdateTimeStamp(balance.getLastUpdateTimeStamp());
            balanceRepository.save(balanceExists);
        }else {
            balanceRepository.save(balance);
        }
    }
    public void saveTransaction(Transaction transaction){
        transactionRepository.save(transaction);
    }

    public Double getBalance(String accountNumber){
        Double returnBalance = 0.0;
        Balance balance=balanceRepository.findByAccountNumber(accountNumber);
        if(balance != null)
            returnBalance = balance.getBalance();
        log.info("Balance "+ returnBalance +" returned for accountNumber "+accountNumber);
        return returnBalance;
    }

    public List<Transaction> getTransactions(String accountNumber, String type, Date fromDate, Date toDate, Pageable pageable) {
        log.info("Filtering according to Account Number: " + accountNumber + ", Type: " + type + ", fromDate: " + fromDate + ", toDate: " + toDate);

        return transactionRepository.findAll(new Specification<Transaction>() {
            @Override
            public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (accountNumber != null) {
                    predicates.add((criteriaBuilder.equal(root.get("accountNumber"), accountNumber)));
                }
                if (type != null) {
                    predicates.add(criteriaBuilder.equal(root.get("type"), type));
                }
                if (fromDate != null && toDate != null && fromDate.before(toDate)) {
                    predicates.add(criteriaBuilder.between(root.get("transactionTs"), fromDate, toDate));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable).getContent();
    }
}
