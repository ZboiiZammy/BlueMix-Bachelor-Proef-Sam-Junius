//
//  IBM_CreateEditItemViewController.swift
//  bluelist-mobiledata-swift
//
//  Created by todd on 8/12/14.
//  Copyright (c) 2014 todd. All rights reserved.
//

import Foundation
import UIKit

class IBM_CreateEditItemViewController : UIViewController, UITextFieldDelegate {

    @IBOutlet weak var itemTextField: UITextField!
    @IBOutlet weak var cancelButton: UIBarButtonItem!
    @IBOutlet weak var saveButton: UIBarButtonItem!

    var item: IBM_Item?
    
    override func viewDidLoad()
    {
        super.viewDidLoad();
        if let itemName = item?.name {
            self.itemTextField.text = itemName
        }
        
        self.itemTextField.becomeFirstResponder();
        self.itemTextField.delegate = self;

    }

    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject!) {
        
        if(sender as? NSObject !=  self.cancelButton && !self.itemTextField.text.isEmpty){
            self.item?.name = self.itemTextField.text
        } else {
            //self.item.delete()
            self.item = nil
        }
    }

    func textFieldShouldReturn( textField : UITextField) -> Bool{
        if (!self.itemTextField.text.isEmpty){
            self.performSegueWithIdentifier("DoneButtonSegue", sender: self as AnyObject);
            return true;
        }
        else{
            return false;
        }
        
    }

    func textField(textField: UITextField!, shouldChangeCharactersInRange range: NSRange, replacementString string: String!) -> Bool {
        if(!self.itemTextField.text.isEmpty && !string.isEmpty){
            self.saveButton.enabled = true;
        }
        return true;
    }

}
