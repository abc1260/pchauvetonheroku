(function() {
    'use strict';

    angular
        .module('pchauvetonherokuApp')
        .controller('PlanetDialogController', PlanetDialogController);

    PlanetDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Planet', 'Planetary_system'];

    function PlanetDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Planet, Planetary_system) {
        var vm = this;

        vm.planet = entity;
        vm.clear = clear;
        vm.save = save;
        vm.planetary_systems = Planetary_system.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.planet.id !== null) {
                Planet.update(vm.planet, onSaveSuccess, onSaveError);
            } else {
                Planet.save(vm.planet, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pchauvetonherokuApp:planetUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
