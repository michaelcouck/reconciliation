package be.stib.maas.reconciliation.toolkit;

import be.stib.maas.reconciliation.model.Dataset;
import be.stib.maas.reconciliation.model.Ticket;
import be.stib.maas.reconciliation.model.Transaction;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class DATASET_GENERATOR {

    private static final String CHARACTERS = "abcdefg";
    private static final Calendar CALENDAR = Calendar.getInstance();
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        CALENDAR.set(2022, Calendar.DECEMBER, 1, 5, 28, 56);
    }

    public static Dataset getDataset() throws IOException {
        Date startDate = new GregorianCalendar(2022, Calendar.OCTOBER, 1, 0, 0, 0).getTime();
        Date endDate = new GregorianCalendar(2022, Calendar.OCTOBER, 31, 23, 59, 59).getTime();
        List<Transaction> transactions = getTransactions("trafi-ticket-data-21-11-2022.json");
        return Dataset.builder()
                .name("de-lijn")
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

    public static Dataset getDataset(final String msp, final Date startDate, final Date endDate) {
        // TODO: This is dummy data while we wait for the API from Trafi
        return Dataset.builder()
                .name(msp)
                .address("123 Fake Street, Springfield, 4321")
                .transactions(getTransaction(10))
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    public static List<Transaction> getTransaction(final int numberOfTransactions) {
        List<Transaction> transactions = new ArrayList<>();

        Date date = CALENDAR.getTime();
        Random random = new Random();
        for (int i = 0; i < numberOfTransactions; i++) {

            double amount = random.nextInt(50);
            double vatPercent = random.nextInt(21);

            double vatAmount = amount * vatPercent / 100;
            double grossTransactionalAmount = round(amount + vatAmount);

            Transaction transaction = Transaction.builder()
                    .UserId(RandomStringUtils.random(3, CHARACTERS))
                    .ProviderId("NMBS")

                    .VatRatePercent(vatPercent)
                    .Amount(amount)
                    .VatAmount(vatAmount)
                    .GrossTransactionalAmount(grossTransactionalAmount)

                    .BookingId(RandomStringUtils.random(3, CHARACTERS))
                    .ProductId(RandomStringUtils.random(3, CHARACTERS))
                    .PurchasedAtDate(DATE_FORMAT.format(date))
                    .PurchasedAtTime(DATE_FORMAT.format(date))
                    .Tickets(getTickets(3))
                    .build();
            transactions.add(transaction);
        }
        return transactions;
    }

    private static double round(final double amount) {
        return BigDecimal.valueOf(amount)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    @SuppressWarnings("SameParameterValue")
    public static List<Transaction> getTransactions(final String fileName) throws IOException {
        File file = FILE.findFileRecursively(new File("."), fileName);
        assert file != null;
        String ticketTransactionsJson = FileUtils.readFileToString(file, Charset.defaultCharset());
        Transaction[] transactions = new GsonBuilder().create().fromJson(ticketTransactionsJson, Transaction[].class);
        return Arrays.asList(transactions);
    }

    public static List<Ticket> getTickets(final int numberOfTickets) {
        List<Ticket> tickets = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < numberOfTickets; i++) {

            Date firstValidatedOn = CALENDAR.getTime();
            CALENDAR.add(Calendar.HOUR, 2);
            Date validTo = CALENDAR.getTime();

            int totalCount = random.nextInt(8) + 2;
            int remaining = random.nextInt(totalCount);

            double ticketValueBeforeConsumption = random.nextInt(50);
            double ticketValueAfterConsumption = round(ticketValueBeforeConsumption * remaining / totalCount);
            double consumptionPrice = round(ticketValueBeforeConsumption - ticketValueAfterConsumption);
            Ticket ticket = Ticket.builder()
                    .Issuer("NMBS")
                    .CardId(RandomStringUtils.random(3, CHARACTERS))
                    .TicketId(RandomStringUtils.random(3, CHARACTERS))
                    .ProductId(RandomStringUtils.random(3, CHARACTERS))
                    .TicketValueBeforeConsumption(ticketValueBeforeConsumption)
                    .TicketValueAfterConsumption(ticketValueAfterConsumption)
                    .ConsumptionPrice(consumptionPrice)
                    .ProductDescription("000 Point to point ticket")
                    .FirstValidatedOn(DATE_FORMAT.format(firstValidatedOn))
                    .InsertionDateInValidationDB(DATE_FORMAT.format(firstValidatedOn))
                    .ValidFrom(DATE_FORMAT.format(firstValidatedOn))
                    .ValidTo(DATE_FORMAT.format(validTo))
                    .TotalCount(totalCount)
                    .Remaining(remaining)
                    .build();
            tickets.add(ticket);
        }
        return tickets;
    }

}