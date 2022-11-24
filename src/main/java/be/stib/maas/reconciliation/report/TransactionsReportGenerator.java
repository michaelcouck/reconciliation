package be.stib.maas.reconciliation.report;

import be.stib.maas.reconciliation.Handler;
import be.stib.maas.reconciliation.model.Dataset;
import be.stib.maas.reconciliation.model.Transaction;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Michael Couck
 * @since 21-11-2022
 */
@Slf4j
@NoArgsConstructor
public class TransactionsReportGenerator<I, O> implements Handler<Dataset, String> {

    @Override
    public String process(final Dataset dataset) {
        calculateProductTotals(dataset);
        TemplateEngine engine = getTemplateEngine();
        Context context = getContext(dataset);
        StringWriter stringWriter = new StringWriter();
        engine.process("commercial-report", context, stringWriter);
        return stringWriter.toString();
    }

    @SuppressWarnings("Convert2Lambda")
    protected void calculateProductTotals(final Dataset dataset) {
        dataset.setProductTotals(new HashMap<>());
        final Map<String, Map<String, Double>> productTotals = dataset.getProductTotals();
        dataset.getTransactions().forEach(new Consumer<Transaction>() {
            @Override
            public void accept(final Transaction transaction) {
                Map<String, Double> totals = productTotals.computeIfAbsent(transaction.getProviderId(), k -> new HashMap<>());
                totals.put(Transaction.PRICE, round(totals.getOrDefault(Transaction.PRICE, 0D) + transaction.getAmount()));
                totals.put(Transaction.VAT, round(totals.getOrDefault(Transaction.VAT, 0D) + transaction.getVatAmount()));
                totals.put(Transaction.TOTAL, round(totals.getOrDefault(Transaction.TOTAL, 0D) + transaction.getGrossTransactionalAmount()));
            }
        });
    }

    private Double round(final Double d) {
        return BigDecimal.valueOf(d)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

}