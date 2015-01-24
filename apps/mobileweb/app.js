/*
 * Copyright 2014 IBM Corp. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Module dependencies
 */
var express = require('express'),
    namespace = require('express-namespace'),
    ibmbluemix = require('ibmbluemix'),
    ibmdata = require('ibmdata'),
	bodyparser = require('body-parser');

//extract application data from bluelist.json
var fs = require('fs');
var appConfig = JSON.parse(fs.readFileSync('public/bluelist.json', 'utf8'));


//initialize the SDK
ibmbluemix.initialize(appConfig); 
var logger = ibmbluemix.getLogger();

//initialize ibmconfig module
var ibmconfig = ibmbluemix.getConfig();

//create an express app
var app = express();
app.use(bodyparser.json());
app.use(bodyparser.urlencoded({
  extended: true
}));

//initialize ibmdata service sdks 
app.use(function(req, res, next) {
    req.data = ibmdata.initializeService(req);
    req.logger = logger;
    next();
});

//get context root to deploy your application
//the context root is '${appHostName}/v1/apps/${applicationId}'
//map the context root to the app namespace
var contextRoot = ibmconfig.getContextRoot();
var appContext = express.Router();
app.use(contextRoot, appContext);

console.log("contextRoot: " + contextRoot);

// log all requests
app.all('*', function(req, res, next) {
    console.log("Received request to " + req.url);
    next();
});

//Create resource URIs for the mbaas Context Route
appContext.get('/items', function(req, res) {

	// Retrieve a Query instance of type "Item" and issue a find() action on it 
	// to retrieve all the items (NO PAGING)
	var query = req.data.Query.ofType("Item");
	query.find().done(function(items) {
		res.send(items);
	},function(err){
		res.status(500);
		res.send(err);
	});
});

// Retrieve a single Item using an id
appContext.get('/item/:id', function(req, res) {

	// Using the Data SDK create a query and pass in a search parameter
	var query = req.data.Query.ofType("Item");
	query.find({
		id: req.params.id
	}, {
		limit: 1
	}).done(function(item) {
		if (item.length == 1) {
			res.send(item);
		} else {
			res.status(404);
			res.send("No such item found");
		}
	});
});

// Create a new Item using the payload passed 
appContext.post('/item', function(req, res) {

	// Create a new Item instance and then save it to the cloud
	var item = req.data.Object.ofType("Item", req.body);
	item.save().then(function(saved) {
		res.send(saved);
	},function(err) {
		res.status(500);
		res.send(err);
	});

});

// Update an existing Item
appContext.put('/item/:id', function(req, res) {
	//Get the object with the given id
	req.data.Object.withId(req.params.id)
	.then(function(item) {
		// Update the Contents of the Object
		item.set(req.body);

		// Save the updated items
		return item.save();
	}).done(function(saved) {
		res.send(saved);
	},function(err){
		console.error("error: ");
		res.send(500, err);
	});        
});

// Delete the Item using a unique id
appContext.delete('/item/:id', function(req, res) {
	//Get the object with the given id so we can delete it
	req.data.Object.withId(req.params.id)
	.then(function(item) {
		// Delete the Item from the Cloud 
		return item.del();
	}).done(function(deleted) {
		// Validated it was deleted
		var isDeleted = deleted.isDeleted();
		if (deleted.isDeleted()) {
			res.send("Delete Successful.");
		} else {
			res.status(500);
			res.send("delete failed.");
		}
	});
});


//host static files in public folder
//endpoint:  https://mobile.mybluemix.net/${appHostName}/v1/apps/${applicationId}/static/
appContext.use('/public',express.static('public'));


//Redirect to the Extending Node.js to use Mobile Cloud Services applications page when accessing the root context
app.get('/', function(req, res){
	res.redirect(contextRoot+"/public");
});

app.listen(ibmconfig.getPort());
console.log('Server started at port: '+ibmconfig.getPort());
