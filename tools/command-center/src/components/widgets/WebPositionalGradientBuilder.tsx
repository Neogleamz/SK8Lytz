import React, { useState } from 'react';
import { Trash2, Plus, ArrowRight, Activity, Pause, FastForward } from 'lucide-react';
import { PositionalMathBuffer } from '../../protocols/PositionalMathBuffer';
import type { BuilderNode } from '../../protocols/PositionalMathBuffer';

const COLOR_PRESET_PALETTE = [
  '#FF0000', '#FF4500', '#FFA500', '#FFFF00', '#9ACD32', '#00FF00', 
  '#00FA9A', '#00FFFF', '#1E90FF', '#0000FF', '#8A2BE2', '#FF00FF', 
  '#FF1493', '#FFFFFF'
];

function hexToHue(hex: string): number {
  let r = 0, g = 0, b = 0;
  if (hex.length === 4) {
    r = parseInt(hex[1] + hex[1], 16);
    g = parseInt(hex[2] + hex[2], 16);
    b = parseInt(hex[3] + hex[3], 16);
  } else if (hex.length === 7) {
    r = parseInt(hex[1] + hex[2], 16);
    g = parseInt(hex[3] + hex[4], 16);
    b = parseInt(hex[5] + hex[6], 16);
  }
  r /= 255; g /= 255; b /= 255;
  const max = Math.max(r, g, b), min = Math.min(r, g, b);
  let h = 0;
  if (max === min) h = 0;
  else if (max === r) h = (60 * ((g - b) / (max - min)) + 360) % 360;
  else if (max === g) h = (60 * ((b - r) / (max - min)) + 120) % 360;
  else if (max === b) h = (60 * ((r - g) / (max - min)) + 240) % 360;
  return h;
}

function hueToHex(h: number): string {
  const s = 1, l = 0.5;
  const c = (1 - Math.abs(2 * l - 1)) * s;
  const x = c * (1 - Math.abs((h / 60) % 2 - 1));
  const m = l - c / 2;
  let r = 0, g = 0, b = 0;
  if (0 <= h && h < 60) { r = c; g = x; b = 0; }
  else if (60 <= h && h < 120) { r = x; g = c; b = 0; }
  else if (120 <= h && h < 180) { r = 0; g = c; b = x; }
  else if (180 <= h && h < 240) { r = 0; g = x; b = c; }
  else if (240 <= h && h < 300) { r = x; g = 0; b = c; }
  else if (300 <= h && h <= 360) { r = c; g = 0; b = x; }
  r = Math.round((r + m) * 255);
  g = Math.round((g + m) * 255);
  b = Math.round((b + m) * 255);
  const toHex = (n: number) => { const hex = n.toString(16); return hex.length === 1 ? '0' + hex : hex; };
  return `#${toHex(r)}${toHex(g)}${toHex(b)}`;
}

interface Props {
  nodes: BuilderNode[];
  onNodesChange: (nodes: BuilderNode[]) => void;
  fillMode: 'GRADIENT' | 'SOLID';
  onFillModeChange: (mode: 'GRADIENT' | 'SOLID') => void;
  transitionType: number;
  onTransitionTypeChange: (type: number) => void;
  direction: number;
  onDirectionChange: (dir: number) => void;
  deviceLedCount?: number;
  selectedColor?: string;
}

