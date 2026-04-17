/**
 * SupportModal.tsx — Support Portal Modal
 *
 * Self-contained modal for the support portal links (Installation Guides,
 * Store, Contact Support). Extracted from DashboardScreen.tsx inline Modal.
 *
 * Extracted from DashboardScreen.tsx (chore/refactor-dashboard-monolith).
 */
import { MaterialCommunityIcons } from '@expo/vector-icons';
import React from 'react';
import { Linking, Modal, Text, TouchableOpacity, View } from 'react-native';
import { Typography } from '../../theme/theme';

interface SupportModalProps {
  visible: boolean;
  onClose: () => void;
  Colors: any;
  styles: any;
}

const SupportModal = React.memo(({ visible, onClose, Colors, styles }: SupportModalProps) => (
  <Modal
    visible={visible}
    transparent
    animationType="fade"
    onRequestClose={onClose}
  >
    <View style={{ flex: 1, backgroundColor: 'rgba(0,0,0,0.8)', justifyContent: 'center', alignItems: 'center' }}>
      <View style={{ backgroundColor: Colors.surface, padding: 24, borderRadius: 16, width: '85%', borderWidth: 1, borderColor: Colors.surfaceHighlight }}>

        <View style={{ alignItems: 'center', marginBottom: 20 }}>
          <MaterialCommunityIcons name="lifebuoy" size={48} color={Colors.primary} />
          <Text style={{ ...Typography.title, color: Colors.primary, marginTop: 12 }}>Support Portal</Text>
          <Text style={{ color: Colors.textMuted, fontSize: 13, textAlign: 'center', marginTop: 8 }}>
            Need help configuring your hardware? Browse our official guides below.
          </Text>
        </View>

        <TouchableOpacity
          style={[styles.groupButton, { backgroundColor: 'rgba(0, 240, 255, 0.1)', borderColor: Colors.primary, borderWidth: 1, marginBottom: 16, paddingVertical: 12 }]}
          onPress={() => Linking.openURL('https://neogleamz.com/pages/getting-started')}
        >
          <View style={{ flexDirection: 'row', alignItems: 'center' }}>
            <MaterialCommunityIcons name="book-open-page-variant" size={20} color={Colors.primary} style={{ marginRight: 8 }} />
            <Text style={[styles.groupButtonText, { color: Colors.primary, fontSize: 14 }]}>Installation Guides</Text>
          </View>
        </TouchableOpacity>

        <TouchableOpacity
          style={[styles.groupButton, { backgroundColor: 'rgba(255, 170, 0, 0.1)', borderColor: '#FFAA00', borderWidth: 1, marginBottom: 16, paddingVertical: 12 }]}
          onPress={() => Linking.openURL('https://neogleamz.com')}
        >
          <View style={{ flexDirection: 'row', alignItems: 'center' }}>
            <MaterialCommunityIcons name="cart" size={20} color="#FFAA00" style={{ marginRight: 8 }} />
            <Text style={[styles.groupButtonText, { color: '#FFAA00', fontSize: 14 }]}>Visit Store</Text>
          </View>
        </TouchableOpacity>

        <TouchableOpacity
          style={[styles.groupButton, { backgroundColor: 'rgba(255, 61, 0, 0.1)', borderColor: Colors.secondary, borderWidth: 1, marginBottom: 16, paddingVertical: 12 }]}
          onPress={() => Linking.openURL('https://neogleamz.com/pages/contact')}
        >
          <View style={{ flexDirection: 'row', alignItems: 'center' }}>
            <MaterialCommunityIcons name="email-fast" size={20} color={Colors.secondary} style={{ marginRight: 8 }} />
            <Text style={[styles.groupButtonText, { color: Colors.secondary, fontSize: 14 }]}>Contact Support</Text>
          </View>
        </TouchableOpacity>

        <TouchableOpacity style={{ paddingVertical: 12, alignItems: 'center' }} onPress={onClose}>
          <Text style={{ color: Colors.textMuted, fontWeight: 'bold' }}>CLOSE</Text>
        </TouchableOpacity>
      </View>
    </View>
  </Modal>
));

export default SupportModal;
