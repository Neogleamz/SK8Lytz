import os

with open('C:\\Neogleamz\\AG_SK8Lytz_App\\SK8Lytz\\tools\\command-center\\src\\components\\widgets\\RelationalDataBankWidget.tsx', 'r') as f:
    content = f.read()

# 1. Add Props Interface
props_interface = '''
export interface FilteredIds {
  users: Set<string>;
  devices: Set<string>;
  sessions: Set<string>;
  crews: Set<string>;
  crewSessions: Set<string>;
}

export interface RelationalDataBankProps {
  activeEntity: { id: string; type: string } | null;
  onEntitySelected: (entity: { id: string; type: string } | null) => void;
  onFilteredIdsChange: (ids: FilteredIds) => void;
}
'''
content = content.replace('export default function RelationalDataBankWidget() {', props_interface + '\\nexport default function RelationalDataBankWidget({ activeEntity, onEntitySelected, onFilteredIdsChange }: RelationalDataBankProps) {')

# 2. Add Effects
effects_code = '''
  useEffect(() => {
    if (!activeEntity) {
      setFilter({ userId: null, deviceId: null, sessionId: null, crewId: null, crewSessionId: null });
      return;
    }
    const rawId = activeEntity.id.replace(/^(user_|skate_|crew_session_|crew_)/, '');
    if (activeEntity.type === 'users') setFilter(f => ({ ...f, userId: rawId }));
    else if (activeEntity.type === 'registeredDevices') setFilter(f => ({ ...f, deviceId: rawId }));
    else if (activeEntity.type === 'skateSessions') setFilter(f => ({ ...f, sessionId: rawId }));
    else if (activeEntity.type === 'crews') setFilter(f => ({ ...f, crewId: rawId }));
    else if (activeEntity.type === 'crewSessions') setFilter(f => ({ ...f, crewSessionId: rawId }));
  }, [activeEntity]);

  useEffect(() => {
    onFilteredIdsChange({
      users: new Set(fUsers.map(u => u.user_id)),
      devices: new Set(fDevices.map(d => d.id)),
      sessions: new Set(fSessions.map(s => s.id)),
      crews: new Set(fCrews.map(c => c.id)),
      crewSessions: new Set(fCrewSessions.map(cs => cs.id))
    });
  }, [fUsers, fDevices, fSessions, fCrews, fCrewSessions]); // omit onFilteredIdsChange to prevent loops if it lacks useCallback in parent
'''

# insert after handleReset
content = content.replace('  const handleReset = () => {', effects_code + '\\n  const handleReset = () => {')

content = content.replace('    });\\n  };', '    });\\n    onEntitySelected(null);\\n  };')

# 3. Update selection handlers
content = content.replace('''onSelectionChanged={e => {
              const row = e.api.getSelectedRows()[0];
              if (row) setFilter(f => ({ ...f, userId: row.user_id }));
            }}''', '''onSelectionChanged={e => {
              const row = e.api.getSelectedRows()[0];
              if (row) {
                setFilter(f => ({ ...f, userId: row.user_id }));
                onEntitySelected({ id: user_, type: 'users' });
              }
            }}''')

content = content.replace('''onSelectionChanged={e => {
              const row = e.api.getSelectedRows()[0];
              if (row) setFilter(f => ({ ...f, deviceId: row.id }));
            }}''', '''onSelectionChanged={e => {
              const row = e.api.getSelectedRows()[0];
              if (row) {
                setFilter(f => ({ ...f, deviceId: row.id }));
                onEntitySelected({ id: row.id, type: 'registeredDevices' });
              }
            }}''')

content = content.replace('''onSelectionChanged={e => {
              const row = e.api.getSelectedRows()[0];
              if (row) setFilter(f => ({ ...f, sessionId: row.id }));
            }}''', '''onSelectionChanged={e => {
              const row = e.api.getSelectedRows()[0];
              if (row) {
                setFilter(f => ({ ...f, sessionId: row.id }));
                onEntitySelected({ id: skate_, type: 'skateSessions' });
              }
            }}''')

content = content.replace('''onSelectionChanged={e => {
              const row = e.api.getSelectedRows()[0];
              if (row) setFilter(f => ({ ...f, crewId: row.id }));
            }}''', '''onSelectionChanged={e => {
              const row = e.api.getSelectedRows()[0];
              if (row) {
                setFilter(f => ({ ...f, crewId: row.id }));
                onEntitySelected({ id: crew_, type: 'crews' });
              }
            }}''')

content = content.replace('''onSelectionChanged={e => {
              const row = e.api.getSelectedRows()[0];
              if (row) setFilter(f => ({ ...f, crewSessionId: row.id }));
            }}''', '''onSelectionChanged={e => {
              const row = e.api.getSelectedRows()[0];
              if (row) {
                setFilter(f => ({ ...f, crewSessionId: row.id }));
                onEntitySelected({ id: crew_session_, type: 'crewSessions' });
              }
            }}''')


with open('C:\\Neogleamz\\AG_SK8Lytz_App\\SK8Lytz\\tools\\command-center\\src\\components\\widgets\\RelationalDataBankWidget.tsx', 'w') as f:
    f.write(content)

print("RelationalDataBankWidget.tsx updated successfully.")
