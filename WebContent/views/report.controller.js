(function () {
    angular
        .module("PsychWebApp")
        .controller("ReportController", ReportController);
    

    function ReportController($window, TargetGroupService, UserService, ReportService, serverURL)
    {
    	var vm = this;
        vm.tab = 'participant';
        
        vm.setTab = function (tabId) {
            vm.tab = tabId;
            
            resetTab();
        };
        
        vm.isSet = function (tabId) {
            return vm.tab === tabId;
        };
        
        function resetTab(){
        	vm.reportDataCorrectImageResponses = [];
			vm.reportDataWrongImageResponses = [];
			vm.reportDataCorrectAndIncorrectCount = [];
			vm.loadedReport = false;
			vm.participantsDropDown.selected = "";
			vm.targetGroups.selected = "";
			
			vm.enableReportDownloadLink = false;
			
			vm.showErrorMessage = false;
			
        }
        
        vm.reportConfigCorrectImage = {
        		  "labels": false,
        		  "title": "Report",
        		  "legend": {
        		    "display": true,
        		    "position": "left"
        		  },
        		  "innerRadius": 20,
        		  "lineLegend": "traditional",
        		  "colors" : [ '#803690', '#00ADF9', '#DCDCDC', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360'],
        		  "lineCurveType":"monotone"
        }
        
        vm.reportConfigWrongImage = {
      		  "labels": false,
      		  "title": "Report",
      		  "legend": {
      		    "display": true,
      		    "position": "left"
      		  },
      		  "innerRadius": 0,
      		  "lineLegend": "traditional",
      		  "colors" : [ '#803690', '#00ADF9', '#DCDCDC', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360'],
      		  "lineCurveType":"monotone"
      	}
        
        vm.reportConfigResponseCount = {
			  "labels": false,
			  "title": "Report",
			  "legend": {
			    "display": true,
			    "position": "left"
			  },
			  "innerRadius": 0,
			  "lineLegend": "lineEnd",
			  "colors": ['#DEB887', '#98FB98', '#f7464a']
        }
        
        function loadTargetGroups(){
        	TargetGroupService
        	.getAllTargetGroups()
        	.success(function(response){
        		vm.targetGroups = response.results;
        	});
        }
        
        function initParticipantTab(){
        	loadTargetGroups();
        	
        	UserService
        	.getAllParticipants()
        	.success(function (response){
        		if (response.status == '200'){
        			vm.participantsDropDown = response.results;
        			vm.participants = response.results;
        		}
        		else{
        			vm.participantsDropDown = [];
        			vm.participants = [];
        		}
        		
        	});
        }
        
        initParticipantTab();
        
        vm.filterParticipants = function(selected){
        	vm.participantsDropDown = [];
        	
        	vm.participants.forEach(function (participant){
        		if (participant.targetGroupId == selected.tgId){
        			vm.participantsDropDown.push(clone(participant));
        		}
        	});
        }
        
        vm.loadReportForParticipant = function(selectedParticipant){
        	
        	ReportService
        	.getParticipantReport(selectedParticipant.participantId)
        	.success(function(response){
        		
        		if(response.status == '200'){
        			var len = response.results.avgImageCorrectReponses.sessionCount;
        			
        			if (len == 0){
        				vm.showErrorMessage = true;
        				vm.loadTrainingMessage = selectedParticipant.userName + " has no training sessions recorded.";
        				vm.loadedReport = false;
        			}
        			else{
        				vm.showErrorMessage = false;
        				
	        			if (len > 5){
	            			vm.chartWidth = (len * 140) + 'px';
	        			}
	        			vm.reportConfigCorrectImage.title = 'Average Correct Image Response Time - ' + selectedParticipant.userName;
	        			vm.reportDataCorrectImageResponses = response.results.avgImageCorrectReponses;
	        			vm.reportDataWrongImageResponses = response.results.avgImageWrongReponses;
	        			
	        			vm.reportDataCorrectAndIncorrectCount = response.results.correctAndIncorrectCount;
	        			
	        			vm.reportConfigWrongImage.title = 'Average Incorrect Image Response Time - ' + selectedParticipant.userName;
	        			
	        			
	        			vm.reportConfigResponseCount.title = 'Correct And Incorrect Image Response Count - ' + selectedParticipant.userName;
	        			vm.loadedReport = true;
        			}
        			
        			
        		}
        		else{
        			vm.loadTrainingMessage = "Could not load reports for " + selectedParticipant.userName;
        			vm.showErrorMessage = true;
        			vm.loadedReport = false;
        			vm.reportDataCorrectImageResponses = [];
        			vm.reportDataWrongImageResponses = [];
        			vm.reportDataCorrectAndIncorrectCount = [];
        		}
        		
        	});
        	
        }
        
        vm.getReportForTargetGroup = function(selectedTargetGroup){
        	vm.enableReportDownloadLink = true;
        	vm.imageReportDownloadLink = serverURL.url + "report?targetGroupId=" + selectedTargetGroup.tgId + "&reportType=2";
        	vm.questionReportDownloadLink = serverURL.url + "report?targetGroupId=" + selectedTargetGroup.tgId + "&reportType=3";
        }
        
    }

})();