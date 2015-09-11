'use strict';

angular.module('m2mApp')
    .factory('errorHandlerInterceptor', function ($q, $rootScope) {
        return {
            'responseError': function (response) {
                if (!(response.status == 401 && response.data.path.indexOf("/api/account") == 0 )){
	                $rootScope.$emit('m2mApp.httpError', response);
	            }
                return $q.reject(response);
            }
        };
    });