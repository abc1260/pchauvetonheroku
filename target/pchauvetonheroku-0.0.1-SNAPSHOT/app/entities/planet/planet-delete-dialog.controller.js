(function() {
    'use strict';

    angular
        .module('pchauvetonherokuApp')
        .controller('PlanetDeleteController',PlanetDeleteController);

    PlanetDeleteController.$inject = ['$uibModalInstance', 'entity', 'Planet'];

    function PlanetDeleteController($uibModalInstance, entity, Planet) {
        var vm = this;

        vm.planet = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Planet.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
