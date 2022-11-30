package be.stib.maas.reconciliation.report;

import be.stib.maas.reconciliation.Handler;
import be.stib.maas.reconciliation.model.Dataset;
import be.stib.maas.reconciliation.model.Transaction;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.StringWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author Michael Couck
 * @since 30-11-2022
 */
@Slf4j
@Service
@NoArgsConstructor
public class ReconciliationReportGenerator implements Handler<Dataset[], String[]> {

    @Override
    public String[] process(final Dataset[] datasets) {
        reconcileTransactions(datasets);
        String reportOne = generateReport(datasets[0]);
        String reportTwo = generateReport(datasets[1]);
        return new String[]{reportOne, reportTwo};
    }

    private String generateReport(final Dataset dataset) {
        TemplateEngine engine = getTemplateEngine();
        Context context = getContext(dataset);
        StringWriter stringWriter = new StringWriter();
        engine.process("reconciliation-report", context, stringWriter);
        return stringWriter.toString();
    }

    @SuppressWarnings("Java8ListSort")
    protected void reconcileTransactions(final Dataset[] datasets) {
        Dataset dataSetOne = datasets[0];
        Dataset dataSetTwo = datasets[1];

        List<Transaction> transactionsOne = dataSetOne.getTransactions();
        List<Transaction> transactionsTwo = dataSetTwo.getTransactions();

        Comparator<Transaction> bookingIdComparator = Comparator.comparing(Transaction::getBookingId);

        Collections.sort(transactionsOne, bookingIdComparator);
        Collections.sort(transactionsTwo, bookingIdComparator);

        removeMatchedTransactions(transactionsOne, transactionsTwo);
        removeMatchedTransactions(transactionsTwo, transactionsOne);

        setTotals(dataSetOne);
        setTotals(dataSetTwo);
    }

    protected void removeMatchedTransactions(final List<Transaction> transactionsOne, final List<Transaction> transactionsTwo) {
        final Comparator<Transaction> bookingIdComparator = Comparator.comparing(Transaction::getBookingId);
        Iterator<Transaction> transactionsOneIterator = transactionsOne.iterator();
        while (transactionsOneIterator.hasNext()) {
            Transaction transactionOne = transactionsOneIterator.next();
            int index = Collections.binarySearch(transactionsTwo, transactionOne, bookingIdComparator);
            if (index >= 0) {
                transactionsOneIterator.remove();
                transactionsTwo.remove(index);
            }
        }
    }

}