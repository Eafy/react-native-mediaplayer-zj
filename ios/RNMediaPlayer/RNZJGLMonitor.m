//
//  RNZJGLMonitor.m
//  RNMediaPlayer
//
//  Created by sean on 2022/3/10.
//  Copyright © 2022 ZJ. All rights reserved.
//

#import "RNZJGLMonitor.h"
#import "RNMonitorManager.h"

@implementation RNZJGLMonitor

- (instancetype)init {
    if (self = [super init]) {
        [[RNMonitorManager shared] addMonitor:self];
    }
    return self;
}

- (void)removeFromSuperview {
    [super removeFromSuperview];
    
    if (_mediaPlayer) {
        if (self.mediaPlayer.glMonitor == self) {
            self.mediaPlayer.glMonitor = nil;            
        }
        _mediaPlayer = nil;
    }
    
    [[RNMonitorManager shared] removeMonitor:self.hash];
}

@end
