import { EventEmitter, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SharedService {

  public static instance : SharedService;
  token: string;
  role: string;
  id: string;
  login: string;
  showTemplate = new EventEmitter<boolean>();

  constructor() {
    return SharedService.instance = SharedService.instance || this;
   }

   public static getInstance() {
    if (this.instance == null) {
      this.instance = new SharedService();
    }
    return this.instance;
   }

   isLoggedIn():boolean {
    if (this.token == null) {
      return false;
    }

    if (this.token == '') {
      return false;
    }
    return this.role != '';
   }
}
