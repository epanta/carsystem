import { LoginResponse } from './../../../model/login-response.model';
import { AuthenticationRequest } from './../../../model/auth.request.model';
import { UserService } from './../../../service/user.service';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { SharedService } from 'src/app/service/shared.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  authenticationRequest = new AuthenticationRequest('', '')
  shared: SharedService;
  message: string = '';

  constructor(
    private userService: UserService,
    private router: Router

  ) {
    this.shared = SharedService.getInstance();
  }

  ngOnInit() {

  }

  submitLogin() {
    this.message = '';
    this.userService.login(this.authenticationRequest).subscribe((loginResponse: LoginResponse) => {
      this.shared.token = loginResponse.token;
      this.shared.role = loginResponse.role;
      this.shared.login = loginResponse.login;
      this.shared.id = loginResponse.id;
      this.shared.showTemplate.emit(true);
      this.router.navigate(['/']);

    }, err => {
      this.shared.token = '';
      this.shared.role = '';
      this.shared.login = '';
      this.shared.id = '';
      this.shared.showTemplate.emit(false);
      this.message = 'Erro';
    });
  }

  cancelLogin() {
    this.message = '';
    window.location.href = '/login';
    window.location.reload();
  }

  getFromGroupClass(isInvalid: boolean | null, isDirty: boolean | null): {} {
    return {
      'form-group row': true,
      'has-error': isInvalid && isDirty,
      'has-success': !isInvalid && isDirty
    }
  }

}
