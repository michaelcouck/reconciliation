package be.stib.maas.reconciliation.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class Dataset {

    private String name;
    private String address;
    private Date startDate;
    private Date endDate;
    private List<Transaction> transactions;
    // MSP identifier : {ticket price, vat, total}
    private Map<String, Map<String, Double>> productTotals;

}