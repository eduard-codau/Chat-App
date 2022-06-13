import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomepageUnregisterUserComponent } from './homepage-unregister-user/homepage-unregister-user.component';
import { RouterModule } from '@angular/router';



@NgModule({
  declarations: [
    HomepageUnregisterUserComponent
  ],
  imports: [
    CommonModule,
    RouterModule
    ]
})
export class HomepageModule { }
