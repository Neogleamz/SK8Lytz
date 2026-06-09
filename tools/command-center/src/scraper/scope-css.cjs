const fs = require('fs');
const path = require('path');

const cssPath = path.join(__dirname, 'ScraperApp.css');
let css = fs.readFileSync(cssPath, 'utf8');

// We need to move :root and @keyframes outside the wrapper
let rootBlock = '';
let keyframesBlocks = '';

// Extract :root
css = css.replace(/:root\s*{([^}]*)}/g, (match) => {
    rootBlock += match + '\n';
    return '';
});

// Extract @keyframes - this requires careful brace matching, but since it's simple formatting we can try a regex that matches up to the closing brace.
// Assuming @keyframes name { ... } only has one level of nested braces (the keyframe steps)
css = css.replace(/@keyframes\s+[^{]+\s*{[^{}]*{[^{}]*}[^{}]*}/g, (match) => {
    keyframesBlocks += match + '\n';
    return '';
});

// Second pass for keyframes with multiple steps
// A more robust regex for 2 levels of nesting:
css = css.replace(/@keyframes\s+[^{]+\s*{(?:[^{}]*{[^{}]*})*[^{}]*}/g, (match) => {
    keyframesBlocks += match + '\n';
    return '';
});

const newCss = `
${rootBlock}

/* Scoped legacy styles */
.scraper-legacy-root {
${css}
}

/* Keyframes hoisted */
${keyframesBlocks}
`;

fs.writeFileSync(cssPath, newCss);
console.log('CSS scoping complete.');
