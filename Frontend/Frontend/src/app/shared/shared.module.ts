import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon'
import { MatMenuModule } from '@angular/material/menu'
import { MatToolbarModule } from '@angular/material/toolbar'
import { RouterModule } from '@angular/router';
import { FriendsForComponent } from './friends-for/friends-for.component';
import { ContactsForComponent } from './contacts/contacts.component';
import { SearchBarComponent } from './search-bar/search-bar.component';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {MatBadgeModule} from '@angular/material/badge';

@NgModule({
  declarations: [
    NavBarComponent,
    FriendsForComponent,
    ContactsForComponent,
    SearchBarComponent
  ],
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatToolbarModule,
    MatAutocompleteModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    RouterModule,
    MatBadgeModule
  ],
  exports: [
    NavBarComponent,
    FriendsForComponent,
    ContactsForComponent,
    SearchBarComponent
  ]
})
export class SharedModule { }
