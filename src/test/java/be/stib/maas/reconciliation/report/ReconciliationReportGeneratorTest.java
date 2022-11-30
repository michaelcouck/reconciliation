package be.stib.maas.reconciliation.report;

import be.stib.maas.reconciliation.AbstractTest;
import be.stib.maas.reconciliation.model.Dataset;
import be.stib.maas.reconciliation.toolkit.DATASET_GENERATOR;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
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
        FileUtils.write(new File("reconciliation-report.html"), Arrays.toString(htmlTransactionsReport), Charset.defaultCharset());
    }

}