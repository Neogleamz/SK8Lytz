import { MaterialCommunityIcons } from '@expo/vector-icons';
import * as Location from 'expo-location';
import React, { useRef, useState } from 'react';
import { ActivityIndicator, StyleSheet, Text, TextInput, TouchableOpacity, View, ScrollView } from 'react-native';
import { useTheme } from '../context/ThemeContext';
import { Spacing , ThemePalette } from '../theme/theme';
import { LocationMarker, LocationPickerMap } from './LocationPickerMap';
import { useRecentSpots, RecentSpot } from '../hooks/useRecentSpots';
import { AppLogger } from '../services/AppLogger';
import { useAppConfig } from '../context/AppConfigContext';

export interface CuratedSpot {
  id: string;
  name: string;
  lat: number;
  lng: number;
  city?: string;
  state?: string;
}

export interface SuggestionItem {
  isCurated?: boolean;
  place_id: number | string;
  name?: string;
  display_name?: string;
  lat: string | number;
  lon: string | number;
  address?: {
    amenity?: string;
    park?: string;
    road?: string;
    city?: string;
    town?: string;
  };
}

interface LocationPickerProps {
  locationLabel: string;
  onLocationLabelChange: (label: string) => void;
  locationCoords?: { lat: number; lng: number };
  onLocationCoordsChange: (coords?: { lat: number; lng: number }) => void;
  locationSpotId?: string;
  onLocationSpotIdChange?: (id?: string) => void;
  isGettingLocation: boolean;
  onDetectLocation: () => void;
  searchRadiusMi?: number;
  curatedSpots?: CuratedSpot[];
}

