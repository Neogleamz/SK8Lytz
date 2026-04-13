const fs = require('fs');

let content = fs.readFileSync('tools/SK8Lytz_Bucket_List.md', 'utf8');

const toMoveRegex = /^- \[x\] .*$/gm;

const completedIndex = content.indexOf('## ✅ Completed Previously');
let topPart = content.substring(0, completedIndex);
let bottomPart = content.substring(completedIndex);

const itemsToMove = [];
topPart = topPart.replace(toMoveRegex, (match) => {
    itemsToMove.push(match);
    return ''; 
});

const remainingMatch = topPart.match(/^- \[ \] /gm) || [];
const remainingCount = remainingMatch.length;

const bottomCompletedCnt = (bottomPart.match(/^- \[x\]/gm) || []).length;
const newTotalCompleted = bottomCompletedCnt + itemsToMove.length;

let newTop = topPart.replace(/pie title Core Development Progress\s+"Completed" : \d+\s+"Remaining" : \d+/, 
`pie title Core Development Progress\n  "Completed" : ${newTotalCompleted}\n  "Remaining" : ${remainingCount}`);

newTop = newTop.replace(/\n{3,}/g, '\n\n');

function resetChart(sectionText) {
    const rCount = (sectionText.match(/^- \[ \] /gm) || []).length;
    return sectionText.replace(/pie title .*?\n\s+"Completed" : \d+\n\s+"Remaining" : \d+/, (m) => {
        return m.replace(/"Completed" : \d+/, `"Completed" : 0`).replace(/"Remaining" : \d+/, `"Remaining" : ${rCount}`);
    });
}

let sections = newTop.split('\n---\n');
for (let i = 1; i < sections.length; i++) {
    sections[i] = resetChart(sections[i]);
}
newTop = sections.join('\n---\n');

const newBottom = bottomPart.replace('## ✅ Completed Previously\n\n', '## ✅ Completed Previously\n\n' + itemsToMove.join('\n') + '\n');

fs.writeFileSync('tools/SK8Lytz_Bucket_List.md', newTop + newBottom);
