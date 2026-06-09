import React from 'react';
import cheatSheetHtml from '../../../../cheat-sheet.html?raw';

export default function CheatSheetWidget() {
  return (
    <div className="w-full h-full min-h-[80vh] bg-white rounded-lg shadow-sm overflow-hidden">
      <iframe 
        srcDoc={cheatSheetHtml}
        title="Cheat Sheet"
        className="w-full h-full border-0"
        style={{ minHeight: '80vh' }}
      />
    </div>
  );
}
