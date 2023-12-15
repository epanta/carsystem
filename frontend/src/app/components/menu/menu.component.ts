import { Component } from '@angular/core';
import { SharedService } from 'src/app/service/shared.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent {

  shared: SharedService;
  
  constructor() {
    this.shared = SharedService.getInstance();
  }

}
