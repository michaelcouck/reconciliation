package be.stib.maas.reconciliation;

import be.stib.maas.reconciliation.model.Dataset;
import be.stib.maas.reconciliation.model.Transaction;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author Michael Couck
 * @since 14-07-2022
 */
public interface Handler<I, O> {

    O process(final I input);

    default TemplateEngine getTemplateEngine() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setPrefix("template/");
        resolver.setSuffix(".html");
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);
        return engine;
    }

    default Context getContext(final Dataset dataset) {
        Context context = new Context(Locale.ENGLISH);
        String now = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        context.setVariable("date", now);
        context.setVariable("dataset", dataset);
        context.setVariable("transactions", dataset.getTransactions());
        return context;
    }

    @SuppressWarnings("Convert2Lambda")
    default void setTotals(final Dataset dataset) {
        List<Transaction> transactionsOne = dataset.getTransactions();
        final Map<String, Map<String, Double>> productTotals = new HashMap<>();
        transactionsOne.forEach(new Consumer<Transaction>() {
            @Override
            public void accept(final Transaction transaction) {
                Map<String, Double> totals = productTotals.computeIfAbsent(transaction.getProviderId(), k -> new HashMap<>());
                totals.put(Transaction.PRICE, round(totals.getOrDefault(Transaction.PRICE, 0D) + transaction.getAmount()));
                totals.put(Transaction.VAT, round(totals.getOrDefault(Transaction.VAT, 0D) + transaction.getVatAmount()));
                totals.put(Transaction.TOTAL, round(totals.getOrDefault(Transaction.TOTAL, 0D) + transaction.getGrossTransactionalAmount()));
            }
        });
        dataset.setProductTotals(productTotals);
    }

    default Double round(final Double d) {
        return BigDecimal.valueOf(d)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

}