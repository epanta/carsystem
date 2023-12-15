import { Router } from '@angular/router';
import { DialogService } from './../../service/dialog.service';
import { Component } from '@angular/core';
import { Car } from 'src/app/model/car.model';
import { CarService } from 'src/app/service/car.service';
import { SharedService } from 'src/app/service/shared.service';
import { CarResponse } from 'src/app/model/car-response';

@Component({
  selector: 'app-car-list',
  templateUrl: './car-list.component.html',
  styleUrls: ['./car-list.component.css']
})
export class CarListComponent {

  page:number=0;
  count:number=5;
  pages:Array<number>;
  car = new Car('', 0, '', '', '', 0, '');
  shared: SharedService;
  message: any;
  classCss: any;
  listCar: Array<Car> = [];
  carFilter = new Car('', 0, '', '', '', 0, '');

  constructor(
    private dialogService: DialogService,
    private carService: CarService,
    private router: Router

  ) {
    this.shared = SharedService.getInstance();
  }

  ngOnInit() {
    this.findAll(this.page, this.count);
  }

  findAll(page:number, count: number) {
    this.carService.findAll(page, count).subscribe((carResponse: any) => {
      this.listCar = carResponse['content']
      this.pages = new Array(carResponse['totalPages']);
    }, err => {
      this.showMessage({
        type: 'error',
        text: err['error']['message']
      });
    });
  }

  filter(id:string) : void {
    this.page = 0;
    this.count = 1;
    this.pages = [1]
    this.carService.findById(id).subscribe((carResponse: CarResponse) => {
    this.car.id = carResponse.id;
    this.car.year = carResponse.year;
    this.car.color = carResponse.color;
    this.car.licensePlate = carResponse.licensePlate;
    this.car.model = carResponse.model;

    this.listCar = [this.car]
    }, err => {
      this.showMessage({
        type: 'error',
        text: err['error']['message']
      });
    });

  }

  clearFilter(): void {
    this.page = 0;
    this.count = 5;
    this.carFilter = new Car('', 0, '', '', '', 0, '');
    this.findAll(this.page, this.count)
  }

  edit(id:string) {
    this.router.navigate(['/car-new', id]);
  }

  detail(id:string) {
    this.router.navigate(['/car-detail', id]);
  }

  delete(id:string) {
    this.dialogService.confirm(' Deseja excluir o carro?')
      .then((confirmed) => {
        if(confirmed) {
          this.message = {};
          this.carService.delete(id).subscribe((response: any) => {
            this.showMessage({
              type: 'success',
              text: 'Registro excluído'
            });
            this.findAll(this.page, this.count);
          }, err => {
            this.showMessage({
              type: 'error',
              text: err['error']['message']
            });
          });
        }
      });
  }

  setNextPage(event:any) {
    event.preventDefault(); //evita o reload da tela
    if (this.page +1 < this.pages.length) { //se atingiu o limite de paginas nao vai pra proxima pagina
      this.page = this.page + 1;
      this.findAll(this.page, this.count);
    }
  }

  setPreviousPage(event:any) {
    event.preventDefault(); //evita o reload da tela
    if (this.page > 0) { //se chegou na ultima pagina nao vai continuar
      this.page = this.page - 1;
      this.findAll(this.page, this.count);
    }
  }

  setPage(i:number, event:any) {
    event.preventDefault(); //evita o reload da tela
    this.page = i;
    this.findAll(this.page, this.count);   
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

}
