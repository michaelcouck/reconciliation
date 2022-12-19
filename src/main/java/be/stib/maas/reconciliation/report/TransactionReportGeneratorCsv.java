package be.stib.maas.reconciliation.report;

import be.stib.maas.reconciliation.Handler;
import be.stib.maas.reconciliation.model.Dataset;
import be.stib.maas.reconciliation.model.Ticket;
import be.stib.maas.reconciliation.model.Transaction;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * @author Michael Couck
 * @since 19-12-2022
 */
@Slf4j
@Service
@NoArgsConstructor
public class TransactionReportGeneratorCsv implements Handler<Dataset, String> {

    @SuppressWarnings("Convert2Lambda")
    @Override
    public String process(final Dataset dataset) {
        StringBuilder stringBuilder = new StringBuilder();
        addHeaders(stringBuilder);
        dataset.getTransactions().forEach(new Consumer<Transaction>() {
            @Override
            public void accept(final Transaction transaction) {
                transaction.getTickets().forEach(new Consumer<Ticket>() {
                    @Override
                    public void accept(final Ticket ticket) {
                        stringBuilder.append(ticket.getIssuer()).append(",");
                        stringBuilder.append(transaction.getBookingId()).append(",");
                        stringBuilder.append(transaction.getPurchasedAtDate()).append(",");
                        stringBuilder.append(transaction.getGrossTransactionalAmount()).append(",");
                        stringBuilder.append(transaction.getUserId()).append(",");
                        stringBuilder.append(ticket.getProductId()).append(",");
                        stringBuilder.append(ticket.getProductDescription()).append(",");
                        stringBuilder.append(ticket.getTotalCount()).append(",");
                        stringBuilder.append(ticket.getRemaining()).append(",");
                        stringBuilder.append(ticket.getTicketValueBeforeConsumption()).append(",");
                        stringBuilder.append(ticket.getTicketValueAfterConsumption()).append(",");
                        stringBuilder.append(ticket.getValidFrom()).append(",");
                        stringBuilder.append(ticket.getValidTo()).append("\n");
                    }
                });
            }
        });
        return stringBuilder.toString();
    }

    private void addHeaders(final StringBuilder stringBuilder) {
        stringBuilder
                .append("Issuer").append(",")
                .append("Booking ID").append(",")
                .append("Purchase Date").append(",")
                .append("Gross Transaction Amount").append(",")
                .append("User ID").append(",")
                .append("Product ID").append(",")
                .append("Product Description").append(",")
                .append("Total Count").append(",")
                .append("Remaining").append(",")
                .append("Value Before Consumption").append(",")
                .append("Value After Consumption").append(",")
                .append("Valid From").append(",")
                .append("Valid To").append("\n");
    }

}