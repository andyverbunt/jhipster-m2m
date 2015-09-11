'use strict';

angular.module('m2mApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


