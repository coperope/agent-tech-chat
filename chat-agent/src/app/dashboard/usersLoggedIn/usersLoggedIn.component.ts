
import { Component, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { HomeService } from 'src/app/home.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
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
  
  modalData: {
		user: any;
	};
	constructor(private router: Router,
    private HomeService: HomeService,
    private modal: NgbModal,
	) {
		this.navigationSubscription = this.router.events.subscribe((e: any) => {
			if (e instanceof NavigationEnd) {
				this.getUsersLoggedIn();
			}
		});
	}

	ngOnInit() {
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
}
