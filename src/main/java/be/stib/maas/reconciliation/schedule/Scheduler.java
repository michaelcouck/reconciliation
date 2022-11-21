package be.stib.maas.reconciliation.schedule;

import be.stib.maas.reconciliation.Config;
import be.stib.maas.reconciliation.Pipeline;
import be.stib.maas.reconciliation.inflow.FetchDatasetFromFileSystem;
import be.stib.maas.reconciliation.purge.Cleanup;
import be.stib.maas.reconciliation.reconcile.ReconcileTransactions;
import be.stib.maas.reconciliation.report.UnmatchedTransactionsReportGenerator;
import be.stib.maas.reconciliation.report.GenerateCSVReport;
import be.stib.maas.reconciliation.transform.TransformDataset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Michael Couck
 * @since 14-07-2022
 */
@Slf4j
@EnableScheduling
@Component(value = "reconciliation-scheduler")
@ConditionalOnProperty(name = "scheduling", havingValue = "true")
public class Scheduler {

    private static final long ONE_SECOND = 1000;
    private static final long FIVE_SECONDS = ONE_SECOND * 5;
    public static final long FIFTEEN_SECONDS = FIVE_SECONDS * 3;
    private static final long THIRTY_SECONDS = FIFTEEN_SECONDS * 2;
    private static final long ONE_MINUTE = THIRTY_SECONDS * 2;

    private final Config config;

    @Autowired
    public Scheduler(final Config config) {
        this.config = config;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Scheduled(initialDelay = ONE_MINUTE, fixedRate = ONE_MINUTE)
    public void reconcile() {
        log.debug("Processing reconciliation data : ");
        Pipeline<Object, Object> pipeline = new Pipeline<>();
        pipeline.registerHandler(new FetchDatasetFromFileSystem());
        pipeline.registerHandler(new TransformDataset());
        pipeline.registerHandler(new ReconcileTransactions());
        pipeline.registerHandler(new UnmatchedTransactionsReportGenerator());
        pipeline.registerHandler(new GenerateCSVReport());
        pipeline.registerHandler(new Cleanup(config));
        pipeline.processHandlers(config.getFilePaths());
    }

}