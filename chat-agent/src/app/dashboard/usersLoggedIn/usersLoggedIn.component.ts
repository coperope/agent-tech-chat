
import { Component, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { HomeService } from 'src/app/home.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { webSocket } from 'rxjs/webSocket';
@Component({
  selector: 'app-usersLoggedIn',
  templateUrl: './usersLoggedIn.component.html',
  styleUrls: ['./usersLoggedIn.component.css']
})
export class UsersLoggedInComponent implements OnInit {
  @ViewChild('sendMessageModal', { static: true }) sendMessageModal: TemplateRef<any>;
  
  Headers = ['Username', 'Host'];
  users: any;
  user: any;
  navigationSubscription: any;
  
  greeting: any;
  name: string;
  subject = webSocket('ws://localhost:8080/WAR2020/ws');

  modalData: {
		user: any;
	};
	constructor(private router: Router,
    private HomeService: HomeService,
    private modal: NgbModal,
	) {
	}

	ngOnInit() {
		this.getUsersLoggedIn();
		this.subject.subscribe(
			msg => {
				this.users = msg;
				console.log(msg);
			}, // Called whenever there is a message from the server.
			err => console.log(err), // Called if at any point WebSocket API signals some kind of error.
			() => console.log('complete') // Called when connection is closed (for whatever reason).
		  );
	}

	getUsersLoggedIn() {
		this.HomeService.getUsersLoggedIn().subscribe(
			(data) => {
				this.users = data;
			},
			(error) => {
				alert(error);
			}
		);
  }
  sendMessage(user, component){
	this.user = user;
	console.log(this.user);
    let action = "Opened";
	this.modal.open(component, { size: 'lg' });
  }
  sendToEveryone(){
	  
  }

  connect(){
	this.subject.next(this.greeting);
  }

  disconnect(){
    this.subject.complete();
  }

  sendMessageWS(){
    this.subject.next(this.name);
    
  }

  handleMessage(message){
    this.greeting = message;
  }
}
