//
//  RNMediaPlayer.m
//  RNMediaPlayer
//
//  Created by sean on 2022/3/10.
//  Copyright © 2022 ZJ. All rights reserved.
//

#import "RNMediaPlayer.h"
#import <ZJMediaPlayer/ZJMediaPlayer.h>
#import "RNMonitorManager.h"

NSString *const kOnMediaPlayerPlayStatus = @"onMediaPlayerPlayStatus";
NSString *const kOnMediaPlayerStatisticalInfo = @"onMediaPlayerStatisticalInfo";
NSString *const kOnMediaPlayerRecordStatus = @"onMediaPlayerRecordStatus";

@interface RNMediaPlayer () <ZJMediaPlayerDelegate>

@property (nonatomic, assign) BOOL isHasListeners;
@property (nonatomic, strong) ZJMediaPlayer *mediaPlayer;

@end

@implementation RNMediaPlayer
RCT_EXPORT_MODULE(MediaPlayer);

- (void)dealloc {
    if (_mediaPlayer) {
        _mediaPlayer.delegate = nil;
        _mediaPlayer.glMonitor = nil;
        _mediaPlayer = nil;
    }
}

- (void)startObserving {
    self.isHasListeners = YES;
}

- (void)stopObserving {
    self.isHasListeners = NO;
}

- (NSMutableDictionary *)emptyBody {
    NSMutableDictionary *body = @{}.mutableCopy;
    return body;
}

- (void)sendEvent:(NSString *)name body:(NSMutableDictionary *)body {
    if (!self.isHasListeners) return;
    [self sendEventWithName:name body:body];
}

+ (BOOL)requiresMainQueueSetup {
    return NO;
}

- (NSArray<NSString *> *)supportedEvents {
    return @[kOnMediaPlayerPlayStatus, kOnMediaPlayerStatisticalInfo, kOnMediaPlayerRecordStatus];
}

- (NSDictionary *)constantsToExport {
    return @{kOnMediaPlayerPlayStatus: kOnMediaPlayerPlayStatus,
             kOnMediaPlayerStatisticalInfo: kOnMediaPlayerStatisticalInfo,
             kOnMediaPlayerRecordStatus: kOnMediaPlayerRecordStatus
    };
}

- (ZJMediaPlayer *)mediaPlayer {
    if (!_mediaPlayer) {
        _mediaPlayer = [[ZJMediaPlayer alloc] init];
    }
    return _mediaPlayer;
}

#pragma mark -

RCT_EXPORT_METHOD(openDebugMode) {
    [[ZJAuthManager shared] openDebugMode];
}

RCT_EXPORT_METHOD(configWithKey:(NSString *)key secret:(NSString *)secret cb:(RCTResponseSenderBlock)cb) {
    BOOL ret = [[ZJAuthManager shared] configWithKey:key secret:secret];
    cb(@[@(ret)]);
}

RCT_EXPORT_METHOD(isAuthOK:(RCTResponseSenderBlock)cb) {
    cb(@[@([[ZJAuthManager shared] isAuthOK])]);
}

#pragma mark -

RCT_EXPORT_METHOD(playerId:(RCTResponseSenderBlock)cb) {
    cb(@[@(self.mediaPlayer.hash)]);
}

RCT_EXPORT_METHOD(addMonitor:(NSInteger)viewId) {
    RNZJGLMonitor *view = [[RNMonitorManager shared] getMonitor:viewId];
    if (!view) return;
    self.mediaPlayer.glMonitor = view;
    view.mediaPlayer = self.mediaPlayer;
}

RCT_EXPORT_METHOD(start:(NSString *)url) {
    self.mediaPlayer.delegate = self;
    [self.mediaPlayer start:url];
}

RCT_EXPORT_METHOD(restart) {
    [self.mediaPlayer restart];
}

RCT_EXPORT_METHOD(seek:(NSInteger)msTime) {
    [self.mediaPlayer seek:msTime];
}

