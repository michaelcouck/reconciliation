function setContent(element, page) {
    const section = $("#" + element);
    section.children().remove();
    section.load(page);
}

function setElementContent(element, content) {
    const section = $("#" + element);
    section.children().remove();
    $('#' + element).html(content);
}

function generateCommercialReport() {
    const form = document.querySelector('#commercial-report-form');
    const formData = new FormData(form);
    axios.post('/report/commercial-report',
        formData,
        {headers: {'Content-Type': 'multipart/form-data', 'Accept': 'text/html'}}
    ).then(function (response) {
        setElementContent("report-result", response.data);
    }).catch(function (error) {
        console.log(error);
        alert('Error from server : ' + error);
    });
}

function generateReconciliationReport() {
    const form = document.querySelector('#reconciliation-report-form');
    const formData = new FormData(form);
    axios.post('/report/reconciliation-report',
        formData,
        {headers: {'Content-Type': 'multipart/form-data'}}
    ).then(function (response) {
        setElementContent("report-result", response.data);
    }).catch(function (error) {
        console.log(error);
        alert('Error from server : ' + error);
    });
}

function generateTransactionReport() {
    const form = document.querySelector('#transaction-report-form');
    const formData = new FormData(form);
    axios.post('/report/transaction-report',
        formData,
        {headers: {'Content-Type': 'multipart/form-data'}}
    ).then(function (response) {
        setElementContent("report-result", response.data);
    }).catch(function (error) {
        console.log(error);
        alert('Error from server : ' + error);
    });
}

function generateTransactionReportCsv() {
    const form = document.querySelector('#transaction-report-form');
    const formData = new FormData(form);
    axios.post('/report/transaction-report-csv',
        formData,
        {headers: {'Content-Type': 'multipart/form-data'}}
    ).then(function (response) {
        downloadWriteFile(response);
    }).catch(function (error) {
        console.log(error);
        alert('Error from server : ' + error);
    });
}

function downloadWriteFile(response) {
    const url = window.URL.createObjectURL(new Blob([response.data]))
    const a = document.createElement('a');
    a.href = url;
    a.download = "transaction-report.csv"
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(url);
}