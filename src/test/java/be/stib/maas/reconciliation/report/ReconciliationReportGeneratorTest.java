package be.stib.maas.reconciliation.report;

import be.stib.maas.reconciliation.AbstractTest;
import be.stib.maas.reconciliation.model.Dataset;
import be.stib.maas.reconciliation.toolkit.DATASET_GENERATOR;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Michael Couck
 * @since 21-11-2022
 */
@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class ReconciliationReportGeneratorTest extends AbstractTest {

    @Spy
    private ReconciliationReportGenerator reconciliationReportGenerator;

    @Test
    public void process() throws IOException {
        Dataset datasetOne = DATASET_GENERATOR.getDataset("delijn", new Date(), new Date());
        Dataset datasetTwo = DATASET_GENERATOR.getDataset("ingenico", new Date(), new Date());
        String[] htmlTransactionsReport = reconciliationReportGenerator.process(new Dataset[]{datasetOne, datasetTwo});
        writeReport(Arrays.toString(htmlTransactionsReport), datasetOne);
    }

    @Test
    public void reconcileTransactions() throws IOException {

        Date startDate = new Date();
        Date endDate = new Date();

        Dataset datasetOne = DATASET_GENERATOR.getDataset("nmbs", startDate, endDate);
        Dataset datasetTwo = DATASET_GENERATOR.getDataset("worldline", startDate, endDate);

        datasetOne.getTransactions().add(datasetTwo.getTransactions().get(0));
        datasetTwo.getTransactions().add(datasetOne.getTransactions().get(5));

        int sizeOne = datasetOne.getTransactions().size();
        int sizeTwo = datasetTwo.getTransactions().size();
        log.info("One : {}, two : {}", sizeOne, sizeTwo);

        Dataset[] datasets = new Dataset[]{datasetOne, datasetTwo};
        reconciliationReportGenerator.reconcileTransactions(datasets);

        sizeOne = datasetOne.getTransactions().size();
        sizeTwo = datasetTwo.getTransactions().size();
        log.info("One : {}, two : {}", sizeOne, sizeTwo);

    }

}