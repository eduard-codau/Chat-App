import { NgModule } from '@angular/core';
import {MatIconModule} from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { SharedModule} from 'src/app/shared/shared.module'
import { UserProfileComponent } from './profile/user-profile.component';

@NgModule({
  declarations: [
    UserProfileComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    MatIconModule
    ],
  exports:[
    UserProfileComponent
  ]
})
export class UserProfileModule { }
