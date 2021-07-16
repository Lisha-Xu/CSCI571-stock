import {Component, EventEmitter, Input, OnInit, Output, SimpleChanges} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {FormControl} from '@angular/forms';

@Component({
  selector: 'app-buy',
  templateUrl: './buy.component.html',
  styleUrls: ['./buy.component.css']
})

export class BuyComponent implements OnInit {

  @Input() allInfo: any;
  storage = window.localStorage;
  @Output() toParent = new EventEmitter();
  constructor(public activeModal: NgbActiveModal) {
  }

  quantity = new FormControl(0);
  ngOnInit(): void {}

  ngOnChange(changes: SimpleChanges){
    for (const propName in changes){
      const chng = changes[propName];
      const cur = JSON.stringify(chng.currentValue);
      // console.log(cur);
      this.allInfo = cur;
    }
  }

  // tslint:disable-next-line:typedef
  store(){
    const value = JSON.parse(this.storage.getItem(this.allInfo.ticker));
    // console.log(value);
    if (value == null){
      this.allInfo.quantity = this.quantity.value;
      this.allInfo.totalCost = Math.round(this.quantity.value * this.allInfo.last * 1000) / 1000;
      this.storage.setItem(this.allInfo.ticker, JSON.stringify([0, 0, 1, this.allInfo]));
    }
    else if (value[2] === 0){
      this.allInfo.quantity = this.quantity.value;
      this.allInfo.totalCost = Math.round(this.quantity.value * this.allInfo.last * 1000) / 1000;
      value[2] = 1;
      value[3] = this.allInfo;
      this.storage.setItem(this.allInfo.ticker, JSON.stringify(value));
    }
    else {
      this.allInfo.quantity = value[3].quantity + this.quantity.value;
      this.allInfo.totalCost = Math.round((this.allInfo.last * this.quantity.value + value[3].totalCost) * 1000) / 1000;
      value[3] = this.allInfo;
      this.storage.setItem(this.allInfo.ticker, JSON.stringify(value));
    }
    this.toParent.emit(null);
  }

}
