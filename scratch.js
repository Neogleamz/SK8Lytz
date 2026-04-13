const fs = require('fs');
let content = fs.readFileSync('tools/SK8Lytz_Bucket_List.md', 'utf8');
const lines = content.split('\n');
const tasksToArchive = [
    'fix/hardware-default-propagation',
    'refactor/state-machine-standard',
    'fix/db-schema-type-parity',
    'perf/delta-sync-protocol',
    'chore/telemetry-efficiency-audit',
    'chore/refactor-admin-tools',
    'fix/display-name-persistence'
];
let newCompleted = [];
let newLines = [];
let inCritical = false;

for (let line of lines) {
    let matched = false;
    for (let t of tasksToArchive) {
        if (line.includes(t)) {
            matched = true;
            newCompleted.push(line.replace('- [ ]', '- [x]'));
            break;
        }
    }
    if (matched) continue;

    // Update charts
    if (line.match(/^  "Completed" : 31/)) { newLines.push('  "Completed" : 38'); } // +7
    else if (line.match(/^  "Remaining" : 26/)) { newLines.push('  "Remaining" : 19'); } // -7
    
    // Critical Stability: moving 4. (hardware, state-machine, db-schema, display-name)
    // Wait display-name was ALREADY [x] in the file, it was just in the wrong section!
    // So the previous Remaining was 4 (because there were 7 items, 1 was [x]). Total 8 items.
    // If I move 4 items out, 3 are [ ] and 1 is [x].
    else if (line.match(/^  "Completed" : 8/) && !inCritical) { newLines.push('  "Completed" : 12'); inCritical = true; } // + 4
    else if (line.match(/^  "Remaining" : 4/)) { newLines.push('  "Remaining" : 1'); } // -3 (since display-name was already counted as complete)
    
    // Engineering Health: moving 3. (delta-sync, telemetry, admin-tools)
    else if (line.match(/^  "Completed" : 7/) && inCritical) { newLines.push('  "Completed" : 10'); } // +3
    else if (line.match(/^  "Remaining" : 8/)) { newLines.push('  "Remaining" : 5'); } // -3
    else { newLines.push(line); }
}

let finalLines = [];
for (let line of newLines) {
    finalLines.push(line);
    if (line.includes('## ✅ Completed Previously')) {
        finalLines.push('');
        for (let cmp of newCompleted) {
            finalLines.push(cmp);
        }
    }
}

fs.writeFileSync('tools/SK8Lytz_Bucket_List.md', finalLines.join('\n'), 'utf8');
console.log('Fixed SK8Lytz_Bucket_List.md');
