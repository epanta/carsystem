import { CarResponse } from "./car-response";
import { Car } from "./car.model";

export class UserResponse {

    public id: string;

    public firstName: string;

    public lastName: string;

    public email: string;

    public birthday: Date;

    public login: string;

    public role: string;

    public phone: string;

    public createdAt: string;

    public lastLogin: string;

    public cars: Array<Car>;

    public image: string;
}