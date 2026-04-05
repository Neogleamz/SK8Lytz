import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, Modal, TouchableOpacity, FlatList, ActivityIndicator, Alert, SafeAreaView } from 'react-native';
import { Colors, Typography, Layout } from '../theme/theme';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { ScenesService, ICloudScene } from '../services/ScenesService';
import { LinearGradient } from 'expo-linear-gradient';

interface Props {
  isVisible: boolean;
  onClose: () => void;
  onApplyScene: (payload: any) => void;
}

export default function CommunityModal({ isVisible, onClose, onApplyScene }: Props) {
  const [activeTab, setActiveTab] = useState<'COMMUNITY' | 'PERSONAL'>('COMMUNITY');
  const [scenes, setScenes] = useState<ICloudScene[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [processingId, setProcessingId] = useState<string | null>(null);

  useEffect(() => {
    if (isVisible) {
      fetchScenes();
    }
  }, [isVisible, activeTab]);

  const fetchScenes = async () => {
    setLoading(true);
    let data: ICloudScene[] = [];
    if (activeTab === 'COMMUNITY') {
       data = await ScenesService.getPublicScenes();
    } else {
       data = await ScenesService.getMyScenes();
    }
    setScenes(data);
    setLoading(false);
  };

  const handleApply = async (scene: ICloudScene) => {
    onApplyScene(scene.scene_payload);
    Alert.alert('Applied!', `${scene.name} has been applied to your hardware.`);
    if (activeTab === 'COMMUNITY') {
        ScenesService.downloadScene(scene.id);
    }
    onClose();
  };

  const handleUpvote = async (sceneId: string) => {
    setProcessingId(sceneId);
    const success = await ScenesService.upvoteScene(sceneId);
    if (success) {
       setScenes(prev => prev.map(s => s.id === sceneId ? { ...s, upvotes: s.upvotes + 1 } : s));
    }
    setProcessingId(null);
  };

  const handleDelete = (sceneId: string) => {
     Alert.alert('Delete Scene', 'Are you sure you want to delete this cloud scene?', [
        { text: 'Cancel', style: 'cancel' },
        { text: 'Delete', style: 'destructive', onPress: async () => {
           setProcessingId(sceneId);
           const success = await ScenesService.deleteScene(sceneId);
           if (success) {
              setScenes(prev => prev.filter(s => s.id !== sceneId));
           } else {
              Alert.alert('Error', 'Failed to delete scene.');
           }
           setProcessingId(null);
        }}
     ]);
  };

  const renderItem = ({ item }: { item: ICloudScene }) => {
    const isOwner = activeTab === 'PERSONAL';
    const payloadKeys = Object.keys(item.scene_payload || {}).length;
    
    // Attempt to parse out basic colors to show a preview if it's a DIY array
    let previewColors: string[] = [];
    if (item.scene_payload?.selectedColor) previewColors.push(item.scene_payload.selectedColor);
    if (item.scene_payload?.multiColors && Array.isArray(item.scene_payload.multiColors) && item.scene_payload.multiColors.length > 0) {
        previewColors = item.scene_payload.multiColors.slice(0, 5); // take max 5
    }
    
    // Provide a fallback UI if no colors exist
    if (previewColors.length === 0) previewColors = ['#333', '#444'];

    return (
      <View style={styles.card}>
        <View style={styles.cardHeader}>
           <Text style={styles.sceneName}>{item.name}</Text>
           <View style={styles.badge}>
              <Text style={styles.badgeText}>{item.scene_payload?.activeMode || 'UNKNOWN'} MODE</Text>
           </View>
        </View>

        <Text style={styles.authorText}>By {item.author_username}</Text>

        <View style={styles.previewContainer}>
           <Text style={styles.statusText}>Payload Complexity: {payloadKeys} Parameters</Text>
           <View style={styles.colorStrip}>
              {previewColors.map((color, idx) => (
                  <View key={idx} style={[styles.colorBlock, { backgroundColor: color }]} />
              ))}
              {item.scene_payload?.multiColors?.length > 5 && (
                  <Text style={{color: '#888', marginLeft: 8}}>+{(item.scene_payload.multiColors.length - 5)} more</Text>
              )}
           </View>
        </View>

        <View style={styles.cardActions}>
            <View style={styles.statsRow}>
               <View style={styles.statItem}>
                 <MaterialCommunityIcons name="heart" size={16} color={Colors.primary} />
                 <Text style={styles.statText}>{item.upvotes}</Text>
               </View>
               <View style={styles.statItem}>
                 <MaterialCommunityIcons name="download" size={16} color="#888" />
                 <Text style={styles.statText}>{item.downloads}</Text>
               </View>
            </View>
            
            <View style={styles.actionButtons}>
               {activeTab === 'COMMUNITY' && (
                 <TouchableOpacity 
                    style={styles.iconButton} 
                    onPress={() => handleUpvote(item.id)}
                    disabled={processingId === item.id}
                 >
                    <MaterialCommunityIcons name="thumb-up-outline" size={20} color="#FFF" />
                 </TouchableOpacity>
               )}
               {isOwner && (
                 <TouchableOpacity 
                    style={[styles.iconButton, { backgroundColor: 'rgba(255,0,0,0.2)' }]} 
                    onPress={() => handleDelete(item.id)}
                    disabled={processingId === item.id}
                 >
                    <MaterialCommunityIcons name="delete" size={20} color="#FF4444" />
                 </TouchableOpacity>
               )}
               <TouchableOpacity 
                  style={styles.applyButton} 
                  onPress={() => handleApply(item)}
               >
                  <Text style={styles.applyButtonText}>Apply Scene</Text>
                  <MaterialCommunityIcons name="chevron-right" size={20} color="#000" />
               </TouchableOpacity>
            </View>
        </View>
      </View>
    );
  };

  return (
    <Modal visible={isVisible} animationType="slide" presentationStyle="pageSheet" onRequestClose={onClose}>
      <SafeAreaView style={styles.container}>
         <View style={styles.header}>
            <TouchableOpacity onPress={onClose} style={styles.closeBtn}>
               <MaterialCommunityIcons name="close" size={28} color={Colors.text} />
            </TouchableOpacity>
            <Text style={styles.headerTitle}>Cloud Scenes</Text>
         </View>

         <View style={styles.tabsContainer}>
            <TouchableOpacity 
               style={[styles.tab, activeTab === 'COMMUNITY' && styles.activeTab]}
               onPress={() => setActiveTab('COMMUNITY')}
            >
               <Text style={[styles.tabText, activeTab === 'COMMUNITY' && styles.activeTabText]}>Global Community</Text>
            </TouchableOpacity>
            <TouchableOpacity 
               style={[styles.tab, activeTab === 'PERSONAL' && styles.activeTab]}
               onPress={() => setActiveTab('PERSONAL')}
            >
               <Text style={[styles.tabText, activeTab === 'PERSONAL' && styles.activeTabText]}>My Saves</Text>
            </TouchableOpacity>
         </View>

         {loading ? (
             <View style={styles.centerLoading}>
                 <ActivityIndicator size="large" color={Colors.primary} />
                 <Text style={{color: Colors.textMuted, marginTop: 12}}>Fetching Scenes...</Text>
             </View>
         ) : (
             <FlatList 
                 data={scenes}
                 keyExtractor={(item) => item.id}
                 renderItem={renderItem}
                 contentContainerStyle={styles.listContainer}
                 ListEmptyComponent={() => (
                    <View style={styles.emptyContainer}>
                       <MaterialCommunityIcons name="cloud-off-outline" size={64} color={Colors.surfaceHighlight} />
                       <Text style={styles.emptyText}>No scenes found.</Text>
                    </View>
                 )}
             />
         )}
      </SafeAreaView>
    </Modal>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.background,
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 20,
    paddingVertical: 16,
    borderBottomWidth: 1,
    borderBottomColor: Colors.surfaceHighlight,
  },
  closeBtn: {
    marginRight: 16,
  },
  headerTitle: {
    ...Typography.header,
    fontSize: 24,
    color: Colors.text,
  },
  tabsContainer: {
    flexDirection: 'row',
    padding: 16,
    gap: 12,
  },
  tab: {
    flex: 1,
    paddingVertical: 12,
    alignItems: 'center',
    borderRadius: Layout.borderRadius,
    backgroundColor: Colors.surface,
  },
  activeTab: {
    backgroundColor: Colors.surfaceHighlight,
    borderWidth: 1,
    borderColor: Colors.primary,
  },
  tabText: {
    ...Typography.body,
    fontWeight: 'bold',
    color: '#888',
  },
  activeTabText: {
    color: Colors.primary,
  },
  listContainer: {
    padding: 16,
    paddingBottom: 40,
    gap: 16,
  },
  card: {
    backgroundColor: Colors.surface,
    borderRadius: Layout.borderRadius,
    padding: 16,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
  },
  cardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 4,
  },
  sceneName: {
    ...Typography.title,
    color: Colors.text,
    fontSize: 18,
    flex: 1,
  },
  badge: {
    backgroundColor: 'rgba(255,255,255,0.1)',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 12,
  },
  badgeText: {
    fontSize: 10,
    fontWeight: 'bold',
    color: Colors.textMuted,
  },
  authorText: {
    ...Typography.caption,
    color: Colors.textMuted,
    marginBottom: 16,
  },
  previewContainer: {
    backgroundColor: 'rgba(0,0,0,0.2)',
    padding: 12,
    borderRadius: Layout.borderRadius,
    marginBottom: 16,
  },
  statusText: {
    fontSize: 12,
    color: '#888',
    marginBottom: 8,
  },
  colorStrip: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  colorBlock: {
    width: 24,
    height: 24,
    borderRadius: 4,
    marginRight: 4,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.2)',
  },
  cardActions: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  statsRow: {
    flexDirection: 'row',
    gap: 12,
  },
  statItem: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
  },
  statText: {
    color: '#888',
    fontSize: 14,
    fontWeight: 'bold',
  },
  actionButtons: {
    flexDirection: 'row',
    gap: 8,
  },
  iconButton: {
    width: 36,
    height: 36,
    borderRadius: 18,
    backgroundColor: 'rgba(255,255,255,0.1)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  applyButton: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: Colors.primary,
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: 20,
    gap: 4,
  },
  applyButtonText: {
    color: '#000',
    fontWeight: 'bold',
    fontSize: 14,
  },
  centerLoading: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  emptyContainer: {
    marginTop: 60,
    alignItems: 'center',
  },
  emptyText: {
    ...Typography.title,
    color: Colors.textMuted,
    marginTop: 16,
  }
});
