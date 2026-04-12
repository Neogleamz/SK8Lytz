const fs = require('fs');
const filepath = 'src/components/CrewModal.tsx';
const stylePath = 'src/components/crew/CrewStyles.ts';

const lines = fs.readFileSync(filepath, 'utf8').split('\n');
const s = lines.findIndex(l => l.includes('createStyles'));

const stylesText = lines.slice(s).join('\n').replace('const createStyles = (Colors: any) =>', 'export const createStyles = (Colors: any) =>');

const currentStyles = `import { StyleSheet, Dimensions, Platform } from 'react-native';\nconst { width } = Dimensions.get('window');\n\n`;

fs.writeFileSync(stylePath, currentStyles + stylesText);
console.log('Fixed CrewStyles.ts');
