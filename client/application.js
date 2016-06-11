(function() {
  angular.module('app', ['ngRoute', 'app.example'])
  
  .config(function($routeProvider) {
    $routeProvider.when('/', {
      template: '<p>TEST TEST TEST</p>'
    })
    .otherwise({ redirectTo: '/' });
  })
  
  .controller('appCtrl', function($log) {
    $log.debug('Initializing appCtrl');
  });
})();