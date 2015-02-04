//
//  IBM_BestPractise.h
//  BlueList
//
//  Created by Erik Junius on 04/02/15.
//  Copyright (c) 2015 International Business Machines. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <IBMData/IBMData.h>

@interface IBM_BestPractise : IBMDataObject <IBMDataObjectSpecialization>

@property(nonatomic, copy) NSString *title;
@property(nonatomic, copy) NSString *text;
@property(nonatomic, copy) NSString *topic;

@end
