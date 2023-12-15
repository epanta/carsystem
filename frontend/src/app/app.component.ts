import { Component } from '@angular/core';
import { SharedService } from './service/shared.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  showTemplate: boolean = false;
  public shared: SharedService;

  constructor() {
    this.shared = SharedService.getInstance();
  }

  ngOnInit() {
  /*  this.shared.showTemplate.subscribe(
      show => this.showTemplate = show
    ); */
    this.showTemplate = true; //novo
    this.shared.showTemplate.emit(true); //novo
  }

  showContentWrapper() {
    return {
      'content-wrapper': true //this.shared.isLoggedIn()
    }
  }

}
