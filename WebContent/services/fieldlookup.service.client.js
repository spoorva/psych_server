(function() {
    'use strict';
    angular
        .module("PsychWebApp")
        .factory("FieldLookupService", FieldLookupService);
    
    
    function FieldLookupService($http, serverURL) {
    	
    	var service = {
    			fetchFields : fetchFields
    	};
    	
    	return service;
    	
    	function fetchFields(fieldGroup){
    		return $http ({
    			method : 'GET',
    			url : serverURL.url + 'fetchField',
    			contentType: 'application/json',
    			params: {fieldName: fieldGroup}
    		});
    	}
    	
    	
    }
})();