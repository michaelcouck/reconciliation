package be.stib.maas.reconciliation.purge;

import be.stib.maas.reconciliation.Handler;

/**
 * @author Michael Couck
 * @since 14-07-2022
 */
public class Cleanup<I, O> implements Handler<I, O> {

    @Override
    public O process(I input) {
        return null;
    }

}