(function() {
    'use strict';

    angular
        .module('pchauvetonherokuApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('galaxy', {
            parent: 'entity',
            url: '/galaxy?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Galaxies'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/galaxy/galaxies.html',
                    controller: 'GalaxyController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('galaxy-detail', {
            parent: 'entity',
            url: '/galaxy/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Galaxy'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/galaxy/galaxy-detail.html',
                    controller: 'GalaxyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Galaxy', function($stateParams, Galaxy) {
                    return Galaxy.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'galaxy',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('galaxy-detail.edit', {
            parent: 'galaxy-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/galaxy/galaxy-dialog.html',
                    controller: 'GalaxyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Galaxy', function(Galaxy) {
                            return Galaxy.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('galaxy.new', {
            parent: 'galaxy',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/galaxy/galaxy-dialog.html',
                    controller: 'GalaxyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                type: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('galaxy', null, { reload: 'galaxy' });
                }, function() {
                    $state.go('galaxy');
                });
            }]
        })
        .state('galaxy.edit', {
            parent: 'galaxy',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/galaxy/galaxy-dialog.html',
                    controller: 'GalaxyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Galaxy', function(Galaxy) {
                            return Galaxy.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('galaxy', null, { reload: 'galaxy' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('galaxy.delete', {
            parent: 'galaxy',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/galaxy/galaxy-delete-dialog.html',
                    controller: 'GalaxyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Galaxy', function(Galaxy) {
                            return Galaxy.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('galaxy', null, { reload: 'galaxy' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
