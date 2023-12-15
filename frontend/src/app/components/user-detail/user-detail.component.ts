import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserResponse } from 'src/app/model/user-response';
import { User } from 'src/app/model/user.model';
import { DialogService } from 'src/app/service/dialog.service';
import { SharedService } from 'src/app/service/shared.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent {

  user = new User('', '', '', '', '', '', '', new Date(), '', '', '', '', new Array());
  shared = new SharedService();
  message : any;
  classCss : any;

  containsImage: boolean = false;

  constructor(
    private dialogService: DialogService,
    private userService: UserService,
    private route: ActivatedRoute
  ) {
    this.shared = SharedService.getInstance();
  }

  ngOnInit() {
    let id:string = this.route.snapshot.params['id'];
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
      this.user.image = userResponse.image;

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

  uploadFile(id:string, user:User) {
    this.dialogService.confirm(' Deseja atualizar a foto?')
    .then((confirmed) => {
      if(confirmed) {
        this.message = {};
        user.password = 'undefined';
        this.userService.uploadFile(id, user).subscribe((userResponse: UserResponse) => {
          this.showMessage({
            type: 'success',
            text: 'Foto atualizada com sucesso'
          });
          this.containsImage = false;
        }, err => {
          this.showMessage({
            type: 'error',
            text: err['error']['message']
          });
        });
      }
    });
  }

  onFileChange(event:any): void {
    if (event.target.files[0].size > 2000000) {
      this.containsImage = false;
      this.showMessage({
        type: 'error',
        text: 'Tamanho máximo da imagem é 2MB'
      })
    } else {
      this.containsImage = true;
      this.user.image = '';
      var reader = new FileReader();
      reader.onloadend = (e: Event) => {
        this.user.image = reader.result;
      }
      reader.readAsDataURL(event.target.files[0])
    }
  }

}
