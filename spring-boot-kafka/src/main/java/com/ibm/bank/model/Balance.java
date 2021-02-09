package com.ibm.bank.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Balance {
    @Id
    @GeneratedValue
    private Integer balanceId;
    private String accountNumber;
    private Date lastUpdateTimeStamp;
    private double balance;
}
