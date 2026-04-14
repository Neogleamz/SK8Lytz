import { MaterialCommunityIcons } from '@expo/vector-icons';
import { LinearGradient } from 'expo-linear-gradient';
import React from 'react';
import {
    Dimensions,
    Modal,
    Platform,
    ScrollView,
    StyleSheet,
    Text,
    TouchableOpacity,
    View,
} from 'react-native';
import { Spacing, Typography } from '../../theme/theme';

const { width } = Dimensions.get('window');

interface Props {
  isVisible: boolean;
  onDismiss: () => void;
}

const COMMAND_GROUPS = [
  {
    icon: 'lightning-bolt',
    title: 'MODES',
    commands: ['"Street Mode"', '"Builder Mode"'],
  },
  {
    icon: 'palette',
    title: 'COLORS',
    commands: ['"Set white"', '"Glow blue"'],
  },
  {
    icon: 'tune',
    title: 'SETTINGS',
    commands: ['"Brightness 50"', '"Speed 10"'],
  },
  {
    icon: 'format-section',
    title: 'SPATIAL',
    commands: ['"Red in the back"', '"White in the front"'],
  },
  {
    icon: 'heart',
    title: 'FAVORITES',
    commands: ['"Play Rainbow"', '"Load Sunset"'],
  },
];

export default function VoiceTutorialModal({ isVisible, onDismiss }: Props) {
  return (
    <Modal
      visible={isVisible}
      transparent={true}
      animationType="fade"
      onRequestClose={onDismiss}
    >
      <View style={styles.overlay}>
        <View style={styles.container}>
          <LinearGradient
            colors={['rgba(0, 240, 255, 0.15)', 'rgba(112, 0, 255, 0.1)']}
            style={styles.headerGradient}
          >
            <MaterialCommunityIcons name="microphone-message" size={48} color="#00F0FF" />
            <Text style={styles.title}>What can I say?</Text>
            <Text style={styles.subtitle}>Hands-free lighting control</Text>
          </LinearGradient>

          <ScrollView style={styles.content} showsVerticalScrollIndicator={false}>
            {COMMAND_GROUPS.map((group, idx) => (
              <View key={idx} style={styles.commandGroup}>
                <View style={styles.groupHeader}>
                  <MaterialCommunityIcons name={group.icon as any} size={18} color="#00F0FF" />
                  <Text style={styles.groupTitle}>{group.title}</Text>
                </View>
                <View style={styles.commandsList}>
                  {group.commands.map((cmd, cIdx) => (
                    <Text key={cIdx} style={styles.commandText}>{cmd}</Text>
                  ))}
                </View>
              </View>
            ))}
          </ScrollView>

          <View style={styles.footer}>
            <TouchableOpacity 
              style={styles.dismissButton} 
              onPress={onDismiss}
              activeOpacity={0.8}
            >
              <LinearGradient
                colors={['#00F0FF', '#7000FF']}
                start={{ x: 0, y: 0 }}
                end={{ x: 1, y: 1 }}
                style={styles.buttonGradient}
              >
                <Text style={styles.buttonText}>GOT IT</Text>
              </LinearGradient>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    </Modal>
  );
}

const styles = StyleSheet.create({
  overlay: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.85)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  container: {
    width: width * 0.85,
    maxHeight: '80%',
    backgroundColor: '#1A1D23',
    borderRadius: 24,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.1)',
  },
  headerGradient: {
    padding: Spacing.xxl,
    alignItems: 'center',
    borderBottomWidth: 1,
    borderBottomColor: 'rgba(255,255,255,0.05)',
  },
  title: {
    ...Typography.title,
    color: '#00F0FF',
    marginTop: Spacing.lg,
    fontSize: 24,
    fontWeight: '900',
    fontFamily: 'Righteous',
  },
  subtitle: {
    color: '#FFF',
    opacity: 0.6,
    fontSize: 14,
    marginTop: Spacing.xs,
    fontWeight: '600',
    letterSpacing: 0.5,
  },
  content: {
    padding: Spacing.xl,
  },
  commandGroup: {
    marginBottom: Spacing.xl,
  },
  groupHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: Spacing.md,
    gap: Spacing.sm,
  },
  groupTitle: {
    fontSize: 12,
    fontWeight: '900',
    color: 'rgba(255,255,255,0.4)',
    letterSpacing: 1.5,
  },
  commandsList: {
    paddingLeft: Spacing.xl,
    gap: Spacing.sm,
  },
  commandText: {
    fontSize: 16,
    color: '#FFF',
    fontWeight: '700',
    fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace',
  },
  footer: {
    padding: Spacing.xl,
    borderTopWidth: 1,
    borderTopColor: 'rgba(255,255,255,0.05)',
  },
  dismissButton: {
    width: '100%',
    height: 56,
    borderRadius: 16,
    overflow: 'hidden',
  },
  buttonGradient: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  buttonText: {
    color: '#000',
    fontSize: 18,
    fontWeight: '900',
    letterSpacing: 2,
  },
});
