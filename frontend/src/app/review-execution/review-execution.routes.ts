import { Routes } from '@angular/router';
import { ReviewerDashboardComponent } from './reviewer-dashboard/reviewer-dashboard';

export const REVIEW_EXECUTION_ROUTES: Routes = [
  {
    path: 'dashboard',
    component: ReviewerDashboardComponent
  },
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  }
];
