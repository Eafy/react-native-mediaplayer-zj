import PropTypes from 'prop-types';
import ReactNative, {
  requireNativeComponent,
  Platform,
  UIManager,
  ViewPropTypes,
  View
} from 'react-native';
import React, { Component } from 'react';

const ZJGLMonitor = requireNativeComponent('GLMonitor');

export default class _GLMonitor extends Component {

  constructor(props){
      super(props)
  }

  viewInfo(){
    let self = this;
    UIManager.dispatchViewManagerCommand(
      ReactNative.findNodeHandle(self.refs.id_GLMonitor),
      UIManager.getViewManagerConfig(ZJGLMonitor).Commands.viewInfo,
      []
    );
  }

  render() {
    return (
      <ZJGLMonitor 
        ref='id_GLMonitor'
        style={this.props.style}
        {...this.props}
        >
      </ZJGLMonitor>
    )
  }
}
