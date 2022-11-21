package be.stib.maas.reconciliation.report;

import be.stib.maas.reconciliation.AbstractTest;
import be.stib.maas.reconciliation.model.Dataset;
import be.stib.maas.reconciliation.model.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UnmatchedTransactionsReportGeneratorTest extends AbstractTest {

    @Spy
    private UnmatchedTransactionsReportGenerator<List<Dataset>, Void> createReport;

    @Test
    public void process() {
        List<Transaction> transactions = getTransactions(10);
        Dataset dataset = Dataset.builder().name("trafi-ingenico-exclusive").transactions(transactions).build();
        createReport.process(Collections.singletonList(dataset));
    }

}