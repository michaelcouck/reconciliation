package be.stib.maas.reconciliation;

import be.stib.maas.reconciliation.model.Dataset;
import be.stib.maas.reconciliation.toolkit.FILE;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public abstract class AbstractTest {

    public void writeReport(final String htmlReport, final Dataset dataset) throws IOException {
        File target = FILE.findFileRecursively(new File("."), "target");
        if (target != null) {
            FileUtils.write(new File(target, dataset.getName() + ".html"), htmlReport, Charset.defaultCharset());
        }
    }

}