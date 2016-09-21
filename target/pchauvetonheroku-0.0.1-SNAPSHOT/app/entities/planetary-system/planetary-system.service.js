(function() {
    'use strict';
    angular
        .module('pchauvetonherokuApp')
        .factory('Planetary_system', Planetary_system);

    Planetary_system.$inject = ['$resource'];

    function Planetary_system ($resource) {
        var resourceUrl =  'api/planetary-systems/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
