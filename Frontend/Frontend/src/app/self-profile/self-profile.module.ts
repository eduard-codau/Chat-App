import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon'
import { SharedModule } from '../shared/shared.module';
import { ProfileComponent } from './profile/profile.component';
import { NavBarComponent } from '../shared/nav-bar/nav-bar.component';
import { EditProfileComponent } from './edit-profile/edit-profile.component';
import { ImageUploadComponent } from './image-upload/image-upload.component';
import { FormsModule } from '@angular/forms';
@NgModule({
  declarations: [
    ProfileComponent,
    EditProfileComponent,
    ImageUploadComponent

  ],
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    SharedModule,
    FormsModule
  ],
  exports: [
    ProfileComponent,
    EditProfileComponent
    
  ]
})
export class SelfProfileModule { }
