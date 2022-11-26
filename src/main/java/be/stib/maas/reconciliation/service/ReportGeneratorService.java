package be.stib.maas.reconciliation.service;

import be.stib.maas.reconciliation.model.Dataset;
import be.stib.maas.reconciliation.model.Transaction;
import be.stib.maas.reconciliation.report.TransactionsReportGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;

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
    public ResponseEntity<String> configuration(@RequestParam final String msp, @RequestParam final Date startDate, @RequestParam final Date endDate) {
        // TODO : Get the data from Trafi API for these dates
        String reportHtml = transactionsReportGenerator.process(getDataset(msp, startDate, endDate));
        return new ResponseEntity<>(reportHtml, HttpStatus.OK);
    }

    private Dataset getDataset(@RequestParam final String msp, @RequestParam final Date startDate, @RequestParam final Date endDate) {
        Transaction transaction = Transaction.builder().GrossTransactionalAmount(10.10).VatAmount(1.10).Amount(9.00).build();
        return Dataset.builder().name(msp).address("123 Fake Street, Springfield, 4321").transactions(Collections.singletonList(transaction)).startDate(startDate).endDate(endDate).build();
    }

}