import {Component, OnInit} from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import {post} from "jquery";
import { Toast } from "bootstrap";
import{ GlobalComponent } from '../global-component';

@Component({
  selector: 'app-customers',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent{
  key = ''
  constructor(public cookieService: CookieService) {
    this.key = this.cookieService.get('key');
  }

  submit(is_login:boolean)
  {
    const type = (is_login ? 'login' : 'register');
    const login = $(`#${type}_login`).val();
    const password = $(`#${type}_password`).val();

    let result = false;
    let new_key = "";


    $.post({
      url: `${GlobalComponent.url}/api/auth/${type}`, // TODO change url
      data: { login: login, pass: password },
      success: function (data) {
        if (data !== "")
        {
          new_key = data;
          result = true;
        }
      },
      async: false
    });

    if (!result)
    {
      // @ts-ignore
      new Toast($("#errorToast")).show();
    }
    else
    {
      this.cookieService.set('key', new_key);
    }

    return result;
  }

  show_key()
  {
    const alertPlaceholder = document.getElementById('apikey_alert');

      const wrapper = document.createElement('div')
      wrapper.innerHTML = [
        `<div class="alert alert-warning alert-dismissible" role="alert">`,
        `   <div>` + this.key + `</div>`,
        '   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>',
        '</div>'
      ].join('');

      // @ts-ignore
      alertPlaceholder.append(wrapper);
  }

  logout()
  {
    this.cookieService.deleteAll();
    this.key = '';
  }
}
