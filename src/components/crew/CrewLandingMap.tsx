import React from 'react';
import { View, Text, Image, Platform, Animated } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import MapView, { Marker, PROVIDER_GOOGLE, PROVIDER_DEFAULT } from 'react-native-maps';
import MapViewCluster from 'react-native-map-clustering';
import { useTheme } from '../../context/ThemeContext';
import { NearbySession, NearbySkateSpot } from '../../services/LocationService';

// ── Color SSOT: must match MapFiltersTray.tsx FILTER_OPTS activeColor exactly ──
function getSpotMarker(spot: NearbySkateSpot): { hex: string; icon: string } {
  if (spot.facility_type === 'roller_rink') return { hex: '#3B82F6', icon: 'roller-skate' };
  if (spot.facility_type === 'skate_shop')  return { hex: '#8B5CF6', icon: 'storefront-outline' };
  if (spot.facility_type === 'skatepark')   return { hex: '#92400E', icon: 'flag-triangle' };
  return { hex: '#555555', icon: 'map-marker' }; // legacy: no facility_type
}

export function CrewLandingMap({ 
  nearbySpots, 
  nearbySessions, 
  pulseAnim, 
  handleJoinById, 
  locationCoords,
  discoverRadiusMi
}: {
  nearbySpots: NearbySkateSpot[];
  nearbySessions: NearbySession[];
  pulseAnim: Animated.Value;
  handleJoinById: (id: string) => void;
  locationCoords: { lat: number; lng: number } | null;
  discoverRadiusMi: number | null;
}) {
  const { Colors } = useTheme();
  const mapRef = React.useRef<any>(null);

  React.useEffect(() => {
    if (mapRef.current && locationCoords) {
      // Default USA-wide zoom out when radius is "ALL" (discoverRadiusMi === null)
      let latDelta = 30;
      let lngDelta = 30;
      
      if (discoverRadiusMi !== null) {
        // 1 degree ~ 69 miles. We want bounded viewport diameter (radius * 2.2 for padding)
        const degrees = (discoverRadiusMi * 2.2) / 69.0;
        latDelta = degrees;
        lngDelta = degrees;
      }

      mapRef.current.animateToRegion({
        latitude: locationCoords.lat,
        longitude: locationCoords.lng,
        latitudeDelta: latDelta,
        longitudeDelta: lngDelta,
      }, 800); // smooth 800ms animation
    }
  }, [discoverRadiusMi, locationCoords]);

  return (
    <MapViewCluster
      ref={mapRef}
      provider={Platform.OS === 'android' ? PROVIDER_GOOGLE : PROVIDER_DEFAULT}
      style={{ flex: 1 }}
      initialRegion={{
        latitude: locationCoords?.lat || 39.8283,
        longitude: locationCoords?.lng || -98.5795,
        latitudeDelta: locationCoords ? 0.2 : 30,
        longitudeDelta: locationCoords ? 0.2 : 30,
      }}
      clusterColor={Colors.primary}
      clusterTextColor="#000"
      showsUserLocation
      showsMyLocationButton
    >
      {/* ── Static Skate Spots — color-coded by facility_type ── */}
      {nearbySpots.map((spot: NearbySkateSpot) => {
        const { hex, icon } = getSpotMarker(spot);
        const descParts = [spot.city, spot.state].filter(Boolean).join(' ');
        return (
          <Marker
            key={`spot-${spot.id}`}
            coordinate={{ latitude: spot.lat, longitude: spot.lng }}
            title={spot.name}
            description={descParts}
          >
            <View style={{
              width: 28,
              height: 28,
              borderRadius: 14,
              backgroundColor: hex,
              borderWidth: 2,
              borderColor: '#FFFFFF',
              justifyContent: 'center',
              alignItems: 'center',
              shadowColor: hex,
              shadowRadius: 6,
              shadowOpacity: 0.6,
              elevation: 4,
            }}>
              <MaterialCommunityIcons name={icon as any} size={14} color="#FFF" />
            </View>
          </Marker>
        );
      })}

      {/* ── Active Crew Sessions — orange-ringed crew avatar beacons ── */}
      {nearbySessions.map((s: NearbySession) => {
        if (!s.lat || !s.lng) return null;
        return (
          <Marker
            key={`session-${s.id}`}
            coordinate={{ latitude: s.lat, longitude: s.lng }}
            onPress={() => handleJoinById(s.id)}
          >
            <Animated.View style={{ opacity: pulseAnim, alignItems: 'center' }}>
              {/* Crew avatar bubble with orange border */}
              <View style={{
                width: 38,
                height: 38,
                borderRadius: 19,
                borderWidth: 3,
                borderColor: '#F97316',
                overflow: 'hidden',
                shadowColor: '#F97316',
                shadowRadius: 8,
                shadowOpacity: 0.85,
                elevation: 6,
              }}>
                {s.crewAvatarUrl ? (
                  <Image
                    source={{ uri: s.crewAvatarUrl }}
                    style={{ width: '100%', height: '100%' }}
                  />
                ) : (
                  <View style={{
                    flex: 1,
                    backgroundColor: s.crewAvatarColor || '#F97316',
                    justifyContent: 'center',
                    alignItems: 'center',
                  }}>
                    <MaterialCommunityIcons
                      name={(s.crewAvatarIcon as any) || 'account-group'}
                      size={18}
                      color="#FFF"
                    />
                  </View>
                )}
              </View>

              {/* Crew name + member count callout below pin */}
              <View style={{
                backgroundColor: 'rgba(0,0,0,0.85)',
                paddingHorizontal: 6,
                paddingVertical: 3,
                borderRadius: 5,
                marginTop: 4,
                alignItems: 'center',
                minWidth: 80,
              }}>
                <Text style={{ color: '#FFF', fontSize: 10, fontWeight: '700' }} numberOfLines={1}>
                  {s.crewName || s.name}
                </Text>
                <Text style={{ color: '#F97316', fontSize: 9 }}>
                  {s.memberCount} Skater{s.memberCount !== 1 ? 's' : ''}
                </Text>
              </View>
            </Animated.View>
          </Marker>
        );
      })}
    </MapViewCluster>
  );
}
