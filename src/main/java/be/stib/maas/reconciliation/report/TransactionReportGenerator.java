package be.stib.maas.reconciliation.report;

import be.stib.maas.reconciliation.Handler;
import be.stib.maas.reconciliation.model.Dataset;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.StringWriter;

/**
 * @author Michael Couck
 * @since 15-12-2022
 */
@Slf4j
@Service
@NoArgsConstructor
public class TransactionReportGenerator implements Handler<Dataset, String> {

    @Override
    public String process(final Dataset dataset) {
        setTotals(dataset);
        return generateReport(dataset);
    }

    private String generateReport(final Dataset dataset) {
        TemplateEngine engine = getTemplateEngine();
        Context context = getContext(dataset);
        StringWriter stringWriter = new StringWriter();
        engine.process("transaction-report", context, stringWriter);
        return stringWriter.toString();
    }

}