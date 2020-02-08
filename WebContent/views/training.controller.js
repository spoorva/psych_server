/**
 * Created by surajnagaraj on 10/27/16.
 */
(function () {
    angular
        .module("PsychWebApp")
        .controller("TrainingController", TrainingController);
    

    function TrainingController(TrainingService, $window, FieldLookupService)
    {
    	var vm = this;
        vm.tab = 'search';
        vm.trainings = [];
        vm.isSelected = false;
        vm.questionsDropDown = [];
        vm.duplicateQuestionMessage = false;
        vm.imageCategories = [];
        vm.imageTypes = [];
        
        vm.setTab = function (tabId) {
            vm.tab = tabId;
            resetTab();
            
            if (tabId == 'search'){
            	initSearchTab();
            }
        };
        
        vm.resetMessages = function(){
        	vm.addError = false;
			vm.addSuccessMessage = '';
			vm.addSuccess = false;
        }
        
        function resetTab(){
        	vm.isSelected = false;
        	vm.duplicateQuestionMessage = false;
			vm.isUpdateSuccessful = false;
			vm.updateFailed = false;
			vm.selectedTrainingDetails = {};
			vm.createSuccessful = false;
			vm.createFailed = false;
			vm.selectedTrainingDetails.trainingDescription = "";
			vm.selectedTrainingDetails.trainingKeywords = "";
			vm.selectedTrainingDetails.trainingName = "";
			vm.selectedTrainingDetails.trainingImages = [];
			vm.selectedTrainingDetails.trainingQuestions = [];
			
			vm.resetMessages();
        }
        
        function initSearchTab(){
			vm.isUpdateSuccessful = false;
			vm.updateFailed = false;
			
        	TrainingService
     		.getAllTrainings()
     		.success(function(response) {
     			vm.trainings = response.results;
     		});
        	
        }
        
        initSearchTab();

        vm.isSet = function (tabId) {
            return vm.tab === tabId;
        };
        
        vm.getSelected = function(training){
			vm.isUpdateSuccessful = false;
			vm.updateFailed = false;
			
        	if(training != undefined){
        		vm.prevTrainingName = training.trainingName;
        		
        		vm.isSelected = true;
        		TrainingService
        		.getTrainingById(training.trainingId)
        		.success(function(response){
        			vm.selectedTrainingDetails = response.results;
        		});
        	}
        }
        
        vm.updateTraining = function(){
        	if (vm.selectedTrainingDetails != undefined){
        		
        		TrainingService
        		.updateTraining(angular.toJson(vm.selectedTrainingDetails))
        		.success(function(response){
        			if (response.status = "200"){
        				vm.isUpdateSuccessful = true;
        				vm.updateFailed = false;
        			}
        			else{
        				vm.isUpdateSuccessful = false;
        				vm.updateFailed = true;
        			}
        		});
        	}
        }
        
        vm.removeTrainingQuestion = function(index){
        	if(index > -1){
        		vm.selectedTrainingDetails.trainingQuestions.splice(index, 1);
        	}
        }
        
        vm.removeTrainingImage = function(index){
        	if(index > -1){
        		vm.selectedTrainingDetails.trainingImages.splice(index, 1);
        	}
        }
        
        vm.initTrainingQuestions = function(){
        	vm.resetMessages();
        	
        	vm.duplicateQuestionMessage = false;
        	vm.questionsDropDown = [];
        	
    		TrainingService
    		.getQuestionCategories()
    		.success(function(response){
    			vm.questionCategories = response.results;
    		});
    		
    		TrainingService
    		.getQuestions()
    		.success(function(response){
    			vm.questions = response.results;
    		});
        }
        
        vm.populateQuestions = function(selectedQuestionCategory){
        	vm.resetMessages();
        	vm.questionsDropDown = [];
        	vm.questions.forEach(function (question){
        		if (question.questionCategoryId == selectedQuestionCategory.questionCategoryId){
        			vm.questionsDropDown.push(clone(question))
        		}
        	});
        }
        
        vm.checkDuplicate = function(){
        	vm.duplicateQuestionMessage = false;
        	
        	vm.resetMessages();
    		
        	if(vm.selectedTrainingDetails.trainingQuestions !== undefined){
        		vm.selectedTrainingDetails.trainingQuestions.forEach(function (question){
        			if (question.questionId == vm.questionsDropDown.selected.questionId){
        				vm.duplicateQuestionMessage = true;
        			}
        		});
        	}else{
        		vm.selectedTrainingDetails.trainingQuestions = [];
        	}
        }
        
        vm.addQuestionToTraining = function(){
        	if (vm.questionCategories.selected != undefined && vm.questionsDropDown.selected != undefined){
        		var duplicate = false;
        		
        		if(vm.selectedTrainingDetails.trainingQuestions !== undefined){
        			vm.selectedTrainingDetails.trainingQuestions.forEach(function (question){
                		if (question.questionId == vm.questionsDropDown.selected.questionId){
                				duplicate = true;
                		}
                	});
        		}
        		else{
        			vm.selectedTrainingDetails.trainingQuestions = [];
        		}
        		
        		if(!duplicate){
        			var newTrainingQuestion = {
        					questionId: vm.questionsDropDown.selected.questionId,
        					questionCategoryName: vm.questionsDropDown.selected.questionCategoryName,
        					questionDescription: vm.questionsDropDown.selected.questionDescription,
        					questionCategoryId: vm.questionsDropDown.selected.questionCategoryId,
        					questionName: vm.questionsDropDown.selected.questionName
        			}
        			
        			vm.selectedTrainingDetails.trainingQuestions.push(newTrainingQuestion);
        			
        			vm.addError = false;
        			vm.addSuccessMessage = 'Question added successfully!';
        			vm.addSuccess = true;
        			vm.questionsDropDown.selected = '';
        			vm.questionCategories.selected = '';
        		}
        		else{
        			vm.addSuccess = false;
        			vm.addErrorMessage = 'Question already in training.';
        			vm.addError = true;
        		}
        		
        	}
        }
        
        vm.initTrainingImages = function(){
        	vm.resetMessages();
        	vm.addImageCount = undefined;
        	vm.addImageDuration = undefined;
        	vm.duplicateImageMessage = false;
        	
        	TrainingService
    		.getAllImageCategories()
    		.success(function(response){
    			vm.imageCategories = response.results;
    		});
    		
        	FieldLookupService
    		.fetchFields("imageType")
    		.success(function(response){
    			vm.imageTypes = response.results;
    		});
        }
        
        
        vm.addImageToTraining = function(){
        	vm.duplicateImageMessage = false;
        	
        	if(vm.imageCategories.selected != null && vm.imageTypes.selected != null &&
        		vm.addImageDuration != null && vm.addImageCount != null){
        		var duplicate = false;
        		
        		if(vm.selectedTrainingDetails.trainingImages !== undefined){
        			vm.selectedTrainingDetails.trainingImages.forEach(function(trainingImage){
            			
            			if(trainingImage.imageTypeId == vm.imageTypes.selected.fieldId 
            					&& trainingImage.imageCategoryId == vm.imageCategories.selected.imageCategoryId){
            				duplicate = true;
            			}
            		});
        		}
        		else{
        			vm.selectedTrainingDetails.trainingImages = [];
        		}
        		
        		
        		if(!duplicate){
        			var newTrainingImage = {
            				duration: vm.addImageDuration,
            				noOfImages: vm.addImageCount,
            				imageTypeId: vm.imageTypes.selected.fieldId,
            				trainingId: vm.selectedTrainingDetails.trainingId,
            				imageCategoryId: vm.imageCategories.selected.imageCategoryId,
            				imageCategoryName: vm.imageCategories.selected.imageCategoryName,
            				imageType: vm.imageTypes.selected.fieldValue
            		}
        			
        			vm.selectedTrainingDetails.trainingImages.push(newTrainingImage);
        			vm.addSuccess = true;
        			vm.addError = false;
        			vm.addSuccessMessage = 'Images added successfully!';
        			vm.imageCategories.selected = '';
        			vm.imageTypes.selected = '';
        			vm.addImageDuration = '';
        			vm.addImageCount = '';
        		}
        		else{
        			vm.addSuccess = false;
        			vm.addError = true;
        			vm.addErrorMessage = 'Images already in training.';
        		}
        	}
        }
        
        
        vm.createTraining = function(){
        	if(vm.selectedTrainingDetails !== undefined){
        		
        		if(vm.selectedTrainingDetails.trainingDescription == undefined){
        			vm.selectedTrainingDetails.trainingDescription = '';
        		}
        		if(vm.selectedTrainingDetails.trainingKeywords == undefined){
        			vm.selectedTrainingDetails.trainingKeywords = '';
        		}
        		
        		TrainingService
        		.createTraining(angular.toJson(vm.selectedTrainingDetails))
        		.success(function(response){
        			if(response.status == '200'){
        				vm.selectedTrainingDetails = {};
        				vm.createSuccessful = true;
        				vm.createFailed = false;
        				initSearchTab();
        				
        			}else{
        				vm.createSuccessful = false;
        				vm.createFailed = true;
        			}
        		});
        	}
        }
        
        vm.checkDuplicateTrainingName = function(checkName){
        	var duplicate = false;
        	if(vm.trainings != undefined){
        		vm.trainings.forEach(function(training){
        			if(training.trainingName.toLowerCase() == checkName.toLowerCase()){
        				duplicate = true;
        			}
        			
        			if(vm.isSelected && vm.prevTrainingName.toLowerCase() == checkName.toLowerCase()){
        				duplicate = false;
        			}
        		})
        	}
        	
        	if(vm.isSelected){
        		vm.updateTrainingForm.updateTrainingName.$error.trainingNameError = duplicate;
        		vm.updateTrainingForm.$invalid = duplicate;
        	}
        	else{
        		vm.createTrainingForm.createTrainingName.$error.trainingNameError = duplicate;
            	vm.createTrainingForm.$invalid = duplicate;
        	}
        	
        }
        
        
    }

})();