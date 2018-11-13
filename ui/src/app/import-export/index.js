import ImportExport from './import-export.service';
import ImportDialogController from './import-dialog.controller';


export default angular.module('thingsboard.importexport', [])
    .factory('importExport', ImportExport)
    .controller('ImportDialogController', ImportDialogController)
    .name;
