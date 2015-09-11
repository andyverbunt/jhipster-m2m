'use strict';

angular.module('m2mApp').controller('BDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'B', 'A', 'User',
        function($scope, $stateParams, $modalInstance, entity, B, A, User) {

        $scope.b = entity;
        $scope.as = A.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            B.get({id : id}, function(result) {
                $scope.b = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('m2mApp:bUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.b.id != null) {
                B.update($scope.b, onSaveFinished);
            } else {
                B.save($scope.b, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
