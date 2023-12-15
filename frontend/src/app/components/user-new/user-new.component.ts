import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { UserResponse } from 'src/app/model/user-response';
import { User } from 'src/app/model/user.model';
import { SharedService } from 'src/app/service/shared.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-user-new',
  templateUrl: './user-new.component.html',
  styleUrls: ['./user-new.component.css']
})
export class UserNewComponent {

  @ViewChild("form")
  form: NgForm

  user = new User('', '', '', '', '', '', '', new Date(), '', '', '', '', new Array());
  shared = new SharedService();
  message : any;
  classCss : any;
  sucessMessage: string;

  constructor(
    private userService: UserService,
    private route: ActivatedRoute
  ) {
    this.shared = SharedService.getInstance();
  }

  ngOnInit() {
    let id : string = this.route.snapshot.params['id'];
    if (id != undefined) {
      this.findById(id);
    }
  }

  findById(id:string) {
    this.userService.findById(id).subscribe((userResponse: UserResponse) => {
      this.user.id = userResponse.id;
      this.user.firstName = userResponse.firstName;
      this.user.lastName = userResponse.lastName;
      this.user.login = userResponse.login;
      this.user.email = userResponse.email;
      this.user.phone = userResponse.phone;
      this.user.role = userResponse.role;
      this.user.birthday = userResponse.birthday;
      this.user.createdAt = userResponse.createdAt;
      this.user.lastLogin = userResponse.lastLogin;
      this.user.cars = userResponse.cars;

    }, err => {
      this.showMessage({
        type: 'error',
        text: err['error']['message']
      });
    });
  }

  register() {
    this.message = {};
    this.userService.createOrUpdate(this.user).subscribe((userResponse: UserResponse) => {
      this.user = new User('', '', '', '', '', '', '', new Date(), '', '', '', '', new Array());
      this.form.resetForm();
      this.showMessage({
        type: 'success',
        text: 'Registered ' + userResponse.login + ' success'
      });
    }, err => {
      this.showMessage({
        type: 'error',
        text: err['error']['message']

      });
    });
  }

  private showMessage(message: {type: string, text: string}) : void {
    this.message = message;
    this.buildClasses(message.type);
    setTimeout(() => {
     this.message = undefined; 
    }, 3000);
  }

  private buildClasses(type: string): void {
    this.classCss = {
      'alert' : true
    }
    this.classCss['alert-'+type] = true;
  }

  getFromGroupClass(isInvalid: boolean | null, isDirty: boolean | null): {} {
    return {
      'form-group row': true,
      'has-error': isInvalid && isDirty,
      'has-success': !isInvalid && isDirty
    }
  }

}
