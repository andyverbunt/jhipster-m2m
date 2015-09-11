'use strict';

angular.module('m2mApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
