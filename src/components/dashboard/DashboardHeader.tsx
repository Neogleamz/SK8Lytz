/**
 * DashboardHeader.tsx — SK8Lytz Dashboard Header
 *
 * Renders both the connected and disconnected header variants.
 * Connected: Back button, logo + connection status, power/support/theme icons.
 * Disconnected: User pill, centered logo, support/theme icons.
 *
 * Extracted from the `renderDashboardHeader()` inline function in
 * DashboardScreen.tsx (chore/refactor-dashboard-monolith).
 */
import { MaterialCommunityIcons } from '@expo/vector-icons';
import React from 'react';
import { Image, Text, TouchableOpacity, View, Platform } from 'react-native';
import { Layout, Spacing , ThemePalette } from '../../theme/theme';

interface DashboardHeaderProps {
  isActuallyConnected: boolean;
  isOfflineMode: boolean;
  isDark: boolean;
  isAdmin: boolean;
  // Connected variant
  displayConnectedDevices: any[];
  customGroups: any[];
  powerStates: Record<string, boolean>;
  handleDisconnect: () => void;
  onReconnectDevice?: (mac: string) => void;
  onPressAdminTools: () => void;
  onPressSupport: () => void;
  onPressTheme: () => void;
  showBackButton?: boolean;
  // Disconnected variant
  authUsername: string | null;
  onPressAccount: () => void;
  // Safe area
  insetTop: number;
  Colors: ThemePalette;
}

