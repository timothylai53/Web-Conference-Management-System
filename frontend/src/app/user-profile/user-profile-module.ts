import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserProfileComponent } from './user-profile';
import { UserProfileRoutingModule } from './user-profile-routing-module';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    UserProfileRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    UserProfileComponent
  ]
})
export class UserProfileModule { }
