package be.stib.maas.reconciliation.toolkit;

import be.stib.maas.reconciliation.model.Dataset;
import be.stib.maas.reconciliation.model.Transaction;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.util.*;

public class DATASET_GENERATOR {

    public static Dataset getDataset(final String msp, final Date startDate, final Date endDate) {
        // TODO: This is dummy data while we wait for the API from Trafi
        return Dataset.builder()
                .name(msp)
                .address("123 Fake Street, Springfield, 4321")
                .transactions(getTransaction(100))
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    public static List<Transaction> getTransaction(final int numberOfTransactions) {
        String alphabet = "abcdefg";
        String randomString = RandomStringUtils.random(6, alphabet);
        double randomDouble = BigDecimal.valueOf(new Random().nextDouble())
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < numberOfTransactions; i++) {
            Transaction transaction = Transaction.builder()
                    .VatAmount(randomDouble / 2)
                    .VatRatePercent((randomDouble / (randomDouble / 2)) * 100)
                    .Amount(randomDouble)
                    .GrossTransactionalAmount(randomDouble)
                    .BookingId(RandomStringUtils.random(6, alphabet))
                    .ProductId(randomString)
                    .ProviderId(randomString)
                    .PurchasedAtDate(randomString)
                    .PurchasedAtTime(randomString)
                    .UserId(randomString)
                    .build();
            transactions.add(transaction);
        }
        return transactions;
    }

    public static Dataset getDataset() throws IOException {
        Date startDate = new GregorianCalendar(2022, Calendar.OCTOBER, 1, 0, 0, 0).getTime();
        Date endDate = new GregorianCalendar(2022, Calendar.OCTOBER, 31, 23, 59, 59).getTime();
        List<Transaction> transactions = getTransactions("trafi-ticket-data-21-11-2022.json");
        return Dataset.builder()
                .name("De Lijn")
                .address("Accounts payable<br>" +
                        "De Lijn<br>" +
                        "20 Motstraat<br>" +
                        "Mechelen<br>" +
                        "2800<br>")
                .transactions(transactions)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    public static List<Transaction> getTransactions(final String fileName) throws IOException {
        File file = FILE.findFileRecursively(new File("."), fileName);
        assert file != null;
        String ticketTransactionsJson = FileUtils.readFileToString(file, Charset.defaultCharset());
        Transaction[] transactions = new GsonBuilder().create().fromJson(ticketTransactionsJson, Transaction[].class);
        return Arrays.asList(transactions);
    }

}