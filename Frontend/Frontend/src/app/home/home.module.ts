import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HomeRoutingModule } from './home-routing.module';
import { HomeRegisteredUserComponent } from './home-registered-user/home-registered-user.component';
import { SharedModule} from 'src/app/shared/shared.module'
import { SelfProfileModule } from '../self-profile/self-profile.module';
import { AppRoutingModule } from '../app-routing.module';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [
    HomeRegisteredUserComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    SharedModule,
    SelfProfileModule
  ],
  exports:[
    HomeRegisteredUserComponent
  ]
})
export class HomeModule { }
