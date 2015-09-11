'use strict';

angular.module('m2mApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('b', {
                parent: 'entity',
                url: '/bs',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'm2mApp.b.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/b/bs.html',
                        controller: 'BController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('b');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('b.detail', {
                parent: 'entity',
                url: '/b/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'm2mApp.b.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/b/b-detail.html',
                        controller: 'BDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('b');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'B', function($stateParams, B) {
                        return B.get({id : $stateParams.id});
                    }]
                }
            })
            .state('b.new', {
                parent: 'b',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/b/b-dialog.html',
                        controller: 'BDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {nameB: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('b', null, { reload: true });
                    }, function() {
                        $state.go('b');
                    })
                }]
            })
            .state('b.edit', {
                parent: 'b',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/b/b-dialog.html',
                        controller: 'BDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['B', function(B) {
                                return B.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('b', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
