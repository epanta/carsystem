import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CarResponse } from 'src/app/model/car-response';
import { Car } from 'src/app/model/car.model';
import { CarService } from 'src/app/service/car.service';
import { DialogService } from 'src/app/service/dialog.service';
import { SharedService } from 'src/app/service/shared.service';

@Component({
  selector: 'app-car-detail',
  templateUrl: './car-detail.component.html',
  styleUrls: ['./car-detail.component.css']
})
export class CarDetailComponent {

  car = new Car('', 0, '', '', '', 0, '');
  shared: SharedService;
  message: any;
  classCss: any;

  containsImage: boolean = false;

  constructor(
    private dialogService: DialogService,
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

  onFileChange(event:any): void {
    if (event.target.files[0].size > 2000000) {
      this.containsImage = false;
      this.showMessage({
        type: 'error',
        text: 'Tamanho máximo da imagem é 2MB'
      })
    } else {
      this.containsImage = true;
      this.car.image = '';
      var reader = new FileReader();
      reader.onloadend = (e: Event) => {
        this.car.image = reader.result;
      }
      reader.readAsDataURL(event.target.files[0])
    }
  }

  uploadFile(id:string, car:Car) {
    this.dialogService.confirm(' Deseja atualizar a foto?')
    .then((confirmed) => {
      if(confirmed) {
        this.message = {};
        this.carService.uploadFile(id, car).subscribe((carResponse: CarResponse) => {
          this.showMessage({
            type: 'success',
            text: 'Foto atualizada com sucesso'
          });
          this.containsImage = false;
        }, err => {
          this.showMessage({
            type: 'error',
            text: err['error']['message']
          });
        });
      }
    });
  }

}
