import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthRoutingModule } from './auth-routing-module';
import { AuthComponent } from './auth'; // Import the component

@NgModule({
  declarations: [
    // LEAVE THIS EMPTY. Do not declare AuthComponent here.
  ],
  imports: [
    CommonModule,
    AuthRoutingModule,
    RouterModule,
    AuthComponent // <--- IMPORT IT HERE INSTEAD
  ],
  exports: [
    AuthComponent
  ]
})
export class AuthModule { }
