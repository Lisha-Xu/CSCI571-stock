var express = require('express');
var axios = require('axios');
var cors = require('cors');
var path = require('path');
var app = express();
app.use(cors());
app.use(express.static(path.join(__dirname,'dist/stockSearchRouter')));

app.get('/info', async (request, response) => {
    let searchValue = request.query.searchValue;
    let infos={};
    infos = await getInfos(searchValue);
    // console.log(typeof (infos));
    response.json(infos)
})

function getInfos(searchValue){
    // let url = "https://api.tiingo.com/tiingo/utilities/search?query=" + searchValue +"&token=62d2f37f0628093b9a3990ed90b67a294f70cdc0";
    let url = "https://api.tiingo.com/tiingo/utilities/search?query=" + searchValue +"&token=4ef2696775718194fbed426fcc6363d13adf2572";
    return axios.get(url)
        .then(function(response){
            let result = response.data;
            for (let i =0; i < result.length; i++){
                delete result[i]['assetType'];
                delete result[i]['countryCode'];
                delete result[i]['isActive'];
                delete result[i]['openFIGI'];
                delete result[i]['permaTicker'];
            }
            // console.log(typeof (response.data));
            return result;
        })
        .catch(function (error){
            return error;
        })
}

app.get('/details', async (request, response) => {
    // console.log("fangwen+++++++++++++++++++")
    let ticker = request.query.ticker;
    let part1 = await getPart1(ticker);
    if (part1 == null){
        response.json(null);
    }
    else {
        let part2 = await getPart2(ticker);
        let infos = Object.assign(part1, part2);
        infos['dailyChart'] = await getPart3(ticker, infos['flag'], infos['timestamp']);
        // console.log(infos)
        infos['news'] = await getNews(ticker);
        infos['hisOhlc'] = await getHisOhlc(ticker);
        // console.log(infos['hisOhlc'])
        infos['hisVolume'] = await getHisVolume(ticker);
        // console.log(infos['hisVolume'])
        // console.log(infos);
        response.json(infos)
    }
})

app.get('/watchlistback', async (request, response) => {
    let ticker = request.query.ticker;
    let part1 = await getPart1(ticker);
    if (part1 == null){
        response.json(null);
    }
    else {
        let part2 = await getPart2(ticker);
        let infos = Object.assign(part1, part2);
        response.json(infos)
    }
})

app.get('/portfolioback', async (request, response) => {
    let ticker = request.query.ticker;
    let infos = await getPart2(ticker);
    // console.log(infos);
    response.json(infos)
})

app.get('*', ((req, res) => {
    res.sendFile(path.join(__dirname,'dist/stockSearchRouter/index.html'));
}))
function getPart1(ticker){
    // let url = "https://api.tiingo.com/tiingo/daily/" + ticker +"?token=62d2f37f0628093b9a3990ed90b67a294f70cdc0";
    let url = "https://api.tiingo.com/tiingo/daily/" + ticker +"?token=4ef2696775718194fbed426fcc6363d13adf2572";
    return axios.get(url)
        .then(function(response){
            return  response.data;
        })
        .catch(function (error){
            return null
        })
}

function getPart2(ticker){
    // let url = "https://api.tiingo.com/iex/?tickers=" + ticker +"&token=62d2f37f0628093b9a3990ed90b67a294f70cdc0";
    let url = "https://api.tiingo.com/iex/?tickers=" + ticker +"&token=4ef2696775718194fbed426fcc6363d13adf2572";
    return axios.get(url)
        .then(function(response){
            let result = response.data[0];
            delete result['quoteTimestamp'];
            delete result['lastSaleTimestamp'];
            delete result['lastSize'];
            result['change'] = result['last'] - result['prevClose'];
            result['change'] = result['change'].toFixed(2);
            result['changePercent'] = result['change'] * 100 / result['prevClose'];
            result['changePercent'] = result['changePercent'].toFixed(2);
            // console.log((result['timestamp']));
            let oriTime = new Date(result['timestamp']);
            // console.log(oriTime.getTime());
            let curTime=new Date();
            // console.log(oriTime,curTime.getHours(),oriTime.getHours(), oriTime.getUTCMonth());
            // let utcTime = new Date(curTime.getUTCFullYear(),curTime.getUTCMonth(),curTime.getUTCDate(),curTime.getUTCHours(),curTime.getUTCMinutes(),curTime.getUTCSeconds());
            // console.log(utcTime.getTime());
            // console.log(oriTime.getTime(),utcTime.getTime(), curTime.getTime());
            if (Math.abs(oriTime.getTime()-curTime.getTime())<60000){
                result['flag'] = true;
                // result['flag'] = false;
            }
            else result['flag'] = false;
            oriTime = oriTime.getTime() - 480*60*1000;
            oriTime = new Date(oriTime);
            result['timestamp']=oriTime.getFullYear() + '-' + zeroFill(oriTime.getMonth()+1) + '-'+zeroFill(oriTime.getDate())+' '
                                 +zeroFill(oriTime.getHours())+':'+zeroFill(oriTime.getMinutes())+':'+zeroFill(oriTime.getSeconds());
            return result;
        })
        .catch(function (error){
            return error;
        })
}

