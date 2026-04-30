import { useState, useEffect } from 'react';

const API_BASE = 'http://localhost:5999';

export interface FieldConfig {
    id: string;
    field_name: string;
    phase_id: number;
    display_label: string;
    data_type: string;
    sort_order: number;
    importance_level?: number;
}

export function useFieldRegistry() {
    const [fields, setFields] = useState<FieldConfig[]>([]);
    const [loading, setLoading] = useState(true);

    const fetchFields = async () => {
        try {
            const res = await fetch(`${API_BASE}/api/field-registry`);
            const data = await res.json();
            if (data.fields) {
                setFields(data.fields);
            } else if (data.error) {
                console.error('Failed to load field registry:', data.error);
            }
        } catch (error) {
            console.error('Failed to fetch field registry:', error);
        }
        setLoading(false);
    };

    useEffect(() => {
        fetchFields();
    }, []);

    const getFieldsForPhase = (phaseId: number) => {
        return fields.filter(f => f.phase_id === phaseId);
    };

    const toggleImportance = async (id: string, currentLevel: number = 0) => {
        const nextLevel = (currentLevel + 1) % 3; // cycles 0 -> 1 -> 2 -> 0
        
        // Optimistic update
        setFields(prev => prev.map(f => f.id === id ? { ...f, importance_level: nextLevel } : f));

        try {
            const res = await fetch(`${API_BASE}/api/field-registry/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ importance_level: nextLevel })
            });
            if (!res.ok) {
                // Revert if failed
                fetchFields();
            }
        } catch (err) {
            fetchFields();
        }
    };

    return { fields, loading, getFieldsForPhase, toggleImportance, fetchFields };
}
