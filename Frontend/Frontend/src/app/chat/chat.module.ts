import { NgModule } from '@angular/core';
import {MatIconModule} from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { SharedModule} from 'src/app/shared/shared.module'
import { ChatPageComponent } from './chat-page/chat-page.component';

@NgModule({
  declarations: [
    ChatPageComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    MatIconModule
    ],
  exports:[
    ChatPageComponent
  ]
})
export class ChatModule { }
