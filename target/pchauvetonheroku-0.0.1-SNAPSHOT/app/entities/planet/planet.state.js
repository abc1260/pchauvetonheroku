(function() {
    'use strict';

    angular
        .module('pchauvetonherokuApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('planet', {
            parent: 'entity',
            url: '/planet?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Planets'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/planet/planets.html',
                    controller: 'PlanetController',
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
        .state('planet-detail', {
            parent: 'entity',
            url: '/planet/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Planet'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/planet/planet-detail.html',
                    controller: 'PlanetDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Planet', function($stateParams, Planet) {
                    return Planet.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'planet',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('planet-detail.edit', {
            parent: 'planet-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/planet/planet-dialog.html',
                    controller: 'PlanetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Planet', function(Planet) {
                            return Planet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('planet.new', {
            parent: 'planet',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/planet/planet-dialog.html',
                    controller: 'PlanetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                surface: null,
                                radius: null,
                                system: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('planet', null, { reload: 'planet' });
                }, function() {
                    $state.go('planet');
                });
            }]
        })
        .state('planet.edit', {
            parent: 'planet',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/planet/planet-dialog.html',
                    controller: 'PlanetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Planet', function(Planet) {
                            return Planet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('planet', null, { reload: 'planet' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('planet.delete', {
            parent: 'planet',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/planet/planet-delete-dialog.html',
                    controller: 'PlanetDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Planet', function(Planet) {
                            return Planet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('planet', null, { reload: 'planet' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
