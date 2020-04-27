import { MessagesComponent } from './dashboard/messages/messages.component';
import { UsersRegisteredComponent } from './dashboard/usersRegistered/usersRegistered.component';
import { UsersLoggedInComponent } from './dashboard/usersLoggedIn/usersLoggedIn.component';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { NgHttpLoaderModule } from 'ng-http-loader';
import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HomeComponentComponent } from './HomeComponent/HomeComponent.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { CookieService } from 'ngx-cookie-service';
import { HeaderComponent } from './header/header.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { MessengerComponent } from './messenger/messenger.component';
import { SendMessageComponent } from './sendMessage/sendMessage.component';

@NgModule({
   declarations: [
      AppComponent,
      HomeComponentComponent,
      LoginComponent,
      RegisterComponent,
      HeaderComponent,
      DashboardComponent,
      MessengerComponent,
      SendMessageComponent,
      UsersLoggedInComponent,
      UsersRegisteredComponent,
      MessagesComponent
   ],
   imports: [
      BrowserModule,
      AppRoutingModule,
      NgHttpLoaderModule.forRoot(),
      FormsModule,
      HttpClientModule,
      ReactiveFormsModule
   ],
   providers: [
      CookieService
   ],
   bootstrap: [
      AppComponent
   ]
})
export class AppModule { }