RCT_EXPORT_METHOD(stop) {
    self.mediaPlayer.delegate = nil;
    [self.mediaPlayer stop];
}

RCT_EXPORT_METHOD(openVideo) {
    [self.mediaPlayer openVideo];
}

RCT_EXPORT_METHOD(openAudio) {
    [self.mediaPlayer openAudio];
}

RCT_EXPORT_METHOD(closeVideo) {
    [self.mediaPlayer closeVideo];
}

RCT_EXPORT_METHOD(closeAudio) {
    [self.mediaPlayer closeAudio];
}

RCT_EXPORT_METHOD(mute:(BOOL)mute) {
    self.mediaPlayer.mute = mute;
}

RCT_EXPORT_METHOD(setMediaCacheTime:(NSInteger)cacheTime pursueTime:(NSInteger)pursueTime) {
    [self.mediaPlayer setMediaCacheTime:cacheTime pursueTime:pursueTime];
}

RCT_EXPORT_METHOD(setDenoiseLevel:(NSInteger)nLevel) {
    [self.mediaPlayer setDenoiseLevel:nLevel];
}

RCT_EXPORT_METHOD(setMediaSyncMode:(BOOL)sync) {
    [self.mediaPlayer setMediaSyncMode:sync];
}

RCT_EXPORT_METHOD(startRecord:(NSString *)filePath) {
    [self.mediaPlayer startRecord:filePath];
}

RCT_EXPORT_METHOD(stopRecord) {
    [self.mediaPlayer stopRecord];
}

RCT_EXPORT_METHOD(isRecording:(RCTResponseSenderBlock)cb) {
    cb(@[@([self.mediaPlayer isRecording])]);
}

RCT_EXPORT_METHOD(getRecordDuration:(RCTResponseSenderBlock)cb) {
    cb(@[@([self.mediaPlayer getRecordDuration])]);
}

#pragma mark - ZJMediaPlayerDelegate

- (void)didMediaPlayerPlay:(ZJMediaStreamPlayer *_Nonnull)player status:(ZJ_MEDIA_PLAY_STATUS)status errCode:(NSInteger)errCode {
    NSMutableDictionary *dic = [self emptyBody];
    dic[@"playerId"] = @(player.hash);
    dic[@"status"] = @(status);
    dic[@"errCode"] = @(errCode);
    [self sendEvent:kOnMediaPlayerPlayStatus body:dic];
}

- (void)didMediaPlayerStatisticalInfo:(ZJMediaStreamPlayer *_Nonnull)player statInfo:(ZJMediaStatisticalInfo *)statInfo {
    NSMutableDictionary *dic = [self emptyBody];
    dic[@"playerId"] = @(player.hash);
    dic[@"videoWidth"] = @(statInfo.videoWidth);
    dic[@"videoHeight"] = @(statInfo.videoHeight);
    dic[@"videoFPS"] = @(statInfo.videoFPS);
    dic[@"videoBps"] = @(statInfo.videoBps);
    dic[@"audioBps"] = @(statInfo.audioBps);
    dic[@"timestamp"] = @(statInfo.timestamp);
    dic[@"videoTotalFrame"] = @(statInfo.videoTotalFrame);
    dic[@"videoDropFrame"] = @(statInfo.videoDropFrame);
    dic[@"onlineCount"] = @(statInfo.onlineCount);
    [self sendEvent:kOnMediaPlayerStatisticalInfo body:dic];
}

- (void)didMediaPlayerRecord:(ZJMediaStreamPlayer *_Nonnull)player status:(ZJ_MEDIA_RECORD_STATUS)status filePath:(NSString *_Nullable)filePath errCode:(NSInteger)errCode {
    NSMutableDictionary *dic = [self emptyBody];
    dic[@"playerId"] = @(player.hash);
    dic[@"status"] = @(status);
    dic[@"errCode"] = @(errCode);
    dic[@"filePath"] = filePath;
    [self sendEvent:kOnMediaPlayerRecordStatus body:dic];
}

@end
