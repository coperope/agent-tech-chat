import { HomeService } from './../home.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
	selector: 'app-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

	loginForm: FormGroup;
	loading = false;
	submitted = false;
	returnUrl: string = '';

	constructor(
		private formBuilder: FormBuilder,
		private route: ActivatedRoute,
		private router: Router,
		private userService: HomeService
		// private authenticationService: AuthenticationService,
	) { }

	ngOnInit() {
		this.loginForm = this.formBuilder.group({
			username: ['', [Validators.required]],
			password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(24)]]
		});
	}

	onSubmit() {
		this.submitted = true;


		this.loading = true;
		this.userService.login(this.loginForm.controls.username.value, this.loginForm.controls.password.value)
			.pipe()
			.subscribe(
				data => {
					//this.router.navigate([this.returnUrl]);
					alert("Uspjeh");
				},
				error => {
					this.loading = false;
					alert(error);
				});
	}
}
