import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {RouterModule} from '@angular/router';

import {AppComponent} from './app.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {GatewayAddComponent} from './gateway-add/gateway-add.component';
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {MatTableModule} from '@angular/material/table';
import {GatewayDetailsComponent} from './gateway-details/gateway-details.component';
import {DeviceAddComponent} from './device-add/device-add.component';


@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    GatewayAddComponent,
    GatewayDetailsComponent,
    DeviceAddComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot([
      {path: '', component: DashboardComponent},
      {path: 'add-gateway', component: GatewayAddComponent},
      {path: 'gateways/:gatewayId', component: GatewayDetailsComponent},
      {path: 'add-device', component: DeviceAddComponent},
      {path: 'gateways/:gatewayId/add-device', component: DeviceAddComponent},
    ], {enableTracing: true}),
    MatButtonModule,
    MatIconModule,
    NoopAnimationsModule,
    ReactiveFormsModule,
    MatTableModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
