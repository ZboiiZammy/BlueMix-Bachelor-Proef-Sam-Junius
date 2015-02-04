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

#import "IBM_ListViewController.h"
#import "IBM_CreateEditTopicViewController.h"
#import "IBM_Topic.h"

@interface IBM_ListViewController ()

//IBOutlets from view
@property (weak, nonatomic) IBOutlet UIBarButtonItem *addButton;

// Items in list
@property NSMutableArray *topicList;

// If edit was triggered, the cell being edited.
@property UITableViewCell *editedCell;

@end

@implementation IBM_ListViewController


#pragma mark - View initialization and refreshing
-(void)loadView
{
    [super loadView];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	self.topicList = [[NSMutableArray alloc]init];

    // Setting up the refresh control
    self.refreshControl = [[UIRefreshControl alloc]init];
    self.refreshControl.attributedTitle = [[NSAttributedString alloc]initWithString:@"Loading Topics"];
    [self.refreshControl addTarget:self action:@selector(handleRefreshAction) forControlEvents:UIControlEventValueChanged];
    
    // Load initial set of items
    [self.refreshControl beginRefreshing];
    [self listTopics:^{
        [self.refreshControl endRefreshing];
    }];
}

-(void) viewWillAppear:(BOOL)animated
{
    [self listTopics:nil];
}

-(void) handleRefreshAction
{
    [self listTopics:^{
        [self.refreshControl performSelectorOnMainThread:@selector(endRefreshing) withObject:nil waitUntilDone:NO];
    }];
}

-(void) reloadLocalTableData
{
    [self.topicList sortUsingComparator:^NSComparisonResult(IBM_Topic* topic1, IBM_Topic* topic2) {
        return [topic1.name caseInsensitiveCompare:topic2.name];
    }];
    
    [self.tableView performSelectorOnMainThread:@selector(reloadData) withObject:nil waitUntilDone:NO];
}

-(void)setEditing:(BOOL)editing animated:(BOOL)animated
{
    [super setEditing:editing animated:animated];
    [self.tableView setEditing:editing animated:YES];
    
    // Disable add button in edit mode
    if (editing) {
        self.addButton.enabled = NO;
    } else {
        self.addButton.enabled = YES;
    }
}

#pragma mark Methods to list, create, update, and delete items
- (void)listTopics: (void(^)(void)) cb
{
	IBMQuery *qry = [IBM_Topic query];
    [[qry find] continueWithBlock:^id(BFTask *task) {
        if(task.error) {
            NSLog(@"listTopics failed with error: %@", task.error);
        } else {
            self.topicList = [NSMutableArray arrayWithArray: task.result];
            [self reloadLocalTableData];
            if(cb){
                cb();
            }

        }
        return nil;
        
    }];
}

- (void) createTopic: (IBM_Topic*) topic
{
    [self.topicList addObject: topic];
    [self reloadLocalTableData];
    
    [[topic save] continueWithBlock:^id(BFTask *task) {
        if(task.error) {
            NSLog(@"createTopic failed with error: %@", task.error);
        }
        return nil;
    }];
    
}

- (void) updateTopic: (IBM_Topic*) topic
{
    self.editedCell.textLabel.text = topic.name;
    [[topic save] continueWithBlock:^id(BFTask *task) {
        if(task.error) {
             NSLog(@"updateTopic failed with error: %@", task.error);
        }
        return nil;
    }];

}

-(void) deleteTopic: (IBM_Topic*) topic
{
    [self.topicList removeObject: topic];
    [self reloadLocalTableData];
    [[topic delete] continueWithBlock:^id(BFTask *task) {
        if(task.error){
             NSLog(@"deleteTopic failed with error: %@", task.error);
        } else {
            [self listTopics: nil];
        }
        return nil;
    }];
    
    // Exit edit mode to avoid need to click Done button
    [self.tableView setEditing:NO animated:YES];
}

#pragma mark - Table view data source
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.topicList.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"ListTopicCell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    cell.textLabel.text = ((IBM_Topic*)self.topicList[indexPath.row]).name;
    return cell;
}

#pragma mark - Navigation to/from Create/Edit View
-(IBAction) updateListWithSegue: (UIStoryboardSegue *)segue
{
    IBM_CreateEditTopicViewController *createEditController = [segue sourceViewController];
    if(createEditController.topic.name && createEditController.topic.name.length > 0){
        if(self.editedCell){
            // Is Update
            [self updateTopic: createEditController.topic];
        }else{
            // Is Add
            [self createTopic: createEditController.topic];
        }
    }
    self.editedCell = nil;
}

-(void) prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    UINavigationController *navigationController = (UINavigationController *)segue.destinationViewController;
    IBM_CreateEditTopicViewController *createEditController = [[navigationController viewControllers] lastObject];
    if(sender == self.addButton){
        createEditController.topic = [[IBM_Topic alloc] init];
    }else{
        // is edit so seed the item with the title
        self.editedCell = sender;
        NSIndexPath* indexPath = [self.tableView indexPathForCell: self.editedCell];
        createEditController.topic = self.topicList[indexPath.row];
    }
}

#pragma mark - Deleting items
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    return YES;
}

-(void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Perform delete
        [self deleteTopic: self.topicList[indexPath.row]];
    }
}

@end
