(function() {
  angular.module('app.example', ['ngRoute']).config(function($routeProvider) {
    $routeProvider.when('/examples', {
      templateUrl: '/example/views/example.route.html',
      controller: 'exampleRouteCtrl',
      controllerAs: 'routeCtrl'
    });
  });
})();