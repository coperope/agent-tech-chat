import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MustMatch } from './must-match.validator';
import { HomeService } from '../home.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  loginForm: FormGroup;
	loading = false;
	submitted = false;
	returnUrl: string = '';

  constructor(
    private formBuilder: FormBuilder,
		private route: ActivatedRoute,
		private router: Router,
		private userService: HomeService
  ) { }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username:  ['',[Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(24)]],
      confirmPassword: ['', Validators.required]
    }, {
      validator: MustMatch('password', 'confirmPassword')
    });
  }

  onSubmit() {
    this.submitted = true;

    if (this.loginForm.invalid) {
      return;
    }

    var registerRequest = {
			username: this.loginForm.controls.username.value,
      password: this.loginForm.controls.password.value,
      host: null
		}

    this.loading = true;
    this.userService.register(registerRequest)
      .pipe()
      .subscribe(
        data => {
          //this.router.navigate([this.returnUrl]);
        },
        error => {
          this.loading = false;
          alert(error);
        });
  }

}
