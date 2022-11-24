package be.stib.maas.reconciliation.transform;

import be.stib.maas.reconciliation.Handler;
import be.stib.maas.reconciliation.model.Dataset;
import be.stib.maas.reconciliation.model.Transaction;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Michael Couck
 * @since 14-07-2022
 */
@Log
@NoArgsConstructor
@SuppressWarnings("Convert2Lambda")
public class TransformDataset<I, O> implements Handler<List<File>, List<Dataset>> {

    @Override
    public List<Dataset> process(final List<File> input) {
        final List<Dataset> dataSets = new ArrayList<>();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        input.forEach(new Consumer<File>() {
            @SneakyThrows
            @Override
            public void accept(final File file) {
                final Dataset dataset = Dataset.builder().name(file.getName()).transactions(new ArrayList<>()).build();
                FileInputStream fileInputStream = new FileInputStream(file);
                List<String> lines = IOUtils.readLines(fileInputStream, Charset.defaultCharset().displayName());
                lines.remove(0);
                lines.forEach(new Consumer<String>() {
                    @SneakyThrows
                    @Override
                    public void accept(final String line) {
                        String[] segments = line.split(",");
                        Date date = simpleDateFormat.parse(segments[0]);
                        String description = segments[1];
                        double amount = Double.parseDouble(segments[2]);
                        Transaction transaction = Transaction.builder().PurchasedAtDate(date.toString()).ProductId(description).GrossTransactionalAmount((int) amount).build();
                        dataset.getTransactions().add(transaction);
                    }
                });
                dataSets.add(dataset);
            }
        });
        return dataSets;
    }

}