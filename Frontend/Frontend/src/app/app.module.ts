import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SelfProfileModule } from './self-profile/self-profile.module';
import { HomeModule } from './home/home.module'
import { UserAuthModule } from './user-auth/user-auth.module';
import { HomepageModule } from './homepage/homepage.module';
import { UserProfileModule } from './user-profile/user-profile.module';
import { ChatModule } from './chat/chat.module';
import { SharedModule } from './shared/shared.module';


//ca sa mearga apelurile http pentru servicii
import { ConversationsService } from './shared/services/Conversation/conversations.service';
import { HttpClientModule } from '@angular/common/http';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    SharedModule,
    SelfProfileModule,
    HomeModule,
    UserAuthModule,
    HomepageModule,
    UserProfileModule,
    ChatModule,

    HttpClientModule
  ],
  providers: [ConversationsService],
  bootstrap: [AppComponent]
})
export class AppModule { }
