'use strict';

angular.module('m2mApp')
    .controller('LogsController', function ($scope, LogsService) {
        $scope.loggers = LogsService.findAll();

        $scope.changeLevel = function (name, level) {
            LogsService.changeLevel({name: name, level: level}, function () {
                $scope.loggers = LogsService.findAll();
            });
        };
    });
