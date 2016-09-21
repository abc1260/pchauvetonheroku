(function () {
    'use strict';

    angular
        .module('pchauvetonherokuApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
