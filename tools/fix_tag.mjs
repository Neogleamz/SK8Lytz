import fs from 'fs';

const filePath = 'src/components/CrewModal.tsx';
let content = fs.readFileSync(filePath, 'utf-8');

// Replace the closing tag using a safer regex that handles spacing
content = content.replace(
  /<\/KeyboardAvoidingView>\s*<\/Modal>/,
  '</KeyboardAvoidingView>\n      </CrewProvider>\n    </Modal>'
);

fs.writeFileSync(filePath, content);
console.log("Closed context tag successful.");
