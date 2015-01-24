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

angular.module('starter.services', [])

/**
 * A simple example service that returns some data.
 */
.factory('ListService', function($rootScope, $q, $cacheFactory) {

    // Use an internal Cache for storing the List and map the operations to manage that from 
    // Mobile Cloud SDK Calls
    var cache = $cacheFactory('');
    var options = {
        handleAs: 'JSON'
    };

    return {

        // Return all the Objects for a Given Class
        allCloud: function() {

            // get the CC Service
            var cc = IBMCloudCode.getService();

            // Create a Defer as this is an async operation
            defer = $q.defer();

            // Clear the Cache with a new set
            cache.remove('items');

            // Gets Some objects from MBaaS for a specific REST Url
            return cc.get('/items', options).then(function(list) {

                // Add a Name Property
                list.forEach(function(item) {

                    // As we have called using REST we get raw JSON back so no get and set methods to access attributes
                    if (angular.isString(item.attributes['name'])) {
                        item.name = item.attributes.name;
                    } else {
                        item.name = '...';
                    }
                });

                // Place the Items in the Cache
                cache.put('items', list);

                // return the Cache
                return $q.when(cache.get('items'));
                
            }, function(err) {
                console.log(err);
                defer.reject(err);
            });

            // Get the Objects for a particular Type
            return defer.promise;

        },

        // Return the Cached List
        allCache: function() {

            // Return the Cached Items
            return cache.get('items');

        },

        add: function(item) {

            // Get a the CC service
            var cc = IBMCloudCode.getService();

            // Create a new REST URI builder
            var uri = new IBMUriBuilder();

            // Append the Item Id from the meta data, this is a raw data object retrieve from REST so need 
            // to go inside its _meta object to get the objectId
            uri = uri.append("item").toString()

            // Create Payload
            var data = {
                name: item
            };

            // add the Item to the Cache    
            var index = cache.get('items').push(data);
			
            // Save the Item to the Cloud 
            return cc.post(uri, data, options).then(function(saved) {

                // Update the root date for display
                saved.name = saved.attributes.name;

                // Replace the Object in the Cache with the now returned object
                // So I need away of replacing the object without Angular performing a refresh
                // So just update the ObjectID so delete operations can work 
                var items = cache.get('items');

                // Replace the Item 
                items.forEach(function(item, i) { if (item.name == saved.name) items[i] = saved;});
				return $q.when(saved);
            });
			
        },
        put: function(item) {

            // Get a the CC service
            var cc = IBMCloudCode.getService();

            // Create a new REST URI
            var uri = new IBMUriBuilder();
            // Append the Item Id from the meta data, this is a raw data object retrieve from REST so need 
            // to go inside its _meta object to get the objectId
            uri = uri.append("item").append(item._meta.objectId).toString();

            // Create Payload
            var data = {
                name: item.name
            };
			
			// Save the Item to the Cloud 
            return cc.put(uri, data, options);


        },

        del: function(item) {

            // Get a the CC service
            var cc = IBMCloudCode.getService();

            // Remove the Item from the Cache
            var items = cache.get('items');
            items.splice(items.indexOf(item), 1)

            // Create a new REST URI
            var uri = new IBMUriBuilder();
            // Append the Item Id from the meta data, this is a raw data object retrieve from REST so need 
            // to go inside its _meta object to get the objectId
            uri = uri.append("item").append(item._meta.objectId).toString()

            // Call the DELETE REST endpoint
            return cc.del(uri);//.then(function(status) {
            /*     defer.resolve(status);
            }, function(err) {
                defer.reject(err);
            }); */

            // Remove it
            //return defer.promise;

        }
    }

})

/**
 * A Service that intialises MBaaS
 */
.factory('InitBaaS',
    function($rootScope, $http, $q) {

        function init() {

            // Create a defer
            var defer = $q.defer();

            // Lets load the Configuration from the overall package.json    
            $http.get("./bluelist.json").success(function(config) {

                // Initialize the SDK
                IBMBluemix.initialize(config).then(function() {
                    // Let the user no they have logged in and can do some stuff if they require
                    console.log("Sucessful initialisation with Application : " + IBMBluemix.config.getApplicationId());
                    // Set the Origin to Local 
                    
                    var cc = IBMCloudCode.initializeService();
                    cc.setBaseUrl(window.location.origin);

                    // Return the Data
                    defer.resolve();

                }, function(response) {
                    // Error
                    console.log("Error:", response);
                    defer.reject(response);
                });

                $rootScope.config = config;;
            });

            return defer.promise;

        };

        return {
            init: function() {
                return init();
            }
        }

    });
