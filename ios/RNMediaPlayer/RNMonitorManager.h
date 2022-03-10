//
//  RNMonitorManager.h
//  RNMediaPlayer
//
//  Created by sean on 2022/3/10.
//  Copyright © 2022 ZJ. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "RNZJGLMonitor.h"

NS_ASSUME_NONNULL_BEGIN

@interface RNMonitorManager : NSObject

+ (RNMonitorManager *)shared;

- (void)addMonitor:(RNZJGLMonitor *)view;

- (void)removeMonitor:(NSInteger)viewId;

- (RNZJGLMonitor *)getMonitor:(NSInteger)viewId;

@end

NS_ASSUME_NONNULL_END
