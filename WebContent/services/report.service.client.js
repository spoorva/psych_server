(function() {
    'use strict';
    angular
        .module("PsychWebApp")
        .factory("ReportService", ReportService);
    
    
    function ReportService($http, serverURL) {
    	
    	
    	var service = {
    			getParticipantReport : getParticipantReport
    	};
    	
    	return service;
    	
    	function getParticipantReport(pId) {
    		if(pId != undefined){
    			return $http ({
                    method: 'GET',
                    url: serverURL.url + 'report',
                    contentType: 'application/json',
                    params: {reportType: 1, participant: pId}
                    });
    		}
    	}
    }
})();