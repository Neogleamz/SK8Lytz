import { useState, useEffect } from 'react';

const API_BASE = 'http://127.0.0.1:5999';

export interface FieldConfig {
    id: string;
    field_name: string;
    phase_id: number;
    display_label: string;
    data_type: string;
    sort_order: number;
    importance_level?: number;
    priority_group?: number;
    is_hard_gate?: number;
    visual_glow?: number;
    validation_rule?: string;
}

// Module-level global state cache
let globalFields: FieldConfig[] = [];
let globalLoading = false;
const globalListeners = new Set<(fields: FieldConfig[]) => void>();

const notifyListeners = () => {
    globalListeners.forEach(listener => listener([...globalFields]));
};

export const fetchFieldRegistryGlobal = async () => {
    if (globalLoading) return;
    globalLoading = true;
    try {
        const res = await fetch(`${API_BASE}/api/field-registry`);
        if (res.ok) {
            const data = await res.json();
            if (data.fields) {
                globalFields = data.fields;
            } else if (data.error) {
                console.error('Failed to load field registry:', data.error);
            }
            notifyListeners();
        }
    } catch (err) {
        console.error('Failed to fetch field registry:', err);
    } finally {
        globalLoading = false;
    }
};

export function useFieldRegistry() {
    const [fields, setFields] = useState<FieldConfig[]>(globalFields);
    const [loading, setLoading] = useState(globalFields.length === 0);

    useEffect(() => {
        const listener = (newFields: FieldConfig[]) => {
            setFields(newFields);
            setLoading(false);
        };
        globalListeners.add(listener);

        if (globalFields.length === 0) {
            fetchFieldRegistryGlobal();
        } else {
            setLoading(false);
        }

        return () => {
            globalListeners.delete(listener);
        };
    }, []);

    const fetchFields = async () => {
        await fetchFieldRegistryGlobal();
    };

    const getFieldsForPhase = (phaseId: number) => {
        return fields.filter(f => f.phase_id === phaseId);
    };

    const toggleImportance = async (id: string, currentLevel: number = 0) => {
        const nextLevel = (currentLevel + 1) % 3; // cycles 0 -> 1 -> 2 -> 0
        
        // Optimistic update globally
        globalFields = globalFields.map(f => f.id === id ? { ...f, importance_level: nextLevel } : f);
        notifyListeners();

        try {
            const res = await fetch(`${API_BASE}/api/field-registry/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ importance_level: nextLevel })
            });
            if (!res.ok) {
                fetchFieldRegistryGlobal();
            }
        } catch (err) {
            fetchFieldRegistryGlobal();
        }
    };

    const updateFieldConfig = async (id: string, updates: Partial<FieldConfig>) => {
        // Optimistic update globally
        globalFields = globalFields.map(f => f.id === id ? { ...f, ...updates } : f);
        notifyListeners();

        try {
            const res = await fetch(`${API_BASE}/api/field-registry/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(updates)
            });
            if (!res.ok) {
                fetchFieldRegistryGlobal();
            }
        } catch (err) {
            fetchFieldRegistryGlobal();
        }
    };

    const resetRegistry = async () => {
        setLoading(true);
        try {
            const res = await fetch(`${API_BASE}/api/field-registry/reset`, {
                method: 'POST'
            });
            if (res.ok) {
                await fetchFieldRegistryGlobal();
            }
        } catch (err) {
            console.error('Failed to reset field registry:', err);
        }
        setLoading(false);
    };

    return { fields, loading, getFieldsForPhase, toggleImportance, updateFieldConfig, resetRegistry, fetchFields };
}