function zeroFill(i){
    if (i >= 0 && i <= 9){
        return '0' + i;
    }
    else {
        return i;
    }
}

//startDate is result['timestamp']
function getPart3(ticker, flag, startDate){
    startDate = startDate.split(" ")[0];
    let result = [];
    // let url = 'https://api.tiingo.com/iex/aapl/prices?startDate='+startDate+'&resampleFreq=4min&columns=close&token=62d2f37f0628093b9a3990ed90b67a294f70cdc0';
    let url = 'https://api.tiingo.com/iex/aapl/prices?startDate='+startDate+'&resampleFreq=4min&columns=close&token=4ef2696775718194fbed426fcc6363d13adf2572';
    return axios.get(url)
        .then(function (response){
            // console.log(response.data);
            for(let i = 0; i < response.data.length; i++){
                let time= new Date(response.data[i]['date']);
                // time = time.getTime()-time.getTimezoneOffset()*60*1000;
                time = time.getTime()-480*60*1000;
                result.push([time, response.data[i]['close']]);
            }
            // console.log(result);
            return result;
        })
        .catch(function (error){
            return error;
        })
}

function getNews(ticker){
    // let url='https://newsapi.org/v2/everything?q='+ticker+'&apiKey=f3a589bc94664ac9bc1aa0bd25712b43';
    let url='https://newsapi.org/v2/everything?q='+ticker+'&apiKey=f024d503c1414bc480e8901988bbc295';
    let Months = ['January','February','March','April','May','June','July','August','September','October','November','December']
    return axios.get(url)
        .then(function(response){
            let result = response.data['articles'];
            let newresult = [];
            for (let i = 0; i < result.length; i++){
                if (result[i]['url']!=null && result[i]['title']!=null && result[i]['description']!= null
                && result[i]['urlToImage']!=null && result[i]['publishedAt']!=null && result[i]['source']['name']!=null){
                    delete result[i]['author'];
                    delete result[i]['content'];
                    result[i]['sourceNew'] = result[i]['source']['name'];
                    delete result[i]['source'];
                    result[i]['publishedAt'] = new Date(result[i]['publishedAt']);
                    let date = result[i]['publishedAt'].getUTCDate();
                    let month = Months[result[i]['publishedAt'].getUTCMonth()];
                    let year = result[i]['publishedAt'].getFullYear();
                    result[i]['publishedAt'] = month+' '+date+', '+year;
                    newresult.push(result[i]);
                }
            }
            return newresult;
        })
        .catch(function (error){
            return error;
        })
}

function getHisOhlc(ticker){
    let today = new Date();
    today.setFullYear(today.getFullYear()-2);
    let month = today.getMonth()+1;
    var startDate = today.getFullYear() + '-' +month+"-"+today.getDate();
    // console.log(startDate);
    let result = [];
    // let url = "https://api.tiingo.com/tiingo/daily/" + ticker +"/prices?startDate="+startDate+"&resampleFreq=daily&token=62d2f37f0628093b9a3990ed90b67a294f70cdc0";
    let url = "https://api.tiingo.com/tiingo/daily/" + ticker +"/prices?startDate="+startDate+"&resampleFreq=daily&token=4ef2696775718194fbed426fcc6363d13adf2572";
    return axios.get(url)
        .then(function(response){
            for (let i =0; i < response.data.length; i++){
                let time= new Date(response.data[i]['date']);
                // console.log(time);
                // time = time.getTime()-time.getTimezoneOffset()*60*1000;
                time = time.getTime();
                result.push([
                    time,
                    response.data[i]["open"],
                    response.data[i]["high"],
                    response.data[i]["low"],
                    response.data[i]["close"]
                ]);
            }
            return result;
        })
        .catch(function (error){
            return error;
        })
}

function getHisVolume(ticker){
    let today = new Date();
    today.setFullYear(today.getFullYear()-2);
    let month = today.getMonth()+1;
    var startDate = today.getFullYear() + '-' +month+"-"+today.getDate();
    // console.log(startDate);
    let result = [];
    // let url = "https://api.tiingo.com/tiingo/daily/" + ticker +"/prices?startDate="+startDate+"&resampleFreq=daily&token=62d2f37f0628093b9a3990ed90b67a294f70cdc0";
    let url = "https://api.tiingo.com/tiingo/daily/" + ticker +"/prices?startDate="+startDate+"&resampleFreq=daily&token=4ef2696775718194fbed426fcc6363d13adf2572";
    return axios.get(url)
        .then(function(response){
            for (let i =0; i < response.data.length; i++){
                let time= new Date(response.data[i]['date']);
                // time = time.getTime()-time.getTimezoneOffset()*60*1000;
                time= time.getTime();
                result.push([time, response.data[i]["volume"]]);
            }
            return result;
        })
        .catch(function (error){
            return error;
        })
}
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Express server currently running on port: ${PORT}`));
