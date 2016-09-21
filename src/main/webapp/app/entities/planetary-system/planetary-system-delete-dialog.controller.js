(function() {
    'use strict';

    angular
        .module('pchauvetonherokuApp')
        .controller('Planetary_systemDeleteController',Planetary_systemDeleteController);

    Planetary_systemDeleteController.$inject = ['$uibModalInstance', 'entity', 'Planetary_system'];

    function Planetary_systemDeleteController($uibModalInstance, entity, Planetary_system) {
        var vm = this;

        vm.planetary_system = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Planetary_system.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
