//
//  IBM_Item.swift
//  bluelist-mobiledata-swift
//
//  Created by Erin Bartholomew on 8/19/14.
//  Copyright (c) 2014 todd. All rights reserved.
//

import UIKit

class IBM_Item: IBMDataObject,IBMDataObjectSpecialization {
   
    let nameKey = "name"
    
    var name: String {
        get {
            if let nameStr = super.objectForKey(nameKey) as? String {
                return nameStr
            } else {
                return ""
            }
        }
        set {
            super.setObject(newValue, forKey: nameKey)
        }
    }
    
    required override init() {
        super.init()
    }
    
    override init(`class` classname: String!) {
        super.init(`class`:classname)
    }
    
    class func dataClassName() -> String! {
        return "Item"
    }
}
