package be.stib.maas.reconciliation.service;

import be.stib.maas.reconciliation.model.Dataset;
import be.stib.maas.reconciliation.model.ReportRequest;
import be.stib.maas.reconciliation.model.Transaction;
import be.stib.maas.reconciliation.report.TransactionsReportGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
@RestController
@SuppressWarnings({"WeakerAccess"})
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportGeneratorService {

    private static final String COMMERCIAL_REPORT = "commercial-report";

    private final TransactionsReportGenerator transactionsReportGenerator;

    @Autowired
    public ReportGeneratorService(final TransactionsReportGenerator transactionsReportGenerator) {
        this.transactionsReportGenerator = transactionsReportGenerator;
    }

    @ResponseBody
    @RequestMapping(value = ReportGeneratorService.COMMERCIAL_REPORT, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> configuration(@RequestBody final ReportRequest reportRequest) {
        // TODO : Get the data from Trafi API for these dates
        Dataset dataset = getDataset(reportRequest.getMsp(), reportRequest.getStartDate(), reportRequest.getEndDate());
        String reportHtml = transactionsReportGenerator.process(dataset);
        return new ResponseEntity<>(reportHtml, HttpStatus.OK);
    }

    private Dataset getDataset(@RequestParam final String msp, @RequestParam final Date startDate, @RequestParam final Date endDate) {
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
    private List<Transaction> getTransaction(final int numberOfTransactions) {
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
                    .BookingId(randomString)
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

}