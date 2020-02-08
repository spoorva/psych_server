(function() {
    'use strict';
    angular
        .module("PsychWebApp")
        .factory("ImageManagementService", ImageManagementService);
    
    
    function ImageManagementService($http, serverURL) {
    	
    	
    	
    	var service = {
    			createImageCategory : createImageCategory,
    			updateImageCategory : updateImageCategory,
    			getAllCategories : getAllCategories,
    			getAllImages : getAllImages,
    			updateImage : updateImage
    	};
    	
    	return service;
    	
    	function createImageCategory(Ic) {
    		if(Ic)
    			return $http ({
                    method: 'POST',
                    url: serverURL.url + 'imageCategory',
                    contentType: 'application/json',
                    data: Ic});
    	}
    	
    	function updateImageCategory(Ic) {
    		return $http ({
                method: 'PUT',
                url: serverURL.url + 'imageCategory',
                contentType: 'application/json',
                data: Ic});
    	}
    	
    	function getAllCategories() {
    		return $http ({
                method: 'GET',
                url: serverURL.url + 'imageCategory',
                contentType: 'application/json'
                });
    	}
    	
    	function getAllImages() {
    		return $http ({
                method: 'GET',
                url: serverURL.url + 'imageUpload',
                contentType: 'application/json'
                });
    	}
    	
    	function updateImage(imgDetails) {
    		return $http ({
                method: 'PUT',
                url: serverURL.url + 'imageUpload',
                contentType: 'application/json',
                data : imgDetails
                });
    	}
    }
})();