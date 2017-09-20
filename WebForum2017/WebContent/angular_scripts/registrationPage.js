var app = angular.module('app', ['ngStorage','ngCookies', 'ngRoute', 'ngAnimate', 'ui.bootstrap', 'ngMaterial', 'ngFileUpload']);

app.run(['$cookies','$window',function($cookies, $window){
	if($cookies.get("activeUser") == null || $cookies.get("activeUser") == undefined){
		$window.location.href = "localhost:8080/WebForum2017/#/";
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



app.controller('registrationPageController', function ($localStorage, $rootScope,$scope, usersFactory) {
	
$scope.register = function (user) {
	
	    
	    if(user && user.username && user.password && user.firstName && user.lastName && user.contactPhone && user.email && user.dateOfRegistration){
	    	if(user.username.trim() != "" && user.password.trim()!="" && user.firstName.trim()!="" && user.lastName.trim()!="" && user.contactPhone.trim()!="" && user.email.trim()!="" && user.dateOfRegistration.trim()!=""){
	    		
	    		user.role = "USER";
	    		
			    usersFactory.addUser(user).success(function(data){
			    	if(data){
			    		toastr.success("You registered successfully!");	
			    		
			    	}else{
			    		toastr.warning("This user already exists!");
			    	}
			    });
	    	}else{
	    		toastr.warning("Please enter valid data");
	    	}
	    }else{
	    	toastr.warning("Please enter valid data");
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
		$location.path('/register');
	}
	
});

app.controller('userController',function($localStorage,$cookies, $rootScope, $scope, $location, usersFactory, messagesFactory, subforumsFactory){

	
	$scope.activeUser = $cookies.getObject("activeUser");
	$scope.message = {receiver : "", sender : $scope.activeUser.username, seen : false, content : ""};
	$scope.inbox = false;
	$scope.editUserRole = false;
	$scope.createForum = false;
	$scope.userHome = false;
	$scope.followedForum = false;
	$scope.search = false;
			
	$scope.messages = [];
	
	$scope.users = [];
	
	$scope.goToInbox = function(){
		$scope.inbox = true;
		$scope.editUserRole = false;
		$scope.createForum = false;
		$scope.userHome = false;
		$scope.followedForum = false;
		$scope.search = false;
	}
	
	$scope.goToUserRole = function(){
		$scope.editUserRole = true;
		$scope.inbox = false;
		$scope.createForum = false;
		$scope.userHome = false;
		$scope.followedForum = false;
		$scope.search = false;
	}
	
	$scope.goToCreateForum = function(){
		$scope.editUserRole = false;
		$scope.inbox = false;
		$scope.createForum = true;
		$scope.userHome = false;
		$scope.followedForum = false;
		$scope.search = false;
		
		subforumsFactory.getFollowSubforum().success(function(data){
			console.log(data);
			$scope.followedSubforum = data;
		});
	}
	
	$scope.subforums = [];
	
	$scope.goToUserHome = function(){
		$scope.userHome = true;
		$scope.editUserRole = false;
		$scope.inbox = false;
		$scope.createForum = false;
		$scope.followedForum = false;
		$scope.search = false;
		
		subforumsFactory.getSubforum().success(function(data){
			console.log(data);
			$scope.subforums = data;
		});
		
	}
	
	$scope.followedSubforum = [];
	
	
	$scope.goToSearch = function(){
		$scope.userHome = false;
		$scope.editUserRole = false;
		$scope.inbox = false;
		$scope.createForum = false;
		$scope.followedForum = false;
		$scope.search = true;
	}
	
	$scope.logout = function() {
		$cookies.remove("activeUser");
		$location.path('/');
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
		if(!$scope.message.receiver){
			toastr.warning('Please enter receiver username.');
		}else if(!$scope.message.content){
			toastr.warning('Please enter content of message.');
		}else{			
			if($scope.message.receiver == $scope.activeUser.username){
				$scope.messages.push($scope.message);
				}
			messagesFactory.sendMessage($scope.message).success(function(data){
				if( data == "Message sent"){
					toastr.success(data);
					$scope.message.receiver = null;
					$scope.message.content = null;
				}
				else{
					toastr.warning(data);
				}
				
			});
		}
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
	
	$scope.userSearch = {
			username : ""
	}
	
	
	
	
	$scope.searchUsers = function() {
		if($scope.username == "" || $scope.username == null){
			toastr.info("Enter username");
		}else{
		usersFactory.searchUsers($scope.username).success(function(data) {
			$scope.users = data;
			console.log(data);
		});
		}
	}
	
	
	
});

app.controller('subforumController',function($localStorage,$cookies, $rootScope, $scope, $filter ,$location, usersFactory, subforumsFactory, themesFactory, commentsFactory ,$filter){

	console.log("evo me u subForumController-u");
	
	$scope.showSubforum = false;
	$scope.followedSubforum = [];
	$scope.subforums = [];
	$scope.followButton = false;
	$scope.saveThemeButton = false;
	$scope.likeThemeButton = false;
	$scope.dislikeThemeButton = false;
	$scope.subforumDetails = false;

	
	$scope.activeUser = $cookies.getObject("activeUser");
	$scope.subforum =  {name: null, description: null, icon:null, moderators: null, listOfRules : null, responsibleModerator : $scope.activeUser.username };
	$scope.activeSubforum = null;
	$scope.subforumDetail = null;
	$scope.themeDetail = null;
	
	$scope.comments = [];
	$scope.savedComments = [];
	
	var datum = new Date();
	var noviDatum = $filter('date')(datum, "dd-MM-yyyy HH:mm");
	$scope.newTheme = { "themesSubforum" : $scope.subforumDetail, "name" : null, "type" : null, "author" : $scope.activeUser.username, 
			"listOfComments" : [], "content" : null, "dateOfCreating" : noviDatum, "like" : 0, "dislike" : 0 , "usersLiked" : [], "usersDisliked" : []}
	$scope.newComment = { "theme" : $scope.themeDetail, "author" : $scope.activeUser.username, "creatingDate" : noviDatum, "parent" : null,
			"children" : [], "text" : null, "likes" : 0, "dislikes" : 0, "changed" : false, "deleted" : false, "usersLiked" : [], "usersDisliked" : []}
	$scope.newSubcomment = { "theme" : $scope.themeDetail, "author" : $scope.activeUser.username, "creatingDate" : noviDatum, "parent" : null,
			"children" : [], "text" : null, "likes" : 0, "dislikes" : 0, "changed" : false, "deleted" : false, "usersLiked" : [], "usersDisliked" : []}
	$scope.themes =  [];
	$scope.savedTheme = [];
	$scope.likedTheme = [];
	$scope.dislikedTheme = [];
	$scope.activeTheme = null;
	
	
	//SEARCH SUBFORUM
	
	$scope.searchSubforums = function() {
		if($scope.name == null || $scope.name == ""){
			toastr.info("Enter name of subforum");
		}else{
		subforumsFactory.searchSubforums($scope.name).success(function(data) {
			$scope.subforums = data;
			console.log(data);
		});
		}
	}
	
	$scope.searchThemes = function() {
		if($scope.name == null || $scope.name == ""){
			toastr.info("Enter name of theme");
		}else{
		themesFactory.searchThemes($scope.name).success(function(data) {
			$scope.themes = data;
		});
		}
	}
	
	//more about subforum
	
	$scope.more = function(s){
		$scope.subforumDetails = true;
		$scope.subforumDetail = s;
		themesFactory.getTheme(s.name).success(function(data){
			$scope.themes = data;
		});
		themesFactory.getSavedTheme($scope.activeUser.username).success(function(data){
			$scope.savedThemes = data;
		});
		$scope.themeDetails = false;
	}
	


	subforumsFactory.getSubforum().success(function(data){
		console.log(data);
		$scope.subforums = data;
	});
	
	subforumsFactory.getFollowSubforum($scope.activeUser.username).success(function(data){
		$scope.followedSubforum = data;
		console.log(data);
	});
	
	$scope.removeSubforum = function(s){
		console.log("removeSubforum angular fja");
		$scope.prom = s;
	}
	
	//PRACENJE FORUMA
	
	$scope.followSubforum = function(s){
		$scope.isFollow = false;
		for(i = 0; i < $scope.followedSubforum.length; i++){
			if($scope.followedSubforum[i].name == s.name){
				$scope.isFollow = true;
			}
		}
		if(!$scope.isFollow){
			$scope.followedSubforum.push(s);
			subforumsFactory.followSubforum($scope.activeUser.username, s).success(function(data){
				toastr.info(data);
			});
		}else{
			toastr.warning('You alredy follow this forum.');
		}
	}
	
	//BRISANJE FORUMA
	
	$scope.deleteSubforum = function(){
		subforumsFactory.deleteSubforum($scope.prom);
		for(i=0; i<$scope.subforums.length; i++){
			if($scope.subforums[i].name == $scope.prom.name){
				$scope.subforums.splice(i,1);
			}
		}
		toastr.success("Deleted subforum.");
	}
	  
	 $scope.setFileEventListener = function(element) {
		 $scope.uploadedFile= element.files[0];
	 } 
	 
	 //KREIRANJE FORUMA
	 
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
					   		$scope.goToUserHome();
					   	}else{
					   		toastr.info("This subforum already exists!");
					   	}
					   });
		 		}
		 $scope.subforum.name = null;
		 $scope.subforum.description = null;
	 
	 }
	 
	 //COMMENT 
		$scope.openTheme = function(theme){
			$scope.themeDetails = true;
			$scope.themeDetail = theme;
			commentsFactory.getComments(theme.name).success(function(data){
				$scope.comments = data;
			});
			commentsFactory.getSavedComments($scope.activeUser.username).success(function(data){
				$scope.savedComments = data;
			});
		}
		$scope.createNewComment = function(){
			if(!$scope.newCommentText){
				toastr.warning('Please enter text of comment');
			}else{
				$scope.newComment.theme = $scope.themeDetail;
				$scope.newComment.text = $scope.newCommentText;
				commentsFactory.addComment($scope.newComment).success(function(data){
					if(data == "Theme commented"){
						toastr.warning(data);
						$scope.comments.push($scope.newComment);
						$scope.newCommentText = null;
						$scope.newComment.children = [];
						$scope.newComment.theme = null;
					}
				});
				$scope.openTheme($scope.themeDetail);
			}
		}
		$scope.subcomment = {};
		$scope.createNewSubcomment = function(comment){
			if(comment.deleted){
				toastr.warning('You cannot reply on deleted comment');
				return;
			}
			if(!$scope.subcomment.com){
				toastr.warning('Please enter text of subcomment');
			}else{
				$scope.newSubcomment.theme = $scope.themeDetail;
				$scope.newSubcomment.text = $scope.subcomment.com;
				for( i = 0; i < $scope.comments.length; i++){
					if($scope.comments[i].text == comment.text){
						if($scope.comments[i].children == null)
							$scope.comments[i].children = [];
						$scope.comments[i].children.push($scope.newSubcomment);
						commentsFactory.addSubcomment($scope.comments[i]).success(function(data){
							toastr.warning(data);
						});
						$scope.subcomment.com = null;
					}
				}
			}
		}
		$scope.editComment = function(comment){
			var date = new Date();
			var newDate = $filter('date')(date, "dd-MM-yyyy HH:mm");
			$scope.editCommentModal = { "theme" : comment.theme, "author" : comment.author, "creatingDate" : newDate, "parent" : comment.parent,
					"children" : comment.children, "text" : comment.text, "likes" : comment.likes, "dislikes" : comment.dislikes, "changed" : comment.changed, "deleted" : comment.deleted}
			$scope.oldCom = comment.text;
		}
		$scope.cancelEditComment = function(){
			$scope.editCommentModal = null;
		}
		$scope.saveEditComment = function(){
			if($scope.editCommentModal.deleted){
				toastr.warning('You cannot edit deleted comment');
				return;
			}
			if($scope.activeUser.username == $scope.editCommentModal.theme.subforum.responibleModerator){
				console.log("responsible");
				commentsFactory.editComment($scope.editCommentModal, $scope.oldCom).success(function(data){
					toastr.warning(data);
					for(i = 0; i < $scope.comments.length; i++){
						if($scope.comments[i].text == $scope.oldCom){
							$scope.comments[i].text = $scope.editCommentModal.text;
							$scope.comments[i].creatingDate = $scope.editCommentModal.creatingDate;
							$scope.comments[i].changed = $scope.editCommentModal.changed;
						}
						for(j = 0; j < $scope.comments[i].children.length; j++){
							if($scope.comments[i].children[j].text == $scope.oldCom){
								$scope.comments[i].children[j].text = $scope.editCommentModal.text;
								$scope.comments[i].children[j].creatingDate = $scope.editCommentModal.creatingDate;
								$scope.comments[i].children[j].changed = $scope.editCommentModal.changed;
							}
						}
					}
				});
			}else if($scope.activeUser.username == $scope.editCommentModal.author){
				console.log("ahutor");
				$scope.editCommentModal.changed = true;
				commentsFactory.editComment($scope.editCommentModal, $scope.oldCom).success(function(data){
					toastr.warning(data);
					for(i = 0; i < $scope.comments.length; i++){
						if($scope.comments[i].text == $scope.oldCom){
							$scope.comments[i].text = $scope.editCommentModal.text;
							$scope.comments[i].creatingDate = $scope.editCommentModal.creatingDate;
							$scope.comments[i].changed = $scope.editCommentModal.changed;
						}
						for(j = 0; j < $scope.comments[i].children.length; j++){
							if($scope.comments[i].children[j].text == $scope.oldCom){
								$scope.comments[i].children[j].text = $scope.editCommentModal.text;
								$scope.comments[i].children[j].creatingDate = $scope.editCommentModal.creatingDate;
								$scope.comments[i].children[j].changed = $scope.editCommentModal.changed;
							}
						}
					}
				});
			}else{
				toastr.warning('You cannot edit comment');
			}
		}
		$scope.deleteComment = function(comment){
			if(comment.deleted){
				toastr.warning('Comment is already deleted');
				return;
			}
			if($scope.isAdministrator()){
				console.log("admin");
				commentsFactory.deleteComment(comment).success(function(data){
					toastr.warning(data);
					for(i = 0; i < $scope.comments.length; i++){
						if($scope.comments[i].text == comment.text){
							$scope.comments[i].deleted = true;
							for(j = 0; j < $scope.comments[i].children.length; j++)
								$scope.comments[i].children[j].deleted = true;
						}else{
							for(j = 0; j < $scope.comments[i].children.length; j++){
								if($scope.comments[i].children[j].text == comment.text)
									$scope.comments[i].children[j].deleted = true;
							}
						}
					}
				});
			}else if($scope.activeUser.username == comment.theme.subforum.responibleModerator){
				console.log("responsibleModerator");
				commentsFactory.deleteComment(comment).success(function(data){
					toastr.warning(data);
					for(i = 0; i < $scope.comments.length; i++){
						if($scope.comments[i].text == comment.text){
							$scope.comments[i].deleted = true;
							for(j = 0; j < $scope.comments[i].children.length; j++)
								$scope.comments[i].children[j].deleted = true;
						}else{
							for(j = 0; j < $scope.comments[i].children.length; j++){
								if($scope.comments[i].children[j].text == comment.text)
									$scope.comments[i].children[j].deleted = true;
							}
						}
					}
				});
			}else if($scope.activeUser.username == comment.author){
				console.log("author");
				commentsFactory.deleteComment(comment).success(function(data){
					toastr.warning(data);
					for(i = 0; i < $scope.comments.length; i++){
						if($scope.comments[i].text == comment.text){
							$scope.comments[i].deleted = true;
							for(j = 0; j < $scope.comments[i].children.length; j++)
								$scope.comments[i].children[j].deleted = true;
						}else{
							for(j = 0; j < $scope.comments[i].children.length; j++){
								if($scope.comments[i].children[j].text == comment.text)
									$scope.comments[i].children[j].deleted = true;
							}
						}
					}
				});
			}else{
				
			}
		}
		$scope.saveComment = function(comment){
			$scope.isSaved = false;
			for(i = 0; i < $scope.savedComments.length; i++){
				if($scope.savedComments[i].text == comment.text)
					$scope.isSaved = true;
			}
			if(!$scope.isSaved){
				$scope.savedComments.push(comment);
				for(i = 0; i < $scope.comments.length; i++){
					if($scope.comments[i].text == comment.text){
						commentsFactory.saveComment($scope.activeUser.username, $scope.comments[i]).success(function(data){
							toastr.warning(data);
						});
					}else{
						for(j = 0; j < $scope.comments[i].children.length; j++){
							if($scope.comments[i].children[j].text == comment.text){
								commentsFactory.saveComment($scope.activeUser.username, $scope.comments[i].children[j]).success(function(data){
									toastr.warning(data);
								});
							}
						}
					}
				}
			}else{
				toastr.warning('You already saved this comment');
			}
		}
		$scope.likeComment = function(comment){
			commentsFactory.likeComment($scope.activeUser.username, comment).success(function(data){
				if(data == "Comment liked"){
					for(i = 0; i < $scope.comments.length; i++){
						if($scope.comments[i].text == comment.text){
							$scope.comments[i].likes++;
							$scope.comments[i].usersLiked.push($scope.activeUser.username);
						}else{
							for(j = 0; j < $scope.comments[i].children.length; j++){
								if($scope.comments[i].children[j].text == comment.text){
									$scope.comments[i].children[j].likes++;
									$scope.comments[i].children[j].usersLiked.push($scope.activeUser.username);
								}
							}
						}
					}
					toastr.warning(data);
				}else{
					toastr.warning(data);
				}
			});
		}
		$scope.dislikeComment = function(comment){
			commentsFactory.dislikeComment($scope.activeUser.username, comment).success(function(data){
				if(data == "Comment disliked"){
					for(i = 0; i < $scope.comments.length; i++){
						if($scope.comments[i].text == comment.text){
							$scope.comments[i].dislikes++;
							$scope.comments[i].usersDisliked.push($scope.activeUser.username);
						}else{
							for(j = 0; j < $scope.comments[i].children.length; j++){
								if($scope.comments[i].children[j].text == comment.text){
									$scope.comments[i].children[j].dislikes++;
									$scope.comments[i].children[j].usersDisliked.push($scope.activeUser.username);
								}
							}
						}
					}
					toastr.warning(data);
				}else{
					toastr.warning(data);
				}
			});
		}

	 
	 //KREIRANJE TEME
	 
	$scope.createTheme = function(){
		if(!$scope.theme.name){
			toastr.warning("Please enter theme's name");
		}
		else if(!$scope.theme.type){
			toastr.warning("Please choose theme's type");
		}
		else if(!$scope.theme.content){
			toastr.warning("Please enter theme's content");
		}
		else{
			var daTe = new Date();
			var newDaTe = $filter('date')(daTe, "dd-MM-yyyy HH:mm");
			$scope.theme.dateOfCreating = newDaTe;
			$scope.theme.themesSubforum = $scope.subforumDetail;
			themesFactory.createTheme($scope.theme).success(function(data){
			   	if(data == "Registered theme"){
			   		toastr.success(data);
			   		$scope.themes.push($scope.theme);
			   	}else{
			   		toastr.warning(data);
			   	}
		    });
		}
	}
	
	$scope.cancelNewTheme = function(){
		themesFactory.getTheme($scope.subforumDetail.name).success(function(data){
			$scope.themes = data;
		});
		$scope.theme.name = null;
		$scope.theme.type = null;
		$scope.theme.content = null;
	}
	
	//BRISANJE TEMA
	
	$scope.removeTheme = function(t){
		$scope.promjenjiva = t;
		console.log($scope.promjenjiva);
	}
	
	$scope.deleteTheme = function(){
		themesFactory.deleteTheme($scope.promjenjiva).success(function(data){
			toastr.warning(data);
		for(i=0; i<$scope.themes.length; i++){
			if($scope.themes[i].name == $scope.promjenjiva.name){
				$scope.themes.splice(i,1);
			}
		}
		});
		toastr.success("Deleted theme.");
	}
	
	
	//SNIMANJE TEME
	
	$scope.saveTheme = function(t){
		$scope.p = false;
		for(i=0; i<$scope.savedTheme.length; i++){
			if( $scope.savedTheme[i].name == t.name){
				$scope.p = true;
			}
		}
		if(!$scope.p){
			$scope.savedThemes.push(t);
			for(i = 0; i < $scope.themes.length; i++){
				if($scope.themes[i].name == t.name){
					themesFactory.saveTheme($scope.activeUser.username, $scope.themes[i]).success(function(data){
						toastr.success(data);
					});
				}
			}
		}else{
				toastr.warning("You are alredy saved theme.");
			}
		
	}
	
	//LAJKOVANJE I DISLAJKOVANJE TEMA
	
	$scope.likeTheme = function(theme){
		themesFactory.likeTheme($scope.activeUser.username, theme).success(function(data){
			if(data == "Theme liked"){
				for(i = 0; i < $scope.themes.length; i++){
					if($scope.themes[i].name == theme.name){
						$scope.themes[i].like++;
						$scope.themes[i].usersLiked.push($scope.activeUser.username);
					}
				}
				toastr.info(data);
			}else{
				toastr.info(data);
			}
		});
	}
	$scope.dislikeTheme = function(theme){
		themesFactory.dislikeTheme($scope.activeUser.username, theme).success(function(data){
			if(data == "Theme disliked"){
				for(i = 0; i < $scope.themes.length; i++){
					if($scope.themes[i].name == theme.name){
						$scope.themes[i].dislike++;
						$scope.themes[i].usersDisliked.push($scope.activeUser.username);
					}
				}
				toastr.info(data);
			}else{
				toastr.info(data);
			}
		});
	}
	  
	
	themesFactory.getTheme().success(function(data){
		console.log(data);
		$scope.themes = data;
	});
	

	
	//IZMJENA TEME
	
	$scope.editT = function(t){
		$scope.name = t.name;
		var daTe = new Date();
		var newDate = $filter('date')(daTe, "dd-MM-yyyy HH:mm");
		
		$scope.theme = {themesSubforum : $scope.subforumDetail , name : "", type : "", author : $scope.activeUser.username, listOfComments: [], content : t.content , dateOfCreating : newDate, like : 0 , dislike : 0, "usersLiked" : [], "usersDisliked" : []};
	}
	
	$scope.editTheme = function(name, content){
		themesFactory.editTheme(name, content).success(function(data){			
		});
		toastr.success("Theme edited!")
		
		for(i=0; i<$scope.themes.length; i++){
			if($scope.themes[i].name == name){
				$scope.themes[i].content = content;
			}
		}
		
	}
	
	 
});