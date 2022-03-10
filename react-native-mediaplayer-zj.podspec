require 'json'

Pod::Spec.new do |s|
  s.name           = 'react-native-mediaplayer-zj'
  s.version        = '1.0.0'
  s.description    = 'ZJ Media Player SDK for React Native at iOS & Android，Supported H.264、Hevc、AAC and other formats.'
  s.summary        = 'rn mediaplayer sdk'
  s.license        = 'Apache License 2.0'
  s.author         = 'eafy'
  s.homepage       = 'https://github.com/Eafy/ZJMediaPlayer'
  s.requires_arc   = true
  s.platform       = :ios, '10.0'

  s.source         = { :git => 'https://github.com/Eafy/react-native-mediaplayer-zj.git', :tag => "v#{s.version}" }
  s.source_files  = "ios/**/*.{h,m,swift}"
  s.exclude_files = ""
  
  s.dependency 'React'
  s.dependency 'ZJMediaPlayer'
  
end
