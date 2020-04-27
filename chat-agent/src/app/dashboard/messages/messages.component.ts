import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { HomeService } from 'src/app/home.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit {
  @ViewChild('sendMessageModal', { static: true }) sendMessageModal: TemplateRef<any>;
  
  inbox: any;
  user: any;
  currUser: any;
  messages: any;
  constructor(private HomeService: HomeService,
    private modal: NgbModal,) { }

  ngOnInit() {
    this.getUserMessages();
    this.currUser = this.HomeService.getUser();
  }

  getUserMessages(){
    this.HomeService.getUserMessages().subscribe(
			(data) => {
				this.inbox = data;
			},
			(error) => {
				alert(error);
			}
		);
  }
  RowSelected(u){
    console.log(u);
    this.messages = u;
  }
  sendToEveryone(component){
    this.modal.open(component, { size: 'lg' });
  }
  sendToUser(){

  }
}
