package be.stib.maas.reconciliation.service;

import be.stib.maas.reconciliation.model.Dataset;
import be.stib.maas.reconciliation.model.ReportRequest;
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

import java.util.Arrays;

@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportGeneratorService {

    private static final String COMMERCIAL_REPORT = "commercial-report";
    private static final String RECONCILIATION_REPORT = "reconciliation-report";

    private final CommercialReportGenerator transactionsReportGenerator;
    private final ReconciliationReportGenerator reconciliationReportGenerator;

    @Autowired
    public ReportGeneratorService(final CommercialReportGenerator transactionsReportGenerator, final ReconciliationReportGenerator reconciliationReportGenerator) {
        this.transactionsReportGenerator = transactionsReportGenerator;
        this.reconciliationReportGenerator = reconciliationReportGenerator;
    }

    @ResponseBody
    @RequestMapping(value = ReportGeneratorService.COMMERCIAL_REPORT, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> commercialReport(@RequestBody final ReportRequest reportRequest) {
        // TODO : Get the data from Trafi API for these dates
        Dataset dataset = DATASET_GENERATOR.getDataset(reportRequest.getMsp(), reportRequest.getStartDate(), reportRequest.getEndDate());
        String reportHtml = transactionsReportGenerator.process(dataset);
        return new ResponseEntity<>(reportHtml, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = ReportGeneratorService.RECONCILIATION_REPORT, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> reconciliationReport(@RequestBody final ReportRequest reportRequest, @RequestParam("ingenico-file") final MultipartFile ingenicoFile) {
        // TODO : Get the data from Trafi API for these dates
        Dataset datasetOne = DATASET_GENERATOR.getDataset(reportRequest.getMsp(), reportRequest.getStartDate(), reportRequest.getEndDate());
        // TODO : Build the dataset from the Ingenico data
        Dataset datasetTwo = DATASET_GENERATOR.getDataset(reportRequest.getMsp(), reportRequest.getStartDate(), reportRequest.getEndDate());
        String[] reportsHtml = reconciliationReportGenerator.process(new Dataset[] {datasetOne, datasetTwo});
        return new ResponseEntity<>(Arrays.toString(reportsHtml), HttpStatus.OK);
    }


}