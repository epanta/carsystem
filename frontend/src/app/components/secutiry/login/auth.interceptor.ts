import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { SharedService } from "src/app/service/shared.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    shared: SharedService;

    constructor() {
        this.shared = SharedService.getInstance();
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        let authRequest: any;

        if (this.shared.isLoggedIn()) {
            authRequest = req.clone({
                    setHeaders: {
                        'Authorization' : 'Bearer ' + this.shared.token
                    }
                });
            return next.handle(authRequest);
        } else {
            return next.handle(req);
        }
    }

}