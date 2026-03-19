import React, { useEffect } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, SafeAreaView, FlatList, ActivityIndicator } from 'react-native';
import { Colors, Typography, Layout } from '../theme/theme';
import Header from '../components/Header';
import DeviceItem from '../components/DeviceItem';
import useBLE from '../hooks/useBLE';

import Sk8lytzController from '../components/Sk8lytzController';

export default function DashboardScreen() {
  const {
    requestPermissions,
    scanForPeripherals,
    allDevices,
    connectToDevice,
    connectedDevice,
    disconnectFromDevice,
    isScanning,
    isBluetoothSupported
  } = useBLE();

  useEffect(() => {
    requestPermissions();
  }, []);

  const handleScan = () => {
    requestPermissions().then((granted) => {
      if (granted) {
        scanForPeripherals();
      }
    });
  };

  return (
    <SafeAreaView style={styles.safeArea}>
      <Header />
      <View style={styles.container}>
        
        <View style={styles.card}>
          <Text style={Typography.title}>Zengge Controller</Text>
          <Text style={[Typography.body, { marginTop: 8, color: Colors.textMuted }]}>
            {connectedDevice 
              ? `Connected to ${connectedDevice.name || 'Device'}` 
              : 'Connect your Zengge SPI controllers to sync LED patterns to your skates.'}
          </Text>

          {connectedDevice ? (
            <TouchableOpacity style={[styles.scanButton, { backgroundColor: Colors.surfaceHighlight }]} onPress={disconnectFromDevice} activeOpacity={0.8}>
              <Text style={styles.scanButtonText}>DISCONNECT</Text>
            </TouchableOpacity>
          ) : (
            <TouchableOpacity 
              style={[styles.scanButton, isScanning && { opacity: 0.7 }]} 
              onPress={handleScan} 
              activeOpacity={0.8}
              disabled={isScanning}
            >
              {isScanning ? (
                <ActivityIndicator color={Colors.text} />
              ) : (
                <Text style={styles.scanButtonText}>SCAN FOR DEVICES</Text>
              )}
            </TouchableOpacity>
          )}
        </View>

        {connectedDevice && <Sk8lytzController />}

        {!isBluetoothSupported && (
          <View style={styles.errorContainer}>
            <Text style={{ color: Colors.error, textAlign: 'center' }}>Bluetooth is not supported or powered on this device. (Are you running on a simulator?)</Text>
          </View>
        )}

        <FlatList
          data={allDevices}
          keyExtractor={(item) => item.id}
          renderItem={({ item }) => (
            <DeviceItem 
              device={item} 
              isConnected={connectedDevice?.id === item.id}
              onPress={() => connectToDevice(item)} 
            />
          )}
          contentContainerStyle={{ paddingTop: 20, paddingBottom: 100 }}
          ListEmptyComponent={
            <View style={styles.emptyStateContainer}>
              <Text style={Typography.caption}>
                {isScanning ? 'Scanning...' : 'No devices found.'}
              </Text>
            </View>
          }
        />

      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: Colors.background,
  },
  container: {
    flex: 1,
    padding: Layout.padding,
  },
  card: {
    backgroundColor: Colors.surface,
    borderRadius: Layout.borderRadius,
    padding: 24,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
    shadowColor: Colors.primary,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.1,
    shadowRadius: 12,
    elevation: 5,
  },
  scanButton: {
    marginTop: 24,
    backgroundColor: Colors.primary,
    paddingVertical: 14,
    borderRadius: Layout.borderRadius,
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: Colors.primary,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.6,
    shadowRadius: 8,
    elevation: 8,
  },
  scanButtonText: {
    color: Colors.text,
    fontSize: 16,
    fontWeight: '800',
    letterSpacing: 1,
  },
  emptyStateContainer: {
    marginTop: 40,
    alignItems: 'center',
  },
  errorContainer: {
    marginTop: 16,
    padding: 12,
    backgroundColor: 'rgba(255, 61, 0, 0.1)',
    borderRadius: 8,
    borderWidth: 1,
    borderColor: Colors.error
  }
});
