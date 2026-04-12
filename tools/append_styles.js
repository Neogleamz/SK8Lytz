const fs = require('fs');
const filepath = 'src/components/CrewModal.tsx';
const stylePath = 'src/components/crew/CrewStyles.ts';

const lines = fs.readFileSync(filepath, 'utf8').split('\n');
const s = lines.findIndex(l => l.includes('createStyles'));

const stylesText = lines.slice(s).join('\n');

const currentStyles = fs.readFileSync(stylePath, 'utf8');

fs.writeFileSync(stylePath, currentStyles + '\n\n' + stylesText);
console.log('Appended fully');
