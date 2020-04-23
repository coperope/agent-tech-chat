import { HomeService } from './../home.service';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpHeaders } from '@angular/common/http';
import {webSocket, WebSocketSubject} from 'rxjs/webSocket';

@Component({
  selector: 'app-messenger',
  templateUrl: './messenger.component.html',
  styleUrls: ['./messenger.component.css']
})
export class MessengerComponent implements OnInit {

  form: FormGroup;
	submitted = false;
  greeting: any;
  name: string;
  subject = webSocket('ws://localhost:8080/WAR2020/ws')
	constructor(public HomeService: HomeService,
		private router: Router,
    private formBuilder: FormBuilder,
	) {
	}

  ngOnInit() {
		this.form = this.formBuilder.group({
			message: ['', Validators.compose([Validators.required, Validators.minLength(6), Validators.maxLength(24)])]
    });
    this.subject.subscribe();
    this.greeting = "CAO";
  }
  onSubmit() {
		this.submitted = true;

		this.HomeService.submit(this.form.value).subscribe(() => {
      //this.router.navigate(['/login', { msgType: 'success', msgBody: 'Success! Please sign in with your new password.' }]);
    }, error => {
      this.submitted = false;
      alert('GRESKA');
    });

  }
  

  connect(){
    this.subject.subscribe();
    this.subject.next(this.greeting);
  }

  disconnect(){
    this.subject.complete();
  }

  sendMessage(){
    this.subject.next(this.name);
    
  }

  handleMessage(message){
    this.greeting = message;
  }

}
