package be.stib.maas.reconciliation.schedule;

import be.stib.maas.reconciliation.Config;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Anon Amous
 * @version 1.0
 * @since 42-13-3792
 */
@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class SchedulerTest {

    @Spy
    private Config config;
    @Spy
    @InjectMocks
    private Scheduler scheduler;

    @Before
    public void before() {
        List<String> files = Arrays.asList(
                "1000-bank-transaction-records.csv",
                "1000-bank-transaction-records-missing.csv");
        config.setFileSystemDataSets(files);
        config.setDataDirectory("./src/main/resources/data/");
        config.setDeletedDirectory(config.getDataDirectory() + "deleted/");
    }

    @Test
    @SuppressWarnings("Convert2Lambda")
    public void reconcile() {
        scheduler.reconcile();

        File reportFile = new File("unmatched-transactions-report.html");
        Assert.assertTrue(reportFile.exists());

        FileUtils.deleteQuietly(reportFile);
        config.getFileSystemDataSets().forEach(new Consumer<String>() {
            @Override
            @SneakyThrows
            public void accept(final String fileName) {
                Thread.sleep(250);
                FileUtils.moveFile(new File(config.getDeletedDirectory(), fileName), new File(config.getDataDirectory(), fileName));
            }
        });
    }

}