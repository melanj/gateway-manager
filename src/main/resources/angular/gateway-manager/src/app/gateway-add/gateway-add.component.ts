import {Component, OnInit} from '@angular/core';
import {GatewayService} from "../gateway.service";
import {Router} from "@angular/router";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {CommonAddComponent} from "../common-add.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-gateway-add',
  templateUrl: './gateway-add.component.html',
  styleUrls: ['./gateway-add.component.scss']
})
export class GatewayAddComponent extends CommonAddComponent implements OnInit {
  gateway;

  constructor(public _router: Router, private gatewayService: GatewayService,
              public dialog: MatDialog) {
    super(_router, dialog);
  }

  gatewayForm = new FormGroup({
    name: new FormControl('', Validators.required),
    serial: new FormControl('', Validators.required),
    ipv4Address: new FormControl('', [Validators.required,
      Validators.pattern('(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)')]),
  });

  ngOnInit(): void {
  }

  addGateway() {
    this.gatewayService.addGateway(this.gatewayForm.value)
      .subscribe({
        next: gateway => {
          console.info('add successful');
          this.gateway = gateway
          this._router.navigate(['/']);
        },
        error: error => {
          this.showErrorAndNavigate(error);
        }
      });
  }

}
