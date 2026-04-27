import { useState, useEffect } from 'react';

const API_BASE = 'http://localhost:5999';

export interface FieldConfig {
    id: string;
    field_name: string;
    phase_id: number;
    display_label: string;
    data_type: string;
    sort_order: number;
}

export function useFieldRegistry() {
    const [fields, setFields] = useState<FieldConfig[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
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
        fetchFields();
    }, []);

    const getFieldsForPhase = (phaseId: number) => {
        return fields.filter(f => f.phase_id === phaseId);
    };

    return { fields, loading, getFieldsForPhase };
}
