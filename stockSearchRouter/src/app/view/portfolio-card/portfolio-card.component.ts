import {ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {BuyComponent} from '../buy/buy.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {SellComponent} from '../sell/sell.component';

@Component({
  selector: 'app-portfolio-card',
  templateUrl: './portfolio-card.component.html',
  styleUrls: ['./portfolio-card.component.css']
})
export class PortfolioCardComponent implements OnInit {

  @Input() savedInfo: any;
  @Input() allInfo: any;
  @Output() sendToParent = new EventEmitter<string>();
  @Output() close = new EventEmitter();
  average: number;
  change: number;
  marketValue: number;
  constructor(private modalService: NgbModal, public changeDetectorRef: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.average = Math.round((this.savedInfo.totalCost / this.savedInfo.quantity) * 1000) / 1000;
    this.change = Math.round((this.allInfo.last - this.average) * 1000) / 1000;
    this.savedInfo.totalCost = Math.round(this.savedInfo.totalCost * 1000) / 1000;
    this.marketValue = Math.round((this.savedInfo.quantity * this.allInfo.last) * 1000) / 1000;
  }
  // tslint:disable-next-line:typedef
  openModal(choice) {
    let modalRef;
    if (choice === 'buy'){
      modalRef = this.modalService.open(BuyComponent);
    }
    else{
      modalRef = this.modalService.open(SellComponent);
    }
    modalRef.componentInstance.allInfo = this.savedInfo;
    modalRef.componentInstance.toParent.subscribe(info => {
      this.sendToParent.emit(null);
    });
    modalRef.componentInstance.close.subscribe(info => {
      this.close.emit(null);
    });
  }
}
