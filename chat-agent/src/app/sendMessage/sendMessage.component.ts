import { HomeService } from 'src/app/home.service';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-sendMessage',
  templateUrl: './sendMessage.component.html',
  styleUrls: ['./sendMessage.component.css']
})
export class SendMessageComponent implements OnInit {
  @Input() user: any;
  @Input() sendToEveryone: any;
  @Output() notifyParent: EventEmitter<any> = new EventEmitter();
  form: FormGroup;
  submitted: boolean;
  constructor(private formBuilder: FormBuilder,
    private userService: HomeService) { }

  ngOnInit() {
    this.form = this.formBuilder.group({
			subject: [, [Validators.required,]],
			content: [, [Validators.required,]]
    });
    console.log(this.user);
  }
  onSubmit() {
		this.submitted = true;

		if (this.form.invalid) {
			return;
    }
    let currentUser = this.userService.getUser();
    console.log(currentUser);
    let message = {
      sender: currentUser.username,
      receiver: this.user,
      subject: this.form.controls.subject.value,
      content: this.form.controls.content.value,
      date: null
    }
    if(!this.sendToEveryone){
      this.userService.sendMessageToUser(message).subscribe(
        (data: any) => { 
          this.notifyParent.emit();
        }	,
        (error) => { alert(error); }
        );
    }else{
      this.userService.sendMessageToEveryone(message).subscribe(
        (data: any) => { 
          this.notifyParent.emit();
        }	,
        (error) => { alert(error); }
        );
    }

  }
  cancel(){
    
  }
}
