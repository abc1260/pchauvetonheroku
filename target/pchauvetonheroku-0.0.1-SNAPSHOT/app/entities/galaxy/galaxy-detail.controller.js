(function() {
    'use strict';

    angular
        .module('pchauvetonherokuApp')
        .controller('GalaxyDetailController', GalaxyDetailController);

    GalaxyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Galaxy'];

    function GalaxyDetailController($scope, $rootScope, $stateParams, previousState, entity, Galaxy) {
        var vm = this;

        vm.galaxy = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pchauvetonherokuApp:galaxyUpdate', function(event, result) {
            vm.galaxy = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
