package be.stib.maas.reconciliation.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Michael Couck
 * @since 15-07-2022
 */
@Getter
@Setter
@Builder
public class Transaction {

    public static final String PRICE = "Sales: ";
    public static final String VAT = "VAT: ";
    public static final String TOTAL = "Total: ";

    private String BookingId;
    private String ProviderId;
    private String UserId;

    private String PurchasedAtDate;
    private String PurchasedAtTime;
    private double GrossTransactionalAmount;
    private double Amount;
    private double VatAmount;
    private double VatRatePercent;
    private String ProductId;

    private List<Ticket> Tickets;

}