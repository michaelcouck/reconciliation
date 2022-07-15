package be.stib.maas.reconciliation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Michael Couck
 * @since 14-07-2022
 */
public class Pipeline<I, O> {

    private final List<Handler<I, O>> handlers;

    @SuppressWarnings("unused")
    public Pipeline() {
        handlers = new ArrayList<>();
    }

    public void registerHandler(final Handler<I, O> handler) {
        handlers.add(handler);
    }

    @SuppressWarnings("unchecked")
    public void processHandlers(final I input) {
        AtomicReference<Object> inputReference = new AtomicReference<>(input);
        handlers.forEach(handler -> {
            Object output = handler.process((I) inputReference.get());
            inputReference.set(output);
        });
    }

}