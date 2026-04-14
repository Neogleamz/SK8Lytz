import { useCallback, useState } from 'react';
import { Alert } from 'react-native';
import { useProductCatalog } from '../hooks/useProductCatalog';
import { supabase } from '../services/supabaseClient';
import type { ProductProfile } from '../types/ProductCatalog';

/**
 * Creates a blank profile template for the Add Product form.
 */
export const createBlankProfile = (): ProductProfile => ({
  id: '',
  displayName: '',
  defaultLedPoints: 16,
  defaultSegments: 1,
  defaultIcType: 1,
  defaultColorSorting: 2,
  detectMinPoints: 1,
  detectMaxPoints: 9,
  vizShape: 'OVAL',
  vizDefaultPoints: 16,
  vizBlobDiameterMm: 5.7,
  vizBaseWidth: 55,
  vizBaseHeight: 115,
  vizStripCount: 2,
  vizStripSeparation: 32,
  vizStripOrientation: 'VERTICAL',
  hardwareAllowsCustomPoints: false,
  batteryCapacityMilliAmpereHour: 3000,
});

/**
 * useProductManager — Domain hook for administrative management of the hardware product catalog.
 */
export function useProductManager() {
  const { allProfiles, saveProfile: saveToCloud, syncFromCloud } = useProductCatalog();
  const [editingProfile, setEditingProfile] = useState<ProductProfile | null>(null);
  const [isSaving, setIsSaving] = useState(false);

  const startEditing = useCallback((profile: ProductProfile) => {
    setEditingProfile({ ...profile });
  }, []);

  const createNew = useCallback(() => {
    setEditingProfile(createBlankProfile());
  }, []);

  const patchEdit = useCallback((patch: Partial<ProductProfile>) => {
    setEditingProfile(prev => prev ? { ...prev, ...patch } : prev);
  }, []);

  const saveProduct = useCallback(async () => {
    if (!editingProfile) return false;
    if (!editingProfile.id.trim()) {
      Alert.alert('Validation Error', 'Product ID is required.');
      return false;
    }

    setIsSaving(true);
    try {
      // Direct session check to ensure we aren't 401ing
      const { data: { session } } = await supabase.auth.getSession();
      if (!session) {
        Alert.alert('Session Expired', 'Please log in as an admin.');
        return false;
      }

      const success = await saveToCloud(editingProfile);
      if (success) {
        setEditingProfile(null);
        return true;
      }
      return false;
    } catch (err) {
      console.error('Save failed:', err);
      return false;
    } finally {
      setIsSaving(false);
    }
  }, [editingProfile, saveToCloud]);

  const cancelEdit = useCallback(() => {
    setEditingProfile(null);
  }, []);

  return {
    allProfiles,
    editingProfile,
    isSaving,
    startEditing,
    createNew,
    patchEdit,
    saveProduct,
    cancelEdit,
    syncFromCloud,
  };
}
