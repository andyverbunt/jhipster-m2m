'use strict';

angular.module('m2mApp')
    .factory('B', function ($resource, DateUtils) {
        return $resource('api/bs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
