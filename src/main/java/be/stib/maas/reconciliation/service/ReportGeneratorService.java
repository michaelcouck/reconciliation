package be.stib.maas.reconciliation.service;

import be.stib.maas.reconciliation.model.Dataset;
import be.stib.maas.reconciliation.report.CommercialReportGenerator;
import be.stib.maas.reconciliation.report.ReconciliationReportGenerator;
import be.stib.maas.reconciliation.report.TransactionReportGenerator;
import be.stib.maas.reconciliation.toolkit.DATASET_GENERATOR;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

@SuppressWarnings("DuplicatedCode")
@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportGeneratorService {

    private static final String COMMERCIAL_REPORT = "commercial-report";
    private static final String RECONCILIATION_REPORT = "reconciliation-report";
    private static final String TRANSACTION_REPORT = "transaction-report";

    private final CommercialReportGenerator commercialReportGenerator;
    private final ReconciliationReportGenerator reconciliationReportGenerator;
    private final TransactionReportGenerator transactionReportGenerator;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public ReportGeneratorService(
            final CommercialReportGenerator commercialReportGenerator,
            final ReconciliationReportGenerator reconciliationReportGenerator,
            final TransactionReportGenerator transactionReportGenerator) {
        this.commercialReportGenerator = commercialReportGenerator;
        this.reconciliationReportGenerator = reconciliationReportGenerator;
        this.transactionReportGenerator = transactionReportGenerator;
    }

    @ResponseBody
    @RequestMapping(
            value = ReportGeneratorService.COMMERCIAL_REPORT,
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> commercialReport(
            @RequestParam("msp") final String msp,
            @RequestParam("startDate") final String startDate,
            @RequestParam("endDate") final String endDate) throws ParseException {
        // TODO : Get the data from Trafi API for these dates
        Dataset dataset = DATASET_GENERATOR.getDataset(msp, simpleDateFormat.parse(startDate), simpleDateFormat.parse(endDate));
        String reportHtml = commercialReportGenerator.process(dataset);
        return new ResponseEntity<>(reportHtml, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = ReportGeneratorService.RECONCILIATION_REPORT, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> reconciliationReport(
            @RequestParam("msp") final String msp,
            @RequestParam("startDate") final String startDate,
            @RequestParam("endDate") final String endDate,
            @RequestParam("ingenico-file") final MultipartFile multipartFormData) throws IOException, ParseException {
        // log.info("File data : {}", new String(multipartFormData.getBytes()));
        // TODO : Get the data from Trafi API for these dates
        Dataset datasetOne = DATASET_GENERATOR.getDataset(msp, simpleDateFormat.parse(startDate), simpleDateFormat.parse(endDate));
        // TODO : Build the dataset from the Ingenico data
        Dataset datasetTwo = DATASET_GENERATOR.getDataset("ingenico", simpleDateFormat.parse(startDate), simpleDateFormat.parse(endDate));
        String[] reportsHtml = reconciliationReportGenerator.process(new Dataset[]{datasetOne, datasetTwo});
        final StringBuilder reportHtml = new StringBuilder();
        Arrays.stream(reportsHtml).forEach(reportHtml::append);
        return new ResponseEntity<>(reportHtml.toString(), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = ReportGeneratorService.TRANSACTION_REPORT, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> transactionReport(
            @RequestParam("msp") final String msp,
            @RequestParam("startDate") final String startDate,
            @RequestParam("endDate") final String endDate) throws ParseException {
        // TODO : Get the data from Trafi API for these dates
        Dataset dataset = DATASET_GENERATOR.getDataset(msp, simpleDateFormat.parse(startDate), simpleDateFormat.parse(endDate));
        String reportHtml = transactionReportGenerator.process(dataset);
        return new ResponseEntity<>(reportHtml, HttpStatus.OK);
    }

}