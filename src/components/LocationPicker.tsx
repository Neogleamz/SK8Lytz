import { MaterialCommunityIcons } from '@expo/vector-icons';
import * as Location from 'expo-location';
import React, { useRef, useState } from 'react';
import { ActivityIndicator, StyleSheet, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { useTheme } from '../context/ThemeContext';
import { Spacing } from '../theme/theme';
import { LocationMarker, LocationPickerMap } from './LocationPickerMap';

interface LocationPickerProps {
  locationLabel: string;
  onLocationLabelChange: (label: string) => void;
  locationCoords?: { lat: number; lng: number };
  onLocationCoordsChange: (coords?: { lat: number; lng: number }) => void;
  isGettingLocation: boolean;
  onDetectLocation: () => void;
  searchRadiusMi?: number;
}

export const LocationPicker: React.FC<LocationPickerProps> = ({
  locationLabel, onLocationLabelChange,
  locationCoords, onLocationCoordsChange,
  isGettingLocation, onDetectLocation,
  searchRadiusMi
}) => {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const [suggestions, setSuggestions] = useState<any[]>([]);
  const [isSearching, setIsSearching] = useState(false);
  const debounceTimer = useRef<NodeJS.Timeout | null>(null);

  const handleSearch = (text: string) => {
    onLocationLabelChange(text);
    onLocationCoordsChange(undefined);
    
    if (debounceTimer.current) clearTimeout(debounceTimer.current);
    
    if (text.length < 3) {
      setSuggestions([]);
      return;
    }

    debounceTimer.current = setTimeout(async () => {
      setIsSearching(true);
      try {
        let viewbox = '';
        if (searchRadiusMi) {
          try {
            // Get last known quickly rather than waiting for gps lock
            const loc = await Location.getLastKnownPositionAsync();
            if (loc) {
              const lat = loc.coords.latitude;
              const lon = loc.coords.longitude;
              // 1 degree latitude = ~69 miles filtering
              const delta = searchRadiusMi / 69;
              const left = lon - delta;
              const right = lon + delta;
              const top = lat + delta;
              const bottom = lat - delta;
              viewbox = `&viewbox=${left},${top},${right},${bottom}&bounded=1`;
            }
          } catch (locErr) {
             console.warn('Could not get loc for bounding', locErr);
          }
        }

        const url = `https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(text)}&format=json&limit=5&addressdetails=1${viewbox}`;
        const res = await fetch(url, { headers: { 'User-Agent': 'SK8Lytz App' } });
        const data = await res.json();
        setSuggestions(data || []);
      } catch (err) {
        console.warn('OSM fetch error', err);
      } finally {
        setIsSearching(false);
      }
    }, 900);
  };

  const selectSuggestion = (item: any) => {
    const parts = [
      item.address?.amenity, item.address?.park, item.address?.road, item.address?.city || item.address?.town
    ].filter(Boolean);
    const shortName = parts.length > 0 ? parts.join(', ') : item.name || item.display_name.split(',')[0];
    
    onLocationLabelChange(shortName);
    onLocationCoordsChange({ lat: parseFloat(item.lat), lng: parseFloat(item.lon) });
    setSuggestions([]);
  };

  return (
    <View style={styles.container}>
      <Text style={styles.label}>LOCATION (OPTIONAL)</Text>
      
      <View style={styles.inputRow}>
        <TextInput
          style={styles.input}
          value={locationLabel}
          onChangeText={handleSearch}
          placeholder="Enter address or park name"
          placeholderTextColor={Colors.textMuted}
        />
        <TouchableOpacity
          style={styles.detectBtn}
          onPress={onDetectLocation}
          disabled={isGettingLocation}
        >
          {isGettingLocation || isSearching ? (
            <ActivityIndicator size="small" color={Colors.primary} />
          ) : (
            <MaterialCommunityIcons name="crosshairs-gps" size={20} color={locationCoords ? Colors.primary : Colors.textMuted} />
          )}
        </TouchableOpacity>
      </View>

      {/* Suggestions List */}
      {suggestions.length > 0 && !locationCoords && (
        <View style={styles.suggestionsContainer}>
          {suggestions.map((item, idx) => (
            <TouchableOpacity key={item.place_id || idx} style={styles.suggestionItem} onPress={() => selectSuggestion(item)}>
              <MaterialCommunityIcons name="map-marker-outline" size={16} color={Colors.textMuted} style={{ marginRight: Spacing.sm }} />
              <Text style={styles.suggestionText} numberOfLines={2}>
                {item.display_name}
              </Text>
            </TouchableOpacity>
          ))}
        </View>
      )}

      {/* Map Thumbnail */}
      {locationCoords && (
        <View style={styles.mapContainer}>
          <LocationPickerMap
            style={styles.map}
            initialRegion={{
              latitude: locationCoords.lat,
              longitude: locationCoords.lng,
              latitudeDelta: 0.005,
              longitudeDelta: 0.005,
            }}
            region={{
              latitude: locationCoords.lat,
              longitude: locationCoords.lng,
              latitudeDelta: 0.005,
              longitudeDelta: 0.005,
            }}
            liteMode={true}
            scrollEnabled={false}
            zoomEnabled={false}
            pitchEnabled={false}
            rotateEnabled={false}
          >
            <LocationMarker coordinate={{ latitude: locationCoords.lat, longitude: locationCoords.lng }} pinColor={Colors.primary} />
          </LocationPickerMap>
          <View style={styles.mapOverlay}>
            <Text style={styles.mapOverlayText}>
              ✓ GPS coordinates attached
            </Text>
          </View>
        </View>
      )}
    </View>
  );
};

const createStyles = (Colors: any) => StyleSheet.create({
  container: {
    marginBottom: Spacing.xl,
    width: '100%',
  },
  label: {
    color: '#8A8A8E',
    fontSize: 10,
    fontWeight: '800',
    marginBottom: Spacing.sm,
    letterSpacing: 1.2,
  },
  inputRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Spacing.sm,
  },
  input: {
    flex: 1,
    height: 48,
    backgroundColor: '#1C1C1E',
    borderRadius: 12,
    paddingHorizontal: Spacing.lg,
    color: '#FFF',
    fontSize: 15,
    borderWidth: 1,
    borderColor: '#2C2C2E',
  },
  detectBtn: {
    width: 48,
    height: 48,
    borderRadius: 12,
    backgroundColor: 'rgba(255,255,255,0.04)',
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.1)',
  },
  suggestionsContainer: {
    marginTop: Spacing.sm,
    backgroundColor: '#1C1C1E',
    borderRadius: 12,
    borderColor: '#2C2C2E',
    borderWidth: 1,
    overflow: 'hidden',
  },
  suggestionItem: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: Spacing.md,
    paddingHorizontal: Spacing.md,
    borderBottomWidth: 1,
    borderBottomColor: '#2C2C2E',
  },
  suggestionText: {
    color: '#FFF',
    fontSize: 13,
    flex: 1,
  },
  mapContainer: {
    marginTop: Spacing.md,
    height: 120,
    borderRadius: 12,
    overflow: 'hidden',
    borderColor: '#2C2C2E',
    borderWidth: 1,
    position: 'relative',
  },
  map: {
    ...StyleSheet.absoluteFillObject,
  },
  mapOverlay: {
    position: 'absolute',
    bottom: 8,
    left: 8,
    backgroundColor: 'rgba(0,0,0,0.6)',
    paddingHorizontal: Spacing.sm,
    paddingVertical: Spacing.xs,
    borderRadius: 8,
  },
  mapOverlayText: {
    color: Colors.primary,
    fontSize: 10,
    fontWeight: '700',
  },
});
