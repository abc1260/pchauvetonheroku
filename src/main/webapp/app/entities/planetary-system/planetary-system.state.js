(function() {
    'use strict';

    angular
        .module('pchauvetonherokuApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('planetary-system', {
            parent: 'entity',
            url: '/planetary-system?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Planetary_systems'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/planetary-system/planetary-systems.html',
                    controller: 'Planetary_systemController',
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
        .state('planetary-system-detail', {
            parent: 'entity',
            url: '/planetary-system/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Planetary_system'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/planetary-system/planetary-system-detail.html',
                    controller: 'Planetary_systemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Planetary_system', function($stateParams, Planetary_system) {
                    return Planetary_system.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'planetary-system',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('planetary-system-detail.edit', {
            parent: 'planetary-system-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/planetary-system/planetary-system-dialog.html',
                    controller: 'Planetary_systemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Planetary_system', function(Planetary_system) {
                            return Planetary_system.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('planetary-system.new', {
            parent: 'planetary-system',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/planetary-system/planetary-system-dialog.html',
                    controller: 'Planetary_systemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                star: null,
                                galaxy: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('planetary-system', null, { reload: 'planetary-system' });
                }, function() {
                    $state.go('planetary-system');
                });
            }]
        })
        .state('planetary-system.edit', {
            parent: 'planetary-system',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/planetary-system/planetary-system-dialog.html',
                    controller: 'Planetary_systemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Planetary_system', function(Planetary_system) {
                            return Planetary_system.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('planetary-system', null, { reload: 'planetary-system' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('planetary-system.delete', {
            parent: 'planetary-system',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/planetary-system/planetary-system-delete-dialog.html',
                    controller: 'Planetary_systemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Planetary_system', function(Planetary_system) {
                            return Planetary_system.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('planetary-system', null, { reload: 'planetary-system' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
