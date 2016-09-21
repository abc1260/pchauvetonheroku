'use strict';

describe('Controller Tests', function() {

    describe('Planetary_system Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPlanetary_system, MockGalaxy;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPlanetary_system = jasmine.createSpy('MockPlanetary_system');
            MockGalaxy = jasmine.createSpy('MockGalaxy');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Planetary_system': MockPlanetary_system,
                'Galaxy': MockGalaxy
            };
            createController = function() {
                $injector.get('$controller')("Planetary_systemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pchauvetonherokuApp:planetary_systemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
