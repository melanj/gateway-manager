import {Component, OnInit} from '@angular/core';
import {GatewayService} from "../gateway.service";
import {Gateway} from "../gateway";
import {Router} from "@angular/router";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  gateways: Gateway[];
  displayedColumns: string[] = ['id', 'name', 'serial', 'ipv4Address', 'actions'];

  constructor(private _router: Router,
              private gatewayService: GatewayService) {
  }

  ngOnInit(): void {
    this.gatewayService.getGateways()
      .subscribe(gateways => this.gateways = gateways);
  }

  openGateway(id) {
    this._router.navigate(['gateways/' + id]);
  }

  deleteGateway(id) {
    //http://www.techtutorhub.com/article/Learn-how-to-show-Angular-Material-Confirm-Dialog-Box-with-Easy-Implementation/71
    alert(id);
  }
}
