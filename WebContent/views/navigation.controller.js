/**
 * Created by surajnagaraj on 10/27/16.
 */
(function () {
    angular
        .module("PsychWebApp")
        .controller("NavigationController", NavigationController);

    function NavigationController(UserService, $location, $rootScope)
    {

        var vm = this;
        vm.$location = $location;
        vm.logout = logout;

        function logout() {
            UserService.logout();
            if($rootScope.currentUser == null) {
                $location.url('/login');
            }
        }
    }

})();