export const LocationPicker: React.FC<LocationPickerProps> = ({
  locationLabel, onLocationLabelChange,
  locationCoords, onLocationCoordsChange,
  locationSpotId, onLocationSpotIdChange,
  isGettingLocation, onDetectLocation,
  searchRadiusMi, curatedSpots = []
}) => {
  const { Colors } = useTheme();
  const styles = React.useMemo(() => createStyles(Colors), [Colors]);
  const { recentSpots, addRecentSpot, error, isLoading } = useRecentSpots();
  const { isVisibilityAllowed } = useAppConfig();
  const showMap = isVisibilityAllowed('visibility_maps_tab');
  
  type SearchFSMState = 'IDLE' | 'GETTING_GPS' | 'GEOCODING' | 'ERROR' | 'SUCCESS';
  const [fsmState, setFsmState] = useState<SearchFSMState>('IDLE');
  const [suggestions, setSuggestions] = useState<SuggestionItem[]>([]);
  const debounceTimer = useRef<ReturnType<typeof setTimeout> | null>(null);

  React.useEffect(() => {
    if (isGettingLocation) {
      setFsmState('GETTING_GPS');
    } else if (fsmState === 'GETTING_GPS') {
      setFsmState(locationCoords ? 'SUCCESS' : 'IDLE');
    }
  }, [isGettingLocation, locationCoords]);

  React.useEffect(() => {
    return () => {
      if (debounceTimer.current) clearTimeout(debounceTimer.current);
    };
  }, []);

  const handleSearch = (text: string) => {
    onLocationLabelChange(text);
    onLocationCoordsChange(undefined);
    if (onLocationSpotIdChange) onLocationSpotIdChange(undefined);
    
    if (debounceTimer.current) clearTimeout(debounceTimer.current);
    
    if (text.length < 2) {
      setSuggestions([]);
      setFsmState('IDLE');
      return;
    }

    debounceTimer.current = setTimeout(async () => {
      try {
        setFsmState('GEOCODING');
        
        // Fast Path: Search curated SK8Lytz spots natively
        const lowerT = text.toLowerCase();
        const localMatches = curatedSpots.filter(s => 
          s.name.toLowerCase().includes(lowerT) || 
          (s.city && s.city.toLowerCase().includes(lowerT))
        ).slice(0, 5);
        
        if (localMatches.length > 0) {
          setSuggestions(localMatches.map(m => ({
            isCurated: true,
            place_id: m.id,
            name: m.name,
            display_name: `${m.name} • ${m.city || ''} ${m.state || ''}`,
            lat: m.lat,
            lon: m.lng,
            spotData: m
          })));
          setFsmState('IDLE');
          return;
        }
        
        // Fallback Path: Nominatim OpenStreetMap Geocoder
        let viewbox = '';
        if (searchRadiusMi) {
          try {
            const loc = await Location.getLastKnownPositionAsync();
            if (loc) {
              const lat = loc.coords.latitude;
              const lon = loc.coords.longitude;
              const delta = searchRadiusMi / 69;
              viewbox = `&viewbox=${lon - delta},${lat + delta},${lon + delta},${lat - delta}&bounded=1`;
            }
          } catch (locErr: unknown) {
            AppLogger.warn('[LocationPicker] Geolocation bounds error', { error: (locErr instanceof Error ? locErr.message : String(locErr)), payload_size: 0, ssi: 0 });
          }
        }

        const url = `https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(text)}&format=json&limit=5&addressdetails=1${viewbox}`;
        const res = await fetch(url, { headers: { 'User-Agent': 'SK8Lytz App' } });
        const data = await res.json();
        setSuggestions(data || []);
        setFsmState(locationCoords ? 'SUCCESS' : 'IDLE');
      } catch (err: unknown) {
        AppLogger.warn('[LocationPicker] OSM fetch error', { error: (err instanceof Error ? err.message : String(err)), payload_size: 0, ssi: 0 });
        setFsmState('ERROR');
      }
    }, 400); // reduced latency since we have local search
  };

  const selectSuggestion = (item: SuggestionItem) => {
    try {
      let shortName = item.name || '';
      let fallbackId = undefined;
      
      if (item.isCurated) {
        shortName = item.name || '';
        fallbackId = typeof item.place_id === 'string' ? item.place_id : undefined;
      } else {
        const parts = [
          item.address?.amenity, item.address?.park, item.address?.road, item.address?.city || item.address?.town
        ].filter(Boolean);
        shortName = parts.length > 0 ? parts.join(', ') : item.name || item.display_name?.split(',')[0] || '';
      }
      
      onLocationLabelChange(shortName);
      onLocationCoordsChange({ lat: parseFloat(String(item.lat)), lng: parseFloat(String(item.lon)) });
      if (onLocationSpotIdChange) onLocationSpotIdChange(fallbackId);
      
      addRecentSpot({ id: fallbackId, name: shortName, lat: parseFloat(String(item.lat)), lng: parseFloat(String(item.lon)) });
      setSuggestions([]);
      setFsmState('SUCCESS');
    } catch (err: unknown) {
      AppLogger.warn('[LocationPicker] Suggestion selection error', { error: (err instanceof Error ? err.message : String(err)), payload_size: 0, ssi: 0 });
      setFsmState('ERROR');
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.label}>LOCATION (OPTIONAL)</Text>
      
      {/* Smart Chips List */}
      {error && <Text style={styles.errorText}>Error loading spots</Text>}
      {!locationCoords && (recentSpots.length > 0 || curatedSpots.length > 0) && (
        <ScrollView horizontal showsHorizontalScrollIndicator={false} style={{ marginBottom: Spacing.sm }} contentContainerStyle={{ gap: Spacing.sm, paddingRight: Spacing.lg }}>
            {recentSpots.map(s => (
                <TouchableOpacity key={`rx-${s.lat}-${s.lng}`} style={styles.chip}
                  onPress={() => selectSuggestion({ isCurated: true, place_id: s.id || '', name: s.name, lat: s.lat, lon: s.lng })}>
                   <MaterialCommunityIcons name="history" size={14} color={Colors.primary} />
                   <Text style={styles.chipText}>{s.name || 'Recent'}</Text>
                </TouchableOpacity>
            ))}
            {curatedSpots.slice(0, 5).map(s => (
                <TouchableOpacity key={`cx-${s.id}`} style={styles.chip}
                  onPress={() => selectSuggestion({ isCurated: true, place_id: s.id, name: s.name, lat: s.lat, lon: s.lng })}>
                   <Text style={styles.chipText}>{s.name}</Text>
                </TouchableOpacity>
            ))}
        </ScrollView>
      )}
      
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
          onPress={() => {
            setFsmState('GETTING_GPS');
            onDetectLocation();
          }}
          disabled={fsmState === 'GETTING_GPS'}
        >
          {fsmState === 'GETTING_GPS' || fsmState === 'GEOCODING' ? (
            <ActivityIndicator size="small" color={Colors.primary} />
          ) : (
            <MaterialCommunityIcons name="crosshairs-gps" size={20} color={locationCoords ? Colors.primary : Colors.textMuted} />
          )}
        </TouchableOpacity>
      </View>

      {/* Suggestions List dropdown with full 4-state matrix */}
      {!locationCoords && (
        fsmState === 'GEOCODING' ? (
          <View style={[styles.suggestionsContainer, { padding: Spacing.md, alignItems: 'center' }]}>
            <ActivityIndicator size="small" color={Colors.primary} />
            <Text style={[styles.suggestionText, { marginTop: Spacing.sm, textAlign: 'center', color: Colors.textMuted }]}>Searching locations...</Text>
          </View>
        ) : fsmState === 'ERROR' ? (
          <View style={[styles.suggestionsContainer, { padding: Spacing.md, alignItems: 'center' }]}>
            <MaterialCommunityIcons name="alert-circle-outline" size={24} color="#FF453A" />
            <Text style={[styles.suggestionText, { marginTop: Spacing.sm, textAlign: 'center', color: '#FF453A' }]}>Failed to load location suggestions.</Text>
          </View>
        ) : suggestions.length > 0 ? (
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
        ) : locationLabel.length >= 2 && fsmState === 'IDLE' ? (
          <View style={[styles.suggestionsContainer, { padding: Spacing.md, alignItems: 'center' }]}>
            <Text style={[styles.suggestionText, { textAlign: 'center', color: Colors.textMuted }]}>No matching locations found</Text>
          </View>
        ) : null
      )}

      {/* Map Thumbnail */}
      {locationCoords && (
        showMap ? (
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
        ) : (
          <View style={{ marginTop: Spacing.md, padding: Spacing.md, backgroundColor: 'rgba(0, 200, 100, 0.1)', borderRadius: 12, borderWidth: 1, borderColor: 'rgba(0, 200, 100, 0.3)', flexDirection: 'row', alignItems: 'center' }}>
            <MaterialCommunityIcons name="check-circle" size={16} color="#00C864" style={{ marginRight: Spacing.sm }} />
            <Text style={{ color: '#00C864', fontSize: 13, fontWeight: '600' }}>
              Address selected successfully
            </Text>
          </View>
        )
      )}
    </View>
  );
};

const createStyles = (Colors: ThemePalette) => StyleSheet.create({
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
  errorText: {
    color: '#FF453A',
    fontSize: 12,
    marginBottom: Spacing.sm,
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
  chip: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: 'rgba(255,255,255,0.06)',
    paddingHorizontal: Spacing.md,
    paddingVertical: Spacing.sm,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.1)',
    gap: Spacing.xxs,
  },
  chipText: {
    color: '#FFF',
    fontSize: 12,
    fontWeight: '600',
  },
});
