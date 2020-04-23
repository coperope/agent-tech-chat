import { HomeService } from './../home.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';


@Component({
	selector: 'app-header',
	templateUrl: './header.component.html',
	styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
	
	constructor(public HomeService: HomeService,
		private router: Router) { }

	ngOnInit() {
		
	}

	logout() {
    this.HomeService.logout()
    .pipe()
    .subscribe(
      data => {
        //this.router.navigate([this.returnUrl]);
        alert(data);
      },
      error => {
        alert(error);
      });
	}

}
