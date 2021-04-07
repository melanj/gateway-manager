import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
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
              private gatewayService: GatewayService,
              private changeDetectorRefs: ChangeDetectorRef) {
  }

  ngOnInit(): void {
    this.refresh();
  }

  private refresh() {
    this.gatewayService.getGateways()
      .subscribe(gateways => this.gateways = gateways);
  }

  openGateway(id) {
    this._router.navigate(['gateways/' + id]);
  }

  deleteGateway(id) {
    this.gatewayService.deleteGateway(id).subscribe({
      next: data => {
        console.info('Delete successful');
        this.refresh();
      },
      error: error => {
        console.error('There was an error!', error);
      }
    });
  }
}