const DashboardHeader = React.memo(({
  isActuallyConnected,
  isOfflineMode,
  isDark,
  isAdmin,
  displayConnectedDevices,
  customGroups,
  powerStates,
  handleDisconnect,
  onReconnectDevice,
  onPressAdminTools,
  onPressSupport,
  onPressTheme,
  showBackButton = true,
  authUsername,
  onPressAccount,
  insetTop,
  Colors,
}: DashboardHeaderProps) => {
  return (
    <View style={{
      paddingHorizontal: Layout.padding,
      paddingTop: insetTop + 12,
      paddingBottom: isActuallyConnected ? 2 : 8,
    }}>
      {isActuallyConnected ? (
        <View style={{ flexDirection: 'row', alignItems: 'center' }}>
          {/* LEFT: close button + user pill */}
          <View style={{ flex: 1, flexDirection: 'row', alignItems: 'center', gap: Spacing.xs }}>
            {showBackButton && (
              <TouchableOpacity onPress={handleDisconnect} style={{ padding: Spacing.xs, marginLeft: -Spacing.xs }}>
                <MaterialCommunityIcons name="chevron-down" size={28} color={Colors.text} />
              </TouchableOpacity>
            )}
            <TouchableOpacity
              onPress={onPressAccount}
              style={{
                flexDirection: 'row', alignItems: 'center',
                paddingHorizontal: Spacing.sm, paddingVertical: Spacing.xs,
                borderRadius: 16, borderWidth: 1, gap: Spacing.xs,
                borderColor: isOfflineMode ? 'rgba(255,170,0,0.35)' : 'rgba(0,240,255,0.25)',
                backgroundColor: isOfflineMode ? 'rgba(255,170,0,0.08)' : 'rgba(0,240,255,0.06)',
              }}
            >
              <View style={[{
                width: 6, height: 6, borderRadius: 3,
                backgroundColor: isOfflineMode ? '#FFA500' : Colors.success,
              }, Platform.OS === 'web' 
                ? { boxShadow: `0px 0px 4px ${isOfflineMode ? '#FFA500' : Colors.success}` } as Record<string, string>
                : { shadowColor: isOfflineMode ? '#FFA500' : Colors.success, shadowOpacity: 0.8, shadowRadius: 4, elevation: 2 }
              ]} />
              <Text style={{ color: Colors.text, fontSize: 10, fontWeight: '700', maxWidth: 55, fontFamily: 'Righteous' }} numberOfLines={1}>
                {authUsername || 'GUEST'}
              </Text>
              <MaterialCommunityIcons name="account-cog" size={12} color={Colors.textMuted} style={{ opacity: 0.8 }} />
            </TouchableOpacity>
          </View>

          {/* CENTER: logo + connection status */}
          <TouchableOpacity activeOpacity={0.7} style={{ position: 'relative', alignItems: 'center' }} onPress={isAdmin ? onPressAdminTools : undefined}>
            <Image source={require('../../../assets/logo.png')} style={{ width: 80, height: 24 }} resizeMode="contain" tintColor={Colors.text} />
            {(() => {
              const connectedCount = displayConnectedDevices.length;
              let expectedCount = 1;
              const firstDevice = displayConnectedDevices[0];
              let groupIds: string[] = [];

              if (firstDevice?.grouped && firstDevice?.groupId) {
                const group = customGroups.find(g => g.id === firstDevice.groupId);
                if (group) {
                  expectedCount = group.deviceIds.length;
                  groupIds = group.deviceIds;
                }
              }

              // Solo Device View
              if (expectedCount <= 1) {
                const statusColor = connectedCount === 0 ? Colors.error : Colors.success;
                return (
                  <View style={{ flexDirection: 'row', alignItems: 'center', marginTop: Spacing.xxs }}>
                    <View style={{ width: 4, height: 4, borderRadius: 2, backgroundColor: statusColor, marginRight: Spacing.xs }} />
                    <Text style={{ color: statusColor, fontSize: 8, fontWeight: 'bold', letterSpacing: 0.5 }}>
                      CONNECTED
                    </Text>
                  </View>
                );
              }

              // Group Device View - Render Inline Skate Icons
              const visibleGroupIds = groupIds.length > 4 ? groupIds.slice(0, 4) : groupIds;
              const remainingCount = groupIds.length > 4 ? groupIds.length - 4 : 0;

              return (
                <View style={{ flexDirection: 'row', alignItems: 'center', marginTop: Spacing.xxs, gap: 4 }}>
                  {visibleGroupIds.map((mac) => {
                    const isConnected = displayConnectedDevices.some(d => d.id.toUpperCase() === mac.toUpperCase());
                    const iconColor = isConnected ? Colors.success : '#555555';
                    
                    return (
                      <TouchableOpacity 
                        key={`header-skate-${mac}`} 
                        disabled={isConnected || !onReconnectDevice}
                        onPress={() => onReconnectDevice && onReconnectDevice(mac)}
                        hitSlop={{ top: 10, bottom: 10, left: 5, right: 5 }}
                      >
                        <MaterialCommunityIcons 
                          name="roller-skate" 
                          size={12} 
                          color={iconColor}
                        />
                      </TouchableOpacity>
                    );
                  })}
                  {remainingCount > 0 && (
                    <Text style={{ color: Colors.textMuted, fontSize: 9, fontWeight: 'bold', marginLeft: 2 }}>
                      +{remainingCount}
                    </Text>
                  )}
                </View>
              );
            })()}
          </TouchableOpacity>

          {/* RIGHT: support / theme */}
          <View style={{ flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'flex-end', gap: Spacing.sm }}>
            <TouchableOpacity
              onPress={onPressSupport}
              style={{ width: 34, height: 34, borderRadius: 17, borderWidth: 1, borderColor: 'rgba(255,255,255,0.15)', backgroundColor: 'rgba(255,255,255,0.07)', alignItems: 'center', justifyContent: 'center' }}
            >
              <MaterialCommunityIcons name="help-circle-outline" size={18} color={Colors.textMuted} />
            </TouchableOpacity>
            <TouchableOpacity
              onPress={onPressTheme}
              style={{ width: 34, height: 34, borderRadius: 17, borderWidth: 1, borderColor: 'rgba(255,255,255,0.15)', backgroundColor: 'rgba(255,255,255,0.07)', alignItems: 'center', justifyContent: 'center' }}
            >
              <MaterialCommunityIcons name={isDark ? 'weather-sunny' : 'weather-night'} size={18} color={Colors.primary} />
            </TouchableOpacity>
          </View>
        </View>
      ) : (
        /* ── Disconnected: 3-column layout ── */
        <View style={{ flexDirection: 'row', alignItems: 'center' }}>
          {/* LEFT: user pill */}
          <View style={{ flex: 1, flexDirection: 'row', alignItems: 'center' }}>
            <TouchableOpacity
              onPress={onPressAccount}
              style={{
                flexDirection: 'row', alignItems: 'center',
                paddingHorizontal: Spacing.sm, paddingVertical: Spacing.xs,
                borderRadius: 16, borderWidth: 1, gap: Spacing.xs,
                borderColor: isOfflineMode ? 'rgba(255,170,0,0.35)' : 'rgba(0,240,255,0.25)',
                backgroundColor: isOfflineMode ? 'rgba(255,170,0,0.08)' : 'rgba(0,240,255,0.06)',
              }}
            >
              <View style={[{
                width: 6, height: 6, borderRadius: 3,
                backgroundColor: isOfflineMode ? '#FFA500' : Colors.success,
              }, Platform.OS === 'web' 
                ? { boxShadow: `0px 0px 4px ${isOfflineMode ? '#FFA500' : Colors.success}` } as Record<string, string>
                : { shadowColor: isOfflineMode ? '#FFA500' : Colors.success, shadowOpacity: 0.8, shadowRadius: 4, elevation: 2 }
              ]} />
              <Text style={{ color: Colors.text, fontSize: 10, fontWeight: '700', maxWidth: 55, fontFamily: 'Righteous' }} numberOfLines={1}>
                {authUsername || 'GUEST'}
              </Text>
              <MaterialCommunityIcons name="account-cog" size={12} color={Colors.textMuted} style={{ opacity: 0.8 }} />
            </TouchableOpacity>
          </View>

          {/* CENTER: logo */}
          <TouchableOpacity activeOpacity={0.7} style={{ alignItems: 'center' }} onPress={isAdmin ? onPressAdminTools : undefined}>
            <Image source={require('../../../assets/logo.png')} style={{ width: 85, height: 26 }} resizeMode="contain" tintColor={Colors.text} />
          </TouchableOpacity>

          {/* RIGHT: support + theme */}
          <View style={{ flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'flex-end', gap: Spacing.sm }}>
            <TouchableOpacity
              style={{ width: 32, height: 32, borderRadius: 16, borderWidth: 1, borderColor: 'rgba(255,255,255,0.15)', backgroundColor: 'rgba(255,255,255,0.07)', alignItems: 'center', justifyContent: 'center' }}
              onPress={onPressSupport}
            >
              <MaterialCommunityIcons name="help-circle-outline" size={16} color={Colors.textMuted} />
            </TouchableOpacity>
            <TouchableOpacity
              onPress={onPressTheme}
              style={{ width: 32, height: 32, borderRadius: 16, borderWidth: 1, borderColor: 'rgba(255,255,255,0.15)', backgroundColor: 'rgba(255,255,255,0.07)', alignItems: 'center', justifyContent: 'center' }}
            >
              <MaterialCommunityIcons name={isDark ? 'weather-sunny' : 'weather-night'} size={16} color={Colors.primary} />
            </TouchableOpacity>
          </View>
        </View>
      )}

      {/* Accent line under logo when disconnected */}
      {!isActuallyConnected && (
        <View style={{ height: 2, width: 30, backgroundColor: Colors.secondary, marginTop: Spacing.sm, borderRadius: 1, alignSelf: 'center' }} />
      )}
    </View>
  );
});

export default DashboardHeader;
