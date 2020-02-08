(function() {
    'use strict';
    angular
        .module("PsychWebApp")
        .factory("QuestionManagementService", QuestionManagementService);
    
    
    function QuestionManagementService($http, serverURL) {
    	
    	
    	var service = {
    			createQuestionCategory : createQuestionCategory,
    			createQuestion : createQuestion,
    			getAllCategories : getAllCategories,
    			updateQuestionCategory : updateQuestionCategory,
    			updateQuestion : updateQuestion,
    			checkDuplicate : checkDuplicate,
    			getAllQuestions : getAllQuestions,
    			getAllResponseTypes : getAllResponseTypes
    	};
    	
    	return service;
    	
    	function createQuestionCategory(questionCategory) {
    		if(questionCategory)
    			return $http ({
                    method: 'POST',
                    url: serverURL.url + 'questionCategory',
                    contentType: 'application/json',
                    data: questionCategory});
    		
    	}
    	
    	function createQuestion(question) {
    		if(question)
    			return $http ({
                    method: 'POST',
                    url: serverURL.url + 'question',
                    contentType: 'application/json',
                    data: question});
    		
    	}
    	
    	function getAllCategories() {
    		return $http ({
                method: 'GET',
                url: serverURL.url + 'questionCategory',
                contentType: 'application/json'
                });
    	}
    	
    	function getAllQuestions() {
    		return $http ({
                method: 'GET',
                url: serverURL.url + 'question',
                contentType: 'application/json'
                });
    	}
    	
    	function getAllResponseTypes() {
    		return $http ({
    			method : 'GET',
    			url : serverURL.url + 'FetchCommonFieldServlet?fieldName=responseType',
    			contentType: 'application/json'
    		});
    	}
    	
    	function updateQuestionCategory(questionCategory) {
    		return $http ({
                method: 'PUT',
                url: serverURL.url + 'questionCategory',
                contentType: 'application/json',
                data: questionCategory});
    	}
    	
    	function updateQuestion(question) {
    		return $http ({
                method: 'PUT',
                url: serverURL.url + 'question',
                contentType: 'application/json',
                data: question});
    	}
    	
    	function checkDuplicate(param, value) {
    		return $http ({
    			method : 'GET',
    			url : serverURL.url + 'questionCategory' + '?' + param + "=" + value,
    			contentType: 'application/json'
    			//params : {param : value}
    		});
    	}
    }
})();