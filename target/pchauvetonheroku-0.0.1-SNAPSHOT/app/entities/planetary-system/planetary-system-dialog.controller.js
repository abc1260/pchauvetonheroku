(function() {
    'use strict';

    angular
        .module('pchauvetonherokuApp')
        .controller('Planetary_systemDialogController', Planetary_systemDialogController);

    Planetary_systemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Planetary_system', 'Galaxy'];

    function Planetary_systemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Planetary_system, Galaxy) {
        var vm = this;

        vm.planetary_system = entity;
        vm.clear = clear;
        vm.save = save;
        vm.galaxies = Galaxy.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.planetary_system.id !== null) {
                Planetary_system.update(vm.planetary_system, onSaveSuccess, onSaveError);
            } else {
                Planetary_system.save(vm.planetary_system, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pchauvetonherokuApp:planetary_systemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
