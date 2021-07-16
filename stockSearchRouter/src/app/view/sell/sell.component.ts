import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {FormControl} from '@angular/forms';

@Component({
  selector: 'app-sell',
  templateUrl: './sell.component.html',
  styleUrls: ['./sell.component.css']
})
export class SellComponent implements OnInit {

  @Input() allInfo: any;
  storage = window.localStorage;
  @Output() toParent = new EventEmitter();
  @Output() close = new EventEmitter();
  constructor(public activeModal: NgbActiveModal) {
  }

  quantity = new FormControl(0);
  ngOnInit(): void {}

  // tslint:disable-next-line:typedef
  store(){
    const value = JSON.parse(this.storage.getItem(this.allInfo.ticker));
    const average = Math.round((value[3].totalCost / value[3].quantity) * 1000) / 1000;
    this.allInfo.quantity = value[3].quantity - this.quantity.value;
    this.allInfo.totalCost = Math.round((value[3].totalCost - average * this.quantity.value) * 1000) / 1000;
    if (this.allInfo.quantity <= 0){
      value[2] = 0;
      value[3] = 0;
      this.close.emit(null);
    }
    else { value[3] = this.allInfo; }
    this.storage.setItem(this.allInfo.ticker, JSON.stringify(value));
    this.toParent.emit(this.allInfo);
  }

}
