package be.stib.maas.reconciliation.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ReportRequest {

    private String msp;
    private Date startDate;
    private Date endDate;

}
