import React from 'react';
import MapView, { Marker, MapViewProps } from 'react-native-maps';

export const LocationPickerMap: React.FC<MapViewProps> = (props) => {
  return <MapView {...props} />;
};

export const LocationMarker = Marker;
