//
//  AppDelegate.swift
//  BlueListDataSwift
//
//  Created by todd on 8/8/14.
//  Copyright (c) 2014 todd. All rights reserved.
//

import UIKit
import CoreData


@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    var window: UIWindow?
    
    
    func application(application: UIApplication!, didFinishLaunchingWithOptions launchOptions: NSDictionary!) -> Bool {
        
        // Override point for customization after application launch.
        
        var applicationId = ""
        var applicationSecret = ""
        var applicationRoute  = ""
        
        var hasValidConfiguration = true
        var errorMessage = ""
        
        // Read the applicationId from the bluelist.plist.
        var configurationPath = NSBundle.mainBundle().pathForResource("bluelist", ofType: "plist")
        
        if(configurationPath != nil){
            var configuration = NSDictionary(contentsOfFile: configurationPath!)
            
            applicationId = configuration!["applicationId"] as String
            print("applicationId " + applicationId)
            
            if(applicationId == ""){
                hasValidConfiguration = false
                errorMessage = "Open the bluelist.plist and set the applicationId to the BlueMix applicationId"
            }
            applicationSecret = configuration!["applicationSecret"] as String
            if(applicationSecret == ""){
                hasValidConfiguration = false
                errorMessage = "Open the bluelist.plist and set the applicationSecret with your BlueMix application's secret"
            }
            applicationRoute = configuration!["applicationRoute"] as String
            if(applicationRoute == ""){
                hasValidConfiguration = false
                errorMessage = "Open the bluelist.plist and set the applicationRoute to the BlueMix application's route"
            }
        }
        
        
        if(hasValidConfiguration){
            // Initialize the SDK and BlueMix services
            IBMBluemix.initializeWithApplicationId(applicationId, andApplicationSecret: applicationSecret, andApplicationRoute: applicationRoute)
            IBMData.initializeService()
            
            IBM_Item.registerSpecialization()
        }else{
            NSException().raise()
        }
        return true
    }
}

