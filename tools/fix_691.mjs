import fs from 'fs';

const filePath = 'src/components/CrewModal.tsx';
const content = fs.readFileSync(filePath, 'utf-8');
const lines = content.split('\n');

const newLines = [
  ...lines.slice(0, 690),
  `          <TouchableOpacity onPress={() => refreshNearby()}>`,
  ...lines.slice(697)
];

fs.writeFileSync(filePath, newLines.join('\n'));
console.log("Rewrite successful.");
