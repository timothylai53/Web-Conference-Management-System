import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminDashboard } from './admin-dashboard';  // Import the standalone component
import { AdminDashboardRoutingModule } from './admin-dashboard-routing-module';

@NgModule({
  imports: [
    CommonModule,
    AdminDashboardRoutingModule,
    AdminDashboard,  // Import standalone component here
  ]
})
export class AdminDashboardModule { }
