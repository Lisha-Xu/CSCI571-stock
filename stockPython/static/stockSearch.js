var form = document.getElementById("form");
form.addEventListener("submit",  (e) =>{
    e.preventDefault();
    const infos =() => {
           getInfo().then(allInfo =>{
            if(allInfo == null){
                resultDisplay(0, null);
            }
            else {
                resultDisplay(1,allInfo);
            }
        });
    };
    var contentChoice = document.getElementById("contentChoice");
    if (document.getElementById("Error") != null){
            var a = document.getElementById("Error");
            contentChoice.removeChild(a);
            contentChoice.style.display = "none";
        }
    if(document.getElementById("choiceTable")!=null){
        var contentChoice = document.getElementById("contentChoice");
        contentChoice.style.display = "none";
        var a = document.getElementById("choiceTable");
        contentChoice.removeChild(a);
    }
    if (document.getElementById('outLookTable')!=null){
        var a = document.getElementById('outLookTable');
        outLook.removeChild(a);
    }
     if (document.getElementById('summaryTable')!=null){
        var a = document.getElementById('summaryTable');
        stockSummary.removeChild(a);
    }
     if (document.getElementById('charts')!=null){
        var a = document.getElementById('charts');
        a.style.display = "none";
    }
    if (document.getElementById('newsTable1')!=null){
        var a = ['','','','','']
        for (var i = 0; i < 5; i++){
            a[i] = document.getElementById('newsTable'+i);
            news.removeChild(a[i]);
        }
    }
    infos();
});

var clear = document.getElementById("clear");
clear.addEventListener("click",()=>{
    var contentChoice = document.getElementById("contentChoice");
        contentChoice.style.display = "none";
    if (document.getElementById("Error") != null){
            var a = document.getElementById("Error");
            contentChoice.removeChild(a);
            contentChoice.style.display = "none";
        }
    if(document.getElementById("choiceTable")!=null){
        var a = document.getElementById("choiceTable");
        contentChoice.removeChild(a);
    }
    if (document.getElementById('outLookTable')!=null){
        var a = document.getElementById('outLookTable');
        outLook.removeChild(a);
    }
     if (document.getElementById('summaryTable')!=null){
        var a = document.getElementById('summaryTable');
        stockSummary.removeChild(a);
    }
     if (document.getElementById('charts')!=null){
        var a = document.getElementById('charts');
        a.style.display = "none";
    }
    if (document.getElementById('newsTable1')!=null){
        var a = ['','','','','']
        for (var i = 0; i < 5; i++){
            a[i] = document.getElementById('newsTable'+i);
            news.removeChild(a[i]);
        }
    }
});

