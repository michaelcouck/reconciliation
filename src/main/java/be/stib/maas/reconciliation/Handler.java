package be.stib.maas.reconciliation;

/**
 * @author Michael Couck
 * @since 14-07-2022
 */
public interface Handler<I, O> {

    O process(final I input);

}