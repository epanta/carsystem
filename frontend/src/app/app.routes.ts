import { RouterModule, Routes } from "@angular/router";
import { HomeComponent } from "./components/home/home.component";
import { LoginComponent } from "./components/secutiry/login/login.component";
import { ModuleWithProviders } from "@angular/core";
import { AuthGuard } from "./components/secutiry/login/auth.guard";
import { UserNewComponent } from "./components/user-new/user-new.component";
import { UserListComponent } from "./components/user-list/user-list.component";
import { CarNewComponent } from "./components/car-new/car-new.component";
import { CarListComponent } from "./components/car-list/car-list.component";
import { CarDetailComponent } from "./components/car-detail/car-detail.component";
import { UserDetailComponent } from "./components/user-detail/user-detail.component";


export const ROUTES: Routes = [
    { path : '', component: HomeComponent, canActivate: [AuthGuard]},
    { path : 'login', component : LoginComponent },
    { path : 'user-new', component : UserNewComponent},
    { path : 'user-new/:id', component : UserNewComponent},
    { path : 'user-list', component : UserListComponent},
    { path : 'car-new', component : CarNewComponent, canActivate: [AuthGuard] },
    { path : 'car-list', component : CarListComponent, canActivate: [AuthGuard] },
    { path : 'car-new/:id', component : CarNewComponent, canActivate: [AuthGuard] },
    { path : 'car-detail/:id', component : CarDetailComponent, canActivate: [AuthGuard] },
    { path : 'user-detail/:id', component : UserDetailComponent }
]

export const routes: ModuleWithProviders<RouterModule> = RouterModule.forRoot(ROUTES);