package be.stib.maas.reconciliation.purge;

import be.stib.maas.reconciliation.Config;
import be.stib.maas.reconciliation.Handler;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.function.Consumer;

/**
 * @author Michael Couck
 * @since 14-07-2022
 */
@Log
@NoArgsConstructor
public class Cleanup<I, O> implements Handler<Object, Void> {

    private Config config;

    public Cleanup(final Config config) {
        this.config = config;
    }

    @Override
    @SuppressWarnings("Convert2Lambda")
    public Void process(final Object input) {
        config.getFileSystemDataSets().forEach(new Consumer<String>() {
            @Override
            @SneakyThrows
            public void accept(final String fileName) {
                File source = new File(config.getDataDirectory(), fileName);
                File target = new File(config.getDeletedDirectory(), fileName);
                log.info("Moving file : " + source + ", to : " + target);
                FileUtils.moveFile(source, target);
                Thread.sleep(250);
            }
        });
        return null;
    }

}