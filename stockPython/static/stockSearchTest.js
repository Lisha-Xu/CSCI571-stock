var SERVER= 'https://stocknodebackend.wl.r.appspot.com';
var ticker = "aapl";

document.addEventListener("load", (e) => {
    e.preventDefault();
    const infos = () => {
        getInfo().then(allInfo => {
            chartPart(allInfo);
        });
    };
    infos();
});

function getInfo(){
    const promise =new Promise((resolve, reject) => {
        let request_url = SERVER + `/details?ticker=` + ticker;
        let xhr = new XMLHttpRequest();
        xhr.responseType = 'json';
        xhr.onload = () => {
        if (xhr.status == 200) {
                resolve(xhr.response);
            }
        };
        xhr.open('GET', request_url, true);
        xhr.send();
    });
    return promise;
}

function chartPart(allInfo) {
    Highcharts.stockChart('container', {

        rangeSelector: {
            selected: 2
        },

        title: {
            text: allInfo.ticker + ' Historical'
        },

        subtitle: {
            text: 'With SMA and Volume by Price technical indicators'
        },

        yAxis: [{
            startOnTick: false,
            endOnTick: false,
            labels: {
                align: 'right',
                x: -3
            },
            title: {
                text: 'OHLC'
            },
            height: '60%',
            lineWidth: 2,
            resize: {
                enabled: true
            }
        }, {
            labels: {
                align: 'right',
                x: -3
            },
            title: {
                text: 'Volume'
            },
            top: '65%',
            height: '35%',
            offset: 0,
            lineWidth: 2
        }],

        tooltip: {
            split: true
        },

        plotOptions: {
            series: {
                dataGrouping: {
                    units: groupingUnits
                }
            }
        },

        series: [{
            type: 'candlestick',
            name: allInfo.ticker,
            id: allInfo.ticker,
            zIndex: 2,
            data: allInfo.hisOhlc
        }, {
            type: 'column',
            name: 'Volume',
            id: 'volume',
            data: allInfo.hisVolume,
            yAxis: 1
        }, {
            type: 'vbp',
            linkedTo: allInfo.ticker,
            params: {
                volumeSeriesID: 'volume'
            },
            dataLabels: {
                enabled: false
            },
            zoneLines: {
                enabled: false
            }
        }, {
            type: 'sma',
            linkedTo: allInfo.ticker,
            zIndex: 1,
            marker: {
                enabled: false
            }
        }]
    });
}