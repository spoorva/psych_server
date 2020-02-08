/**
 * Created by surajnagaraj on 10/27/16.
 */
(function () {
    angular
        .module("PsychWebApp")
        .controller("LocationController", LocationController);
    

    function LocationController(LocationService, $window)
    {
    	var vm = this;
        vm.tab = 'search';
        vm.isLocationNameDuplicate = false;
        vm.isLocationCodeDuplicate = false;
        vm.isSearchClicked = false;
        
        vm.newLocation = {
        		name : '',
        		description : '',
        		keywords : '',
        		code : '',
        		address1 : '',
        		address2 : '',
        		city : '',
        		state : '',
        		zipcode : '',
        		phoneNo : '',
        		faxNo : '',
        		email : ''
        };
        
        function initSearchLoc() {
        	vm.searchLoc = {
            		name : '',
            		locationCode : '',
            		description : '',
            		keywords : '',
            		address : '',
            		city : '',
            		state : '',
            		zipcode : '',
            		phoneNo : '',
            		faxNo : '',
            		email : ''
            };
        }
        initSearchLoc();
        
       
	    LocationService
	 		.getAllStates()
	 		.success(function(response) {
	 			vm.statesList = response;
	 		});

        vm.setTab = function (tabId) {
            //console.log("Setting tab to " + tabId);
            vm.tab = tabId;
            if(tabId === 'search') {
            	vm.locationSearchResults = [];
            	initSearchLoc();
            	vm.isSearchClicked = false;
            }
        };

        vm.isSet = function (tabId) {
            //console.log("Tab set to " + tabId);
            return vm.tab === tabId;
        };
        
        vm.create = create;
        
        vm.checkDuplicate = checkDuplicate;
        
        function checkDuplicate(param, value){
        	//console.log(param + " " + value);
        	
        	if (param == 'locationName'){
        		LocationService
        		.checkDuplicate(param, value)
        		.then(function(response) {
        			if(typeof(response.data.results) == "boolean") {
        				vm.locationCreateForm.$invalid = response.data.results; 
        				vm.locationCreateForm.createLocationName.$error.locationNameError = response.data.results;
        			}
        		})
        	}
        	
        	if (param == 'locationCode'){
        		LocationService
        		.checkDuplicate(param, value)
        		.then(function(response) {
        			if(response.data.results == true) {
        				vm.locationCreateForm.createLocationCode.$error.locationCodeError = true;
        			}
        			else{
        				vm.locationCreateForm.createLocationCode.$error.locationCodeError = false;
        			}
        		})
        	}
        	
        }
        
        function create(newLocation) {
        	
        	vm.isCreateFailed = false;
        	vm.isCreateSuccessful = false;
        	
        	var Location = {
        		locationName : newLocation.name,
        		locationDescription : newLocation.description,
        		locationKeywords : newLocation.keywords,
        		locationCode : newLocation.code,
        		locationAddressLine1 : newLocation.address1,
        		locationAddressLine2 : newLocation.address2,
        		locationCity : newLocation.city,
        		locationStateId : newLocation.state.toString(),
        		locationZipCode : newLocation.zipcode,
        		locationPhoneNumber : newLocation.phoneNo,
        		locationFaxNumber : newLocation.faxNo,
        		locationEmail : newLocation.email
        		
        	};
        	console.log(Location);
        	
        	/*LocationService
        		.checkDuplicate('locationName', newLocation.name)
        		.then(function(response) {
        			if(response.results === 'true') {
        				LocationService
        					.checkDuplicate('locationCode', newLocation.code)
        					.then(function(response) {
        						if(response.results === "true") {
        							
        						}
        						
        						else 
        							vm.isLocationCodeDuplicate = true;
        				});
        			}
        			
        			else
        				vm.isLocationNameDuplicate = true;
        		})*/
        	
        	LocationService
    		.createLocation(Location)
    		.success(function(response) {
    			if(response.status == '200') {
    			//vm.isCreateSuccessful = true;
    			$window.alert('Location has been created successfully');
    			vm.newLocation = {
    	        		name : '',
    	        		description : '',
    	        		keywords : '',
    	        		code : '',
    	        		address1 : '',
    	        		address2 : '',
    	        		city : '',
    	        		state : '',
    	        		zipcode : '',
    	        		phoneNo : '',
    	        		faxNo : '',
    	        		email : ''
    	        };
    			
    			}
    			
    			else
    				//vm.isCreateFailed = true;
    				$window.alert('Location creation failed');
    		});
        	
        }
        
        vm.search = search;
        var locationId = '';
        
        function search(locationSearch) {
        	
        	var locationsList = [];
        	
        	LocationService
        		.getAllLocations()
        		.success(function(response) {
        			locationsList = response.results;
        			if(locationSearch.state === undefined) {
        				locationSearch.state = '';
        			}
                	var locationParams = {
                			locationName : locationSearch.name,
                    		locationDescription : locationSearch.description,
                    		locationKeywords : locationSearch.keywords,
                    		locationCode : locationSearch.locationCode,
                    		locationAddress : locationSearch.address,
                    		locationCity : locationSearch.city,
                    		locationStateId : locationSearch.state.toString(),
                    		locationZipCode : locationSearch.zipcode,
                    		locationPhoneNumber : locationSearch.phoneNo,
                    		locationFaxNumber : locationSearch.faxNo,
                    		locationEmail : locationSearch.email	
                	};
                	console.log(locationParams);
                	
                	vm.locationSearchResults = JSONSearch(locationParams, locationsList);
                	
//                	var keys = [];
//                	var xPath = '//*';
//                	
//                	for (var param in locationParams) {
//                		//console.log(param + " " + locationParams[param]);
//                		if(locationParams[param] != ''){
//                			if(param.toLowerCase().indexOf("id") != -1){
//                				xPath += '[' + param + '="' + locationParams[param] + '"]'
//                			}
//                			else{
//                				xPath += '[contains(' + param + ',' + '"' +locationParams[param] + '"' + ')]';
//                			}
//                			
//                			keys.push(param);
//                		}
//                	}
//                	
//                	if(keys.length > 0) {
//                		var found = JSON.search(locationsList, xPath);
//                		
//                		vm.locationSearchResults = found;
//                	}
//                	else {
//                		vm.locationSearchResults = locationsList;
//                	}
                	
                	vm.isSearchClicked = true;
                	
                	/*LocationService
                		.getAllLocations(locationParams)
                		.success(function(response) {
                			locationId = response.locationId;
                		});*/
        		
        		
        		})
        	
        	
        	
        }
        
        vm.selectLocation = selectLocation;
        var updateLocationId = '';
        var updateLocationCode = '';
        function selectLocation(index) {
        	var location = vm.locationSearchResults[index]
        	vm.locationUpdate = {
        			name : location.locationName,
        			description : location.locationDescription,
        			keywords : location.locationKeywords,
        			address1 : location.locationAddressLine1,
        			address2 : location.locationAddressLine2,
        			city : location.locationCity,
        			state : parseInt(location.locationStateId),
        			zipCode : location.locationZipCode,
        			phoneNo : location.locationPhoneNumber,
        			faxNo : location.locationFaxNumber,
        			email : location.locationEmail
        			
        	}
        	updateLocationId = vm.locationSearchResults[index].locationId;
        	updateLocationCode = vm.locationSearchResults[index].locationCode;
        	
        	
        	
        	console.log("modal location state value = " + vm.locationUpdate.state);
        	
        	
        }
        
        vm.update = update;
        
        function update(locationUpdate) {
        	var locationUpdateParams = {
        			locationId : updateLocationId.toString(),
        			locationName : locationUpdate.name,
            		locationDescription : locationUpdate.description,
            		locationKeywords : locationUpdate.keywords,
            		locationCode : updateLocationCode,
            		locationAddressLine1 : locationUpdate.address1,
            		locationAddressLine2 : locationUpdate.address2,
            		locationCity : locationUpdate.city,
            		locationStateId : locationUpdate.state.toString(),
            		locationZipCode : locationUpdate.zipCode,
            		locationPhoneNumber : locationUpdate.phoneNo,
            		locationFaxNumber : locationUpdate.faxNo,
            		locationEmail : locationUpdate.email	
        	};
        	console.log(locationUpdateParams);
        	LocationService
    			.updateLocation(locationUpdateParams)
    			.success(function(response) {
    				console.log(response);
    				if(response.status =='200') {
    					vm.isUpdateSuccessful = true;
    					$window.alert('Location has been updated successfully');
    					vm.locationSearchResults = response.results;
    				}
    				
    				else {
    					$window.alert('Location update failed');
    				}
    				
    			});
        }
        
        vm.selectLocationDetails = selectLocationDetails;
        
        function selectLocationDetails(loc) {
        	var location = vm.locationSearchResults[loc];
        	console.log(location);
        	vm.selectedLocation = location;
        }
    }
    

})();