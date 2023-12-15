import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../model/user.model';
import { CAR_SERVICE_API } from './carservice.api';
import { AuthenticationRequest } from '../model/auth.request.model';
import { LoginResponse } from '../model/login-response.model';
import { UserResponse } from '../model/user-response';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

login(authenticationRequest: AuthenticationRequest) {
  return this.http.post<LoginResponse>(`${CAR_SERVICE_API}/api/signin`, authenticationRequest)
}

createOrUpdate(user:User) {
  if (user.id != null && user.id != '') {
    return this.http.put<UserResponse>(`${CAR_SERVICE_API}/api/users/${user.id}`, user);
  } else {
    return this.http.post<UserResponse>(`${CAR_SERVICE_API}/api/users`, user);
  }
}

findAll(page:number,count:number) {
  let params = new HttpParams();
  params = params.append('page', page);
  params = params.append('count', count);
  
  return this.http.get<any>(`${CAR_SERVICE_API}/api/users`, {params: params});
}

findById(id:string) {
  return this.http.get<UserResponse>(`${CAR_SERVICE_API}/api/users/${id}`)
}

delete(id:string) {
  return this.http.delete(`${CAR_SERVICE_API}/api/users/${id}`);
}

uploadFile(id:string, user:User) {
  return this.http.patch<any>(`${CAR_SERVICE_API}/api/users/${id}`, user);
}

}
