import { NgModule } from '@angular/core';
import {Routes, RouterModule, Router} from '@angular/router';
import {IndexComponent} from './view/index/index.component';
import {DetailsComponent} from './view/details/details.component';
import {WatchlistComponent} from './view/watchlist/watchlist.component';
import {PortfolioComponent} from './view/portfolio/portfolio.component';
import {NoResultComponent} from './view/no-result/no-result.component';

const routes: Routes = [
  {
    path: '',
    component: IndexComponent
  },
  {
    path: 'details/:searchValue',
    component: DetailsComponent
  },
  {
    path: 'watchlist',
    component: WatchlistComponent
  },
  {
    path: 'portfolio',
    component: PortfolioComponent
  },
  {
    path: '**',
    component: NoResultComponent
  }
];

@NgModule({
  declarations: [],
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
