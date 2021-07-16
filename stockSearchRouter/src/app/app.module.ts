import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { IndexComponent } from './view/index/index.component';
import { DetailsComponent } from './view/details/details.component';
import { WatchlistComponent } from './view/watchlist/watchlist.component';
import { PortfolioComponent } from './view/portfolio/portfolio.component';
import {HttpClientModule} from '@angular/common/http';
import {AppRoutingModule} from './app-routing.module';
import {MatTabsModule} from '@angular/material/tabs';
import {HighchartsChartModule} from 'highcharts-angular';

import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {ReactiveFormsModule, FormsModule} from '@angular/forms';
import { NoResultComponent } from './view/no-result/no-result.component';
import { NewsCardComponent } from './view/news-card/news-card.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NewsContentComponent } from './view/news-card/news-content/news-content.component';
import { BuyComponent } from './view/buy/buy.component';
import { WatchCardComponent } from './view/watch-card/watch-card.component';
import { PortfolioCardComponent } from './view/portfolio-card/portfolio-card.component';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import { SellComponent } from './view/sell/sell.component';
import {RouterModule} from '@angular/router';
import { HashLocationStrategy, LocationStrategy  } from '@angular/common';

@NgModule({
  declarations: [
    AppComponent,
    IndexComponent,
    DetailsComponent,
    WatchlistComponent,
    PortfolioComponent,
    NoResultComponent,
    NewsCardComponent,
    NewsContentComponent,
    BuyComponent,
    WatchCardComponent,
    PortfolioCardComponent,
    SellComponent
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    MatAutocompleteModule,
    MatInputModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    FormsModule,
    MatTabsModule,
    HighchartsChartModule,
    NgbModule,
    MatProgressSpinnerModule
  ],
  // providers: [{provide : LocationStrategy , useClass: HashLocationStrategy}],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
