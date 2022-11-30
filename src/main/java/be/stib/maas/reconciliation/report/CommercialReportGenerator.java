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
 * @since 21-11-2022
 */
@Slf4j
@Service
@NoArgsConstructor
public class CommercialReportGenerator implements Handler<Dataset, String> {

    @Override
    public String process(final Dataset dataset) {
        setTotals(dataset);
        TemplateEngine engine = getTemplateEngine();
        Context context = getContext(dataset);
        StringWriter stringWriter = new StringWriter();
        engine.process("commercial-report", context, stringWriter);
        return stringWriter.toString();
    }

}