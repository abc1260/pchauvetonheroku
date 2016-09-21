(function() {
    'use strict';

    angular
        .module('pchauvetonherokuApp')
        .controller('PlanetDetailController', PlanetDetailController);

    PlanetDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Planet', 'Planetary_system'];

    function PlanetDetailController($scope, $rootScope, $stateParams, previousState, entity, Planet, Planetary_system) {
        var vm = this;

        vm.planet = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pchauvetonherokuApp:planetUpdate', function(event, result) {
            vm.planet = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
