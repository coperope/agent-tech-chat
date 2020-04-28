import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { HomeService } from 'src/app/home.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

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
  form: FormGroup;
  constructor(private HomeService: HomeService,
    private modal: NgbModal,
    private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.getUserMessages();
    this.currUser = this.HomeService.getUser();
    this.form = this.formBuilder.group({
			subject: [, [Validators.required,]],
			content: [, [Validators.required,]]
    });
  }

  getUserMessages(){
    this.HomeService.getUserMessages().subscribe(
			(data) => {
        this.inbox = data;
        if (this.user){
          this.messages = this.inbox[this.user];
          document.getElementById("scrollableContainer").scrollTop = document.getElementById("scrollableContainer").scrollHeight;
        }
			},
			(error) => {
				alert(error);
			}
		);
  }
  RowSelected(u, username, event){
    this.messages = u;
    this.user = username;
  }
  sendToEveryone(component){
    //this.modal.open(component, { size: 'lg' });
    let message = {
      sender: this.currUser.username,
      receiver: null,
      subject: this.form.controls.subject.value,
      content: this.form.controls.content.value,
      date: null
    }
    this.HomeService.sendMessageToEveryone(message).subscribe(
      (data: any) => { 
        this.updateMessages();
      }	,
      (error) => { alert(error); }
      );
  }
  sendToUser(){
    let message = {
      sender: this.currUser.username,
      receiver: this.user,
      subject: this.form.controls.subject.value,
      content: this.form.controls.content.value,
      date: null
    }
    this.HomeService.sendMessageToUser(message).subscribe(
      (data: any) => { 
        this.updateMessages();
      }	,
      (error) => { alert(error); }
      );
  }
  updateMessages(){
    this.getUserMessages();
    this.form.controls.subject.setValue('');
    this.form.controls.content.setValue('');
  }
  scrollToBottom(){
    document.getElementById("scrollableContainer").scrollTop = document.getElementById("scrollableContainer").scrollHeight;
  }
}
