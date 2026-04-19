/**
 * BLEErrorBoundary.tsx — Crash Shield for BLE-dependent UI
 *
 * Wraps DockedController and DashboardScreen to catch render-time
 * exceptions (null device access, state corruption) and display a
 * recoverable fallback instead of a full app crash.
 *
 * Uses class component (required by React ErrorBoundary API).
 */
import React from 'react';
import { Text, TouchableOpacity, View, StyleSheet } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { AppLogger } from '../../services/AppLogger';

interface Props {
  children: React.ReactNode;
  /** Human-readable name for telemetry (e.g., 'DockedController', 'DashboardScreen') */
  componentName: string;
  /** Optional callback to attempt recovery (e.g., trigger reconnect) */
  onRecover?: () => void;
}

interface State {
  hasError: boolean;
  errorMessage: string | null;
}

export class BLEErrorBoundary extends React.Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.state = { hasError: false, errorMessage: null };
  }

  static getDerivedStateFromError(error: Error): State {
    return { hasError: true, errorMessage: error?.message || JSON.stringify(error) || 'Unknown Object Error' };
  }

  componentDidCatch(error: Error, info: React.ErrorInfo) {
    AppLogger.error(`[ErrorBoundary] ${this.props.componentName} crashed`, {
      error: error?.message || JSON.stringify(error),
      stack: error.stack?.slice(0, 500),
      componentStack: info.componentStack?.slice(0, 500),
    });
  }

  handleRecover = () => {
    this.setState({ hasError: false, errorMessage: null });
    this.props.onRecover?.();
  };

  render() {
    if (this.state.hasError) {
      return (
        <View style={styles.container}>
          <View style={styles.card}>
            <MaterialCommunityIcons name="alert-circle-outline" size={48} color="#FF5A00" />
            <Text style={styles.title}>Something Went Wrong</Text>
            <Text style={styles.subtitle}>
              {this.props.componentName} encountered an unexpected error.
            </Text>
            {this.state.errorMessage && (
              <Text style={styles.errorDetail} numberOfLines={3}>
                {this.state.errorMessage}
              </Text>
            )}
            <TouchableOpacity style={styles.button} onPress={this.handleRecover} activeOpacity={0.7}>
              <MaterialCommunityIcons name="refresh" size={20} color="#0A0C12" />
              <Text style={styles.buttonText}>Try Again</Text>
            </TouchableOpacity>
          </View>
        </View>
      );
    }
    return this.props.children;
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#0A0C12',
    padding: 24,
  },
  card: {
    backgroundColor: '#141829',
    borderRadius: 16,
    padding: 32,
    alignItems: 'center',
    width: '100%',
    maxWidth: 340,
    borderWidth: 1,
    borderColor: '#252c47',
  },
  title: {
    color: '#FFFFFF',
    fontSize: 20,
    fontWeight: '700',
    marginTop: 16,
    marginBottom: 8,
  },
  subtitle: {
    color: '#8a96b3',
    fontSize: 14,
    textAlign: 'center',
    marginBottom: 12,
    lineHeight: 20,
  },
  errorDetail: {
    color: '#ff4040',
    fontSize: 12,
    textAlign: 'center',
    marginBottom: 16,
    fontFamily: 'monospace',
    opacity: 0.7,
  },
  button: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#00f0ff',
    paddingHorizontal: 24,
    paddingVertical: 12,
    borderRadius: 8,
    gap: 8,
    marginTop: 8,
  },
  buttonText: {
    color: '#0A0C12',
    fontSize: 16,
    fontWeight: '600',
  },
});
