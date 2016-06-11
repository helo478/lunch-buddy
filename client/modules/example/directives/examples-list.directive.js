(function() {
  angular.module('app.example').directive('examplesList', function() {
    return {
      restrict: 'E',
      templateUrl: '/example/views/examples-list.directive.html',
      controller: 'examplesListDirectiveCtrl',
      controllerAs: 'ctrl'
    };
  });
})();