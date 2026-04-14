import { MaterialCommunityIcons } from '@expo/vector-icons';
import React, { useRef, useState } from 'react';
import { ActivityIndicator, Dimensions, Modal, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import MapView from 'react-native-map-clustering';
import { Marker, Region } from 'react-native-maps';
import { SkateSpotBottomSheet } from '../components/SkateSpotBottomSheet';
import { useTheme } from '../context/ThemeContext';
import { SkateSpot, SkateSpotsService } from '../services/SkateSpotsService';
import { Spacing } from '../theme/theme';

const { width, height } = Dimensions.get('window');

const INITIAL_REGION: Region = {
  latitude: 39.8283,
  longitude: -98.5795,
  latitudeDelta: 35,
  longitudeDelta: 35,
};

interface SkateMapScreenProps {
  visible: boolean;
  onClose: () => void;
}

export const SkateMapScreen: React.FC<SkateMapScreenProps> = ({ visible, onClose }) => {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const mapRef = useRef<any>(null);

  const [spots, setSpots] = useState<Partial<SkateSpot>[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  
  // Bottom sheet state
  const [selectedSpot, setSelectedSpot] = useState<Partial<SkateSpot> | null>(null);
  const [isSheetVisible, setIsSheetVisible] = useState(false);

  // Fetch spots in view
  const fetchSpotsForRegion = async (region: Region) => {
    setIsLoading(true);
    
    const bbox = {
      minLat: region.latitude - region.latitudeDelta / 2,
      maxLat: region.latitude + region.latitudeDelta / 2,
      minLng: region.longitude - region.longitudeDelta / 2,
      maxLng: region.longitude + region.longitudeDelta / 2,
    };

    // 1. Fetch Native
    const nativeSpots = await SkateSpotsService.getNativeSpots(bbox);
    
    // 2. Fetch OSM if sufficiently zoomed in and native count is low
    // Zoomed in roughly check: delta < 1 degree
    let fallbackSpots: Partial<SkateSpot>[] = [];
    if (region.latitudeDelta < 1 && nativeSpots.length < 5) {
      fallbackSpots = await SkateSpotsService.getFallbackOSMSpots(bbox);
    }
    
    // Deduplicate on coordinates just in case
    const merged: Partial<SkateSpot>[] = [...nativeSpots];
    fallbackSpots.forEach(fs => {
      const isDup = merged.find(n => Math.abs((n.lat ?? 0) - (fs.lat ?? 0)) < 0.001 && Math.abs((n.lng ?? 0) - (fs.lng ?? 0)) < 0.001);
      if (!isDup) merged.push(fs);
    });

    setSpots(merged);
    setIsLoading(false);
  };

  const handleSpotPress = (spot: Partial<SkateSpot>) => {
    setSelectedSpot(spot);
    setIsSheetVisible(true);
  };

  const onSpotUpdated = (updated: SkateSpot) => {
    setSpots(prev => prev.map(s => s.id === updated.id ? updated : s));
  };

  return (
    <Modal visible={visible} onRequestClose={onClose} animationType="slide">
      <View style={styles.container}>
        <MapView
          ref={mapRef}
          style={styles.map}
          initialRegion={INITIAL_REGION}
          userInterfaceStyle="dark"
          customMapStyle={mapStyleDark}
          clusterColor="#FFAA00"
          onRegionChangeComplete={fetchSpotsForRegion}
          showsUserLocation
          showsMyLocationButton={false}
        >
          {spots.map((spot, idx) => (
            <Marker
              key={spot.id || `temp-${idx}`}
              coordinate={{ latitude: spot.lat!, longitude: spot.lng! }}
              onPress={() => handleSpotPress(spot)}
              pinColor={spot.source === 'native' ? '#FFAA00' : '#8E8E93'}
            />
          ))}
        </MapView>

        <View style={styles.header}>
          <TouchableOpacity style={styles.backButton} onPress={onClose}>
            <MaterialCommunityIcons name="arrow-left" size={24} color="#FFF" />
          </TouchableOpacity>
          
          <View style={styles.searchBar}>
            <MaterialCommunityIcons name="magnify" size={20} color={Colors.textMuted} />
            <Text style={styles.searchText}>Search area for rinks...</Text>
          </View>
        </View>

      {isLoading && (
        <View style={styles.loadingBubble}>
          <ActivityIndicator color="#000" size="small" />
          <Text style={styles.loadingText}>Scanning...</Text>
        </View>
      )}

      {/* Density Scanner FAB */}
      <TouchableOpacity 
        style={styles.fab} 
        onPress={() => {
          if (mapRef.current) {
            // Mock recenter to area or trigger reload
            // In future, can jump to current user location
          }
        }}
      >
        <MaterialCommunityIcons name="crosshairs-gps" size={24} color="#000" />
      </TouchableOpacity>

      <SkateSpotBottomSheet
        visible={isSheetVisible}
        onClose={() => setIsSheetVisible(false)}
        spot={selectedSpot}
        onSpotUpdated={onSpotUpdated}
      />
      </View>
    </Modal>
  );
};

const mapStyleDark = [
  { elementType: "geometry", stylers: [{ color: "#242f3e" }] },
  { elementType: "labels.text.fill", stylers: [{ color: "#746855" }] },
  { elementType: "labels.text.stroke", stylers: [{ color: "#242f3e" }] },
  {
    featureType: "administrative.locality",
    elementType: "labels.text.fill",
    stylers: [{ color: "#d59563" }]
  },
  {
    featureType: "poi",
    elementType: "labels.text.fill",
    stylers: [{ color: "#d59563" }]
  },
  {
    featureType: "poi.park",
    elementType: "geometry",
    stylers: [{ color: "#263c3f" }]
  },
  {
    featureType: "poi.park",
    elementType: "labels.text.fill",
    stylers: [{ color: "#6b9a76" }]
  },
  {
    featureType: "road",
    elementType: "geometry",
    stylers: [{ color: "#38414e" }]
  },
  {
    featureType: "road",
    elementType: "geometry.stroke",
    stylers: [{ color: "#212a37" }]
  },
  {
    featureType: "road",
    elementType: "labels.text.fill",
    stylers: [{ color: "#9ca5b3" }]
  },
  {
    featureType: "water",
    elementType: "geometry",
    stylers: [{ color: "#17263c" }]
  },
  {
    featureType: "water",
    elementType: "labels.text.fill",
    stylers: [{ color: "#515c6d" }]
  },
  {
    featureType: "water",
    elementType: "labels.text.stroke",
    stylers: [{ color: "#17263c" }]
  }
];

const createStyles = (Colors: any) => StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#000',
  },
  map: {
    ...StyleSheet.absoluteFillObject,
  },
  header: {
    position: 'absolute',
    top: 50,
    left: 16,
    right: 16,
    flexDirection: 'row',
    alignItems: 'center',
    gap: Spacing.md,
  },
  backButton: {
    width: 44,
    height: 44,
    borderRadius: 22,
    backgroundColor: 'rgba(0,0,0,0.6)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  searchBar: {
    flex: 1,
    height: 44,
    borderRadius: 22,
    backgroundColor: 'rgba(0,0,0,0.6)',
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: Spacing.lg,
    gap: Spacing.sm,
  },
  searchText: {
    color: Colors.textMuted,
    fontSize: 15,
  },
  fab: {
    position: 'absolute',
    bottom: 40,
    right: 20,
    width: 56,
    height: 56,
    borderRadius: 28,
    backgroundColor: '#FFAA00',
    justifyContent: 'center',
    alignItems: 'center',
    shadowColor: '#FFAA00',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 8,
    elevation: 5,
  },
  loadingBubble: {
    position: 'absolute',
    top: 110,
    alignSelf: 'center',
    backgroundColor: '#FFAA00',
    borderRadius: 20,
    paddingHorizontal: Spacing.lg,
    paddingVertical: Spacing.sm,
    flexDirection: 'row',
    alignItems: 'center',
    gap: Spacing.sm,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.3,
    shadowRadius: 4,
    elevation: 5,
  },
  loadingText: {
    color: '#000',
    fontWeight: '800',
    fontSize: 12,
  }
});
