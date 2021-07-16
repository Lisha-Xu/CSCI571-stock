import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-portfolio',
  templateUrl: './portfolio.component.html',
  styleUrls: ['./portfolio.component.css']
})
export class PortfolioComponent implements OnInit{  // , AfterViewChecked, AfterViewInit {
  emptyFlag = 0;
  showSpinner: boolean;
  storage: any = window.localStorage;
  allInfo: any;
  allInfoArray: [any];
  savedInfoArray: [any];
  length = 0;
  tickers: string[] = [];

  myPortfolio = [];
  constructor(private http: HttpClient) { }
  readonly SERVER: string = 'https://stocknodebackend.wl.r.appspot.com';
  // readonly SERVER: string = 'http://localhost:3000';
  // router: Router;

  ngOnInit(): void {
    this.showSpinner = true;
    this.emptyFlag = 0;
    for (let i = 0; i < this.storage.length; i++) {
      const key = this.storage.key(i);
      // console.log(key);
      const value = JSON.parse(this.storage.getItem(key));
      // console.log(value);
      if (value[2] === 1) {
        this.emptyFlag = 1;
        this.getInfo(key).subscribe(allInfo => {
          this.allInfo = allInfo;
          this.myPortfolio.push([this.allInfo, value[3]]);
          this.showSpinner = false;
        });
      }
    }
    if (this.emptyFlag !== 1) {
      this.showSpinner = false;
    }
  }

  // tslint:disable-next-line:typedef
  getInfo(ticker) {
    const requestURL = this.SERVER + `/portfolioback?ticker=` + ticker;
    // console.log(requestURL);
    return this.http.get(requestURL);
  }

  // tslint:disable-next-line:typedef
    getData(item: any){
    this.emptyFlag = 0;
    for (let i = 0; i < this.storage.length; i++) {
      const key = this.storage.key(i);
      // console.log(key);
      const value = JSON.parse(this.storage.getItem(key));
      // console.log(value);
      if (value[2] === 1) {
        this.emptyFlag = 1;
        this.getInfo(key).subscribe(allInfo => {
          this.allInfo = allInfo;
          this.myPortfolio[this.myPortfolio.findIndex(obj => obj[0].ticker === this.allInfo.ticker)][0] = this.allInfo;
          this.myPortfolio[this.myPortfolio.findIndex(obj => obj[0].ticker === this.allInfo.ticker)][1] = value[3];
          this.showSpinner = false;
        });
      }
    }
  }

  // tslint:disable-next-line:typedef
  onElementDeleted(element) {
    const index = this.myPortfolio.findIndex((elt) => (elt === element));
    if (index !== -1) {
      this.myPortfolio.splice(index, 1);
    }
  }

}
