/**
 * Created by surajnagaraj on 10/26/16.
 */
(function() {
    'use strict';
    angular
        .module("PsychWebApp")
        .controller("MainController", MainController);

    function MainController($scope, $location)
    {
        $scope.$location = $location;
    }
})();