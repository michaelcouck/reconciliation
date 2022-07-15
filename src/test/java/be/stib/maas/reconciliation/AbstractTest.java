package be.stib.maas.reconciliation;

import be.stib.maas.reconciliation.model.Transaction;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class AbstractTest {

    public List<Transaction> getTransactions(final int size) {
        List<Transaction> transactions = new ArrayList<>();
        for (int i = size; i > 0; i--) {
            Transaction transaction = getTransaction(
                    new Date(RandomUtils.nextInt()),
                    RandomStringUtils.random(8),
                    RandomUtils.nextInt());
            transactions.add(transaction);
        }
        return transactions;
    }

    Transaction getTransaction(final Date date, final String description, final int amount) {
        return Transaction.builder().date(date).description(description).amount(amount).build();
    }

}