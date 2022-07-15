package be.stib.maas.reconciliation.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Dataset {

    private String name;
    private List<Transaction> transactions;

}