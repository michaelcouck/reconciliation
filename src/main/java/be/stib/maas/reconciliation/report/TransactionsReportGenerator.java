package be.stib.maas.reconciliation.report;

import be.stib.maas.reconciliation.Handler;
import be.stib.maas.reconciliation.model.Dataset;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.StringWriter;

/**
 * @author Michael Couck
 * @since 21-11-2022
 */
@Log
@NoArgsConstructor
public class TransactionsReportGenerator<I, O> implements Handler<Dataset, String> {

    @Override
    public String process(final Dataset dataset) {
        TemplateEngine engine = getTemplateEngine();
        Context context = getContext(dataset);
        StringWriter stringWriter = new StringWriter();
        engine.process("transaction-report", context, stringWriter);
        String html = stringWriter.toString();
        log.info(html);
        return html;
    }


}