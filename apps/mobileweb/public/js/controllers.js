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

angular.module('starter.controllers', [])

// A simple controller that fetches a list of data from a service
.controller('ListIndexCtrl', function($rootScope, $scope, $location, $ionicLoading, $ionicModal, ListService, InitBaaS) {

    $scope.loadItems = function() {

        // Clear the List before adding new items
        // This needs to be improved
        $scope.list = [];

        // Refresh
        if (!$scope.$$phase) {
            $scope.$apply();
        }

        // Because we are retrieving all the items every time we do something
        // We need to clear the list before loading in some new values
        $ionicLoading.show({
            template: 'Loading...'
        });

        // "List is " is a service returning data from the 
        ListService.allCloud().then(function(list) {

            // Update the model with a list of Items
            $scope.list = list;

            // Let Angular know we have some data because of the Async nature of IBMBluemix
            // This is required to make sure the information is up to date
            if (!$scope.$$phase) {
                $scope.$apply();
            }

            // Trigger refresh complete on the pull to refresh action
            $scope.$broadcast('scroll.refreshComplete');

            $ionicLoading.hide();

        }, function(err) {
            console.log(err);

            $scope.list = null;
            $ionicLoading.hide();

        });

    }

    $scope.select = function(item) {
        // Shows/hides the delete button on hover

        $location.path("#/tab/list")
    };

    $scope.onRefresh = function() {
        // Go back to the Cloud and load a new set of Objects as a hard refresh has been done
        $scope.loadItems();
    }

    $scope.createItem = function(item) {

        // Add the Item and then hide the modal view
        ListService.add(item.name);

        // Clear the Item 
        $scope.list = ListService.allCache();

        // Hide the Modal View
        $scope.closeItem();

    };

    $scope.updateItem = function(item) {

        // Add the Item and then hide the modal view
        ListService.put(item).then(function(){
			$scope.onRefresh();
		}, function(err) {
            console.log(err);
        });

        // Update based on the latest cache 
        $scope.list = ListService.allCache();

        // Hide the Modal View
        $scope.closeItem(true);
		
    };

    $scope.newItem = function() {
        // Clear the Form
        $scope.item.name = '';

        $scope.editMode = false;

        $scope.itemModal.show();
    };

    $scope.editItem = function(item) {

		item.originalName = item.name;
		
        // Clear the Form
        $scope.item = item;
        $scope.editMode = true;

        $scope.itemModal.show();
		
    };

    $scope.closeItem = function(onSave) {

        if ($scope.item.originalName != undefined)
        {
            if (onSave != true)
            {
                $scope.item.name = $scope.item.originalName;
				$scope.onRefresh();
            }
            delete $scope.item.originalName;
        }
        // Reverse the Paint Bug
        $scope.itemModal.hide();

    }
    $scope.clearSearch = function() {
        $scope.item.name = '';
    };

    $scope.onItemDelete = function(item) {

        // Delete the Item
        ListService.del(item);

        $scope.list = ListService.allCache();

    };
	
	// Form Model
    $scope.item = {};
	
	// Initialize Mobile Cloud SDK and wait for it to configure itself
    // Once complete keep a reference to it so we can talk to it later
    if (!$rootScope.IBMBluemix) {
        InitBaaS.init().then(function() {
            $rootScope.IBMBluemix = IBMBluemix;
            $scope.loadItems();
        }, function(err) {
            console.log(err);
        });
    } else {
        // load a refresh from the cloud
        $scope.list = ListService.allCache();
    }
	
    // Create our modal
    $ionicModal.fromTemplateUrl('templates/list-add.html', function(modal) {
        $scope.itemModal = modal;
    }, {
        scope: $scope,
        animation: 'slide-in-up',
        focusFirstInput: true
    });

})

// A simple controller that shows a tapped item's data
.controller('AboutCtrl', function($rootScope, $scope) {

    // Display information about the Configuration
    if (typeof IBMBluemix === "object") {
        $scope.version = IBMBluemix.getVersion();
    }

});
