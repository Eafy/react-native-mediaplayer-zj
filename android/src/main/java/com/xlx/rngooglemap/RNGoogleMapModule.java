
package com.xlx.rngooglemap;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class RNGoogleMapModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNGoogleMapModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNGoogleMap";
  }
}