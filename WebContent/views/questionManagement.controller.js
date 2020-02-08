/**
 * Created by surajnagaraj on 10/27/16.
 */
(function () {
    angular
        .module("PsychWebApp")
        .controller("QuestionManagementController", QuestionManagementController);
    

    function QuestionManagementController(QuestionManagementService, $window){
    	var vm = this;
    	vm.tab = 'questionCategories';
    	vm.subTab = 'searchQuestionCategories'
    	
    	vm.createQ = {
    			newQuestionName : '',
    			newQuestionDescription : '',
    			newQuestionCategoryId : ''
    	};
    	
    	vm.createQc = {
    			newQuestionCategoryName : '',
    			newQuestionCategoryDescription : '',
    			newResponseType : '',
    			newStartLabel : '',
    			newEndLabel : ''
    	};
    	
    	
    	
    	
    	
    	function initSearchQc() {
    		vm.searchQc = {
        			questionCategoryName : '',
        			questionCategoryDescription : '',
        			responseTypeFieldId : '',
        			startLabel : '',
        			endLabel : ''
        	};
    	}
    	initSearchQc();
    	
    	function initSearchQ() {
    		vm.searchQ = {
        			questionName : '',
        			questionDescription : '',
        			questionCategoryId : ''
        	};
    	}
    	initSearchQ();
    	
    	vm.setTab = function (tabId) {
            //console.log("Setting tab to " + tabId);
            vm.tab = tabId;
            if(tabId === "questionCategories") {
        		vm.setSubTab("searchQuestionCategories");
        	}
            
            if(tabId === "questions") {
        		vm.setSubTab("searchQuestions");
        	}
        };
        
        vm.setSubTab = function (tabId) {
        	vm.subTab = tabId;
        	if(tabId === "searchQuestionCategories") {
        		vm.questionCategorySearchResults = [];
            	initSearchQc();
            	vm.isSearchClicked = false;
        	}
        	
        	if(tabId === "searchQuestions") {
        		vm.questionSearchResults = [];
            	initSearchQ();
            	vm.isQuestionSearchClicked = false;
        	}
        }

        vm.isSet = function (tabId) {
            //console.log("Tab set to " + tabId);
        	
            return vm.tab === tabId;
        };
        
        vm.isSetSubTab = function (tabId) {
        	return vm.subTab === tabId;
        };
        
        QuestionManagementService
        	.getAllCategories()
        	.success(function(response) {
        		if(response.status === '200')
        			vm.questionCategoryList = response.results;
        	});
        
        QuestionManagementService
    		.getAllResponseTypes()
    		.success(function(response) {
    			if(response.status === '200')
    				vm.responseTypeList = response.results;
    	});
        
        vm.createQuestionCategory = createQuestionCategory;
        
        function createQuestionCategory(qc) {
        	var questionCategory = {
        			newQuestionCategoryName : qc.newQuestionCategoryName,
        			newQuestionCategoryDescription : qc.newQuestionCategoryDescription,
        			newResponseType : qc.newResponseType,
        			newStartLabel : qc.newStartLabel,
        			newEndLabel : qc.newEndLabel
        	};
        	
        	console.log(questionCategory);
        	
        	QuestionManagementService
        		.createQuestionCategory(questionCategory)
        		.success(function(response) {
        			if(response.status === '200') {
        				vm.createQc = {
        		    			newQuestionCategoryName : '',
        		    			newQuestionCategoryDescription : '',
        		    			newResponseType : '',
        		    			newStartLabel : '',
        		    			newEndLabel : ''
        		    	};
        				
        				$window.alert('Question Category has been created successfully');
        				QuestionManagementService
        	        	.getAllCategories()
        	        	.success(function(response) {
        	        		if(response.status === '200')
        	        			vm.questionCategoryList = response.results;
        	        	});
        				
        			}
        			
        			else
        				$window.alert('Question Category creation failed');
        		});
        }
        
        vm.createQuestion = createQuestion;
        
        function createQuestion(question) {
        	var questionParams = {
        			newQuestionName : question.newQuestionName,
        			newQuestionDescription : question.newQuestionDescription,
        			newQuestionCategoryId : parseInt(question.newQuestionCategoryId)
        	};
        	
        	console.log(questionParams);
        	
        	QuestionManagementService
        		.createQuestion(questionParams)
        		.success(function(response) {
        			if(response.status === '200') {
        				vm.createQ = {
        		    			newQuestionName : '',
        		    			newQuestionDescription : '',
        		    			newQuestionCategoryId : ''
        		    	};
        				
        				$window.alert('Question has been created successfully');
        			}
        			
        			else
        				$window.alert('Question creation failed');
        		});
        }
        
        vm.searchQuestionCategories = searchQuestionCategories;
        
        function searchQuestionCategories(searchQc) {
        	var questionCategoriesList = [];
        	
        	QuestionManagementService
        		.getAllCategories()
        		.success(function(response) {
        			questionCategoriesList = response.results;
        			
        			if(searchQc.responseTypeFieldId === undefined)
        				searchQc.responseTypeFieldId = '';
        			
                	var questionCategoryParams = {
                			questionCategoryName : searchQc.questionCategoryName,
                			questionCategoryDescription : searchQc.questionCategoryDescription,
                			responseTypeFieldId : searchQc.responseTypeFieldId.toString(),
                			startLabel : searchQc.startLabel,
                			endLabel : searchQc.endLabel	
                	};
                	
                	vm.questionCategorySearchResults = JSONSearch(questionCategoryParams, questionCategoriesList);
                	
                	vm.isSearchClicked = true;
                
        		})
        }
        
        vm.searchQuestions = searchQuestions;
        
        function searchQuestions(q) {
        	var questionsList = [];
        	
        	QuestionManagementService
        		.getAllQuestions()
        		.success(function(response) {
        			questionsList= response.results;
        			
        			if(q.questionCategoryId === undefined)
        				q.questionCategoryId = '';
        			//console.log(locationSearch);
                	var questionParams = {
                			questionName : q.questionName,
                			questionDescription : q.questionDescription	,
                			questionCategoryId : q.questionCategoryId.toString()
                	};
                	
                	vm.questionSearchResults = JSONSearch(questionParams, questionsList);
                	vm.isQuestionSearchClicked = true;
                
        		})
        }
        
        vm.selectQuestionCategory = selectQuestionCategory;
        var updateQuestionCategoryId = '';
        var selectedQuestionCategory = '';
        function selectQuestionCategory(index) {
        	var qc = vm.questionCategorySearchResults[index];
        	selectedQuestionCategory = index;
        	console.log(qc);
        	vm.updateQc = {
        			questionCategoryName : qc.questionCategoryName,
        			questionCategoryDescription : qc.questionCategoryDescription,
        			responseType : parseInt(qc.responseTypeFieldId),
        			startLabel : qc.startLabel,
        			endLabel : qc.endLabel
        	};
        	updateQuestionCategoryId = vm.questionCategorySearchResults[index].questionCategoryId;
        }
        
        vm.selectQuestion = selectQuestion;
        var updateQuestionId = '';
        var selectedQuestion = '';
        function selectQuestion(index) {
        	var q = vm.questionSearchResults[index];
        	selectedQuestion = index;
        	console.log(q);
        	vm.updateQ = {
        			questionName : q.questionName,
        			questionDescription : q.questionDescription,
        			questionCategoryId : q.questionCategoryId
        	};
        	updateQuestionId = vm.questionSearchResults[index].questionId;
        }
        
        vm.updateQuestionCategory = updateQuestionCategory;
        
        function updateQuestionCategory(qc) {
        	
        	var qcUpdateParams = {
        			questionCategoryName : qc.questionCategoryName,
        			questionCategoryDescription : qc.questionCategoryDescription,
        			responseType : qc.responseType,
        			startLabel : qc.startLabel,
        			endLabel : qc.endLabel,
        			questionCategoryId : parseInt(updateQuestionCategoryId)
        	};
        	//console.log(locationUpdateParams);
        	console.log(qcUpdateParams);
        	QuestionManagementService
    			.updateQuestionCategory(qcUpdateParams)
    			.success(function(response) {
    				if(response.status =='200') {
    					vm.isUpdateSuccessful = true;
    					$window.alert('Question Category has been updated successfully');
    					vm.responseTypeList.forEach(function(rT){
    						if(rT.fieldId == qc.responseType){
    							qcUpdateParams.responseType = rT.fieldValue;
    							qcUpdateParams.responseTypeFieldId = qc.responseType;
    						}
    					});
    					
    					vm.questionCategorySearchResults[selectedQuestionCategory] = qcUpdateParams;
    				}
    				
    				else {
    					$window.alert('Question Category update failed');
    				}
    				
    			});
        }
        
        vm.updateQuestion = updateQuestion;
        
        function updateQuestion(q) {
        	console.log(q);
        	var questionUpdateParams = {
        			questionName : q.questionName,
        			questionDescription : q.questionDescription,
        			questionId : parseInt(updateQuestionId),
        			questionCategoryId : parseInt(q.questionCategoryId)
        	};
        	//console.log(locationUpdateParams);
        	//console.log(questionUpdateParams);
        	QuestionManagementService
    			.updateQuestion(questionUpdateParams)
    			.success(function(response) {
    				if(response.status =='200') {
    					vm.isUpdateSuccessful = true;
    					$window.alert('Question has been updated successfully');
    					
    					vm.questionCategoryList.forEach(function(qc){
    						if(qc.questionCategoryId == q.questionCategoryId){
    							questionUpdateParams.questionCategoryName = qc.questionCategoryName;
    						}
    					})
    					 
    					vm.questionSearchResults[selectedQuestion] = questionUpdateParams;
    				}
    				
    				else {
    					$window.alert('Question update failed');
    				}
    				
    			});
        }
        
        vm.selectQuestionCategoryDetails = selectQuestionCategoryDetails;
        
        function selectQuestionCategoryDetails(qc) {
        	var questionCategory = vm.questionCategorySearchResults[qc];
        	console.log(questionCategory);
        	vm.selectedQc = questionCategory;
        }
        
        vm.selectQuestionDetails = selectQuestionDetails;
        
        function selectQuestionDetails(q) {
        	var question = vm.questionSearchResults[q];
        	console.log(question);
        	vm.selectedQ = question;
        }
        
        
    }
})();