# MaaS PTO reconciliation technical approach

### Functionality
This component inputs data from two data sources, transforms the data into a format that can be matched, then 
matches(reconciles) the transactions. The output of the reconciliation is a csv file, which can then be used as input
to generate a PDF report for accounting to pay invoices, and to inspect transactions in the event of a dispute between 
the PTO and Stib/MaaS

### Technology stack
1) Spring