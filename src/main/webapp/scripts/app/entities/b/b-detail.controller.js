'use strict';

angular.module('m2mApp')
    .controller('BDetailController', function ($scope, $rootScope, $stateParams, entity, B, A, User) {
        $scope.b = entity;
        $scope.load = function (id) {
            B.get({id: id}, function(result) {
                $scope.b = result;
            });
        };
        $rootScope.$on('m2mApp:bUpdate', function(event, result) {
            $scope.b = result;
        });
    });
