import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeRegisteredUserComponent } from './home/home-registered-user/home-registered-user.component';
import { LoginComponent } from './user-auth/login/login.component';
import { RegisterComponent } from './user-auth/register/register.component';
import { ProfileComponent } from './self-profile/profile/profile.component';
import { HomepageUnregisterUserComponent } from './homepage/homepage-unregister-user/homepage-unregister-user.component';
import { UserProfileComponent } from './user-profile/profile/user-profile.component';
import { ChatPageComponent } from './chat/chat-page/chat-page.component';
import { EditProfileComponent } from './self-profile/edit-profile/edit-profile.component';
const routes: Routes = [

  { path: 'registered', component: HomeRegisteredUserComponent },
  { path: '', component: HomepageUnregisterUserComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'user-profile', component: UserProfileComponent },
  { path: "my-profile", component: ProfileComponent },
  { path: "my-profile/chat", component: ChatPageComponent },
  {path: "edit-profile", component:EditProfileComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
