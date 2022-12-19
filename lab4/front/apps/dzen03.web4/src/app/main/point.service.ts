import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Point } from './point';
import{ GlobalComponent } from '../global-component';

@Injectable({
  providedIn: 'root',
})
export class PointService {

  constructor(private http: HttpClient) {}
  getPointsWithObservable(key:string): Observable<Point[]> {
    const url = `${GlobalComponent.url}/api/graph/points/get?key=` + key; // TODO change url
    return this.http
      .get(url)
      .pipe(map(this.extractData), catchError(this.handleErrorObservable));
  }

  private extractData(res: any) {
    return res;
  }
  private handleErrorObservable(error: any) {
    console.error(error.message || error);
    return throwError(error);
  }
}
