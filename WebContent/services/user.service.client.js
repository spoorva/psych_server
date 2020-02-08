/**
 * Created by surajnagaraj on 10/27/16.
 */

(function() {
    'use strict';
    angular
        .module("PsychWebApp")
        .factory("UserService", UserService);

    function UserService($http, $rootScope, serverURL) {
    	
    	/*var awsURL = 'http://ec2-54-175-16-62.compute-1.amazonaws.com:8080/Psych-1/';
    	var localServerURL = 'http://localhost:8080/Psych-1/';
    	var serverURL = 'http://localhost:8080/Psych-1/'; */
    	
        var service = {
            login: login,
            logout: logout,
            setCurrentUser: setCurrentUser,
            updateProfile: updateProfile,
            findIfUserLoggedIn: findIfUserLoggedIn,
            changePassword: changePassword,
            getAllParticipants: getAllParticipants
        };

        return service;

        function login(user) {

        	if(user) {
        		
        	console.log("Data being sent: " + user.email + " " + user.password);
            return $http ({
                method: 'POST',
                url: 'adminAuthentication',
                contentType: 'application/json',
                data: {
    				email: user.email,
    				password: user.password
    		}});
            
           
                
        	}
        }

        function logout() {
            $rootScope.currentUser = null;
            return $http ({
                method: 'GET',
                url: serverURL.url + 'adminAuthentication?logout=yes',
                contentType: 'application/json',
                data: ""
    		});
        }

        function setCurrentUser(user) {
            $rootScope.currentUser = user;
        }
        
        function updateProfile(userData) {
        	if(userData) {
        		
            	console.log("Data being sent: " + userData.firstName + " " + userData.lastName + " " + userData.email);
                return $http ({
                    method: 'PUT',
                    url: serverURL.url + 'userProfile',
                    contentType: 'application/json',
                    data: userData
        		});
                    
            	}
        }
        
        function findIfUserLoggedIn() {

        	return $http ({
                method: 'GET',
                url: serverURL.url + 'adminAuthentication?loggedIn=yes',
                contentType: 'application/json',
                data: ""
    		});
        }
        
        function changePassword(password) {
        	 return $http ({
                 method: 'PUT',
                 url: serverURL.url + 'changePassword',
                 contentType: 'application/json',
                 data: password
     		});
        }
        
        function getAllParticipants(){
        	return $http ({
                method: 'GET',
                url: serverURL.url + 'UserProfile',
                contentType: 'application/json',
                params: {participant: 'all'}
    		});
        }
    }
})();
