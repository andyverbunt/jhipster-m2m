'use strict';

angular.module('m2mApp')
    .controller('ADetailController', function ($scope, $rootScope, $stateParams, entity, A, B, User) {
        $scope.a = entity;
        $scope.load = function (id) {
            A.get({id: id}, function(result) {
                $scope.a = result;
            });
        };
        $rootScope.$on('m2mApp:aUpdate', function(event, result) {
            $scope.a = result;
        });
    });
