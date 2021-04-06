import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {GatewayService} from "../gateway.service";
import {DeviceService} from "../device.service";
import {Device} from "../device";

@Component({
  selector: 'app-gateway-details',
  templateUrl: './gateway-details.component.html',
  styleUrls: ['./gateway-details.component.scss']
})
export class GatewayDetailsComponent implements OnInit {
  gateway;
  devices : Device[];
  displayedColumns: string[] = ['uid', 'vendor', 'dateCreated', 'status', 'actions'];

  constructor(private route: ActivatedRoute,
              private gatewayService: GatewayService,
              private deviceService: DeviceService) { }

  ngOnInit(): void {
    const routeParams = this.route.snapshot.paramMap;
    const gatewayId = Number(routeParams.get('gatewayId'));
    this.gatewayService.getGateway(gatewayId)
      .subscribe(gateway => this.gateway = gateway);
    this.deviceService.getDevices()
      .subscribe(devices => this.devices = devices);
  }

  deleteDevice(id) {

  }
}
