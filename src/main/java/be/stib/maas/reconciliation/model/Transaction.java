package be.stib.maas.reconciliation.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class Transaction implements Comparable<Transaction> {

    private Date date;
    private String description;
    private int amount;

    @Override
    public int compareTo(final Transaction that) {
        return Integer.compare(this.hashCode(), that.hashCode());
    }
}