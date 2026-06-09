import React from 'react';
import { View, TouchableOpacity } from 'react-native';
import { Spacing } from '../../../../theme/theme';
import { QUICK_PALETTE } from './DiagnosticLabConstants';

export const QuickColorGrid = ({ onSelect, activeColor }: { onSelect: (c: {r:number,g:number,b:number}) => void, activeColor?: {r:number,g:number,b:number} }) => {
  return (
    <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.sm, marginBottom: Spacing.md }}>
      {QUICK_PALETTE.map((c, i) => {
        const isActive = activeColor && activeColor.r === c.r && activeColor.g === c.g && activeColor.b === c.b;
        return (
          <TouchableOpacity 
            key={i} 
            onPress={() => onSelect({ r: c.r, g: c.g, b: c.b })}
            style={{ 
              width: 32, height: 32, borderRadius: 16, backgroundColor: c.hex,
              borderWidth: 2, borderColor: isActive ? '#00f0ff' : 'rgba(255,255,255,0.1)',
              justifyContent: 'center', alignItems: 'center'
            }}
          />
        );
      })}
    </View>
  );
};
