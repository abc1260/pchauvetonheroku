(function() {
    'use strict';

    angular
        .module('pchauvetonherokuApp')
        .controller('GalaxyDialogController', GalaxyDialogController);

    GalaxyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Galaxy'];

    function GalaxyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Galaxy) {
        var vm = this;

        vm.galaxy = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.galaxy.id !== null) {
                Galaxy.update(vm.galaxy, onSaveSuccess, onSaveError);
            } else {
                Galaxy.save(vm.galaxy, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pchauvetonherokuApp:galaxyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
