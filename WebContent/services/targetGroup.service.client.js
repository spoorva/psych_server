(function() {
    'use strict';
    angular
        .module("PsychWebApp")
        .factory("TargetGroupService", TargetGroupService);
    
    
    function TargetGroupService($http, serverURL) {
    	/*var awsURL = 'http://ec2-54-175-16-62.compute-1.amazonaws.com:8080/Psych-1/';
    	var localServerURL = 'http://localhost:8080/Psych-1/';
    	var serverURL = 'http://localhost:8080/Psych-1/';*/
    	
    	var service = {
    			createTargetGroup : createTargetGroup,
    			getAllTargetGroups : getAllTargetGroups,
    			updateTargetGroup : updateTargetGroup,
    			checkDuplicate : checkDuplicate
    	};
    	
    	return service;
    	
    	function createTargetGroup(targetGroup) {
    		if(targetGroup)
    			return $http ({
                    method: 'POST',
                    url: serverURL.url + 'targetGroup',
                    contentType: 'application/json',
                    data: targetGroup});
    		
    	}
    	
    	function getAllTargetGroups() {
    		return $http ({
                method: 'GET',
                url: serverURL.url + 'targetGroup',
                contentType: 'application/json'
                });
    	}
    	
    	function updateTargetGroup(updatedTargetGroup) {
    		return $http ({
                method: 'PUT',
                url: serverURL.url + 'targetGroup',
                contentType: 'application/json',
                data: updatedTargetGroup});
    	}
    	
    	function checkDuplicate(param, value) {
    		return $http ({
    			method : 'GET',
    			url : serverURL.url + 'targetGroup',
    			contentType: 'application/json',
    			params : {param : value}
    		});
    	}
    }
    
})();