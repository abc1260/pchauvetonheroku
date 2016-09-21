(function() {
    'use strict';
    angular
        .module('pchauvetonherokuApp')
        .factory('Galaxy', Galaxy);

    Galaxy.$inject = ['$resource'];

    function Galaxy ($resource) {
        var resourceUrl =  'api/galaxies/:id';

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
