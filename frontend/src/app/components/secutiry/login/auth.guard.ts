import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { Observable } from "rxjs";
import { SharedService } from "src/app/service/shared.service";

@Injectable()
export class AuthGuard {

    public shared: SharedService;

    constructor(private router: Router) {
        this.shared = SharedService.getInstance();
    }

    canActivate(route: ActivatedRouteSnapshot, 
        state: RouterStateSnapshot): Observable<boolean> | boolean {
      if (!this.shared.isLoggedIn()) {
        this.router.navigate(['/login']);
        return false;
      }
      // logged in, so return true
      this.shared.isLoggedIn();
      return true;
    }

}