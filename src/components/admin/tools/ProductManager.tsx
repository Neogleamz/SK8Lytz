import React from 'react';
import { View, Text, Switch, TouchableOpacity, ScrollView, Modal, SafeAreaView, TextInput, ViewStyle, TextStyle } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Spacing } from '../../../theme/theme';
import { adminStyles as styles } from '../adminStyles';

export interface ProductManagerProps {
  visible: boolean;
  onClose: () => void;
  bg: string;
  cardBg: string;
  borderColor: string;
  textPrimary: string;
  textMuted: string;
  allProfiles: any[];
  editingProfile: any;
  startEditing: (p: any) => void;
  createNew: () => void;
  patchEdit: (d: any) => void;
  saveProduct: () => Promise<any>;
  productSaving: boolean;
}

export const ProductManager = React.memo(({
  visible, onClose, bg, cardBg, borderColor, textPrimary, textMuted,
  allProfiles, editingProfile, startEditing, createNew, patchEdit, saveProduct, productSaving
}: ProductManagerProps) => {

  const fieldWrapperStyle: ViewStyle = { marginBottom: Spacing.lg, backgroundColor: cardBg, padding: Spacing.md, borderRadius: 8, borderLeftWidth: 3, borderLeftColor: '#9D4EFF' };
  const fieldLabelStyle: TextStyle = { color: textMuted, fontSize: 11, fontWeight: '700', textTransform: 'uppercase', marginBottom: Spacing.sm, letterSpacing: 0.5 };
  const fieldInputStyle: TextStyle = { color: textPrimary, fontSize: 16, padding: 0, fontWeight: '600' };

  const renderField = (label: string, key: string, placeholder: string, keyboardType: any = 'default') => (
    <View style={fieldWrapperStyle}>
      <Text style={fieldLabelStyle}>{label}</Text>
      <TextInput
        style={fieldInputStyle}
        value={editingProfile?.[key]?.toString() || ''}
        onChangeText={v => {
          if (keyboardType === 'numeric') {
            patchEdit({ [key]: v ? Number(v.replace(/[^0-9.-]/g, '')) : 0 });
          } else {
            patchEdit({ [key]: v });
          }
        }}
        placeholder={placeholder}
        placeholderTextColor={textMuted}
        keyboardType={keyboardType}
        autoCapitalize={keyboardType === 'default' ? 'characters' : 'none'}
      />
    </View>
  );

  const renderToggle = (label: string, key: string, desc: string) => (
    <View style={[fieldWrapperStyle, { flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }]}>
      <View style={{ flex: 1, paddingRight: Spacing.md }}>
        <Text style={fieldLabelStyle}>{label}</Text>
        <Text style={{ color: textMuted, fontSize: 11 }}>{desc}</Text>
      </View>
      <Switch 
        value={!!editingProfile?.[key]} 
        onValueChange={v => patchEdit({ [key]: v })}
        trackColor={{ false: '#333', true: '#FF5A00' }}
      />
    </View>
  );

  return (
    <View style={{ flex: 1 }}>
      <SafeAreaView style={{ flex: 1, backgroundColor: bg }}>
        <View style={{ flexDirection: 'row', alignItems: 'center', padding: Spacing.lg, borderBottomWidth: 1, borderBottomColor: borderColor, backgroundColor: cardBg }}>
          <TouchableOpacity onPress={onClose} style={{ marginRight: Spacing.lg, padding: Spacing.xs }}>
            <MaterialCommunityIcons name="arrow-left" size={24} color="#FF5A00" />
          </TouchableOpacity>
          <View style={{ flex: 1 }}>
            <Text style={[styles.title, { color: textPrimary }]}>📦 PRODUCT MANAGER</Text>
            <Text style={{ color: textMuted, fontSize: 11, marginTop: Spacing.xxs }}>Edit hardware product catalog entries</Text>
          </View>
        </View>

        <View style={{ flex: 1 }}>
          {/* ── PILL SELECTOR ── */}
          <View style={{ borderBottomWidth: 1, borderBottomColor: borderColor }}>
            <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={{ paddingHorizontal: Spacing.lg, paddingVertical: Spacing.md, gap: Spacing.md }}>
              {allProfiles.map((p: any) => {
                const isActive = editingProfile?.id === p.id;
                return (
                  <TouchableOpacity key={p.id} onPress={() => startEditing(p)}
                    style={{ paddingHorizontal: Spacing.lg, paddingVertical: Spacing.sm, borderRadius: 20, backgroundColor: isActive ? '#9D4EFF' : '#333' }}>
                    <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm }}>
                      {p.brandIcon && <MaterialCommunityIcons name={p.brandIcon as any} size={14} color="#FFF" />}
                      <Text style={{ color: '#FFF', fontWeight: '600' }}>{p.displayName || p.id}</Text>
                    </View>
                  </TouchableOpacity>
                )
              })}
              <TouchableOpacity onPress={createNew}
                style={{ paddingHorizontal: Spacing.lg, paddingVertical: Spacing.sm, borderRadius: 20, borderWidth: 1, borderColor: '#FF5A00', borderStyle: 'dashed', backgroundColor: (editingProfile && !editingProfile.id) ? 'rgba(255,90,0,0.2)' : 'transparent' }}>
                <Text style={{ color: '#FF5A00', fontWeight: '800' }}>+ ADD NEW</Text>
              </TouchableOpacity>
            </ScrollView>
          </View>

          {/* ── EDITOR ── */}
          {editingProfile ? (
            <ScrollView style={{ flex: 1, padding: Spacing.lg }}>
              <Text style={{ color: textPrimary, fontSize: 18, fontWeight: '800', marginBottom: Spacing.lg }}>
                {editingProfile.id ? 'EDIT PROFILE' : 'NEW HARDWARE PROFILE'}
              </Text>

              {renderField('Product ID (Hardware Ref)', 'id', 'e.g. SK8LYTZ_V2')}
              {renderField('Display Name', 'displayName', 'e.g. SK8Lytz Pro')}
              {renderField('Brand Icon', 'brandIcon', 'e.g. circle-double')}
              {renderField('Brand Color Hex', 'vizThemeColor', 'e.g. #FF5A00')}

              <Text style={{ color: textMuted, fontWeight: '800', marginTop: Spacing.md, marginBottom: Spacing.sm }}>HARDWARE DEFAULTS</Text>
              {renderField('Default LEDs', 'defaultLedPoints', '16', 'numeric')}
              {renderField('Virtual Segments', 'defaultSegments', '1', 'numeric')}
              {renderField('IC Type (1=WS2812B)', 'defaultIcType', '1', 'numeric')}
              {renderField('Color Sorting (2=GRB)', 'defaultColorSorting', '2', 'numeric')}
              {renderField('Battery Capacity (mAh)', 'batteryCapacityMilliAmpereHour', '3000', 'numeric')}
              {renderToggle('Customizable Profile', 'hardwareAllowsCustomPoints', 'Allow user to cut and set custom length')}

              <Text style={{ color: textMuted, fontWeight: '800', marginTop: Spacing.md, marginBottom: Spacing.sm }}>AUTODETECT THRESHOLDS</Text>
              {renderField('Min HW Points', 'detectMinPoints', '1', 'numeric')}
              {renderField('Max HW Points', 'detectMaxPoints', '99', 'numeric')}

              <Text style={{ color: textMuted, fontWeight: '800', marginTop: Spacing.md, marginBottom: Spacing.sm }}>VISUALIZER GEOMETRY</Text>
              {renderField('Canvas Shape', 'vizShape', 'OVAL | RING | DUAL_STRIP')}
              {renderField('Base Width', 'vizBaseWidth', '55', 'numeric')}
              {renderField('Base Height', 'vizBaseHeight', '115', 'numeric')}
              {renderField('Blob Diameter (mm)', 'vizBlobDiameterMm', '5.7', 'numeric')}
              {renderField('Visualizer Default Points', 'vizDefaultPoints', '16', 'numeric')}
              {renderField('Strip Count (Railz)', 'vizStripCount', '2', 'numeric')}
              {renderField('Strip Separation (Railz)', 'vizStripSeparation', '32', 'numeric')}
              {renderField('Strip Orientation', 'vizStripOrientation', 'VERTICAL | HORIZONTAL')}
              {renderToggle('Mirrored Render', 'vizIsMirrored', 'Mirror Seg2 over Seg1')}

              <TouchableOpacity 
                onPress={saveProduct} 
                disabled={productSaving}
                style={{ backgroundColor: '#9D4EFF', padding: Spacing.lg, borderRadius: 12, alignItems: 'center', marginTop: Spacing.xl, marginBottom: Spacing.xxxl }}
              >
                <Text style={{ color: '#FFF', fontWeight: '800', fontSize: 15 }}>
                  {productSaving ? 'SAVING...' : '💾  SAVE TO CATALOG'}
                </Text>
              </TouchableOpacity>
            </ScrollView>
          ) : (
            <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', padding: Spacing.xxl }}>
              <MaterialCommunityIcons name="cube-outline" size={48} color={borderColor} style={{ marginBottom: Spacing.lg }} />
              <Text style={{ color: textMuted, textAlign: 'center', fontWeight: '600' }}>
                Select a hardware profile from the top menu to view or edit its settings.
              </Text>
            </View>
          )}
        </View>
      </SafeAreaView>
    </View>
  );
});
