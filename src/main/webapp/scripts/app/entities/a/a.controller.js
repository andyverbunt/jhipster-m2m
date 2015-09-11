'use strict';

angular.module('m2mApp')
    .controller('AController', function ($scope, A) {
        $scope.as = [];
        $scope.loadAll = function() {
            A.query(function(result) {
               $scope.as = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            A.get({id: id}, function(result) {
                $scope.a = result;
                $('#deleteAConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            A.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.a = {nameA: null, id: null};
        };
    });
