package be.stib.maas.reconciliation.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class Transaction implements Comparable<Transaction> {

    private String BookingId;
    private String ProviderId;
    private String UserId;

    private Date PurchasedAtDate;
    private Date PurchasedAtTime;
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