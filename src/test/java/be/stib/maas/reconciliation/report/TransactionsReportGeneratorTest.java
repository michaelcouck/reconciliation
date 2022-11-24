package be.stib.maas.reconciliation.report;

import be.stib.maas.reconciliation.AbstractTest;
import be.stib.maas.reconciliation.model.Dataset;
import be.stib.maas.reconciliation.model.Transaction;
import be.stib.maas.reconciliation.toolkit.FILE;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class TransactionsReportGeneratorTest extends AbstractTest {

    @Spy
    private TransactionsReportGenerator<Dataset, String> createReport;

    @Test
    public void process() throws IOException {
        Dataset dataset = getDataset();
        String htmlTransactionsReport = createReport.process(dataset);
        Assert.assertTrue(htmlTransactionsReport.contains("Total:"));
        Assert.assertTrue(htmlTransactionsReport.contains("34.0"));
        FileUtils.write(new File(dataset.getName() + ".html"), htmlTransactionsReport, Charset.defaultCharset());
    }

    @Test
    public void calculateProductTotals() throws IOException {
        Dataset dataset = getDataset();
        createReport.calculateProductTotals(dataset);
        log.info("Product totals : {}", dataset.getProductTotals());

        Map<String, Double> nmbsTotals = dataset.getProductTotals().get("nmbs");
        nmbsTotals.values().removeAll(Arrays.asList(30.76D, 33.4D, 5.64D, 18.54D, 34.0D, 1.92D));
        Assert.assertTrue(nmbsTotals.values().isEmpty());

        Map<String, Double> deLijnTotals = dataset.getProductTotals().get("delijn");
        deLijnTotals.values().removeAll(Arrays.asList(30.76D, 33.4D, 5.64D, 18.54D, 34.0D, 1.92D));
        Assert.assertTrue(deLijnTotals.values().isEmpty());
    }

    private Dataset getDataset() throws IOException {
        Date startDate = new GregorianCalendar(2022, Calendar.OCTOBER, 1, 0, 0, 0).getTime();
        Date endDate = new GregorianCalendar(2022, Calendar.OCTOBER, 31, 23, 59, 59).getTime();
        return Dataset.builder()
                .name("De Lijn")
                .address("Accounts payable<br>" +
                        "De Lijn<br>" +
                        "20 Motstraat<br>" +
                        "Mechelen<br>" +
                        "2800<br>")
                .transactions(getTransactions("trafi-ticket-data-21-11-2022.json"))
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private List<Transaction> getTransactions(final String fileName) throws IOException {
        File file = FILE.findFileRecursively(new File("."), fileName);
        assert file != null;
        String ticketTransactionsJson = FileUtils.readFileToString(file, Charset.defaultCharset());
        Transaction[] transactions = new GsonBuilder().create().fromJson(ticketTransactionsJson, Transaction[].class);
        return Arrays.asList(transactions);
    }

}