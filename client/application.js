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
  })

  .controller('NavbarCtrl', function($log, $location) {
    $log.debug('Initializing NavbarCtrl');
    
    this.navCollapsed;
    
    this.isActive = function(viewLocation) {
      return viewLocation === $location.path();
    };
  });
  
})();