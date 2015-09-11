'use strict';

angular.module('m2mApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('a', {
                parent: 'entity',
                url: '/as',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'm2mApp.a.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/a/as.html',
                        controller: 'AController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('a');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('a.detail', {
                parent: 'entity',
                url: '/a/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'm2mApp.a.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/a/a-detail.html',
                        controller: 'ADetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('a');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'A', function($stateParams, A) {
                        return A.get({id : $stateParams.id});
                    }]
                }
            })
            .state('a.new', {
                parent: 'a',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/a/a-dialog.html',
                        controller: 'ADialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {nameA: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('a', null, { reload: true });
                    }, function() {
                        $state.go('a');
                    })
                }]
            })
            .state('a.edit', {
                parent: 'a',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/a/a-dialog.html',
                        controller: 'ADialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['A', function(A) {
                                return A.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('a', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