function getInfo(){
    const promise =new Promise((resolve, reject) => {

        let searchValue = document.getElementById('searchValue').value;
        let request_url = "/info?searchValue=" + searchValue;
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

// Buttoon Choice and Display the choiced content
function resultDisplay(flag = 1,allInfo){
    if(flag == 0){
        var contentChoice = document.getElementById("contentChoice");
        contentChoice.innerHTML="<p id='Error'>Error: No record has been found, please enter a valid symbol.</p>"
        contentChoice.style.backgroundColor='#6060A9';
        contentChoice.style.display = "block";
    }
    else {
        var contentChoice = document.getElementById("contentChoice");
        if (document.getElementById("Error") != null){
            var a = document.getElementById("Error");
            contentChoice.removeChild(a);
            contentChoice.style.display = "none";
        }
        contentChoice.style.display = "block";
        contentChoice.style.backgroundColor = "#f1f1f1";
        contentChoice.style.height = "50px";
        contentChoice.style.marginTop = "15px";
        contentChoice.style.marginBottom = "30px";
        var tablePart = document.createElement('table');
        tablePart.setAttribute("border-collapse", 'collapse');
        tablePart.setAttribute("id", "choiceTable");
        tablePart.setAttribute("cellspacing", '0');
        tablePart.setAttribute("cellpadding", '0');
        tablePart.setAttribute("height", '100%');

        var row = document.createElement('tr');
        row.setAttribute("backgroundColor", '#f1f1f1');
        var name = ["Company Outlook", "Stock Summary", "Charts", "Latest News"];

        var cell = ['', '', '', ''];
        for (var i = 0; i < 4; i++) {
            cell[i] = document.createElement('td');
            cell[i].innerText = name[i];
            cell[i].style.paddingLeft = "15px";
            cell[i].style.paddingRight = "15px";
            row.appendChild(cell[i]);
        }
        var flag = [true, false, false, false];

        cell[0].style.backgroundColor = "#cccccc";
        showOutlook();
        overLookPart(allInfo);
        cell[0].onclick = () => {
            flag[0] = true;
            flag[1] = false;
            flag[2] = false;
            flag[3] = false;
            cell[0].style.backgroundColor = "#cccccc";
            cell[1].style.backgroundColor = "#f1f1f1";
            cell[2].style.backgrouf1f1f1ndColor = "#f1f1f1";
            cell[3].style.backgroundColor = "#f1f1f1";
            showOutlook();
            overLookPart(allInfo);
        };
        cell[1].onclick = () => {
            flag[0] = false;
            flag[1] = true;
            flag[2] = false;
            flag[3] = false;
            cell[0].style.backgroundColor = "#f1f1f1";
            cell[1].style.backgroundColor = "#cccccc";
            cell[2].style.backgroundColor = "#f1f1f1";
            cell[3].style.backgroundColor = "#f1f1f1";
            showSummary();
            SummaryPart(allInfo);
        };
        cell[2].onclick = () => {
            flag[0] = false;
            flag[1] = false;
            flag[2] = true;
            flag[3] = false;
            cell[0].style.backgroundColor = "#f1f1f1";
            cell[1].style.backgroundColor = "#f1f1f1";
            cell[2].style.backgroundColor = "#cccccc";
            cell[3].style.backgroundColor = "#f1f1f1";
            showCharts();
            chartPart(allInfo);
        };
        cell[3].onclick = () => {
            flag[0] = false;
            flag[1] = false;
            flag[2] = false;
            flag[3] = true;
            cell[0].style.backgroundColor = "#f1f1f1";
            cell[1].style.backgroundColor = "#f1f1f1";
            cell[2].style.backgroundColor = "#f1f1f1";
            cell[3].style.backgroundColor = "#cccccc";
            showNews();
            newsPart(allInfo.latestNews);
        };

        cell[0].onmouseover = function () {
            this.style.backgroundColor = "#dddddd";
        };
        cell[0].onmouseout = function () {
            if (flag[0] != true) {
                this.style.backgroundColor = "#f1f1f1";
            }
            else {
                this.style.backgroundColor =  "#cccccc";
            }
        };
        cell[1].onmouseover = function () {
            this.style.backgroundColor = "#dddddd";
        };
        cell[1].onmouseout = function () {
            if (flag[1] != true) {
                this.style.backgroundColor = "#f1f1f1";
            }
            else {
                this.style.backgroundColor =  "#cccccc";
            }
        };
        cell[2].onmouseover = function () {
            this.style.backgroundColor = "#dddddd";
        };
        cell[2].onmouseout = function () {
            if (flag[2] != true) {
                this.style.backgroundColor = "#f1f1f1";
            }
            else {
                this.style.backgroundColor =  "#cccccc";
            }
        };
        cell[3].onmouseover = function () {
            this.style.backgroundColor = "#dddddd";
        };
        cell[3].onmouseout = function () {
            if (flag[3] != true) {
                this.style.backgroundColor = "#f1f1f1";
            }
            else {
                this.style.backgroundColor =  "#cccccc";
            }
        };
        tablePart.appendChild(row);
        contentChoice.appendChild(tablePart);
    }
}

function showOutlook(){
    document.getElementById("outLook").style.display="block";
    document.getElementById("stockSummary").style.display="none";
    document.getElementById("charts").style.display="none";
    document.getElementById("news").style.display="none";
}
function showSummary(){
    document.getElementById("outLook").style.display="none";
    document.getElementById("stockSummary").style.display="block";
    document.getElementById("charts").style.display="none";
    document.getElementById("news").style.display="none";
}
function showCharts(){
    document.getElementById("outLook").style.display="none";
    document.getElementById("stockSummary").style.display="none";
    document.getElementById("charts").style.display="block";
    document.getElementById("news").style.display="none";
}
function showNews(){
    document.getElementById("outLook").style.display="none";
    document.getElementById("stockSummary").style.display="none";
    document.getElementById("charts").style.display="none";
    document.getElementById("news").style.display="block";
}


function overLookPart(allInfo){
    var outLook = document.getElementById("outLook");
    if (document.getElementById('outLookTable')!=null){
        var a = document.getElementById('outLookTable');
        outLook.removeChild(a);
    }
    var tablePart = document.createElement('table');
    tablePart.setAttribute("id","outLookTable");

    tablePart.style.borderSpacing = "2px 1px";
    tablePart.style.width = "100%";
    tablePart.style.backgroundColor = "#e0dcdc";

    var thtxt = ['Company Name','Stock Ticker Symbol','Stock Exchange Code',
        'Company Start Date','Description'];
    var tdtxt = [allInfo.name, allInfo.ticker,allInfo.exchangeCode, allInfo.startDate, allInfo.description];
    for ( var i = 0; i < 5; i++){
        var r = document.createElement('tr');
        var thead = document.createElement('th');

        thead.setAttribute("width","33%");
        thead.style.backgroundColor = "#e8e6e6";
        thead.innerText = thtxt[i];
        thead.style.textAlign = "left";

        r.appendChild(thead);
        var cell = document.createElement('td');
        cell.setAttribute("width","67%");
        if (i != 4){
            cell.innerText = tdtxt[i];
        }
        cell.style.backgroundColor="#fbfbfb";
        cell.style.textAlign= "center";
        if(i==4){
            // r.setAttribute("height","100px");
            var textContent = document.createElement("div");
            textContent.style.overflow = 'hidden';
            textContent.style.textOverflow = 'ellipsis';
            textContent.style.display = "-webkit-box";
            textContent.style.webkitLineClamp = 5;
            textContent.style.webkitBoxOrient = "vertical";
            cell.appendChild(textContent);
            textContent.innerText = tdtxt[i];
        }
        r.appendChild(cell);
        tablePart.appendChild(r)
    }
    outLook.appendChild(tablePart);
}

function SummaryPart(allInfo){
    var stockSummary = document.getElementById("stockSummary");
    if (document.getElementById('summaryTable')!=null){
        var a = document.getElementById('summaryTable');
        stockSummary.removeChild(a);
    }
    var tablePart = document.createElement('table');
    tablePart.setAttribute("id","summaryTable");
    tablePart.style.borderSpacing = "2px 1px";
    tablePart.style.width = "100%";
    tablePart.style.backgroundColor = "#e0dcdc";
    var thtxt = ['Stock Ticker Symbol','Trading Day','Previous Closing Price',
        'Opening Price','High Price', 'Low Price', 'Last Price', 'Change', 'Change Percent', 'Number of Shares Traded'];
    var tdtxt = [allInfo.ticker,allInfo.timestamp,allInfo.prevClose, allInfo.open,allInfo.high, allInfo.low, allInfo.last, allInfo.change,
        allInfo.changePercent, allInfo.volume];
    for ( var i = 0; i < 10; i++){
        var r = document.createElement('tr');
        var thead = document.createElement('th');
        thead.setAttribute("width","33%");
        thead.style.backgroundColor = "#e8e6e6";
        thead.innerText = thtxt[i];
        thead.style.textAlign = "left";
        thead.innerText = thtxt[i];
        r.appendChild(thead);
        var cell = document.createElement('td');
        if (i==8){
            cell.innerText = tdtxt[i] + '%';
        }
        else {
            cell.innerText = tdtxt[i];
        }
        cell.style.backgroundColor="#fbfbfb";
        cell.style.textAlign= "center";
        cell.setAttribute("width","67%");
        r.appendChild(cell);
        if(i == 7|| i ==8){
            var changeImg = document.createElement("img");
            if (allInfo.change < 0){
                changeImg.setAttribute("src","static/RedArrowDown.jpg")
            }
            else {
                changeImg.setAttribute("src","static/GreenArrowUp.jpg")
            }
            changeImg.style.height = "15px";
            changeImg.style.marginLeft = "10px";
            cell.appendChild(changeImg);
        }
        tablePart.appendChild(r)
    }
    stockSummary.appendChild(tablePart);
}

function newsPart(latestNews){
    var news = document.getElementById("news");
    if (document.getElementById('newsTable1')!=null){
        var a = ['','','','','']
        for (var i = 0; i < 5; i++){
            a[i] = document.getElementById('newsTable'+i);
            news.removeChild(a[i]);
        }
    }
    for (var i = 0; i < 5; i++){
        var newsBox = document.createElement("div");
        newsBox.setAttribute("id","newsTable"+i);
        newsBox.style.width = "100%";
        newsBox.style.height = "100px";
        newsBox.style.marginTop = "20px";
        newsBox.style.backgroundColor = "#e8e8e8";
        newsBox.style.borderRadius="5px";
        var pictureBox = document.createElement("div");
        pictureBox.style.width = "75px";
        pictureBox.style.height = "100%";
        pictureBox.style.float = "left";
        pictureBox.style.marginLeft="15px";
        pictureBox.style.overflow="hidden";
        var picture = document.createElement("img");
        picture.setAttribute("src", latestNews[i]['urlToImage']);
        picture.style.display ="block";
        picture.style.height = "80px";
        picture.style.margin = "10px 15px";
        picture.style.position ="relative";
        picture.style.right = "40px";
        pictureBox.appendChild(picture);


        var wordBox = document.createElement('div');
        wordBox.style.width = "815px";
        wordBox.style.marginTop = "10px";
        wordBox.style.height = "100%";
        wordBox.style.float = "right";
        var title = document.createElement('h4');
        title.style.margin = 0;
        title.innerText = latestNews[i]['title'];
        title.style.letterSpacing="-0.019em";
        wordBox.appendChild(title);
        var date = document.createElement("p");
        date.style.margin = 0;
        date.innerText = 'Published Date:' + latestNews[i]['publishedAt'];
        wordBox.appendChild(date);
        var link = document.createElement("a");
        link.setAttribute("href", latestNews[i]['url']);
        link.innerText = "See Original Post";
        link.setAttribute("target","_blank");
        wordBox.appendChild(link);

        newsBox.appendChild(pictureBox);
        newsBox.appendChild(wordBox);

        news.appendChild(newsBox);
    }
}

function getTodayDate(){
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth()+1;
    var strDate = date.getDate();
    if (month >= 1 && month <=9){
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9){
        strDate = "0"+ strDate;
    }
    var todayDate= year + '-' + month + '-' + strDate;
    return todayDate;
}


function chartPart(allInfo) {
    var todayDate = getTodayDate();
    Highcharts.stockChart('charts', {
        chart:{
            zoomType: 'x'
        },
        title: {
            text: 'Stock Price ' + allInfo.ticker+ ' ' + todayDate
        },

        subtitle: {
            text: '<a href ="https://api.tiingo.com/" target="_blank"><u>Source：Tiingo</u></a>',
            useHTML: true
        },

        rangeSelector: {
            buttons: [{
                type: 'day',
                count: 7,
                text: '7d'

            }, {
                type: 'day',
                count: 15,
                text: '15d'
            }, {
                type: 'month',
                count: 1,
                text: '1m'
            }, {
                type: 'month',
                count: 3,
                text: '3m'
            }, {
                type: 'all',
                count: 1,
                text: '6m'
            }],
            selected: 4,
            inputEnabled: false
        },

        xAxis: {
            type: 'datetime',
            startOnTick:true,
            endOnTick:true
        },

        yAxis: [{
            labels: {
                format: '{value}',
                style: {
                    color: Highcharts.getOptions().colors[1]
                },
            },
            title: {
                text: 'Stock Price',
                style: {
                    color: Highcharts.getOptions().colors[1]
                }
            },
            opposite: false,


        },
            {
                title: {
                    text: 'Volume',
                    style: {
                        color: Highcharts.getOptions().colors[1]
                    }
                },

                labels: {
                    formatter: function () {
                        if (this.value == 0){
                            return this.value;
                        }
                        return this.value / 1000 + ' k';
                    },
                    style: {
                        color: Highcharts.getOptions().colors[1]
                    }
                },
                opposite: true,
                gridLineWidth: 0,
            }
        ],

        plotOptions: {
            column: {
                pointWidth: 2,
                pointPlacement: "on"
            },
            area:{
                pointPlacement: "on"
            }

        },
        //
        series: [{
            name: allInfo.ticker,
            type: 'area',
            yAxis:0,
            tooltip: {
                valueDecimals: 2
            },
            getExtremesFromAll: true,
            fillColor: {
                linearGradient: {
                    x1: 0,
                    y1: 0,
                    x2: 0,
                    y2: 1
                },
                stops: [
                    [0, Highcharts.getOptions().colors[0]],
                    [1, Highcharts.color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                ]
            },
            threshold: 0,
            data: allInfo.closeData
        },
            {
                name: allInfo.ticker+" Volume",
                type: 'column',
                yAxis: 1,
                gapSize: 5,
                tooltip: {
                    valueDecimals: 0
                },
                threshold: 0,
                getExtremesFromAll: true,
                data: allInfo.volumeData
            }]
    });
}
