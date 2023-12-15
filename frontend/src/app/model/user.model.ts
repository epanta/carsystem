import { Car } from "./car.model";

export class User {
    constructor (
        public id: string,
        public login: string,
        public password: string,
        public role: string,
        public firstName: string,
        public lastName: string,
        public email: string,
        public birthday: Date,
        public phone: string,
        public createdAt: string,
        public lastLogin: string,
        public image: string | ArrayBuffer | null,
        public cars: Array<Car>
    ){}
}