import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {InfoDialogModel, InformationDialogComponent} from "./information-dialog/information-dialog.component";

export class CommonAddComponent {

    constructor(public _router: Router,
                public dialog: MatDialog) {
    }

    showErrorAndNavigate(response) {
      let message = 'Unknown error has occurred';
      if(response){
        message = response;
      }
      if(response.error){
        message = response.error;
      }
      if(response.error.message){
        message = response.error.message;
      }
      if (message.includes('\"')) {
        message = message.split('\"')[1];
      }
      const dialogRef = this.dialog.open(InformationDialogComponent, {
            maxWidth: "400px",
            data: new InfoDialogModel("Error",
                message)
        });

        dialogRef.afterClosed().subscribe(dialogResult => {
            if (dialogResult) {
                this._router.navigate(['/'])
            }
        });
    }
}
