<!DOCTYPE html>
<html lang="en" ng-app="PsychWebApp">

<head>
<%@ page isThreadSafe="false"%>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>Admin Controller</title>

<!-- Custom CSS -->


<!-- Bootstrap Core CSS -->
<link href="./css/bootstrap.min.css" rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/select2/3.4.5/select2.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/selectize.js/0.8.5/css/selectize.default.css">
<link href="./css/sb-admin.css" rel="stylesheet">
<link href="./css/locations.css" rel="stylesheet">


<!-- Custom Fonts -->
<link href="./font-awesome/css/font-awesome.min.css" rel="stylesheet"
	type="text/css">

<!-- Angular JS library from Google CDN -->
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.5/angular.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.5/angular-sanitize.js"></script>
<!-- Angular JS routing CDN -->
<script src="https://code.angularjs.org/1.4.5/angular-route.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/angular-messages/1.4.5/angular-messages.js"></script>
<script data-require="d3@*" data-semver="3.3.11"
	src="//cdnjs.cloudflare.com/ajax/libs/d3/3.3.11/d3.js"></script>

<script src="./js/select.js"></script>
<script src="./js/angular-charts.min.js"></script>
<script src="./js/defiant.min.js"></script>
<link rel="stylesheet" href="./css/select.css">

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/select2/3.4.5/select2.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/selectize.js/0.8.5/css/selectize.default.css">
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->

<script src="./js/utils.js"></script>
<script src="./app.js"></script>
<script src="./config.js"></script>
<script src="./views/main.controller.js"></script>
<script src="./services/fieldlookup.service.client.js"></script>
<script src="./services/user.service.client.js"></script>
<script src="./services/location.service.client.js"></script>
<script src="./services/questionManagement.service.client.js"></script>
<script src="./services/targetGroup.service.client.js"></script>
<script src="./services/training.service.client.js"></script>
<script src="./services/imageManagement.service.client.js"></script>
<script src="./services/training.service.client.js"></script>
<script src="./services/report.service.client.js"></script>
<script src="./views/login.controller.js"></script>
<script src="./views/adminProfile.controller.js"></script>
<script src="./views/location.controller.js"></script>
<script src="./views/targetGroup.controller.js"></script>
<script src="./views/navigation.controller.js"></script>
<script src="./views/questionManagement.controller.js"></script>
<script src="./views/imageManagement.controller.js"></script>
<script src="./views/training.controller.js"></script>
<script src="./views/report.controller.js"></script>

<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <script src="../js/locationTabs.js"></script>
    <![endif]-->

</head>

<body ng-controller="MainController">

	<div ng-show="currentUser" ng-include="'./views/navigation.view.html'">

	</div>

	<div id="page-wrapper">
		<div class="container-fluid">
			<div class="row">

				<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main"
					ng-view></div>
				<!-- /.container-fluid -->
			</div>
		</div>
	</div>
	<!-- /#page-wrapper -->

	<!-- /#wrapper -->

	<!-- jQuery -->
	<!--<script src="../js/jquery.js"></script>

    <!-- Bootstrap Core JavaScript -->
	<!-- <script src="../js/bootstrap.min.js"></script>-->


</body>

</html>
