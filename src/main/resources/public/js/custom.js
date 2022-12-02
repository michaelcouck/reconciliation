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
    });
}