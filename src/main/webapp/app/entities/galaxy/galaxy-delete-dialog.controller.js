(function() {
    'use strict';

    angular
        .module('pchauvetonherokuApp')
        .controller('GalaxyDeleteController',GalaxyDeleteController);

    GalaxyDeleteController.$inject = ['$uibModalInstance', 'entity', 'Galaxy'];

    function GalaxyDeleteController($uibModalInstance, entity, Galaxy) {
        var vm = this;

        vm.galaxy = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Galaxy.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
