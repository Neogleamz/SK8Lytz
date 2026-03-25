const fs = require('fs');
const xml = fs.readFileSync('C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/ZENGGE_APK/decompiled/resources/res/values/strings.xml', 'utf8');

const extract = (prefix) => {
    // using literal regex to avoid powerShell string escaping problems
    const regex = new RegExp('<string name="' + prefix + '(.*?)">(.*?)</string>', 'g');
    let match;
    const results = [];
    while ((match = regex.exec(xml)) !== null) {
        // match[1] depends on the name pattern. Sometimes it's the exact mode number, sometimes it includes trailing stuff.
        results.push('- `' + prefix + match[1] + '`: ' + match[2]);
    }
    return results;
};

const javaModes = extract('java_Mode_');
const symphonyBuild = extract('symphony_SymphonyBuild_');
const symphonyEffect = extract('symphony_SymphonyEffect');
const christmasModes = extract('christmas_mode_name_');
const musicModes = extract('music_');
const cameraModes = extract('camera_');
const micModes = extract('mic_');

// Helper to sort symphony modes specifically by the embedded digit
const sortModes = (arr) => {
    return arr.sort((a, b) => {
        const numA = parseInt(a.match(/(\d+)/)?.[0] || '0');
        const numB = parseInt(b.match(/(\d+)/)?.[0] || '0');
        return numA - numB;
    });
};

let markdown = '\n\n## 6. Exhaustive List of ZENGGE App Modes (Extracted from APK strings.xml)\n\n';
markdown += '### Classic Java Modes (20+)\n' + sortModes(javaModes).join('\n') + '\n\n';
markdown += '### Symphony Build Modes (300)\n' + sortModes(symphonyBuild).join('\n') + '\n\n';
markdown += '### Symphony Effects (44)\n' + sortModes(symphonyEffect).join('\n') + '\n\n';
markdown += '### Christmas Modes\n' + christmasModes.join('\n') + '\n\n';
markdown += '### Music Modes\n' + musicModes.join('\n') + '\n\n';
markdown += '### Camera Modes\n' + cameraModes.join('\n') + '\n\n';
markdown += '### Microphone Modes\n' + micModes.join('\n') + '\n\n';

fs.appendFileSync('C:/Users/Magma/.gemini/antigravity/brain/0d631c29-ae69-4a96-9767-6b6c24120e82/Zengge_APK_Master_Reference.md', markdown);
fs.appendFileSync('C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/SK8Lytz_App_Master_Reference.txt', markdown);

console.log('Successfully appended all modes to both reference documents.');
