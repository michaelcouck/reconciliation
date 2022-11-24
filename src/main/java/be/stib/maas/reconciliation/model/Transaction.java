package be.stib.maas.reconciliation.model;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class Transaction implements Comparable<Transaction> {

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

    @Override
    public int compareTo(final Transaction that) {
        return Integer.compare(this.hashCode(), that.hashCode());
    }
}