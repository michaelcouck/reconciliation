function setContent(element, page) {
    const section = $("#" + element);
    section.children().remove();
    section.load(page);
}

function generateReport(msp, startDate, endDate) {
    const reportRequest = {
        'msp': msp.value,
        'startDate': startDate.value,
        'endDate': endDate.value
    };
    const reportRequestJson = JSON.stringify(reportRequest);
    // noinspection JSUnresolvedFunction
    axios.post('/report/commercial-report',
        reportRequestJson,
        {headers: {'Content-Type': 'application/json'}}
    ).then(function (response) {
        let reportWindow = window.open("about:blank", "hello", "width=1140,height=720");
        reportWindow.document.write(response.data);
    }).catch(function (error) {
        console.log(error);
    });
}