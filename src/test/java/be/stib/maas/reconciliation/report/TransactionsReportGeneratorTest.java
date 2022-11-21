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
public class TransactionsReportGeneratorTest extends AbstractTest {

    @Spy
    private TransactionsReportGenerator<Dataset, String> createReport;

    @Test
    public void process() {
        List<Transaction> transactions = getTransactions(10);
        Dataset dataset = Dataset.builder().name("trafi-transaction-report").transactions(transactions).build();
        createReport.process(dataset);
    }

    private List<Transaction> getTransactions(final String file) {
        return null;
    }

}