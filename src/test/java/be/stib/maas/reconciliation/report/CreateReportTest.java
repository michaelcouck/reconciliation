package be.stib.maas.reconciliation.report;

import be.stib.maas.reconciliation.AbstractTest;
import be.stib.maas.reconciliation.model.Dataset;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CreateReportTest extends AbstractTest {

    @Spy
    private CreateReport<List<Dataset>, Void> createReport;

    @Test
    public void process() {
        Dataset dataset = Dataset.builder().name("Reconciliation Report").transactions(getTransactions(10)).build();
        createReport.process(Collections.singletonList(dataset));
    }

}