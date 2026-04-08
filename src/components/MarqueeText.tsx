import React, { useState, useRef, useEffect } from 'react';
import { View, Animated, StyleSheet, TextStyle, StyleProp } from 'react-native';

interface MarqueeTextProps {
  children: string;
  style?: StyleProp<TextStyle>;
  containerStyle?: any;
}

const MarqueeText = ({ children, style, containerStyle }: MarqueeTextProps) => {
  const [textWidth, setTextWidth] = useState(0);
  const [containerWidth, setContainerWidth] = useState(0);
  const anim = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    if (textWidth > containerWidth && containerWidth > 0) {
      Animated.loop(
        Animated.sequence([
          Animated.delay(1500),
          Animated.timing(anim, {
            toValue: -(textWidth - containerWidth + 8),
            duration: 18 * (textWidth - containerWidth),
            useNativeDriver: true,
          }),
          Animated.delay(1000),
          Animated.timing(anim, {
            toValue: 0,
            duration: 0,
            useNativeDriver: true,
          })
        ])
      ).start();
    } else {
      anim.setValue(0);
      anim.stopAnimation();
    }
  }, [textWidth, containerWidth, children]);

  return (
    <View 
      style={[{ overflow: 'hidden', width: '100%', alignItems: 'center' }, containerStyle]} 
      onLayout={(e) => setContainerWidth(e.nativeEvent.layout.width)}
    >
      <Animated.Text
        numberOfLines={1}
        onLayout={(e) => setTextWidth(e.nativeEvent.layout.width)}
        style={[style, { transform: [{ translateX: anim }] }]}
      >
        {children}
      </Animated.Text>
    </View>
  );
};

export default MarqueeText;
