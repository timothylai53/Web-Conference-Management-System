import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ReviewSetupComponent } from './review-setup.component';

const routes: Routes = [
  { path: '', component: ReviewSetupComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ReviewSetupRoutingModule {}
