var app = angular.module('app', ['ngStorage','ngCookies', 'ngRoute', 'ngAnimate', 'ui.bootstrap', 'ngMaterial', 'ngFileUpload']);

app.run(['$cookies','$window',function($cookies, $window){
	if($cookies.get("activeUser") == null || $cookies.get("activeUser") == undefined){
		$window.location.href = "localhost:8080/WebForum2017/";
	}else{
		user = JSON.parse($cookies.get("activeUser"));
		console.log(user);
			$window.location.href = "localhost:8080/WebForum2017/#/user";
		
	}
}]);

app.config(function($routeProvider){
	$routeProvider.when('/',
			{
				templateUrl : 'homePage.html'
			}).when('/user',
			{
				templateUrl : 'userPage.html'
			}).when('/login',
			{	
				templateUrl : 'loginPage.html'
			}).when('/register',
			{
				templateUrl : 'registrationPage.html'
			});
	});

app.config(function($mdThemingProvider){
	$mdThemingProvider.theme('default');
});

app.config(function($logProvider){
	$logProvider.debugEnabled(true);
});


app.controller('registrationPageController', function ($localStorage, $rootScope,$scope, usersFactory) {
	
$scope.register = function (user) {
	
	    
	    if(user && user.username && user.password && user.firstName && user.lastName && user.contactPhone && user.email && user.dateOfRegistration){
	    	if(user.username.trim() != "" && user.password.trim()!="" && user.firstName.trim()!="" && user.lastName.trim()!="" && user.contactPhone.trim()!="" && user.email.trim()!="" && user.dateOfRegistration.trim()!=""){
	    		
	    		user.role = "USER";
	    		
			    usersFactory.addUser(user).success(function(data){
			    	if(data){
			    		toastr.info("This user already exists!");
			    	}else{
			    		toastr.info("You registered successfully!");
			    	}
			    });
	    	}else{
	    		toast("Please enter valid data");
	    	}
	    }else{
	    	toast("Please enter valid data");
	    }
	    
	  };
	  
});

app.controller('loginController',function($localStorage,$cookies, $rootScope, $scope, $location, usersFactory){
	
	$scope.login = function(user){
		
		
		usersFactory.getUser(user).success(function(data){
			if(data){
				var logged = data;
				$cookies.putObject("activeUser", logged);
				$localStorage.loggedUser = logged.username;
				if(logged.role == 'USER'){
					$location.path('/user');
				}else if(logged.role == 'ADMINISTRATOR'){
					$location.path('/user');
				}else{
					$location.path('/manager');
				}
				
			}else{
				toastr.warning('Wrong username or password!');
			}
		});
	};
	
	$scope.goToLogin = function(){
		$location.path('/login');
	}
	
	$scope.goToRegister = function(){
		$location.path('register');
	}
	
});

app.controller('userController',function($localStorage,$cookies, $rootScope, $scope, $location, usersFactory, messagesFactory){

	
	$scope.activeUser = $cookies.getObject("activeUser");
	$scope.message = {receiver : "", sender : $scope.activeUser.username, seen : false, content : ""};
	$scope.inbox = false;
	$scope.editUserRole = false;
	$scope.createForum = false;
			
	
	$scope.messages = [];
	
	$scope.users = [];
	
	$scope.goToInbox = function(){
		$scope.inbox = true;
		$scope.editUserRole = false;
		$scope.createForum = false;
	}
	
	$scope.goToUserRole = function(){
		$scope.editUserRole = true;
		$scope.inbox = false;
		$scope.createForum = false;
	}
	
	$scope.goToCreateForum = function(){
		$scope.editUserRole = false;
		$scope.inbox = false;
		$scope.createForum = true;
	}
	
	$scope.editUser = function(u){
		$scope.username = u.username;
		$scope.user = {username:"", password:"", firstName:"", lastName:"", role : u.role, contactPhone:"", email:"", dateOfRegistration:"" };
	}
	
	$scope.editUserRole = function(username, role){
		usersFactory.editUserRole(username, role).success(function(data){			
		});
		toastr.success("User role changed!")
	}
	

	usersFactory.getUsers($scope.activeUser.username).success(function(data){
		console.log(data);
		$scope.users = data;
	})
	
	$scope.showMessage = function(message){
		message.seen = true;
	}
	
	$scope.sendMessage = function(){
		messagesFactory.sendMessage($scope.message);
		toastr.success('Message has been sent!');
	}
	
	$scope.replyMessage = function(){
		$scope.repliedMessage = { receiver : $scope.sender, sender : $scope.activeUser.username, seen : false, content : $scope.replyContent}
		console.log($scope.repliedMessage);
		messagesFactory.sendMessage($scope.repliedMessage);
		toastr.success("Replied message!")
	}
	
	$scope.readMessage = function(m){
		messagesFactory.readMessage(m);
		$scope.sender = m.sender;
		$scope.content = m.content;
	}
	
	messagesFactory.getMessage($scope.activeUser.username).success(function(data){
		console.log(data);
		$scope.messages = data;
	})
	
	$scope.logout = function(){
		$cookies.remove("activeUser");
		$location.path('/login');
		toastr.info('User logged out!');
	}

	$scope.isAdmin = function(){
		if($scope.activeUser.role == 'ADMINISTRATOR'){
			return true;
		}
		else 
			return false;
	}
	
	$scope.isUser = function(){
		if($scope.activeUser.role == 'USER'){
			return true;
		}
		else
			return false;
	}
	
	
});

	
