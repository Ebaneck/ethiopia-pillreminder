'use strict';

/* put your routes here */

angular.module('pill-reminder', ['motech-dashboard', 'YourModuleServices', 'ngCookies', 'bootstrap'])
    .config(['$routeProvider', function ($routeProvider) {

        $routeProvider
            .when('/welcome', { templateUrl: '../pill-reminder/resources/partials/welcome.html', controller: YourController })
            .otherwise({redirectTo: '/welcome'});
    }]);
