import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegistrationPaymentComponent } from './registration-payment.component';

const routes: Routes = [
  { path: '', component: RegistrationPaymentComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RegistrationPaymentRoutingModule {}
