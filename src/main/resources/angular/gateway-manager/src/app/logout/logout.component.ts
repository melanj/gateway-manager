import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "../authentication.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.scss']
})
export class LogoutComponent implements OnInit {

  constructor(private loginService: AuthenticationService,
              private router: Router) { }

  ngOnInit(): void {
    this.loginService.logOut();
    this.router.navigate(['login']);
  }

}
