import { Component, OnInit } from '@angular/core';
import { Point } from './point';
import { Observable } from 'rxjs';
import { PointService } from './point.service';
import { Toast } from 'bootstrap';
import { CookieService } from 'ngx-cookie-service';
import {Router} from "@angular/router";
import{ GlobalComponent } from '../global-component';


@Component({
  selector: 'app-orders',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],
})
export class MainComponent implements OnInit {
  title = 'lab4_web';
  radio_r = [-2, -1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2];
  radio_x = [-2, -1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2];

  observableBooks: Observable<Point[]>;
  points: Point[] = [];
  errorMessage = '';
  key = '';
  constructor(private bookService: PointService, public cookieService: CookieService, private router:Router) {
    this.key = this.cookieService.get('key');
    if (this.key === "")
    {
      this.router.navigate(['/login']);
    }
    this.observableBooks = this.bookService.getPointsWithObservable(this.key);
    this.observableBooks.subscribe({
      next: (points) => (this.points = points),
      error: (error) => (this.errorMessage = error),
    });
  }
  ngOnInit()
  {
    loaded();
  }


  r_select(r: number) {
    r_change(r);
  }

  x_select(x: number) {
    (<HTMLInputElement>document.getElementById('x')).value = String(x);
    x_selected = true;
  }

  clicked() {
    validate();
  }



  form_submit() {
    let x = (<HTMLInputElement>document.getElementById('x')).value;
    let y = (<HTMLInputElement>document.getElementById('y')).value;
    $.post(
      `${GlobalComponent.url}/api/graph/submit`, // TODO change url
      { key: this.key, x: x, y: y, r: r_num },
      function (data) {
        if (data.error === undefined)
        {
          add_to_html(data.x, data.y, data.r, data.inside);

        }
      }
    );
    return false;
  }


}

function add_to_html(x:number, y:number, r:number, inside:boolean) {
  const svgns = 'http://www.w3.org/2000/svg';

  // @ts-ignore
  // document.getElementById('graph').innerHTML = '';

  let circle = document.createElementNS(svgns, 'circle');
  circle.setAttribute('cx', `${50 + x * 8}%`);
  circle.setAttribute('cy', `${50 - y * 8}%`);
  circle.setAttribute('r', '2%');
  circle.setAttribute('fill', `${inside ? 'green' : 'red'}`);
  // @ts-ignore
  document.getElementById('graph').appendChild(circle);


  let tbody = document.getElementById('graph');
  // @ts-ignore
  tbody.innerHTML +=  `<tr><td>${x}</td><td>${y}</td><td>${x}</td><td>${r}</td><td>${inside}</td></tr>`;
}

let r_selected = false;
let x_selected = false;
let r_num = -1;

function validate_by_id(
  id: any,
  min: number,
  max: number,
  id_to_disable: string
) {
  const input = <HTMLInputElement>document.getElementById(id);
  const disable = document.getElementById(id_to_disable);
  if (input === null || disable == null) return false;
  const is_parsed = try_parse(input.value);
  if (is_parsed[0] && min <= is_parsed[1] && is_parsed[1] <= max) {
    disable.className = '';
    return true;
  } else {
    disable.className = 'incorrect';
    return false;
  }
}

function validate() {
  const submit = <HTMLInputElement>document.getElementById('submit');
  let fl = validate_by_id('y', -3, 3, 'y') && r_selected && x_selected;
  // fl &= validate_by_id('input_form:y_text', -4, 4, 'input_form:y');
  submit.disabled = !fl;
  return fl;
}

function try_parse(y: string) {
  try {
    const a = parseFloat(y);
    const reg = new RegExp('^-?\\d*\\.?\\d*$');
    if (isNaN(a) || !reg.test(y)) return [false];
    return [true, a];
  } catch (exc) {
    return [false];
  }
}

function point_clicked(e: any) {
  if (!r_selected) {
    alert('Select R!!!');
    return;
  }
  const element = document.getElementById('graph');
  if (element === null) return;
  const click_x = e.clientX,
    click_y = e.clientY;
  const pos = element.getBoundingClientRect();
  const screen_x = pos.x,
    screen_y = pos.y;
  const scale = element.getBoundingClientRect().width / 100;
  const x = ((click_x - screen_x) / scale - 50) / 8,
    y = -((click_y - screen_y) / scale - 50) / 8;

  (<HTMLInputElement>document.getElementById('x')).value = String(x);
  (<HTMLInputElement>document.getElementById('y')).value = String(y);
  x_selected = true;
  if (validate()) {
    // @ts-ignore
    document.getElementById('submit').click();
  } else {
  }
}

function loaded() {
  validate();
  // @ts-ignore
  document
    .getElementById('graph')
    .addEventListener('click', point_clicked, false);
  }



function r_change(r: number) {
  if (r <= 0) {
    // @ts-ignore
    new Toast(document.getElementById('liveToast')).show();
    // @ts-ignore
    document.getElementById('r_' + r).checked = false;
    return;
  }
  // @ts-ignore
  document
    .getElementById('draw')
    .setAttribute(
      'style',
      'transform: translate(' +
        (50 - 10 * r) +
        'px, ' +
        (50 - 10 * r) +
        'px) scale(' +
        0.2 * r +
        ')'
    );

  let circles = $("#graph circle");

  for (let i = 0; i < circles.length; i++) {
    if (circles[i].getAttribute('rad') !== r.toString())
      circles[i].setAttribute('hidden', 'true');
    else
      circles[i].removeAttribute('hidden');
  }

  r_selected = true;
  r_num = r;
  validate();
}

