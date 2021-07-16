import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-watch-card',
  templateUrl: './watch-card.component.html',
  styleUrls: ['./watch-card.component.css']
})
export class WatchCardComponent implements OnInit {
  @Input() allInfo: any;
  @Output() close = new EventEmitter();

  ngOnInit(): void { }

  // tslint:disable-next-line:typedef
  remove(){
    const value = JSON.parse(localStorage.getItem(this.allInfo.ticker));
    value[1] = 0;
    localStorage.setItem(this.allInfo.ticker, JSON.stringify(value));
    this.close.emit(null);
  }
}
