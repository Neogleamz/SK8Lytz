import React from 'react';
import { View, Text, Platform, Animated } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import MapView, { Marker, PROVIDER_GOOGLE, PROVIDER_DEFAULT } from 'react-native-maps';
import MapViewCluster from 'react-native-map-clustering';
import { useTheme } from '../../context/ThemeContext';

export function CrewLandingMap({ 
  nearbySpots, 
  nearbySessions, 
  pulseAnim, 
  handleJoinById, 
  locationCoords
}: any) {
  const { Colors } = useTheme();
  
  return (
    <MapViewCluster
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
      {/* Static Spots */}
      {nearbySpots.map((spot: any) => {
        let pin = spot.is_indoor ? 'blue' : 'brown';
        if (spot.facility_type === 'roller_rink') pin = 'blue';
        if (spot.facility_type === 'pro_shop' || spot.has_pro_shop) pin = 'purple';
        
        let desc = `${spot.city || ''} ${spot.state || ''}`;
        const tags = [];
        if (spot.is_indoor) tags.push('Indoor');
        else tags.push('Outdoor');
        if (spot.has_pro_shop) tags.push('Has Pro Shop');
        if (spot.has_adult_night) tags.push('Adult Night');

        return (
          <Marker
            key={`spot-${spot.id}`}
            coordinate={{ latitude: spot.lat, longitude: spot.lng }}
            pinColor={pin}
            title={spot.name}
            description={`${desc} (${tags.join(' • ')})`}
          />
        );
      })}

      {/* Active Remote Sessions (Glowing Beacons) */}
      {nearbySessions.map((s: any) => {
        if (!s.lat || !s.lng) return null;
        return (
          <Marker
            key={`session-${s.id}`}
            coordinate={{ latitude: s.lat, longitude: s.lng }}
            onPress={() => handleJoinById(s.id)}
          >
            <Animated.View style={{
               width: 24, height: 24, borderRadius: 12,
               backgroundColor: 'rgba(255,0,0,0.8)',
               borderWidth: 2, borderColor: '#FFF',
               shadowColor: '#F00', shadowRadius: 10, shadowOpacity: 1,
               opacity: pulseAnim,
               justifyContent: 'center', alignItems: 'center'
            }}>
              <MaterialCommunityIcons name="lightning-bolt" size={12} color="#FFF" />
            </Animated.View>
            <View style={{ backgroundColor: 'rgba(0,0,0,0.8)', padding: 4, borderRadius: 4, position: 'absolute', top: 30, alignSelf: 'center', minWidth: 80, alignItems: 'center' }}>
              <Text style={{ color: '#FFF', fontSize: 10, fontWeight: '700' }}>{s.crewName || s.name}</Text>
              <Text style={{ color: Colors.primary, fontSize: 9 }}>{s.memberCount} Skaters</Text>
            </View>
          </Marker>
        );
      })}
    </MapViewCluster>
  );
}
