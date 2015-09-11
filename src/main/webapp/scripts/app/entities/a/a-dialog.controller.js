'use strict';

angular.module('m2mApp').controller('ADialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'A', 'B', 'User',
        function($scope, $stateParams, $modalInstance, entity, A, B, User) {

        $scope.a = entity;
        $scope.bs = B.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            A.get({id : id}, function(result) {
                $scope.a = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('m2mApp:aUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.a.id != null) {
                A.update($scope.a, onSaveFinished);
            } else {
                A.save($scope.a, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
