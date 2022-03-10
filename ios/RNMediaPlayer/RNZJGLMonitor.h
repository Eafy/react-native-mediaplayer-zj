//
//  RNZJGLMonitor.h
//  RNMediaPlayer
//
//  Created by sean on 2022/3/10.
//  Copyright © 2022 ZJ. All rights reserved.
//

#import <ZJMediaPlayer/ZJMediaPlayer.h>
#import <React/RCTView.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNZJGLMonitor : ZJGLMonitor

/// 播放器ID
@property (nonatomic, strong) ZJMediaPlayer *mediaPlayer;
@property (nonatomic, copy) RCTDirectEventBlock onViewInfo;

@end

NS_ASSUME_NONNULL_END
