import { MaterialCommunityIcons } from '@expo/vector-icons';
import React, { useState } from 'react';
import { Modal, NativeScrollEvent, NativeSyntheticEvent, SafeAreaView, ScrollView, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { useTheme } from '../../context/ThemeContext';
import { Layout, Spacing } from '../../theme/theme';

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
            <TouchableOpacity onPress={onDecline} style={{ padding: Spacing.xs }}>
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
          <Text style={[styles.sectionTitle, { color: Colors.primary }]}>SK8Lytz EULA Version 1.0</Text>
          
          <Text style={[styles.heading, { color: Colors.text }]}>1. Acknowledgment and Acceptance of Terms</Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted }]}>
            This Agreement is a binding legal contract between you ("User," "You," or "Your") and Neogleamz ("Company," "We," "Us," or "Our"). By downloading, installing, accessing, or utilizing the SK8Lytz App, You expressly agree to be bound by the terms and conditions set forth herein. The App is designed to control high-intensity lighting hardware; Your use of the App signifies Your complete understanding and acceptance of the physical and technical risks involved. If You do not agree to these terms, do not use the App.
          </Text>

          <Text style={[styles.heading, { color: Colors.text }]}>2. Physical Safety, Assumption of Risk, & Waiver</Text>
          <Text style={[styles.subHeading, { color: Colors.text }]}>2.1 Not Safety Equipment</Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted }]}>
            The SK8Lytz App and its associated hardware are intended strictly as an entertainment platform and are NOT classified as safety gear. The lights controlled by this App do not replace, nor do they act as substitutes for, proper safety equipment including, but not limited to, helmets, protective pads, and standard safety reflectors.
          </Text>

          <Text style={[styles.subHeading, { color: Colors.text }]}>2.2 Assumption of Risk</Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted }]}>
            USER EXPRESSLY ACKNOWLEDGES THAT ROLLER SKATING AND RELATED ACTIVITIES ARE INHERENTLY DANGEROUS. You agree that You skate entirely at Your own risk.
          </Text>

          <Text style={[styles.subHeading, { color: Colors.text }]}>2.3 Operational Hazard Warning</Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted }]}>
            User agrees to NEVER modify App settings, adjust lighting profiles, or interact with the mobile interface while in physical motion. Doing so severely increases the risk of collision, injury, or death.
          </Text>

          <Text style={[styles.heading, { color: Colors.text }]}>3. Photosensitivity and Medical Warning</Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted }]}>
            <Text style={{ fontWeight: 'bold' }}>WARNING: EPILEPSY AND SEIZURES.</Text> The App permits the operation of high-frequency flashing, strobing, and pulsating lighting effects (including, but not limited to, "Symphony" modes). A small percentage of individuals may experience epileptic seizures or blackouts when exposed to certain light patterns or flashing lights.
          </Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted, marginTop: Spacing.md }]}>
            If You, or anyone in Your family, have an epileptic condition or history of seizures, You must consult a physician prior to utilizing this App and its associated hardware.
          </Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted, marginTop: Spacing.md }]}>
            DISCONTINUE USE IMMEDIATELY and consult a physician if You experience any symptoms such as dizziness, altered vision, eye or muscle twitches, loss of awareness, disorientation, or any involuntary movement or convulsion.
          </Text>

          <Text style={[styles.heading, { color: Colors.text }]}>4. Hardware Interaction & BLE Disclaimer</Text>
          <Text style={[styles.subHeading, { color: Colors.text }]}>4.1 "As-Is" Provision</Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted }]}>
            The App and its connectivity features are provided on an "AS-IS" and "AS-AVAILABLE" basis without warranties of any kind.
          </Text>

          <Text style={[styles.subHeading, { color: Colors.text }]}>4.2 Bluetooth Connectivity</Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted }]}>
            The App utilizes Bluetooth Low Energy (BLE) to communicate with Neogleamz hardware. User acknowledges that BLE connections are subject to environmental interference, range limitations, and unexpected drops.
          </Text>

          <Text style={[styles.subHeading, { color: Colors.text }]}>4.3 Battery Estimates</Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted }]}>
            Any battery life modeling, indicators, or estimates displayed within the App are approximations. Neogleamz is not liable for unexpected hardware power failures, sudden loss of illumination, or any consequences resulting from the hardware going dark during use.
          </Text>

          <Text style={[styles.heading, { color: Colors.text }]}>5. Data Privacy & Location Services</Text>
          <Text style={[styles.subHeading, { color: Colors.text }]}>5.1 Location Data Consent</Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted }]}>
            By voluntarily enabling and utilizing specific social or discovery features within the App, including but not limited to "Street Mode" or "Crew Hub," User provides explicit consent for the App to transmit real-time GPS coordinates and location data.
          </Text>

          <Text style={[styles.subHeading, { color: Colors.text }]}>5.2 Use of Data</Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted }]}>
            This location data is utilized strictly for the functional operation of the designated features (e.g., proximity discovery and connectivity tuning).
          </Text>

          <Text style={[styles.subHeading, { color: Colors.text }]}>5.3 Third-Party Disclosure</Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted }]}>
            Neogleamz respects Your privacy. We will NOT sell, rent, or lease Your location data or personal identifiers to third-party advertisers or data brokers under any circumstances.
          </Text>

          <Text style={[styles.heading, { color: Colors.text }]}>6. Limitation of Liability</Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted }]}>
            TO THE MAXIMUM EXTENT PERMITTED BY APPLICABLE LAW, IN NO EVENT SHALL NEOGLEAMZ, ITS AFFILIATES, DIRECTORS, EMPLOYEES, OR LICENSORS BE LIABLE FOR ANY DIRECT, INDIRECT, PUNITIVE, INCIDENTAL, SPECIAL, CONSEQUENTIAL, OR EXEMPLARY DAMAGES, INCLUDING WITHOUT LIMITATION DAMAGES FOR LOSS OF PROFITS, GOODWILL, USE, DATA, OR OTHER INTANGIBLE LOSSES, THAT RESULT FROM THE USE OF, OR INABILITY TO USE, THIS APP.
          </Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted, marginTop: Spacing.md }]}>
            THIS LIMITATION OF LIABILITY FULLY APPLIES TO ANY PERSONAL INJURY, PROPERTY DAMAGE, HARDWARE DISCONNECTS, OR FAILURE OF LIGHTS DURING NAVIGATION. UNDER NO CIRCUMSTANCES WILL NEOGLEAMZ BE RESPONSIBLE FOR ANY DAMAGE, LOSS, OR INJURY RESULTING FROM HACKING, TAMPERING, OR OTHER UNAUTHORIZED ACCESS OR USE OF THE APP OR YOUR ACCOUNT.
          </Text>

          <Text style={[styles.heading, { color: Colors.text }]}>7. Dispute Resolution & Mandatory Arbitration</Text>
          <Text style={[styles.subHeading, { color: Colors.text }]}>7.1 Binding Arbitration</Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted }]}>
            Any dispute, claim, or controversy arising out of or relating to this Agreement, or the breach, termination, enforcement, interpretation, or validity thereof, including the determination of the scope or applicability of this agreement to arbitrate, shall be determined by binding arbitration rather than in court.
          </Text>

          <Text style={[styles.subHeading, { color: Colors.text }]}>7.2 Class Action Waiver</Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted }]}>
            YOU AND NEOGLEAMZ AGREE THAT EACH MAY BRING CLAIMS AGAINST THE OTHER ONLY IN YOUR OR ITS INDIVIDUAL CAPACITY, AND NOT AS A PLAINTIFF OR CLASS MEMBER IN ANY PURPORTED CLASS OR REPRESENTATIVE PROCEEDING.
          </Text>

          <Text style={[styles.heading, { color: Colors.text }]}>8. General Provisions</Text>
          <Text style={[styles.paragraph, { color: Colors.textMuted }]}>
            If any provision of this Agreement is held to be unenforceable or invalid, such provision will be changed and interpreted to accomplish the objectives of such provision to the greatest extent possible under applicable law, and the remaining provisions will continue in full force and effect.
          </Text>
          <View style={{ height: 40 }} />
        </ScrollView>

        {!isViewOnly && (
          <View style={[styles.footer, { backgroundColor: Colors.surface, borderTopColor: Colors.surfaceHighlight }]}>
            {!scrolledToBottom ? (
              <Text style={{ color: Colors.textMuted, fontSize: 13, marginBottom: Spacing.lg, textAlign: 'center' }}>
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
              <TouchableOpacity onPress={onDecline} style={{ marginTop: Spacing.lg }}>
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
    paddingHorizontal: Spacing.xl,
    paddingVertical: Spacing.lg,
    borderBottomWidth: 1,
  },
  title: { fontSize: 18, fontWeight: 'bold' },
  scrollContent: { flex: 1, padding: Spacing.xl },
  sectionTitle: { fontSize: 20, fontWeight: '900', marginBottom: Spacing.xl },
  heading: { fontSize: 15, fontWeight: 'bold', marginBottom: Spacing.sm, marginTop: Spacing.lg },
  subHeading: { fontSize: 13, fontWeight: '800', marginBottom: Spacing.xs, marginTop: Spacing.md, opacity: 0.9 },
  paragraph: { fontSize: 14, lineHeight: 22, opacity: 0.9 },
  footer: { padding: Spacing.xl, borderTopWidth: 1 },
  acceptButton: {
    paddingVertical: Spacing.lg,
    borderRadius: Layout.borderRadius,
    alignItems: 'center',
  },
  acceptButtonText: { fontSize: 16, fontWeight: 'bold' },
});
