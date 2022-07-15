package be.stib.maas.reconciliation.transform;

import be.stib.maas.reconciliation.model.Dataset;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TransformDatasetTest {

    @Spy
    private TransformDataset<?, ?> transformDataset;

    @Test
    public void process() {
        List<Dataset> datasets = transformDataset.process(Arrays.asList(
                new File("./src/main/resources/data/1000-bank-transaction-records.csv"),
                new File("./src/main/resources/data/1000-bank-transaction-records-missing.csv")));
        Assert.assertEquals(2, datasets.size());
    }

}