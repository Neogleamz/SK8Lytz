import React, { useEffect, useRef } from 'react';
import {
  View, Text, StyleSheet, Modal, TouchableOpacity,
  SafeAreaView, Animated, Easing, Dimensions
} from 'react-native';
import { useTheme } from '../../context/ThemeContext';
import { Typography, Layout } from '../../theme/theme';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { BlurView } from 'expo-blur';

const { width } = Dimensions.get('window');

interface Props {
  isVisible: boolean;
  onClose: () => void;
  isListening: boolean;
  transcript: string;
  error: string | null;
}

const WaveformBar = ({ index, isListening }: { index: number, isListening: boolean }) => {
  const anim = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    if (isListening) {
      Animated.loop(
        Animated.sequence([
          Animated.timing(anim, {
            toValue: 0.3 + Math.random() * 0.7,
            duration: 300 + Math.random() * 500,
            easing: Easing.inOut(Easing.ease),
            useNativeDriver: false,
          }),
          Animated.timing(anim, {
            toValue: 0.1,
            duration: 300 + Math.random() * 500,
            easing: Easing.inOut(Easing.ease),
            useNativeDriver: false,
          }),
        ])
      ).start();
    } else {
      anim.setValue(0.1);
    }
  }, [isListening]);

  return (
    <Animated.View
      style={{
        width: 6,
        height: anim.interpolate({
          inputRange: [0, 1],
          outputRange: [4, 60],
        }),
        backgroundColor: '#00F0FF',
        borderRadius: 3,
        marginHorizontal: 2,
        opacity: isListening ? 1 : 0.3,
      }}
    />
  );
};

export default function VoiceCommandModal({ isVisible, onClose, isListening, transcript, error }: Props) {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const fadeAnim = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    if (isVisible) {
      Animated.timing(fadeAnim, {
        toValue: 1,
        duration: 300,
        useNativeDriver: true,
      }).start();
    } else {
      fadeAnim.setValue(0);
    }
  }, [isVisible]);

  if (!isVisible) return null;

  return (
    <Modal visible={isVisible} transparent animationType="fade" onRequestClose={onClose}>
      <BlurView intensity={60} tint="dark" style={StyleSheet.absoluteFill}>
        <SafeAreaView style={styles.container}>
          <Animated.View style={[styles.content, { opacity: fadeAnim }]}>
            {/* Header / Dismiss */}
            <TouchableOpacity onPress={onClose} style={styles.dismissArea}>
              <MaterialCommunityIcons name="chevron-down" size={32} color="rgba(255,255,255,0.3)" />
            </TouchableOpacity>

            {/* Visualizer */}
            <View style={styles.visualizerContainer}>
              <View style={styles.waveform}>
                {[...Array(12)].map((_, i) => (
                  <WaveformBar key={i} index={i} isListening={isListening} />
                ))}
              </View>
              
              <View style={[styles.micOuter, isListening && styles.micActive]}>
                <View style={styles.micInner}>
                  <MaterialCommunityIcons 
                    name={isListening ? "microphone" : "microphone-off"} 
                    size={40} 
                    color={isListening ? "#00F0FF" : "rgba(255,255,255,0.5)"} 
                  />
                </View>
              </View>
            </View>

            {/* Text Feedback */}
            <View style={styles.textContainer}>
              <Text style={styles.statusText}>
                {isListening ? 'LISTENING...' : 'RESOLVING...'}
              </Text>
              
              <Text style={styles.transcript} numberOfLines={3}>
                {transcript || (error ? 'Error captured.' : 'Speak your command...')}
              </Text>
              
              {error && (
                <View style={styles.errorPill}>
                  <MaterialCommunityIcons name="alert-circle" size={14} color="#FF4444" />
                  <Text style={styles.errorText}>{error}</Text>
                </View>
              )}

              {!transcript && !error && (
                <Text style={styles.hintText}>
                  Try "Red Glow", "Faster", or "Red in the back"
                </Text>
              )}
            </View>

            {/* Footer / Stop Button */}
            <TouchableOpacity style={styles.stopButton} onPress={onClose}>
              <LinearGradient
                colors={['#00F0FF', '#7000FF']}
                start={{ x: 0, y: 0 }}
                end={{ x: 1, y: 1 }}
                style={styles.stopGradient}
              >
                <Text style={styles.stopText}>DONE</Text>
              </LinearGradient>
            </TouchableOpacity>
          </Animated.View>
        </SafeAreaView>
      </BlurView>
    </Modal>
  );
}

// Inline LinearGradient import from expo-linear-gradient for the button
import { LinearGradient } from 'expo-linear-gradient';

const createStyles = (Colors: any) => StyleSheet.create({
  container: { 
    flex: 1, 
    justifyContent: 'flex-end',
  },
  content: {
    backgroundColor: 'rgba(10, 10, 10, 0.9)',
    borderTopLeftRadius: 40,
    borderTopRightRadius: 40,
    padding: 24,
    paddingBottom: 40,
    alignItems: 'center',
    borderTopWidth: 1,
    borderTopColor: 'rgba(0, 240, 255, 0.2)',
  },
  dismissArea: {
    padding: 10,
    marginBottom: 20,
  },
  visualizerContainer: {
    alignItems: 'center',
    marginBottom: 40,
  },
  waveform: {
    flexDirection: 'row',
    height: 60,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 30,
    width: '100%',
  },
  micOuter: {
    width: 100,
    height: 100,
    borderRadius: 50,
    backgroundColor: 'rgba(255,255,255,0.05)',
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 2,
    borderColor: 'rgba(255,255,255,0.1)',
  },
  micActive: {
    borderColor: '#00F0FF',
    shadowColor: '#00F0FF',
    shadowOpacity: 0.5,
    shadowRadius: 15,
    elevation: 10,
  },
  micInner: {
    width: 80,
    height: 80,
    borderRadius: 40,
    backgroundColor: 'rgba(255,255,255,0.08)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  textContainer: {
    alignItems: 'center',
    marginBottom: 40,
    width: '100%',
  },
  statusText: {
    color: '#00F0FF',
    fontSize: 12,
    letterSpacing: 2,
    fontWeight: 'bold',
    marginBottom: 16,
    opacity: 0.8,
  },
  transcript: {
    ...Typography.header,
    color: '#FFF',
    fontSize: 28,
    textAlign: 'center',
    minHeight: 100,
    lineHeight: 38,
  },
  hintText: {
    color: 'rgba(255,255,255,0.4)',
    fontSize: 14,
    marginTop: 10,
    fontStyle: 'italic',
  },
  errorPill: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: 'rgba(255,68,68,0.1)',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 20,
    marginTop: 10,
    gap: 6,
  },
  errorText: {
    color: '#FF4444',
    fontSize: 12,
    fontWeight: 'bold',
  },
  stopButton: {
    width: '100%',
    height: 64,
    borderRadius: 32,
    overflow: 'hidden',
  },
  stopGradient: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  stopText: {
    color: '#000',
    fontWeight: '900',
    fontSize: 18,
    letterSpacing: 1,
  },
});
