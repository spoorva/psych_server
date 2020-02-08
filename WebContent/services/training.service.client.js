(function() {
    'use strict';
    angular
        .module("PsychWebApp")
        .factory("TrainingService", TrainingService);
    
    
    function TrainingService($http, serverURL) {
    	/*var awsURL = 'http://ec2-54-175-16-62.compute-1.amazonaws.com:8080/Psych-1/';
    	var localServerURL = 'http://localhost:8080/Psych-1/';
    	var serverURL = 'http://localhost:8080/Psych-1/';*/
    	
    	var service = {
    			getAllTrainings : getAllTrainings,
    			getTrainingById: getTrainingById,
    			getQuestionCategories: getQuestionCategories,
    			getQuestions: getQuestions,
    			getAllImageCategories: getAllImageCategories,
    			updateTraining: updateTraining,
    			createTraining: createTraining
    	};
    	
    	return service;
    	
    	function getAllTrainings() {
    		return $http ({
    			method : 'GET',
    			url : serverURL.url + 'training',
    			contentType: 'application/json',
    			params : {'dropDown' : 'yes'}
    		});
    	}
    	
    	function getTrainingById(id){
    		return $http ({
    			method : 'GET',
    			url : serverURL.url + 'training',
    			contentType: 'application/json',
    			params : {trainingId : id}
    		});
    	}
    	
    	function updateTraining(newTraining){
    		return $http ({
    			method : 'POST',
    			url : serverURL.url + 'training',
    			contentType: 'application/json',
    			data: newTraining
    		});
    	}
    	
    	function getQuestionCategories(){
    		return $http ({
    			method : 'GET',
    			url : serverURL.url + 'questionCategory',
    			contentType: 'application/json'
    		});
    	}
    	
    	function getQuestions(){
    		return $http ({
    			method : 'GET',
    			url : serverURL.url + 'question',
    			contentType: 'application/json'
    		});
    	}
    	
    	
    	function getAllImageCategories(){
    		return $http ({
    			method : 'GET',
    			url : serverURL.url + 'imageCategory',
    			contentType: 'application/json'
    		});
    	}
    	
    	function updateTraining(updatedTraining){
    		return $http ({
    			method : 'PUT',
    			url : serverURL.url + 'training',
    			contentType: 'application/json',
    			data: updatedTraining
    		});
    	}
    	
    	function createTraining(newTraining){
    		return $http ({
    			method : 'POST',
    			url : serverURL.url + 'training',
    			contentType: 'application/json',
    			data: newTraining
    		});
    	}
    }
})();