import {Gateway} from "./gateway";
import {Status} from './status.enum';

export class Device {
  id: number;
  uid: number;
  vendor: String;
  dateCreated: String;
  status: Status;
  gateway: Gateway;
}
