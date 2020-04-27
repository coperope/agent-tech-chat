import { UsersLoggedInComponent } from './dashboard/usersLoggedIn/usersLoggedIn.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponentComponent } from './HomeComponent/HomeComponent.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { MessengerComponent } from './messenger/messenger.component';
import { CommonModule } from "@angular/common";
import { UsersRegisteredComponent } from './dashboard/usersRegistered/usersRegistered.component';
import { SendMessageComponent } from './sendMessage/sendMessage.component';
import { MessagesComponent } from './dashboard/messages/messages.component';

const routes: Routes = [
  {
		path: '',
		component: HomeComponentComponent
  },	
  {
		path: 'login',
		component: LoginComponent
	},
	{
		path: 'register',
		component: RegisterComponent
  },
  {
		path: 'messenger',
		component: MessengerComponent
  },
  {
		path: 'dashboard',
    component: DashboardComponent,
    children:[
      {path: 'usersLoggedIn', component: UsersLoggedInComponent},
      {path: 'usersRegistered', component: UsersRegisteredComponent},
      {path: 'messages', component: MessagesComponent},
      {path: 'sendMessage', component: SendMessageComponent},
		],
	}
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {onSameUrlNavigation: 'reload', useHash: true}),
    CommonModule
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
