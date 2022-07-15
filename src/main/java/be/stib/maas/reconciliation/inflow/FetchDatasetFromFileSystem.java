package be.stib.maas.reconciliation.inflow;

import be.stib.maas.reconciliation.Handler;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Couck
 * @since 14-07-2022
 */
@Log
@NoArgsConstructor
@SuppressWarnings("unused")
public class FetchDatasetFromFileSystem<I, O> implements Handler<List<String>, List<File>> {

    /**
     * Fetches the data sets from the file system and presents them as a list files for further processing.
     *
     * @param filePaths the list of files on the file system where the transaction data sets reside
     * @return a list of files that contain the transactions
     */
    @Override
    public List<File> process(final List<String> filePaths) {
        log.info("File paths : " + filePaths);
        final List<File> files = new ArrayList<>();
        filePaths.forEach(filePath -> {
            File file = new File(filePath);
            files.add(file);
        });
        log.info("File : " + files);
        return files;
    }

}