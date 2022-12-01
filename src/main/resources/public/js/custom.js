function setContent(element, page) {
    const section = $("#" + element);
    section.children().remove();
    section.load(page);
}

function setElementContent(element, content) {
    const section = $("#" + element);
    section.children().remove();
    // section.innerHTML = content;
    $('#' + element).html('\'' + content + '\'');
}

function generateCommercialReport() {
    const form = document.querySelector('#commercial-report-form');
    const formData = new FormData(form);
    axios.post('/report/commercial-report',
        formData,
        {headers: {'Content-Type': 'multipart/form-data', 'Accept': 'text/html'}}
    ).then(function (response) {
        console.log("Response data : " + response.data);
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
        /*let reportWindow = window.open("about:blank", "hello", "width=1140,height=720");
        reportWindow.document.write(response.data);*/
        setContent("report-result", response.data);
    }).catch(function (error) {
        console.log(error);
    });
}