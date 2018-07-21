import {Hero} from "./hero";

export class Event {
  id: string;
  name: string;
  objectId: string;
  type: string;
  payload: Hero;
  createdDate: Date;
  errorMessage: string;
}
