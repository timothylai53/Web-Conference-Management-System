import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { ReviewSetupRoutingModule } from './review-setup-routing-module';
import { ReviewSetupComponent } from './review-setup.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule,
    ReviewSetupRoutingModule,
    ReviewSetupComponent
  ]
})
export class ReviewSetupModule {}
