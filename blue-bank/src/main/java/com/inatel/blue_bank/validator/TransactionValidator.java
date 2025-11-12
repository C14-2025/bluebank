package com.inatel.blue_bank.validator;

import com.inatel.blue_bank.exception.DeniedOperationException;
import com.inatel.blue_bank.exception.EntityNotFoundException;
import com.inatel.blue_bank.model.entity.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionValidator {

    public void validate(Transaction transaction) {

        if (transaction.getPayer() == null) {
            throw new EntityNotFoundException("Provided payer does not exist");
        }

        if (transaction.getPayee() == null) {
            throw new EntityNotFoundException("Provided payee does not exist");
        }

        BigDecimal payerBalance = transaction.getPayer().getBalance();
        BigDecimal amount = transaction.getAmount();

        if (payerBalance.compareTo(transaction.getAmount()) < 0) {
            throw new DeniedOperationException("Payer has insufficient funds");
        }
    }
}
