import React from 'react';
import MapView, { MapViewProps, Marker } from 'react-native-maps';

export const LocationPickerMap: React.FC<MapViewProps & { children?: React.ReactNode }> = ({ children, ...props }) => {
  return (
    <MapView {...props}>
      {children}
    </MapView>
  );
};

export const LocationMarker = Marker;
