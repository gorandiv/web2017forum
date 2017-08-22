app.factory('usersFactory', function($http){
	
	
	var factory = {};
	
	factory.getUser = function(user) {
		return $http.post('/WebForum2017/rest/users/getUser', {"username":user.username, "password":user.password});
	};
	
	factory.addUser = function(user){
		return $http.post('/WebForum2017/rest/users/addUser', {"username":user.username, "password":user.password,
														     "firstName":user.firstName, "lastName":user.lastName,
														     "role":user.role, "contactPhone":user.contactPhone,
														     "email":user.email, "dateOfRegistration":user.dateOfRegistration});
	}
	
	factory.getUsers = function(username){
		return $http.get('/WebForum2017/rest/users/getUsers/' + username);
		
	}
	
	factory.editUserRole = function(username, role){
		console.log("usao sam u users factory, edit user role metoda");
		return $http.post('/WebForum2017/rest/users/editUserRole/' + username + '/' + role);
	}
	
	
	
	return factory;
});

app.factory('messagesFactory', function($http){
	var factory = {};
	
	factory.sendMessage = function(message) {
		return $http.post('/WebForum2017/rest/messages/sendMessage', {"receiver":message.receiver, "sender":message.sender, 
			"seen" : message.seen, "content" : message.content});
	}
	
	factory.getMessage = function(username){
		return $http.get('/WebForum2017/rest/messages/getMessage/' + username);
	}
	
	factory.readMessage = function(message){
		return $http.post('/WebForum2017/rest/messages/readMessage', {"receiver":message.receiver, "sender":message.sender, 
			"seen" : message.seen, "content" : message.content});
	}
		
	return factory;
});