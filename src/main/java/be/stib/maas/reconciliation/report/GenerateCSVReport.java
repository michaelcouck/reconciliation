package be.stib.maas.reconciliation.report;

import be.stib.maas.reconciliation.Handler;
import be.stib.maas.reconciliation.model.Dataset;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.util.List;

/**
 * @author Michael Couck
 * @since 22-07-2022
 */
@Log
@NoArgsConstructor
public class GenerateCSVReport<I, O> implements Handler<List<Dataset>, Void> {

    @Override
    public Void process(final List<Dataset> input) {
        // TODO : Generate CSV file from datasets for unmatched transactions
        return null;
    }

}