import { Component, OnInit } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-watchlist',
  templateUrl: './watchlist.component.html',
  styleUrls: ['./watchlist.component.css']
})
export class WatchlistComponent implements OnInit {

  emptyFlag = 0;
  showChild = true;
  storage: any = window.localStorage;
  watchStock = [];
  allInfo: any;
  showSpinner: boolean;
  // save Information in the first parameter
  // watchFlag save in the second parameter

  constructor(private http: HttpClient) {  }
  readonly SERVER: string = 'https://stocknodebackend.wl.r.appspot.com';
  // readonly SERVER: string = 'http://localhost:3000';


  ngOnInit(): void {
    this.showSpinner = true;
    for (let i = 0; i < this.storage.length; i++) {
      const key = this.storage.key(i);
      // console.log(key);
      const value = JSON.parse(this.storage.getItem(key));
      if (value[1] === 1) {
        this.emptyFlag = 1;
      }
    }
    if (this.emptyFlag !== 1) {
      this.emptyFlag = 0;
      this.showSpinner = false;
    }
    for (let i = 0; i < this.storage.length; i++) {
      const key = this.storage.key(i);
      // console.log(key);
      const value = JSON.parse(this.storage.getItem(key));
      // console.log(value);
      if (value[1] === 1) {
        // this.watchStock.push([]);
        this.getInfo(key).subscribe(allInfo => {
          this.allInfo = allInfo;
          this.allInfo.name = value[0].name;
          this.watchStock.push(this.allInfo);
          // console.log(allInfo);
          this.showSpinner = false;
        });
      }
  }

}
  // tslint:disable-next-line:typedef
  getInfo(ticker) {
    const requestURL = this.SERVER + `/watchlistback?ticker=` + ticker;
    // console.log(requestURL);
    return this.http.get(requestURL);
  }

  // tslint:disable-next-line:typedef
  onElementDeleted(element) {
    this.emptyFlag = 0;
    const index = this.watchStock.findIndex((elt) => (elt === element));
    if (index !== -1) {
      this.watchStock.splice(index, 1);
    }
    for (let i = 0; i < this.storage.length; i++) {
      const key = this.storage.key(i);
      // console.log(key);
      const value = JSON.parse(this.storage.getItem(key));
      // console.log(value);
      if (value[1] === 1) {
        this.emptyFlag = 1;
      }
    }
    if (this.emptyFlag !== 1) {
      this.emptyFlag = 0;
      this.showSpinner = false;
    }
  }

}
