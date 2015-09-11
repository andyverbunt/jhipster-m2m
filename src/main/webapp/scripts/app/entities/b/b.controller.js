'use strict';

angular.module('m2mApp')
    .controller('BController', function ($scope, B) {
        $scope.bs = [];
        $scope.loadAll = function() {
            B.query(function(result) {
               $scope.bs = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            B.get({id: id}, function(result) {
                $scope.b = result;
                $('#deleteBConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            B.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteBConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.b = {nameB: null, id: null};
        };
    });
