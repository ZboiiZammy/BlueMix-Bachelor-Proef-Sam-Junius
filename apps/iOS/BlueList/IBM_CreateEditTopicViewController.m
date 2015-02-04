//-------------------------------------------------------------------------------
// Copyright 2014 IBM Corp. All Rights Reserved
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//-------------------------------------------------------------------------------

#import "IBM_CreateEditTopicViewController.h"
#import "IBM_Topic.h"

@interface IBM_CreateEditTopicViewController () <UITextFieldDelegate>

// IBOutlets from View
@property (weak, nonatomic) IBOutlet UITextField *topicTextField;
@property (weak, nonatomic) IBOutlet UIBarButtonItem *cancelButton;
@property (weak, nonatomic) IBOutlet UIBarButtonItem *saveButton;

@end

@implementation IBM_CreateEditTopicViewController

#pragma mark - View initialization
- (void)viewDidLoad
{
    [super viewDidLoad];
    if(self.topic.name){
        // is edit so initialize the text field with the item title
        self.topicTextField.text = self.topic.name;
    }
    
    [self.topicTextField becomeFirstResponder];
    [self.topicTextField setDelegate:self];
}

#pragma mark - Navigation
-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if(sender != self.cancelButton && self.topicTextField.text.length > 0){
        // save the value from the text field back to the topic
        self.topic.name = self.topicTextField.text;
    }else{
        self.topic = nil;
    }
}

#pragma mark - Enablement of Save/Done
-(BOOL) textFieldShouldReturn:(UITextField *)textField
{
    // This is used to handle the done key being pressed on the keyboard
    if(self.topicTextField.text.length > 0){
        [self performSegueWithIdentifier:@"DoneButtonSegue" sender:self];
        return YES;
    }
    return NO;
}

-(BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    // Controls the enablement of the save button
    [self.saveButton setEnabled:self.topicTextField.text.length - range.length + string.length > 0];
    return YES;
}

@end
