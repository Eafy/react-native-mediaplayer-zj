//
//  RNMonitorManager.m
//  RNMediaPlayer
//
//  Created by sean on 2022/3/10.
//  Copyright © 2022 ZJ. All rights reserved.
//

#import "RNMonitorManager.h"

@interface RNMonitorManager ()

@property (nonatomic, strong) NSMutableDictionary *monitorDic;

@end

@implementation RNMonitorManager

+ (RNMonitorManager *)shared
{
    static RNMonitorManager *__shared = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        if (__shared == nil) {
            __shared = [[RNMonitorManager alloc] init];
        }
    });
    
    return __shared;
}

- (NSMutableDictionary *)monitorDic {
    if (!_monitorDic) {
        _monitorDic = [NSMutableDictionary dictionary];
    }
    return _monitorDic;
}

- (void)addMonitor:(RNZJGLMonitor *)view {
    if (!view) return;
    [self.monitorDic setObject:view forKey:@(view.hash)];
}

- (void)removeMonitor:(NSInteger)viewId {
    [self.monitorDic removeObjectForKey:@(viewId)];
}

- (RNZJGLMonitor *)getMonitor:(NSInteger)viewId {
    return [self.monitorDic objectForKey:@(viewId)];
}

@end
