//
//  IBM_BestPractise.m
//  BlueList
//
//  Created by Erik Junius on 04/02/15.
//  Copyright (c) 2015 International Business Machines. All rights reserved.
//



#import "IBM_BestPractise.h"

@implementation IBM_BestPractise

@dynamic title;
@dynamic text;
@dynamic topic;

+(void) initialize
{
    [self registerSpecialization];
}

+(NSString*) dataClassName
{
    return @"BestPractise";
}

@end