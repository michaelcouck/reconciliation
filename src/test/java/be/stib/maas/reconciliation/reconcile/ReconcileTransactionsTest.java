package be.stib.maas.reconciliation.reconcile;

import be.stib.maas.reconciliation.AbstractTest;
import be.stib.maas.reconciliation.model.Dataset;
import be.stib.maas.reconciliation.model.Transaction;
import lombok.extern.java.Log;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Log
@SuppressWarnings("Convert2Lambda")
@RunWith(MockitoJUnitRunner.class)
public class ReconcileTransactionsTest extends AbstractTest {

    @Spy
    private ReconcileTransactions<List<Dataset>, List<Dataset>> reconcileTransactions;

    @Test
    public void process() {
        int firstSize = 100;
        int secondSize = 10;
        List<Transaction> transactionListOne = getTransactions(firstSize);
        List<Transaction> transactionListTwo = getTransactions(secondSize);
        transactionListTwo.addAll(transactionListOne);

        Dataset one = Dataset.builder().name("one").transactions(transactionListOne).build();
        Dataset two = Dataset.builder().name("two").transactions(transactionListTwo).build();
        List<Dataset> unmatchedTransactionsDatasets = reconcileTransactions.process(Arrays.asList(one, two));
        unmatchedTransactionsDatasets.forEach(new Consumer<Dataset>() {
            @Override
            public void accept(final Dataset dataset) {
                dataset.getTransactions().forEach(new Consumer<Transaction>() {
                    @Override
                    public void accept(final Transaction transaction) {
                        log.info("Transaction : " + transaction);
                    }
                });
                Assert.assertEquals(secondSize, dataset.getTransactions().size());
            }
        });
    }

}