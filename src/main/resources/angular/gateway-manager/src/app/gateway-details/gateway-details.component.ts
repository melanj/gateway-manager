import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {GatewayService} from "../gateway.service";
import {DeviceService} from "../device.service";
import {Device} from "../device";
import {ConfirmDialogComponent, ConfirmDialogModel} from "../confirm-dialog/confirm-dialog.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-gateway-details',
  templateUrl: './gateway-details.component.html',
  styleUrls: ['./gateway-details.component.scss']
})
export class GatewayDetailsComponent implements OnInit {
  gateway;
  devices: Device[];
  displayedColumns: string[] = ['uid', 'vendor', 'dateCreated', 'status', 'actions'];

  constructor(private route: ActivatedRoute,
              private gatewayService: GatewayService,
              private deviceService: DeviceService,
              private changeDetectorRefs: ChangeDetectorRef,
              private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.refresh();
  }

  private refresh() {
    const routeParams = this.route.snapshot.paramMap;
    const gatewayId = Number(routeParams.get('gatewayId'));
    this.gatewayService.getGateway(gatewayId)
      .subscribe(gateway => this.gateway = gateway);
    this.deviceService.getDevicesByGatewayId(gatewayId)
      .subscribe(devices => this.devices = devices);
    this.changeDetectorRefs.detectChanges();
  }

  deleteDeviceWithConfirm(id) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      maxWidth: "400px",
      data: new ConfirmDialogModel("Confirm Delete",
        'Are you sure you want to delete selected device?')
    });

    dialogRef.afterClosed().subscribe(dialogResult => {
      if (dialogResult) {
        this.deleteDevice(id);
      }
    });
  }

  deleteDevice(id) {
    this.deviceService.deleteDevice(id).subscribe({
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
