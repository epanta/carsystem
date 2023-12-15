export class Car {
    constructor (
        public id: string,
        public year: number,
        public licensePlate: string,
        public model: string,
        public color: string,
        public counter: number,
        public image: string | ArrayBuffer | null
    ){}
}