package be.stib.maas.reconciliation.report;

import be.stib.maas.reconciliation.Handler;
import be.stib.maas.reconciliation.model.Dataset;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.File;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * @author Michael Couck
 * @since 14-07-2022
 */
@Log
@NoArgsConstructor
public class CreateReport<I, O> implements Handler<List<Dataset>, List<Dataset>> {

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

    private TemplateEngine getTemplateEngine() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setPrefix("template/");
        resolver.setSuffix(".html");
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);
        return engine;
    }

    private Context getContext(final Dataset dataset) {
        Context context = new Context(Locale.ENGLISH);
        String now = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        context.setVariable("date", now);
        context.setVariable("dataset", dataset);
        context.setVariable("transactions", dataset.getTransactions());
        return context;
    }

}