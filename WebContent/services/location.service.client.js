(function() {
    'use strict';
    angular
        .module("PsychWebApp")
        .factory("LocationService", LocationService);
    
    
    function LocationService($http, serverURL) {
    	/*var awsURL = 'http://ec2-54-175-16-62.compute-1.amazonaws.com:8080/Psych-1/';
    	var localServerURL = 'http://localhost:8080/Psych-1/';
    	var serverURL = 'http://localhost:8080/Psych-1/';*/
    	
    	var service = {
    			createLocation : createLocation,
    			getAllLocations : getAllLocations,
    			getAllStates : getAllStates,
    			updateLocation : updateLocation,
    			checkDuplicate : checkDuplicate
    	};
    	
    	return service;
    	
    	function createLocation(location) {
    		if(location)
    			return $http ({
                    method: 'POST',
                    url: serverURL.url + 'location',
                    contentType: 'application/json',
                    data: location});
    		
    	}
    	
    	function getAllLocations() {
    		return $http ({
                method: 'GET',
                url: serverURL.url + 'location',
                contentType: 'application/json'
                });
    	}
    	
    	function getAllStates() {
    		return $http ({
    			method : 'GET',
    			url : serverURL.url + 'fetchField',
    			contentType: 'application/json',
    			params : {'fieldName' : 'State'}
    		});
    	}
    	
    	function updateLocation(updatedLocation) {
    		return $http ({
                method: 'PUT',
                url: serverURL.url + 'location',
                contentType: 'application/json',
                data: updatedLocation});
    	}
    	
    	function checkDuplicate(param, value) {
    		return $http ({
    			method : 'GET',
    			url : serverURL.url + 'location' + '?' + param + "=" + value,
    			contentType: 'application/json'
    			//params : {param : value}
    		});
    	}
    }
    
})();