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
	$scope.userHome = false;
			
	
	$scope.messages = [];
	
	$scope.users = [];
	
	$scope.goToInbox = function(){
		$scope.inbox = true;
		$scope.editUserRole = false;
		$scope.createForum = false;
		$scope.userHome = false;
	}
	
	$scope.goToUserRole = function(){
		$scope.editUserRole = true;
		$scope.inbox = false;
		$scope.createForum = false;
		$scope.userHome = false;
	}
	
	$scope.goToCreateForum = function(){
		$scope.editUserRole = false;
		$scope.inbox = false;
		$scope.createForum = true;
		$scope.userHome = false;
	}
	
	$scope.goToUserHome = function(){
		$scope.userHome = true;
		$scope.editUserRole = false;
		$scope.inbox = false;
		$scope.createForum = false;
	}
	
	$scope.editUser = function(u){
		$scope.username = u.username;
		$scope.user = {username:"", password:"", firstName:"", lastName:"", role : u.role, contactPhone:"", email:"", dateOfRegistration:"" };
	}
	
	$scope.editUserRole = function(username, role){
		usersFactory.editUserRole(username, role).success(function(data){			
		});
		toastr.success("User role changed!")
		
		for(i=0; i<$scope.users.length; i++){
			if($scope.users[i].username == username){
				$scope.users[i].role = role;
			}
		}
		
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

app.controller('subforumController',function($localStorage,$cookies, $rootScope, $scope, $location, usersFactory, subforumsFactory){

	console.log("evo me u subForumController-u");
	
	$scope.subforums = [];
	
	$scope.activeUser = $cookies.getObject("activeUser");
	$scope.subforum =  {name: null, description: null, icon:null, moderators: null, listOfRules : null, responsibleModerator : $scope.activeUser.username };
	
	subforumsFactory.getSubforum().success(function(data){
		console.log(data);
		$scope.subforums = data;
	});
	
	$scope.removeSubforum = function(s){
		console.log("removeSubforum angular fja");
		$scope.prom = s;
	}
	
	$scope.deleteSubforum = function(){
		subforumsFactory.deleteSubforum($scope.prom);
		for(i=0; i<$scope.subforums.length; i++){
			if($scope.subforums[i].name == $scope.prom.name){
				$scope.subforums.splice(i,1);
			}
		}
		toastr.success("Deleted subforum.");
	}
	
	$scope.createSubforum = function () {
		subforumsFactory.addSubforum($scope.subforum).success(function(data){
		   	if(data == "Registered subforum"){
		   		toastr.info("This subforum already exists!");
		   	}else{
		   		toastr.info("You created subforum successfully!");
		   	}
		   });
	    
	  };
	  
	 $scope.setFileEventListener = function(element) {
		 $scope.uploadedFile= element.files[0];
	 } 
	 
	 $scope.createSubforum = function() {
		 createSubforum();
		 
	 }
	 
	 function createSubforum(){
		 if(!$scope.uploadedFile){
			 return;
		 }
		 else if(!$scope.subforum.name){
			 toastr.warning("You need to input subforum's name.");
		 }
		 else if(!$scope.subforum.description){
			 toastr.warning("You need to input subforum's description");
		 }
		 else {
					subforumsFactory.addSubforum($scope.subforum, $scope.uploadedFile).success(function(data){
					   	if(data == "Registered subforum"){
					   		toastr.info("You created subforum successfully!");
					   	}else{
					   		toastr.info("This subforum already exists!");
					   	}
					   });
		 		}
		 $scope.subforum.name = null;
		 $scope.subforum.description = null;
		 
	 
	 }
	 
	 	
	 
	 
	 
});