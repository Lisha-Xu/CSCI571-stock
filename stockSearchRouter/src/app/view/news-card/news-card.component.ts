import {Component, Input, OnInit} from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {NewsContentComponent} from './news-content/news-content.component';

@Component({
  selector: 'app-news-card',
  templateUrl: './news-card.component.html',
  styleUrls: ['./news-card.component.css']
})
export class NewsCardComponent implements OnInit {

  constructor(private modalService: NgbModal) { }

  @Input() news: any;

  ngOnInit(): void {
  }
  // tslint:disable-next-line:typedef
  open() {
    const modalRef = this.modalService.open(NewsContentComponent);
    modalRef.componentInstance.news = this.news;

  }
}
