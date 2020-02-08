/**
 * Created by surajnagaraj on 10/26/16.
 */
//awsURL : http://ec2-54-166-55-193.compute-1.amazonaws.com:8080/Psych-1/
//localURL : http://localhost:8080/Psych-1/
(function(){
    'use strict';
    angular
        .module("PsychWebApp")
        //.constant("serverURL", { url : 'http://ec2-54-166-55-193.compute-1.amazonaws.com:8080/Psych-1/'})
        .constant("serverURL", { url : 'http://localhost:8080/Psych/'})
        .directive('fileUpload', function () {
        	return {
        		scope: true,        //create a new scope
        		link: function (scope, el, attrs) {
        			el.bind('change', function (event) {
	                var files = event.target.files;
	                //iterate files since 'multiple' may be specified on the element
	                for (var i = 0;i<files.length;i++) {
	                    //emit event upward
	                    scope.$emit("fileSelected", { file: files[i] });
                }                                       
            });
        }
    };
        })
        .filter('propsFilter', function() {
		  return function(items, props) {
		    var out = [];
		
		    if (angular.isArray(items)) {
		      var keys = Object.keys(props);
		
		      items.forEach(function(item) {
		        var itemMatches = false;
		
		        for (var i = 0; i < keys.length; i++) {
		          var prop = keys[i];
		          var text = props[prop].toLowerCase();
		          if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
		            itemMatches = true;
		            break;
		          }
		        }
		
		        if (itemMatches) {
		          out.push(item);
		        }
		      });
		    } else {
		      // Let the output be the input untouched
		      out = items;
		    }
		
		    return out;
		  }
		})
        .config(function($routeProvider, $httpProvider)
        {
            $routeProvider
                .when('/login', {

                    templateUrl: "./views/login.view.html",
                    controller: "LoginController",
                    controllerAs: "model"
                })
                .when('/profile', {

                    templateUrl: "./views/adminProfile.view.html",
                    controller: "AdminProfileController",
                    controllerAs: "model",
                    resolve:{
                    	loggedin: RedirectToPageIfLoggedIn
                    }
                })
                .when('/password', {

                    templateUrl: "./views/changePassword.view.html",
                    controller: "AdminProfileController",
                    controllerAs: "model",
                    resolve:{
                    	loggedin: RedirectToPageIfLoggedIn
                    }
                })
                .when('/location', {

                    templateUrl: "./views/location.view.html",
                    controller: "LocationController",
                    controllerAs: "model",
                    resolve:{
                    	loggedin: RedirectToPageIfLoggedIn
                    }
                })
                .when('/targetgroup', {

                    templateUrl: "./views/targetGroup.view.html",
                    controller: "TargetGroupController",
                    controllerAs: "model",
                    resolve:{
                    	loggedin: RedirectToPageIfLoggedIn
                    }
                })
                .when('/questionManagement', {

                    templateUrl: "./views/questionManagement.view.html",
                    controller: "QuestionManagementController",
                    controllerAs: "model",
                    resolve:{
                    	loggedin: RedirectToPageIfLoggedIn
                    }
                })
                 .when('/imageManagement', {

                    templateUrl: "./views/imageManagement.view.html",
                    controller: "ImageManagementController",
                    controllerAs: "model",
                    resolve:{
                    	loggedin: RedirectToPageIfLoggedIn
                    }
                })
                .when('/training', {

                    templateUrl: "./views/training.view.html",
                    controller: "TrainingController",
                    controllerAs: "model",
                    resolve:{
                    	loggedin: RedirectToPageIfLoggedIn
                    }
                })
                .when('/report', {

                    templateUrl: "./views/report.view.html",
                    controller: "ReportController",
                    controllerAs: "model",
                    resolve:{
                    	loggedin: RedirectToPageIfLoggedIn
                    }
                })
                .otherwise({
                    redirectTo: '/login'
                })
        })
        
        function RedirectToPageIfLoggedIn(UserService, $rootScope, $location) {
        	UserService
            .findIfUserLoggedIn()
            .success(function (response) {
            	if(response.status == '200') {
                    var user = {
                		firstName : response.firstName,
                		lastName : response.lastName,
                		email : response.email,
                		role: response.role
                    };
                    UserService.setCurrentUser(user);
            	}
                else {
                	UserService.logout();
                    $rootScope.user = null;
                    $rootScope.errorMessage = 'Please log in to continue.';
                    $location.url('/login');
                }

            });
    	}
})();
