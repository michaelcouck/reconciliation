package be.stib.maas.reconciliation;

import be.stib.maas.reconciliation.model.Dataset;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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

}