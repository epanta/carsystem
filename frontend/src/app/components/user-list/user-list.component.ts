import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { UserResponse } from 'src/app/model/user-response';
import { User } from 'src/app/model/user.model';
import { DialogService } from 'src/app/service/dialog.service';
import { SharedService } from 'src/app/service/shared.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent {

  page:number=0;
  count:number=5;
  pages:Array<number>;
  shared = new SharedService();
  message : any;
  classCss : any;
  listUser : Array<User> = [];
  user = new User('', '', '', '', '', '', '', new Date(), '', '', '', '', new Array());
  userFilter = new User('', '', '', '', '', '', '', new Date(), '', '', '', '', new Array());

  constructor(
    private dialogService: DialogService,
    private userService: UserService,
    private router: Router
  ) {
    this.shared = SharedService.getInstance();
  }

  ngOnInit() {
    this.findAll(this.page, this.count);
  }

  findAll(page:number, count: number) {
    this.userService.findAll(page, count).subscribe((userResponse: any) => {
      this.listUser = userResponse['content']
      this.pages = new Array(userResponse['totalPages']);
    }, err => {
      this.showMessage({
        type: 'error',
        text: err['error']['message']
      });
    });
  }

  filter(id:string) : void {
    this.page = 0;
    this.count = 1;
    this.pages = [1]
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

    this.listUser = [this.user]
    }, err => {
      this.showMessage({
        type: 'error',
        text: err['error']['message']
      });
    });

  }

  clearFilter(): void {
    this.page = 0;
    this.count = 5;
    this.userFilter = new User('', '', '', '', '', '', '', new Date(), '', '', '', '', new Array());
    this.findAll(this.page, this.count)
  }

  edit(id:string) {
    this.router.navigate(['/user-new', id])
  }

  detail(id:string) {
    this.router.navigate(['/user-detail', id]);
  }

  file(id:string) {
    this.router.navigate(['/image-new', id])
  }


  delete(id:string) {
    this.dialogService.confirm(' Deseja excluir o usuário?')
      .then((confirmed) => {
        if(confirmed) {
          this.message = {};
          this.userService.delete(id).subscribe((response: any) => {
            //this.form.resetForm();
            this.showMessage({
              type: 'success',
              text: 'Registro excluído'
            });
            this.findAll(this.page, this.count);
          }, err => {
            this.showMessage({
              type: 'error',
              text: err['error']['message']
            });
          });
        }
      });
      window.location.reload();
  }

  setNextPage(event:any) {
    event.preventDefault(); //evita o reload da tela
    if (this.page +1 < this.pages.length) { //se atingiu o limite de paginas nao vai pra proxima pagina
      this.page = this.page + 1;
      this.findAll(this.page, this.count);
    }
  }

  setPreviousPage(event:any) {
    event.preventDefault(); //evita o reload da tela
    if (this.page > 0) { //se chegou na ultima pagina nao vai continuar
      this.page = this.page - 1;
      this.findAll(this.page, this.count);
    }
  }

  setPage(i:number, event:any) {
    event.preventDefault(); //evita o reload da tela
    this.page = i;
    this.findAll(this.page, this.count);   
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

}
