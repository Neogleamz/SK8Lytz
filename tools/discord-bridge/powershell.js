const { exec } = require('child_process');

/**
 * Uses a Windows PowerShell inline script to bring the active window and simulate SendKeys.
 * Note: By default, SendKeys types into whatever window is in the foreground.
 * @param {string} text - The text to type.
 */
function sendKeystrokes(text) {
  return new Promise((resolve, reject) => {
    // Escape single quotes for powershell strings by replacing ' with ''
    const escapedText = text.replace(/'/g, "''");
    
    // We send characters individually, or we can send the whole block.
    // SendKeys requires special escaping for chars like { } [ ] + ^ % ~
    let sanitizedForSendKeys = escapedText
      .replace(/\\/g, "\\\\")
      .replace(/([+^%~{}\[\]\(\)])/g, "{\\$1}"); // wrap special chars in braces
      
    const psScript = `
      Add-Type -AssemblyName System.Windows.Forms
      [System.Windows.Forms.SendKeys]::SendWait('${sanitizedForSendKeys}')
      [System.Windows.Forms.SendKeys]::SendWait('{ENTER}')
    `;

    const command = `powershell -NoProfile -Command "${psScript.replace(/\\n/g, '; ')}"`;

    exec(command, (error, stdout, stderr) => {
      if (error) {
        console.error('[SendKeys] Execution error:', error);
        return reject(error);
      }
      resolve(stdout);
    });
  });
}

module.exports = { sendKeystrokes };
