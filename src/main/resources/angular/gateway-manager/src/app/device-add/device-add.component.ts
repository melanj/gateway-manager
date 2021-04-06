import {Component, OnInit} from '@angular/core';
import {GatewayService} from "../gateway.service";
import {DeviceService} from "../device.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Gateway} from "../gateway";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Status} from "../status.enum";

@Component({
  selector: 'app-device-add',
  templateUrl: './device-add.component.html',
  styleUrls: ['./device-add.component.scss']
})
export class DeviceAddComponent implements OnInit {
  device;
  gateways: Gateway[];
  statusTypes = Object.values(Status);
  gatewayId;
  title = 'Add Device';

  constructor(private _router: Router,
              private route: ActivatedRoute,
              private deviceService: DeviceService,
              private gatewayService: GatewayService) {
  }

  ngOnInit(): void {
    const routeParams = this.route.snapshot.paramMap;
    this.gatewayId = Number(routeParams.get('gatewayId'));
    this.gatewayService.getGateways()
      .subscribe(gateways => {
        this.gateways = gateways;
        if (this.gatewayId > 0) {
          this.title = 'Add a device to ' + this.gateways.filter(
            gateway => gateway.id === this.gatewayId)[0].name;
          this.deviceForm.get('gateway').setValue(this.gatewayId)
          this.deviceForm.get('gateway').disable()
        }
      });
  }

  deviceForm = new FormGroup({
    uid: new FormControl('', [Validators.required, Validators.pattern("^[0-9]*$")]),
    vendor: new FormControl('', Validators.required),
    dateCreated: new FormControl(new Date().toJSON().slice(0, 10), Validators.required),
    status: new FormControl('', Validators.required),
    gateway: new FormControl('', Validators.required),
  });

  addDevice() {
    this.deviceService.addDevice(this.deviceForm.value)
      .subscribe(device => this.device = device);
    if (this.gatewayId > 0) {
      this._router.navigate(['/gateways/' + this.gatewayId])
    } else {
      this._router.navigate(['/'])
    }
  }

}