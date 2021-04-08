import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Device} from "./device";
import {catchError, tap} from 'rxjs/operators';
import {Observable, of} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class DeviceService {
  private devicesUrl = 'api/devices';

  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient) {
  }

  getDevices(): Observable<Device[]> {
    return this.http.get<Device[]>(this.devicesUrl)
      .pipe(
        tap(_ => console.log('fetched devices')),
        catchError(this.handleError<Device[]>('getDevices', []))
      );
  }

  getDevicesByGatewayId(id: number): Observable<Device[]> {
    return this.http.get<Device[]>(this.devicesUrl + '?gateway_id=' + id)
      .pipe(
        tap(_ => console.log('fetched devices for gateway ' + id)),
        catchError(this.handleError<Device[]>('getDevices', []))
      );
  }

  getDevice(id: number): Observable<Device> {
    const url = `${this.devicesUrl}/${id}`;
    return this.http.get<Device>(url).pipe(
      tap(_ => console.log(`fetched Device id=${id}`)),
      catchError(this.handleError<Device>(`device id=${id}`))
    );
  }

  addDevice(device: Device): Observable<Object> {
    return this.http.post<Device>(this.devicesUrl, device, this.httpOptions);
     // .pipe(
     //   catchError(this.handleError('addDevice', device))
     // );
  }

  deleteDevice(id: number): Observable<Object> {
    return this.http.delete(`${this.devicesUrl}/${id}`);
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      console.log(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }
}
