import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {FormControl} from '@angular/forms';
import {Observable, Subject} from 'rxjs';
import {debounceTime, distinctUntilChanged} from 'rxjs/operators';

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})


export class IndexComponent implements OnInit {
  private getDataTerms = new Subject<string>();
  showSpinner = 0;
  data: object;


  searchValue = new FormControl('');
  constructor(private http: HttpClient) {
  }
  readonly SERVER: string = 'https://stocknodebackend.wl.r.appspot.com';
  // readonly SERVER: string = 'http://localhost:3000';
  // tslint:disable-next-line:typedef
  getData(value: string){

    this.getDataTerms.next(value);
  }

  // tslint:disable-next-line:typedef
  getName(term) {
      const requestURL = this.SERVER + `/info?searchValue=` + term;
      // console.log(requestURL);
      return this.http.get(requestURL);
  }

  ngOnInit(): void {
    this.getDataTerms.pipe(
      debounceTime(1000),
      distinctUntilChanged()).subscribe(
      term => {
        this.showSpinner = 1;
        this.getName(term).subscribe(data => {
          // console.log(data);
          this.data = data;
          this.showSpinner = 2;
        });

      }
    );
  }
}
