package be.stib.maas.reconciliation.service;

import be.stib.maas.reconciliation.model.Dataset;
import be.stib.maas.reconciliation.report.CommercialReportGenerator;
import be.stib.maas.reconciliation.report.ReconciliationReportGenerator;
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
import java.util.function.Consumer;

@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportGeneratorService {

    private static final String COMMERCIAL_REPORT = "commercial-report";
    private static final String RECONCILIATION_REPORT = "reconciliation-report";

    private final CommercialReportGenerator transactionsReportGenerator;
    private final ReconciliationReportGenerator reconciliationReportGenerator;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public ReportGeneratorService(final CommercialReportGenerator transactionsReportGenerator, final ReconciliationReportGenerator reconciliationReportGenerator) {
        this.transactionsReportGenerator = transactionsReportGenerator;
        this.reconciliationReportGenerator = reconciliationReportGenerator;
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
        String reportHtml = transactionsReportGenerator.process(dataset);
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

}