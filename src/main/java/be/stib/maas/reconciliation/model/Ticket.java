package be.stib.maas.reconciliation.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author Michael Couck
 * @since 15-12-2022
 */
@Getter
@Setter
@Builder
public class Ticket {

    private String TicketId;
    private String ValidFrom;
    private String ValidTo;
    private String FirstValidatedOn; // First time the ticket was used
    private String InsertionDateInValidationDB; // For the metro gates eventually I suppose
    private String Issuer; // NMBS, De Lijn, Stib, Tec, etc.
    private String ProductId; // BMC contract identifier
    private String ProductDescription; // BMS contract description in English

    private double TicketValueBeforeConsumption; // For multi trip tickets
    private double TicketValueAfterConsumption; // Needs a calculation to account for trips that are taken
    private double ConsumptionPrice; // Needs calculation, example 5 trips for €10, this trip 10 / 5 = €2

    private String CardId; // The client identifier in Trafi database, or the Mobib card number for example

    private int TotalCount;
    private int Remaining;

}