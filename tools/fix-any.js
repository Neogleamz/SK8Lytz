const fs = require('fs');
const files = [
  'src/components/dashboard/CrewHubSlab.tsx',
  'src/components/dashboard/DashboardCrewPanel.tsx',
  'src/components/dashboard/MySkatesSlab.tsx',
  'src/components/dashboard/RegisteredFleetSlab.tsx',
  'src/components/dashboard/SkateGroupCard.tsx',
  'src/components/dashboard/SupportModal.tsx',
];

files.forEach(f => {
  let content = fs.readFileSync(f, 'utf8');
  content = content.replace(/StyleProp<any>/g, 'StyleProp<import(\'react-native\').ViewStyle | import(\'react-native\').TextStyle>');
  fs.writeFileSync(f, content, 'utf8');
});