export default function WebPositionalGradientBuilder({ 
   nodes, onNodesChange, 
   fillMode, onFillModeChange, 
   transitionType, onTransitionTypeChange,
   direction, onDirectionChange,
   deviceLedCount = 60, selectedColor = '#00F0FF'
}: Props) {

  const [activeNodeId, setActiveNodeId] = useState<string | null>(nodes[0]?.id || null);

  const addNode = () => {
      let newPosition = 50;
      if (nodes.length > 0) {
          const last = nodes[nodes.length - 1];
          newPosition = Math.min(100, last.position + Math.floor((100 - last.position) / 2));
          if (newPosition === last.position) newPosition = Math.max(0, last.position - 10);
      }
      
      const newNode: BuilderNode = {
          id: `node_${Date.now()}`,
          position: newPosition,
          colorHex: selectedColor
      };
      
      const updated = [...nodes, newNode].sort((a,b) => a.position - b.position);
      onNodesChange(updated);
      setActiveNodeId(newNode.id);
  };

  const removeNode = (id: string) => {
      if (nodes.length <= 1) return;
      const updated = nodes.filter(n => n.id !== id);
      onNodesChange(updated);
      if (activeNodeId === id) {
          setActiveNodeId(updated[0].id);
      }
  };

  const updateNode = (id: string, updates: Partial<BuilderNode>) => {
      const updated = nodes.map(n => n.id === id ? { ...n, ...updates } : n);
      onNodesChange(updated.sort((a,b) => a.position - b.position));
  };

  const activeNode = nodes.find(n => n.id === activeNodeId);
  const maxPins = Math.min(deviceLedCount, 32);
  const previewLeds = PositionalMathBuffer.generateArray(nodes, deviceLedCount, fillMode === 'GRADIENT');

  return (
    <div className="flex flex-col gap-4 bg-slate-900/50 p-4 rounded-lg border border-slate-800">
      <div className="flex justify-between items-center text-xs font-bold text-slate-400">
         <span>LAYOUT (MAX {maxPins})</span>
      </div>

      {/* VISUAL MAP PREVIEW */}
      <div className="w-full h-3 rounded flex overflow-hidden">
         {previewLeds.map((c: any, i: number) => (
             <div key={i} className="flex-1" style={{ backgroundColor: `rgb(${c.r}, ${c.g}, ${c.b})` }} />
         ))}
      </div>

      {/* PIN SELECTOR ROW */}
      <div className="flex gap-2 items-center overflow-x-auto pb-2">
        {nodes.map(n => (
            <button 
               key={n.id}
               onClick={() => setActiveNodeId(n.id)}
               style={{
                   backgroundColor: n.colorHex,
                   borderColor: activeNodeId === n.id ? '#00F0FF' : 'rgba(255,255,255,0.3)',
                   boxShadow: activeNodeId === n.id ? `0 0 10px ${n.colorHex}` : 'none'
               }}
               className={`w-8 h-8 rounded-full border-2 flex justify-center items-center flex-shrink-0 transition-all`}
            >
               {activeNodeId === n.id && <span className="text-black text-xs font-bold font-mono px-1">↓</span>}
            </button>
        ))}
        {nodes.length < maxPins && (
            <button 
               onClick={addNode}
               className="w-8 h-8 rounded-full border border-dashed border-slate-500 bg-slate-800/50 flex justify-center items-center hover:bg-slate-700 transition-colors"
            >
               <Plus size={14} className="text-slate-400" />
            </button>
        )}
      </div>

      {/* ACTIVE PIN EDITOR */}
      {activeNode && (
         <div className="bg-black/20 p-4 rounded-lg border border-slate-800/50 flex flex-col gap-4">
            <div className="flex items-center gap-4">
               <span className="text-white font-mono font-bold text-xs w-10">@{activeNode.position}%</span>
               <input 
                   type="range" 
                   min="0" max="100" 
                   value={activeNode.position}
                   onChange={(e) => updateNode(activeNode.id, { position: parseInt(e.target.value) })}
                   className="flex-1 accent-cyan-400 h-2 bg-slate-700 rounded-lg appearance-none cursor-pointer"
               />
               <button onClick={() => removeNode(activeNode.id)} disabled={nodes.length <= 1} className="text-slate-500 hover:text-red-400 disabled:opacity-30">
                   <Trash2 size={16} />
               </button>
            </div>

            <div className="flex flex-wrap gap-2 justify-between">
               {COLOR_PRESET_PALETTE.map((color) => (
                  <button
                     key={color}
                     onClick={() => updateNode(activeNode.id, { colorHex: color })}
                     style={{
                        backgroundColor: color,
                        borderColor: activeNode.colorHex.toUpperCase() === color.toUpperCase() ? '#00F0FF' : 'rgba(255,255,255,0.2)'
                     }}
                     className="w-6 h-6 rounded-full border-2 transition-transform hover:scale-110"
                  />
               ))}
            </div>
         </div>
      )}

      {/* BEHAVIOR TIER */}
      <div className="flex gap-4">
          <div className="flex-1 flex flex-col gap-2">
              <span className="text-slate-400 text-[10px] font-bold">FILLING</span>
              <div className="flex bg-black/20 rounded p-1">
                  <button 
                      onClick={() => onFillModeChange('GRADIENT')}
                      className={`flex-1 py-1 text-[10px] font-bold rounded ${fillMode === 'GRADIENT' ? 'bg-slate-800 text-cyan-400' : 'text-slate-500 hover:text-slate-300'}`}
                  >
                      GRADIENT
                  </button>
                  <button 
                      onClick={() => onFillModeChange('SOLID')}
                      className={`flex-1 py-1 text-[10px] font-bold rounded ${fillMode === 'SOLID' ? 'bg-slate-800 text-cyan-400' : 'text-slate-500 hover:text-slate-300'}`}
                  >
                      SOLID
                  </button>
              </div>
          </div>

          <div className="flex-1 flex flex-col gap-2">
              <span className="text-slate-400 text-[10px] font-bold">DIRECTION</span>
              <div className="flex bg-black/20 rounded p-1">
                  <button 
                      onClick={() => onDirectionChange(1)}
                      className={`flex-1 py-1 text-[10px] font-bold rounded ${direction === 1 ? 'bg-slate-800 text-cyan-400' : 'text-slate-500 hover:text-slate-300'}`}
                  >
                      FORWARD
                  </button>
                  <button 
                      onClick={() => onDirectionChange(0)}
                      className={`flex-1 py-1 text-[10px] font-bold rounded ${direction === 0 ? 'bg-slate-800 text-cyan-400' : 'text-slate-500 hover:text-slate-300'}`}
                  >
                      REVERSE
                  </button>
              </div>
          </div>
      </div>

      <div className="flex flex-col gap-2">
        <span className="text-slate-400 text-[10px] font-bold">ANIMATION</span>
        <div className="flex flex-wrap gap-2">
            {[
                { id: 0x01, label: 'STATIC',  icon: Pause },
                { id: 0x02, label: 'FLOW',    icon: ArrowRight },
                { id: 0x03, label: 'STROBE',  icon: Activity },
                { id: 0x04, label: 'JUMP',    icon: FastForward },
            ].map(t => {
                const Icon = t.icon;
                return (
                    <button 
                        key={t.id}
                        onClick={() => onTransitionTypeChange(t.id)}
                        className={`flex items-center gap-1 px-3 py-1.5 rounded-md border text-[10px] font-bold transition-colors ${transitionType === t.id ? 'border-cyan-400 bg-cyan-400/10 text-cyan-400' : 'border-slate-700 hover:bg-slate-800 text-slate-400'}`}
                    >
                        <Icon size={12} />
                        {t.label}
                    </button>
                )
            })}
        </div>
      </div>
    </div>
  );
}
