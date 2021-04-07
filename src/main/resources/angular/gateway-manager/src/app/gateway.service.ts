import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {catchError, tap} from 'rxjs/operators';
import {Gateway} from "./gateway";

@Injectable({
  providedIn: 'root'
})
export class GatewayService {


  private gatewaysUrl = 'api/gateways';
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient) {
  }

  getGateways(): Observable<Gateway[]> {
    return this.http.get<Gateway[]>(this.gatewaysUrl)
      .pipe(
        tap(_ => console.log('fetched gateways')),
        catchError(this.handleError<Gateway[]>('getGateways', []))
      );
  }

  getGateway(id: number): Observable<Gateway> {
    const url = `${this.gatewaysUrl}/${id}`;
    return this.http.get<Gateway>(url).pipe(
      tap(_ => console.log(`fetched gateway id=${id}`)),
      catchError(this.handleError<Gateway>(`province id=${id}`))
    );
  }

  addGateway(gateway: Gateway): Observable<Gateway> {
    return this.http.post<Gateway>(this.gatewaysUrl, gateway, this.httpOptions)
      .pipe(
        catchError(this.handleError('addGateway', gateway))
      );
  }

  deleteGateway(id: number): Observable<Object> {
    return this.http.delete(`${this.gatewaysUrl}/${id}`)
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      console.log(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }
}
