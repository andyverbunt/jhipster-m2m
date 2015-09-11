 'use strict';

angular.module('m2mApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-m2mApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-m2mApp-params')});
                }
                return response;
            },
        };
    });