import React from 'react';
import { Text, View } from 'react-native';
import { Spacing } from '../../../../theme/theme';
import { useDiagnosticLabStyles } from './DiagnosticLabStyles';

interface HwBadgeProps {
  targetDeviceId: string | null;
  connectedDevices: any[];
  hwSettings?: {
    ledPoints?: number;
    colorSorting?: number;
    colorSortingName?: string;
    icName?: string;
    segments?: number;
    detected?: boolean;
  };
}

export function DiagnosticLabHwBadge({ targetDeviceId, connectedDevices, hwSettings }: HwBadgeProps) {
  const { S } = useDiagnosticLabStyles();
  
  const targetName = targetDeviceId ? connectedDevices.find(d => d.id === targetDeviceId)?.name || targetDeviceId.slice(-6) : null;
  
  return (
    <View style={S.hwBadge}>
      <Text style={S.hwBadgeLabel}>TARGET: </Text>
      {targetDeviceId ? (
        <>
          <Text style={[S.hwBadgeVal, { color: '#00ccff', paddingRight: Spacing.sm }]}>{targetName}</Text>
          {hwSettings?.detected ? (
            <>
              <Text style={[S.hwBadgeVal, { color: '#00CC88' }]}>{hwSettings.ledPoints ?? '?'} LEDs</Text>
              <Text style={S.hwBadgeLabel}> · </Text>
              <Text style={[S.hwBadgeVal, { color: '#FF9500' }]}>{hwSettings.colorSortingName ?? '?'}</Text>
              <Text style={S.hwBadgeLabel}> · </Text>
              <Text style={[S.hwBadgeVal, { color: '#c084fc' }]}>{hwSettings.icName ?? '?'}</Text>
            </>
          ) : (
           <Text style={[S.hwBadgeVal, { color: '#FF4040' }]}>WAITING 0x63...</Text>
          )}
        </>
      ) : (
        <Text style={[S.hwBadgeVal, { color: '#FF4040' }]}>NO DEVICE TARGETED (GO TO DEVICES TAB)</Text>
      )}
    </View>
  );
}
