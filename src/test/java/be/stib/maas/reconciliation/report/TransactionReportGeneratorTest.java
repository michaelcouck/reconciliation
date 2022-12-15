package be.stib.maas.reconciliation.report;

import be.stib.maas.reconciliation.AbstractTest;
import be.stib.maas.reconciliation.model.Dataset;
import be.stib.maas.reconciliation.toolkit.DATASET_GENERATOR;
import be.stib.maas.reconciliation.toolkit.FILE;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author Michael Couck
 * @since 15-12-2022
 */
@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class TransactionReportGeneratorTest extends AbstractTest {

    @Spy
    private TransactionReportGenerator transactionReportGenerator;

    @Test
    public void process() throws IOException {
        Dataset dataset = DATASET_GENERATOR.getDataset("nmbs", new Date(), new Date());
        String htmlTransactionsReport = transactionReportGenerator.process(dataset);
        File target = FILE.findFileRecursively(new File("."), "target");
        if (target != null) {
            FileUtils.write(new File(target, dataset.getName() + ".html"), htmlTransactionsReport, Charset.defaultCharset());
        }
    }

}