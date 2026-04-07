import React from 'react';
import { View, Image, StyleSheet } from 'react-native';

export const LocationPickerMap: React.FC<any> = ({ style, initialRegion, children }) => {
  // Use OSM static map for web fallback
  const lat = initialRegion?.latitude || 0;
  const lng = initialRegion?.longitude || 0;

  return (
    <View style={[style, { overflow: 'hidden' }]}>
      <Image 
        source={{ uri: `https://staticmap.openstreetmap.de/staticmap.php?center=${lat},${lng}&zoom=15&size=400x150&markers=${lat},${lng},red-pushpin` }} 
        style={StyleSheet.absoluteFillObject}
        resizeMode="cover"
      />
    </View>
  );
};

export const LocationMarker: React.FC<any> = () => null;
