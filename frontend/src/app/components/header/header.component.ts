import { Component } from '@angular/core';
import { SharedService } from 'src/app/service/shared.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  public shared: SharedService;

  constructor() {
    this.shared = SharedService.getInstance();
    this.shared.role = '';
  }

  signOut() : void {
    this.shared.token = '';
    this.shared.role = '';
    this.shared.login = '';
    this.shared.id = '';
    window.location.href = '/login';
    window.location.reload;
  }

}
