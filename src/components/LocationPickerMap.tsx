import React from 'react';
import MapView, { MapViewProps, Marker, UrlTile } from 'react-native-maps';

export const LocationPickerMap: React.FC<MapViewProps & { children?: React.ReactNode }> = ({ children, ...props }) => {
  return (
    <MapView {...props} mapType="none">
      {/* OSM tile layer — replaces Google Maps tiles, no API key required */}
      <UrlTile
        urlTemplate="https://tile.openstreetmap.org/{z}/{x}/{y}.png"
        maximumZ={19}
        flipY={false}
        zIndex={-1}
      />
      {children}
    </MapView>
  );
};

export const LocationMarker = Marker;
