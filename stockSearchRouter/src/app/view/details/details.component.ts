import { Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {HttpClient} from '@angular/common/http';
import { Options } from 'highcharts/highstock';
import * as Highcharts from 'highcharts';
import HC_stock from 'highcharts/modules/stock';
HC_stock(Highcharts);
import HC_indicators from 'highcharts/indicators/indicators';
HC_indicators(Highcharts);
import HC_vbp from 'highcharts/indicators/volume-by-price';
import {BuyComponent} from '../buy/buy.component';

HC_vbp(Highcharts);

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})


export class DetailsComponent implements OnInit {
  private timer;
  constructor(public route: ActivatedRoute, private http: HttpClient, private modalService: NgbModal) {
  }
  searchValue: string;
  curTime: string;
    readonly SERVER: string = 'https://stocknodebackend.wl.r.appspot.com';
  // readonly SERVER: string = 'http://localhost:3000';
  allInfo: any = {ticker: ''};
  Highcharts1: typeof Highcharts = Highcharts;
  Highcharts2: typeof Highcharts = Highcharts;
  chartOptions: Options;
  // Historical chart part
  hisChartOptions: Options;
  showSpinner = true;
  storage: any = window.localStorage;
  watchFlag: boolean;
  changeWatchFlag = 0;     // 0: none; 1: add; 2: remove
  private timerWatch;
  private timerBuy;
  buyFlag = 0;
  modalRef;
  updateFlag: boolean;
  count = 0;

  ngOnInit(): void {
    this.updateInfo(0);
    this.changeWatchFlag = 0;
    this.buyFlag = 0;
    this.timer = setInterval( () => {
      this.updateInfo(1);
      }, 15000);

  }

  // tslint:disable-next-line:typedef use-lifecycle-interface
  ngOnDestroy() {
    if (this.timer) {
      clearInterval(this.timer);
    }
    if (this.timerWatch) {
      clearInterval(this.timerWatch);
    }
    if (this.timerBuy) {
      clearInterval(this.timerBuy);
    }
  }

  // tslint:disable-next-line:typedef
  updateInfo(flag){
    this.route.params.subscribe((params) => {
      this.searchValue = params.searchValue;

      if ((flag === 1 && this.allInfo && this.allInfo.flag) || flag === 0) {
        this.getDetail(this.searchValue).subscribe(allInfo => {
          this.allInfo = allInfo;
          if (this.allInfo == null) {
            this.showSpinner = false;
          }
          if (this.allInfo !== null) {
            this.setChartOption();
            this.updateFlag = true;
            if (flag === 0) {
              this.setHisChartOptions();
            }
            const value = JSON.parse(this.storage.getItem(this.allInfo.ticker));
            this.watchFlag = !(value == null || value[1] === 0);
            this.showSpinner = false;
            this.curTime = this.getCurrentTime();
            if (this.modalRef) {
              this.modalRef.componentInstance.allInfo = this.allInfo;
            }
          }
        });
      }

    });
  }

  // tslint:disable-next-line:typedef
  getDetail(ticker) {
    const requestURL = this.SERVER + `/details?ticker=` + ticker;
    // console.log(requestURL);
    return this.http.get(requestURL);
  }

  // tslint:disable-next-line:typedef
  getCurrentTime(){
    const date = new Date();
    const month = this.zeroFill(date.getMonth() + 1); // 月
    const day = this.zeroFill(date.getDate()); // 日
    const hour = this.zeroFill(date.getHours()); // 时
    const minute = this.zeroFill(date.getMinutes()); // 分
    const second = this.zeroFill(date.getSeconds()); // 秒
    const curTime = date.getFullYear() + '-' + month + '-' + day
      + ' ' + hour + ':' + minute + ':' + second;
    return curTime;
  }

  // tslint:disable-next-line:typedef
  zeroFill(i){
    if (i >= 0 && i <= 9){
      return '0' + i;
    }
    else {
      return i;
    }
  }
  // tslint:disable-next-line:typedef
  setChartOption(){
    this.chartOptions = {
      title: {text: this.allInfo.ticker, style: {color: '#808084'}},
      rangeSelector: {enabled: false},
      xAxis: {
        type: 'datetime',
      },

      plotOptions: {
        line: {
        pointPlacement: 'on'
        }
      },

      series: [
        {
          color: this.decideColor(),
          name: this.allInfo.ticker,
          type: 'line',
          data: this.allInfo.dailyChart
        }
      ]
    };
  }

  // tslint:disable-next-line:typedef
  decideColor(){
    if (this.allInfo.change > 0){
      return 'green';
    }
    else if (this.allInfo.change < 0){
      return 'red';
    }
    else { return 'black'; }
  }

  // tslint:disable-next-line:typedef
  setHisChartOptions(){
    this.hisChartOptions = {

      rangeSelector: {
        selected: 2,
        buttons: [{
          type: 'month',
          count: 1,
          text: '1m'
        }, {
          type: 'month',
          count: 3,
          text: '3m'
        }, {
          type: 'month',
          count: 6,
          text: '6m'
        }, {
          type: 'ytd',
          text: 'YTD'
        }, {
          type: 'year',
          count: 1,
          text: '1y'
        }, {
          type: 'all',
          text: 'All'
        }]
      },

      title: {
        text: this.allInfo.ticker + ' Historical'
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

      series: [{
        type: 'candlestick',
        name: this.allInfo.ticker,
        id: this.allInfo.ticker,
        zIndex: 2,
        data: this.allInfo.hisOhlc
      }, {
        type: 'column',
        name: 'Volume',
        id: 'volume',
        data: this.allInfo.hisVolume,
        yAxis: 1
      }, {
        type: 'vbp',
        linkedTo: this.allInfo.ticker,
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
        linkedTo: this.allInfo.ticker,
        zIndex: 1,
        marker: {
          enabled: false
        }
      }]
    };
  }

  // tslint:disable-next-line:typedef
    open() {
      this.modalRef = this.modalService.open(BuyComponent);
      this.modalRef.componentInstance.allInfo = this.allInfo;
      this.modalRef.componentInstance.toParent.subscribe(info => {
        this.buyFlag = 1;
        this.timerBuy = setInterval(() => {
          this.buyFlag = 0;
          this.modalRef = null;
        }, 5000);
      });
    }

  // tslint:disable-next-line:typedef
    setWatchList(){
      if (this.storage.getItem(this.allInfo.ticker) == null){
        this.watchFlag = true;
        this.storage.setItem(this.allInfo.ticker, JSON.stringify([this.allInfo, 1, 0, 0]));
        this.changeWatchFlag = 1;
      }
      else if (this.watchFlag === false){
        this.watchFlag = true;
        const value = JSON.parse(this.storage.getItem(this.allInfo.ticker));
        value[1] = 1;
        this.storage.setItem(this.allInfo.ticker, JSON.stringify(value));
        this.changeWatchFlag = 1;
      }
      else {
        this.watchFlag = false;
        const value = JSON.parse(this.storage.getItem(this.allInfo.ticker));
        value[1] = 0;
        this.storage.setItem(this.allInfo.ticker, JSON.stringify(value));
        this.changeWatchFlag = 2;
      }
      this.timerWatch = setInterval(() => {
        this.changeWatchFlag = 0;
      }, 5000);
    }
}
