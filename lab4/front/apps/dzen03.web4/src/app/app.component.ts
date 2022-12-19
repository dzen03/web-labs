import { Component } from '@angular/core';
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  title = 'Dzenzelyuk Ilya P32102';

  key = ''
  constructor(public cookieService: CookieService, private router:Router) {
    this.key = this.cookieService.get('key');
  }
}
