package be.stib.maas.reconciliation.report;

import be.stib.maas.reconciliation.Handler;
import be.stib.maas.reconciliation.model.Dataset;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Michael Couck
 * @since 14-07-2022
 */
@Log
@NoArgsConstructor
public class UnmatchedTransactionsReportGenerator<I, O> implements Handler<List<Dataset>, List<Dataset>> {

    @Override
    @SuppressWarnings("Convert2Lambda")
    public List<Dataset> process(final List<Dataset> input) {
        TemplateEngine engine = getTemplateEngine();
        input.forEach(new Consumer<Dataset>() {
            @SneakyThrows
            @Override
            public void accept(final Dataset dataset) {
                Context context = getContext(dataset);
                StringWriter stringWriter = new StringWriter();
                engine.process("unmatched-transactions", context, stringWriter);
                String html = stringWriter.toString();
                log.info(html);
                FileUtils.write(new File(dataset.getName() + "-unmatched-transactions-report.html"), html, Charset.defaultCharset());
            }
        });
        return input;
    }

}