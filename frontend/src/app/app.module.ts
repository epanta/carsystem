import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';


import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { MenuComponent } from './components/menu/menu.component';
import { FooterComponent } from './components/footer/footer.component';
import { LoginComponent } from './components/secutiry/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { routes } from './app.routes';
import { UserService } from './service/user.service';
import { SharedService } from './service/shared.service';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AuthInterceptor } from './components/secutiry/login/auth.interceptor';
import { AuthGuard } from './components/secutiry/login/auth.guard';
import { UserNewComponent } from './components/user-new/user-new.component';
import { UserListComponent } from './components/user-list/user-list.component';
import { DialogService } from './service/dialog.service';
import { CarNewComponent } from './components/car-new/car-new.component';
import { CarService } from './service/car.service';
import { CarListComponent } from './components/car-list/car-list.component';
import { UploadImageComponent } from './components/upload-image/upload-image.component';
import { CarSearchComponent } from './components/car-search/car-search.component';
import { UserSearchComponent } from './components/user-search/user-search.component';
import { CarDetailComponent } from './components/car-detail/car-detail.component';
import { UserDetailComponent } from './components/user-detail/user-detail.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    MenuComponent,
    FooterComponent,
    LoginComponent,
    HomeComponent,
    UserNewComponent,
    UserListComponent,
    CarNewComponent,
    CarListComponent,
    UploadImageComponent,
    CarSearchComponent,
    UserSearchComponent,
    CarDetailComponent,
    UserDetailComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    routes
  ],
  providers: [
    UserService, 
    SharedService,
    DialogService,
    CarService,
    AuthGuard,
    {
      provide: HTTP_INTERCEPTORS, 
      useClass: AuthInterceptor,
      multi: true
    }],
  bootstrap: [AppComponent]
})
export class AppModule { }
