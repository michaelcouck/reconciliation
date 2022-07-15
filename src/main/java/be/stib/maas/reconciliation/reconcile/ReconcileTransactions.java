package be.stib.maas.reconciliation.reconcile;

import be.stib.maas.reconciliation.Handler;
import be.stib.maas.reconciliation.model.Dataset;
import be.stib.maas.reconciliation.model.Transaction;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Consumer;

/**
 * @author Michael Couck
 * @since 14-07-2022
 */
@NoArgsConstructor
@SuppressWarnings("Convert2Lambda")
public class ReconcileTransactions<I, O> implements Handler<List<Dataset>, List<Dataset>> {

    @Override
    public List<Dataset> process(final List<Dataset> input) {
        final List<Dataset> unmatchedTransactionsDatasets = new ArrayList<>();
        input.forEach(new Consumer<Dataset>() {
            @Override
            public void accept(final Dataset source) {
                input.forEach(new Consumer<Dataset>() {
                    @Override
                    public void accept(final Dataset target) {
                        if (source.getName().equals(target.getName())) {
                            return;
                        }
                        Dataset unmatchedTransactionsDataset = reconcileDatasets(source, target);
                        if (unmatchedTransactionsDataset != null) {
                            unmatchedTransactionsDatasets.add(unmatchedTransactionsDataset);
                        }
                    }
                });
            }
        });
        return unmatchedTransactionsDatasets;
    }

    private Dataset reconcileDatasets(final Dataset datasetOne, final Dataset datasetTwo) {
        TreeSet<Transaction> one = new TreeSet<>(datasetOne.getTransactions());
        TreeSet<Transaction> two = new TreeSet<>(datasetTwo.getTransactions());
        one.removeAll(two);
        if (one.size() > 0) {
            String name = datasetOne.getName() + "-" + datasetTwo.getName();
            return Dataset.builder().name(name).transactions(new ArrayList<>(one)).build();
        }
        return null;
    }

}