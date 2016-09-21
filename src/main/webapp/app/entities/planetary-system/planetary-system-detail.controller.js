(function() {
    'use strict';

    angular
        .module('pchauvetonherokuApp')
        .controller('Planetary_systemDetailController', Planetary_systemDetailController);

    Planetary_systemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Planetary_system', 'Galaxy'];

    function Planetary_systemDetailController($scope, $rootScope, $stateParams, previousState, entity, Planetary_system, Galaxy) {
        var vm = this;

        vm.planetary_system = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pchauvetonherokuApp:planetary_systemUpdate', function(event, result) {
            vm.planetary_system = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
