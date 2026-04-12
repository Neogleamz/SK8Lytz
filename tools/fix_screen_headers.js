const fs = require('fs');

const files = [
  'CrewControllerScreen.tsx',
  'CrewCreateScreen.tsx',
  'CrewDetailScreen.tsx',
  'CrewJoinScreen.tsx',
  'CrewManageScreen.tsx',
  'CrewScheduleScreen.tsx'
];

for (const file of files) {
  const filepath = `src/components/crew/${file}`;
  let code = fs.readFileSync(filepath, 'utf8');

  // Replace invalid theme import
  code = code.replace(/import \{ Colors \} from '\.\.\/\.\.\/theme';/, `import { useTheme } from '../../context/ThemeContext';\nimport * as Clipboard from 'expo-clipboard';\nimport { profileService, PermanentCrew } from '../../services/ProfileService';\nimport { crewService, CrewSession } from '../../services/CrewService';\nimport { locationService } from '../../services/LocationService';\nimport { AppLogger } from '../../services/AppLogger';\nimport DateTimePicker from '@react-native-community/datetimepicker';\nimport * as ImagePicker from 'expo-image-picker';\n`);

  // Inject timeAgo helper
  const functionRef = `export function ${file.replace('.tsx', '')}() {`;
  if(!code.includes('function timeAgo')) {
      code = code.replace(functionRef, `function timeAgo(iso: string): string {
  const diff = Date.now() - new Date(iso).getTime();
  const m = Math.floor(diff / 60000);
  if (m < 1) return 'just now';
  if (m < 60) return \`\${m}m ago\`;
  return \`\${Math.floor(m / 60)}h ago\`;
}\n\n${functionRef}`);
  }

  // Add Colors hook initialization
  code = code.replace(functionRef, `${functionRef}\n  const { Colors } = useTheme();\n  const styles = createStyles(Colors);`);
  code = code.replace(/const styles = createStyles\(Colors\);\s+export function/s, 'export function');

  // Fix formState destructuring
  code = code.replace(/const \{ hub, manage, session,(.*?) \} = context;/g, `const { hub, manage, session,$1, formState } = context;`);
  
  // Strip bad manage destructure variables
  code = code.replace(/, setSelectedCrewId, setCrewName/g, '');

  fs.writeFileSync(filepath, code);
}
console.log('Injected imports and standardized headers across remaining screens');
