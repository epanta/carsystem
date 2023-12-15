import { CarService } from './../../service/car.service';
import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CarResponse } from 'src/app/model/car-response';
import { Car } from 'src/app/model/car.model';
import { SharedService } from 'src/app/service/shared.service';

@Component({
  selector: 'app-car-new',
  templateUrl: './car-new.component.html',
  styleUrls: ['./car-new.component.css']
})
export class CarNewComponent {

  @ViewChild("form")
  form: NgForm

  car = new Car('', 0, '', '', '', 0, '');

  shared: SharedService;
  message: any;
  classCss: any;

  constructor(
    private carService: CarService,
    private route: ActivatedRoute

  ) {
    this.shared = SharedService.getInstance();
  }

  ngOnInit() {
    let id: string = this.route.snapshot.params['id'];
    if (id != undefined) {
      this.findById(id);
    }
  }

  findById(id:string) {
    this.carService.findById(id).subscribe((carResponse: CarResponse) => {
      this.car.id = carResponse.id;
      this.car.year = carResponse.year;
      this.car.color = carResponse.color;
      this.car.licensePlate = carResponse.licensePlate;
      this.car.model = carResponse.model;
      this.car.image = carResponse.image;
    }, err => {
      this.showMessage({
        type: 'error',
        text: err['error']['message']
      });
    });
  }

  register() {
    this.message = {};
    this.carService.createOrUpdate(this.car).subscribe((carResponse: CarResponse) => {
      this.car = new Car('', 0, '', '', '', 0, '');
      this.form.resetForm();
      this.showMessage({
        type: 'success',
        text: 'Registered ' + carResponse.licensePlate + ' success'
      });
    }, err => {
      this.showMessage({
        type: 'error',
        text: err['error']['message']
      });
    });
  }

  private showMessage(message: {type: string, text: string}) : void {
    this.message = message;
    this.buildClasses(message.type);
    setTimeout(() => {
     this.message = undefined; 
    }, 3000);
  }

  private buildClasses(type: string): void {
    this.classCss = {
      'alert' : true
    }
    this.classCss['alert-'+type] = true;
  }

  getFromGroupClass(isInvalid: boolean | null, isDirty: boolean | null): {} {
    return {
      'form-group row': true,
      'has-error': isInvalid && isDirty,
      'has-success': !isInvalid && isDirty
    }
  }

}
