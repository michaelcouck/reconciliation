package be.stib.maas.reconciliation.purge;

import be.stib.maas.reconciliation.Config;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.inject.Inject;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@RunWith(MockitoJUnitRunner.class)
public class CleanupTest {

    @Spy
    private Config config;
    @Spy
    @Inject
    private Cleanup<List<String>, List<File>> cleanup;

    @Before
    public void before() {
        List<String> files = Arrays.asList(
                "1000-bank-transaction-records.csv",
                "1000-bank-transaction-records-missing.csv");
        config.setFileSystemDataSets(files);
        config.setDataDirectory("./src/main/resources/data/");
        config.setDeletedDirectory(config.getDataDirectory() + "deleted/");
        ReflectionTestUtils.setField(cleanup, "config", config);
    }

    @Test
    @SuppressWarnings({"Convert2Lambda", "ConstantConditions"})
    public void process() {
        try {
            cleanup.process(new Object());
            File[] files = new File(config.getDataDirectory()).listFiles();
            Assert.assertEquals(1, files.length);
            File[] movedFiles = new File(config.getDeletedDirectory()).listFiles();
            Assert.assertEquals(2, movedFiles.length);
        } finally {
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

}