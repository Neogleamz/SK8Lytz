import React, { Component, ErrorInfo, ReactNode } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Platform } from 'react-native';
import { logFatalCrash } from '../utils/CrashReporter';

interface Props {
  children: ReactNode;
}

interface State {
  hasError: boolean;
  error: Error | null;
  eventId: string | null;
}

export class GlobalErrorBoundary extends Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.state = { hasError: false, error: null, eventId: null };
  }

  static getDerivedStateFromError(error: Error): State {
    return { hasError: true, error, eventId: null };
  }

  componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    this.logCrashToSupabase(error, errorInfo);
  }

  async logCrashToSupabase(error: Error, errorInfo: ErrorInfo) {
    const eventId = await logFatalCrash(error, errorInfo.componentStack || 'No component stack available');
    if (eventId) {
      this.setState({ eventId });
    }
  }

  render() {
    if (this.state.hasError) {
      return (
        <View style={[styles.container, { paddingTop: Platform.OS === 'ios' ? 50 : 30 }]}>
          <View style={styles.content}>
            <Text style={styles.title}>Oops, SK8Lytz crashed!</Text>
            <Text style={styles.subtitle}>
              We've caught the error and a detailed flight log has been sent to our Command Center for immediate investigation.
            </Text>
            
            {this.state.eventId && (
              <Text style={styles.eventText}>Error ID: {this.state.eventId}</Text>
            )}

            <TouchableOpacity 
              style={styles.button}
              onPress={() => this.setState({ hasError: false, error: null, eventId: null })}
            >
              <Text style={styles.buttonText}>Restart App</Text>
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
    backgroundColor: '#0a0a0a',
  },
  content: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 24,
  },
  title: {
    color: '#ff4d4f',
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 16,
    textAlign: 'center',
  },
  subtitle: {
    color: '#8c8c8c',
    fontSize: 16,
    textAlign: 'center',
    marginBottom: 32,
    lineHeight: 24,
  },
  eventText: {
    color: '#434343',
    fontSize: 12,
    fontFamily: Platform.select({ ios: 'Courier', default: 'monospace' }),
    marginBottom: 32,
  },
  button: {
    backgroundColor: '#1890ff',
    paddingHorizontal: 32,
    paddingVertical: 16,
    borderRadius: 8,
  },
  buttonText: {
    color: '#ffffff',
    fontSize: 16,
    fontWeight: 'bold',
  }
});
