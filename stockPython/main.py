import requests
import time
import datetime
from dateutil.relativedelta import relativedelta
import json
from flask import Flask, request, jsonify
app = Flask(__name__)
headers = {
    'Content-Type': 'application/json'
}


@app.route('/')
def index():
    return app.send_static_file('stockSearch.html')

@app.route('/info', methods=['GET'])
def info():
    if request.method == 'GET':
        searchValue = request.args.get('searchValue')

        # Part 1 information: Company Outlook
        outLookURL = "https://api.tiingo.com/tiingo/daily/" + searchValue + "?token=62d2f37f0628093b9a3990ed90b67a294f70cdc0"
        outLookRes = requests.get(outLookURL, headers = headers).json()
        if outLookRes.__contains__('detail') and outLookRes['detail'] =='Not found.':
            return "error"
        allInfo = outLookRes

        # Part 2 information: Stock Summary
        summaryURL = "https://api.tiingo.com/iex/?tickers="+searchValue+",spy&token=62d2f37f0628093b9a3990ed90b67a294f70cdc0"
        summaryInfo = requests.get(summaryURL, headers = headers).json()
        for index in range(0,len(summaryInfo)):
            if summaryInfo[index]['ticker'].lower()==searchValue.lower():
                selectedObj = summaryInfo[index]
                break
        allInfo.update(selectedObj)
        allInfo['timestamp'] = allInfo['timestamp'].split('T')[0]
        allInfo['change'] = round(allInfo['last'] - allInfo['prevClose'],2)
        allInfo['low'] = round(allInfo['low'],2)
        allInfo['last'] = round(allInfo['last'],2)
        allInfo['changePercent'] = round((allInfo['last'] - allInfo['prevClose'])/allInfo['prevClose'] * 100,2)

        # Part 3 information: Chart
        closeData = []
        volumeData = []
        todayDate = datetime.date.today()
        startTime = todayDate - relativedelta(months=+6)
        chartData = requests.get("https://api.tiingo.com/iex/"+searchValue+"/prices?startDate="+str(startTime)+"&resampleFreq=12hour&columns=open,high,low,close,volume&token=62d2f37f0628093b9a3990ed90b67a294f70cdc0", headers=headers).json()
        for index in range(0, len(chartData)):
            strtime = chartData[index]['date']
            ts = time.strptime(strtime, "%Y-%m-%dT%H:%M:%S.%fZ")
            ts = time.mktime(ts)-23*3600
            closeData.append([ts*1000, chartData[index]['close']])
            volumeData.append([ts*1000, chartData[index]['volume']])
        allInfo['closeData'] = closeData
        allInfo['volumeData'] = volumeData

        # Part 4 information: Latest 5 News
        newsURL = 'http://newsapi.org/v2/everything?q='+searchValue+'&apiKey=f024d503c1414bc480e8901988bbc295'
        response = requests.get(newsURL).json()
        articles = response['articles']
        latestNews = []
        count = 0
        j = 0
        while(count<5):
            if articles[j]['title'] is None or articles[j]['urlToImage'] is None or articles[j]['publishedAt'] is None or articles[j]['url'] is None:
                j += 1
                print(j)
            else:
                articles[j]['publishedAt'] = articles[j]['publishedAt'].split('T')[0]
                articles[j]['publishedAt'] = articles[j]['publishedAt'].replace('-',"/");
                latestNews.append(articles[j])
                j += 1
                count += 1
        allInfo['latestNews'] = latestNews
        return jsonify(allInfo)


if __name__ == '__main__':
    app.run()



