//
//  RNGLMonitorViewManager.m
//  RNMediaPlayer
//
//  Created by sean on 2022/3/10.
//  Copyright © 2022 ZJ. All rights reserved.
//

#import "RNGLMonitorViewManager.h"
#import <React/RCTUIManager.h>
#import "RNZJGLMonitor.h"

@implementation RNGLMonitorViewManager
RCT_EXPORT_MODULE(GLMonitor)

RCT_EXPORT_VIEW_PROPERTY(displayRatioType, NSInteger)

RCT_CUSTOM_VIEW_PROPERTY(image, NSDictionary, RNZJGLMonitor) {
    UIImage *img = [RCTConvert UIImage:json];
    if (img) {
        view.image = img;
    }
}

- (UIView *)view {
    RNZJGLMonitor *view = [[RNZJGLMonitor alloc] init];
    return view;
}

- (NSDictionary *)constantsToExport {
    return @{@"RatioType_ScaleAspectFit": @(ZJGLMonitorDisplayRatioType_ScaleAspectFit),
             @"RatioType_ScaleToFill": @(ZJGLMonitorDisplayRatioType_ScaleToFill),
             @"RatioType_ScaleAspectFill": @(ZJGLMonitorDisplayRatioType_ScaleAspectFill),
             @"RatioType_1_1": @(ZJGLMonitorDisplayRatioType_1_1),
             @"RatioType_16_9": @(ZJGLMonitorDisplayRatioType_16_9),
             @"RatioType_4_3": @(ZJGLMonitorDisplayRatioType_4_3)
        
    };
}

RCT_EXPORT_METHOD(viewInfo:(nonnull NSNumber *)viewTag) {
    [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *,UIView *> *viewRegistry) {
        RNZJGLMonitor *view = (RNZJGLMonitor *)viewRegistry[viewTag];
        if ([view isKindOfClass:[RNZJGLMonitor class]] && view.onViewInfo) {
            view.onViewInfo(@{@"viewId": @(view.hash)});
        }
    }];
}

RCT_EXPORT_METHOD(getImage:(nonnull NSNumber *)viewTag success:(RCTPromiseResolveBlock)success failure:(RCTPromiseRejectBlock)failure) {
    if (!viewTag) failure(@"-1", @"Parameter error", nil);
    
    [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *,UIView *> *viewRegistry) {
        RNZJGLMonitor *view = (RNZJGLMonitor *)viewRegistry[viewTag];
        if ([view isKindOfClass:[RNZJGLMonitor class]]) {
            UIImage *img = view.image;
            if (img) {
                NSString *documentsPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
                NSString *filePath = [NSString stringWithFormat:@"%@/RNMediaPlayer/Pic/%ld.png", documentsPath, (long)[[NSDate date] timeIntervalSince1970]];
                BOOL result = [UIImagePNGRepresentation(img) writeToFile:filePath atomically:YES];
                if (result) {
                    success(@{@"path": filePath});
                } else {
                    failure(@"-2", @"Failed to save image", nil);
                }
            } else {
                failure(@"-3", @"Failed to get image", nil);
            }
        }
    }];
}

RCT_EXPORT_METHOD(snapshot:(nonnull NSNumber *)viewTag success:(RCTPromiseResolveBlock)success failure:(RCTPromiseRejectBlock)failure) {
    if (!viewTag) failure(@"-1", @"Parameter error", nil);
    
    [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *,UIView *> *viewRegistry) {
        RNZJGLMonitor *view = (RNZJGLMonitor *)viewRegistry[viewTag];
        if ([view isKindOfClass:[RNZJGLMonitor class]]) {
            UIImage *img = [view snapshot];
            if (img) {
                NSString *documentsPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
                NSString *filePath = [NSString stringWithFormat:@"%@/RNMediaPlayer/Pic/%ld.png", documentsPath, (long)[[NSDate date] timeIntervalSince1970]];
                BOOL result = [UIImagePNGRepresentation(img) writeToFile:filePath atomically:YES];
                if (result) {
                    success(@{@"path": filePath});
                } else {
                    failure(@"-2", @"Failed to save image", nil);
                }
            } else {
                failure(@"-3", @"Failed to get image", nil);
            }
        }
    }];
}


@end
