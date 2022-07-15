package be.stib.maas.reconciliation.inflow;

import lombok.extern.java.Log;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Log
@RunWith(MockitoJUnitRunner.class)
public class FetchDatasetFromFileSystemTest {

    @Spy
    private FetchDatasetFromFileSystem<?, ?> fetchDatasetFromFileSystem;

    @Test
    public void process() throws IOException {
        List<String> filePaths = Arrays.asList(
                "./src/main/resources/data/1000-bank-transaction-records.csv",
                "./src/main/resources/data/1000-bank-transaction-records-missing.csv");
        List<File> files = fetchDatasetFromFileSystem.process(filePaths);
        Assert.assertEquals(2, files.size());
        for (final File file : files) {
            log.info("File absolute path : " + file.getAbsolutePath());
            Assert.assertTrue(file.exists());
        }
    }

}