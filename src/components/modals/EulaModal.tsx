import React, { useState } from 'react';
import { View, Text, Modal, StyleSheet, ScrollView, TouchableOpacity, SafeAreaView, NativeSyntheticEvent, NativeScrollEvent } from 'react-native';
import { useTheme } from '../../context/ThemeContext';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Layout } from '../../theme/theme';

interface EulaModalProps {
  visible: boolean;
  onAccept: () => void;
  onDecline?: () => void;
  isViewOnly?: boolean;
}

export default function EulaModal({ visible, onAccept, onDecline, isViewOnly = false }: EulaModalProps) {
  const { Colors } = useTheme();
  const [scrolledToBottom, setScrolledToBottom] = useState(false);

  const handleScroll = (event: NativeSyntheticEvent<NativeScrollEvent>) => {
    const { layoutMeasurement, contentOffset, contentSize } = event.nativeEvent;
    const paddingToBottom = 40;
    if (layoutMeasurement.height + contentOffset.y >= contentSize.height - paddingToBottom) {
      setScrolledToBottom(true);
    }
  };

  return (
    <Modal visible={visible} animationType="slide" transparent={false}>
      <SafeAreaView style={[styles.container, { backgroundColor: Colors.background }]}>
        <View style={[styles.header, { borderBottomColor: Colors.surfaceHighlight }]}>
          <Text style={[styles.title, { color: Colors.text }]}>End User License Agreement</Text>
          {isViewOnly && onDecline && (
            <TouchableOpacity onPress={onDecline} style={{ padding: 4 }}>
              <MaterialCommunityIcons name="close" size={24} color={Colors.textMuted} />
            </TouchableOpacity>
          )}
        </View>

        <ScrollView 
          style={styles.scrollContent} 
          onScroll={handleScroll} 
          scrollEventThrottle={16}
          indicatorStyle="white"
        >
          <Text style={[styles.sectionTitle, { color: Colors.primary }]}>SK8Lytz "Rock Solid" EULA Version 1.0</Text>
          
          <Text style={[styles.heading, { color: Colors.text }]}>Section 1: Acceptance</Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted }]}>
            By downloading or using SK8Lytz, you agree to be bound by these terms. This App controls high-intensity lighting hardware; your use signifies your understanding of the physical risks involved.
          </Text>

          <Text style={[styles.heading, { color: Colors.text }]}>Section 2: Physical Safety & Waiver</Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted }]}>
            SK8Lytz is an entertainment platform, NOT safety gear. The lights do not replace helmets, pads, or reflectors. You acknowledge that skating is inherently dangerous and you "skate at your own risk." NEVER modify app settings while in motion.
          </Text>

          <Text style={[styles.heading, { color: Colors.text }]}>Section 3: Photosensitivity Warning</Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted }]}>
            App permits high-frequency flashing/strobing (especially Symphony modes). Individuals with epilepsy should consult a physician before use. Discontinue if you experience dizziness or disorientation.
          </Text>

          <Text style={[styles.heading, { color: Colors.text }]}>Section 4: Hardware & BLE Disclaimer</Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted }]}>
            App provided "As-Is." BLE connections may drop due to interference. Battery modeling is an approximation; Neogleamz is not liable for unexpected hardware darkness.
          </Text>

          <Text style={[styles.heading, { color: Colors.text }]}>Section 5: Data & Privacy</Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted }]}>
            By using Street Mode/Crew Hub, you consent to real-time GPS coordinates transmission for discovery. Data is used for connectivity tuning and will NOT be sold to 3rd party advertisers.
          </Text>

          <Text style={[styles.heading, { color: Colors.text }]}>Section 6: Limitation of Liability</Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted }]}>
            Neogleamz is NOT liable for direct or indirect damages (including personal injury) resulting from app use, hardware disconnects, or failure of lights during navigation.
          </Text>

          <Text style={[styles.heading, { color: Colors.text }]}>Section 7: Mandatory Arbitration</Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted }]}>
            Any disputes shall be resolved through binding arbitration. You waive the right to class-action lawsuits.
          </Text>
          <View style={{ height: 40 }} />
        </ScrollView>

        {!isViewOnly && (
          <View style={[styles.footer, { backgroundColor: Colors.surface, borderTopColor: Colors.surfaceHighlight }]}>
            {!scrolledToBottom ? (
              <Text style={{ color: Colors.textMuted, fontSize: 13, marginBottom: 16, textAlign: 'center' }}>
                Please scroll to the bottom to accept.
              </Text>
            ) : null}
            <TouchableOpacity 
              style={[
                styles.acceptButton, 
                { backgroundColor: scrolledToBottom ? Colors.primary : Colors.surfaceHighlight }
              ]} 
              disabled={!scrolledToBottom}
              onPress={onAccept}
            >
              <Text style={[
                styles.acceptButtonText, 
                { color: scrolledToBottom ? '#000' : Colors.textMuted }
              ]}>
                I ACCEPT
              </Text>
            </TouchableOpacity>
            {onDecline && (
              <TouchableOpacity onPress={onDecline} style={{ marginTop: 16 }}>
                <Text style={{ color: Colors.textMuted, textAlign: 'center', fontSize: 13 }}>DECLINE & EXIT</Text>
              </TouchableOpacity>
            )}
          </View>
        )}
      </SafeAreaView>
    </Modal>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 20,
    paddingVertical: 16,
    borderBottomWidth: 1,
  },
  title: { fontSize: 18, fontWeight: 'bold' },
  scrollContent: { flex: 1, padding: 24 },
  sectionTitle: { fontSize: 20, fontWeight: '900', marginBottom: 24 },
  heading: { fontSize: 15, fontWeight: 'bold', marginBottom: 8, marginTop: 16 },
  paragraph: { fontSize: 14, lineHeight: 22, opacity: 0.9 },
  footer: { padding: 24, borderTopWidth: 1 },
  acceptButton: {
    paddingVertical: 16,
    borderRadius: Layout.borderRadius,
    alignItems: 'center',
  },
  acceptButtonText: { fontSize: 16, fontWeight: 'bold' },
});
