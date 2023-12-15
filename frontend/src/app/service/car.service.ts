import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Car } from '../model/car.model';
import { CAR_SERVICE_API } from './carservice.api';
import { CarResponse } from '../model/car-response';

@Injectable({
  providedIn: 'root'
})
export class CarService {

  constructor(private http:HttpClient) { }

  createOrUpdate(car:Car) {
    if (car.id != null && car.id != '') {
      return this.http.put<CarResponse>(`${CAR_SERVICE_API}/api/cars/${car.id}`, car)
    } else {
      car.id = '';
      return this.http.post<CarResponse>(`${CAR_SERVICE_API}/api/cars`, car)
    }
  }

  findAll(page:number, count:number) {
    let params = new HttpParams();
    params = params.append('page', page);
    params = params.append('count', count);
    return this.http.get<any>(`${CAR_SERVICE_API}/api/cars`, {params: params});
  }

  findById(id:string) {
    return this.http.get<CarResponse>(`${CAR_SERVICE_API}/api/cars/${id}`);
  }

  delete(id:string) {
    return this.http.delete(`${CAR_SERVICE_API}/api/cars/${id}`);
  }

  update(car:Car) {
    return this.http.put(`${CAR_SERVICE_API}/api/cars/${car.id}`, car);
  }

  uploadFile(id:string, car:Car) {
    return this.http.patch<any>(`${CAR_SERVICE_API}/api/cars/${id}`, car);
  }

}
