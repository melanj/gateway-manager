import {Component, OnInit} from '@angular/core';
import {GatewayService} from "../gateway.service";
import {Router} from "@angular/router";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-gateway-add',
  templateUrl: './gateway-add.component.html',
  styleUrls: ['./gateway-add.component.scss']
})
export class GatewayAddComponent implements OnInit {
  gateway;

  constructor(private _router: Router, private gatewayService: GatewayService) {
  }

  gatewayForm = new FormGroup({
    name: new FormControl('', Validators.required),
    serial: new FormControl('', Validators.required),
    ipv4Address: new FormControl('', Validators.required),
  });

  ngOnInit(): void {
  }

  addGateway() {
    this.gatewayService.addGateway(this.gatewayForm.value)
      .subscribe(gateway => this.gateway = gateway);
    this._router.navigate(['/'])
  }

}
