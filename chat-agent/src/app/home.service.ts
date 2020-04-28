import { Injectable, Injector } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { interval, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';
@Injectable({
	providedIn: 'root'
})
export class HomeService {
	private user: any = null;
	private get router() { return this.injector.get(Router); }

	constructor(private cookieService: CookieService, private http: HttpClient,
		private injector: Injector) {

	}
	getUser(){
		return this.user;
	}
	setUser(user: any) {
		this.user = user;
		this.cookieService.set('user', JSON.stringify(user));
	}
	removeUser() {
		this.user = null;
		this.cookieService.delete('user');
	}
	checkLoggedIn() {
		if (this.cookieService.get('user')) {
			this.user = JSON.parse(this.cookieService.get('user'));
		}
		return this.user;
	}
	submit(message) {

		var host = "ws://localhost:8080/WAR2020/ws";
		const httpOptions = {
			headers: new HttpHeaders({ 'Content-Type': 'application/text', accept: 'text/plain' }),
			responseType: 'text',
			observe: 'response'
		};
		return this.http.post(`/WAR2020/rest/chat/post/${message.message}`, '', { headers: new HttpHeaders({ 'Content-Type': 'application/text', accept: 'text/plain' }), observe: 'body', responseType: 'text' })
			.pipe(
				map(response => {
					console.log(response);
					return response;
				}),
				catchError((response) => {
					console.log(response);
					return throwError(response.error);
				})
			);

	}

	login(username: string, password: string) {
		if (this.checkLoggedIn()) {
			this.router.navigate(['/']);
		}
		var user = {
			username: username,
			password: password,
			host: null
		}
		const headers = new HttpHeaders({
			'Content-Type': 'application/json'
		});
		return this.http.post('/WAR2020/rest/users/login', user, { headers, observe: 'response' })
			.pipe(
				map((response) => {
					this.setUser(user);
					this.router.navigate(['/dashboard/messages']);

					return this.user;
				}),
				catchError((response) => {
					return throwError(response.error);
				})
			);
	}
	logout() {
		return this.http.delete(`/WAR2020/rest/users/loggedIn/${this.user.username}`, {observe: 'response' })
			.pipe(
				map((response) => {
					this.removeUser();
					this.router.navigate(['/login']);
					return response;
				}),
				catchError((response) => {
					return throwError(response.error);
				})
			);
	}
	register(registerRequest: any) {
		const headers = new HttpHeaders({
			'Content-Type': 'application/json'
		});
		return this.http.post('/WAR2020/rest/users/register', registerRequest, { headers, observe: 'response' })
			.pipe(
				map((response) => {
					const userState = response.body;
					this.router.navigate(['/login']);

					return this.user;
				}),
				catchError((response) => {
					return throwError(response.error);
				})
			);
	}

	getUsersLoggedIn() {
		return this.http.get('/WAR2020/rest/users/loggedIn', { observe: 'response' }).pipe(
			map((response) => {
				console.log(response);
				return response.body;
			}),
			catchError((response) => {
				return throwError(response.error);
			})
		);
	}

	getUsersRegistered() {
		return this.http.get('/WAR2020/rest/users/registered', { observe: 'response' }).pipe(
			map((response) => {
				console.log(response);
				return response.body;
			}),
			catchError((response) => {
				return throwError(response.error);
			})
		);
	}
	sendMessageToUser(message){
		const headers = new HttpHeaders({
			'Content-Type': 'application/json'
		});
		return this.http.post(`/WAR2020/rest/messages/users`, message, { headers, observe: 'response' })
		.pipe(
			map((response) => {
				const userState = response.body;
				return userState;
			}),
			catchError((response) => {
				return throwError(response.error);
			})
		);
	}
	sendMessageToEveryone(message){
		const headers = new HttpHeaders({
			'Content-Type': 'application/json'
		});
		return this.http.post(`/WAR2020/rest/messages/all`, message, { headers, observe: 'response' })
		.pipe(
			map((response) => {
				const userState = response.body;
				return userState;
			}),
			catchError((response) => {
				return throwError(response.error);
			})
		);
	}
	getUserMessages(){
		const headers = new HttpHeaders({
			'Content-Type': 'application/json'
		});
		return this.http.get(`/WAR2020/rest/messages/${this.user.username}`, { observe: 'response' }).pipe(
			map((response) => {
				console.log(response);
				return response.body;
			}),
			catchError((response) => {
				return throwError(response.error);
			})
		);
	}
}
