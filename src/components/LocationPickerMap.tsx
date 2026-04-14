import React from 'react';
import MapView, { MapViewProps, Marker } from 'react-native-maps';

export const LocationPickerMap: React.FC<MapViewProps> = (props) => {
  return <MapView {...props} />;
};

export const LocationMarker = Marker;
