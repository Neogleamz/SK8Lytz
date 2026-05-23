import React, { useState, useEffect, useRef } from 'react';

interface OmniTerminalProps {
  logs: { type: string; message: string; source?: string }[];
  historyLogs: string[];
  fetchHistory: () => void;
}

export const OmniTerminal: React.FC<OmniTerminalProps> = ({ logs, historyLogs, fetchHistory }) => {
  const [isAnchored, setIsAnchored] = useState<boolean>(true);
  const [pos, setPos] = useState({ x: 0, y: 0 });
  const [isDragging, setIsDragging] = useState(false);
  const [dragOffset, setDragOffset] = useState({ x: 0, y: 0 });
  const [isClosed, setIsClosed] = useState<boolean>(false);
  const logsRef = useRef<HTMLDivElement>(null);

  const getInitialPos = () => {
    return { x: window.innerWidth / 2 - 400, y: window.innerHeight - 300 };
  };

  useEffect(() => {
    if (!isAnchored && pos.x === 0 && pos.y === 0) {
      setPos(getInitialPos());
    }
  }, [isAnchored]);

  useEffect(() => {
    if (logsRef.current) {
      logsRef.current.scrollTop = logsRef.current.scrollHeight;
    }
  }, [logs]);

  const handlePointerDown = (e: React.PointerEvent) => {
    if (isAnchored) return;
    setIsDragging(true);
    e.currentTarget.setPointerCapture(e.pointerId);
    setDragOffset({
      x: e.clientX - pos.x,
      y: e.clientY - pos.y
    });
  };

  const handlePointerMove = (e: React.PointerEvent) => {
    if (!isDragging || isAnchored) return;
    setPos({
      x: e.clientX - dragOffset.x,
      y: e.clientY - dragOffset.y
    });
  };

  const handlePointerUp = (e: React.PointerEvent) => {
    if (!isDragging) return;
    setIsDragging(false);
    e.currentTarget.releasePointerCapture(e.pointerId);
  };

  if (isClosed) {
    return (
      <button 
        onClick={() => setIsClosed(false)}
        style={{
          position: 'fixed', bottom: '20px', left: '20px', zIndex: 1000,
          background: 'rgba(138,43,226,0.2)', border: '1px solid #8a2be2',
          color: '#fff', padding: '10px 20px', borderRadius: '30px', cursor: 'pointer',
          fontWeight: 800, fontFamily: 'Outfit, sans-serif', letterSpacing: '0.1em',
          boxShadow: '0 4px 15px rgba(138,43,226,0.3)', backdropFilter: 'blur(10px)',
          display: 'flex', alignItems: 'center', gap: '8px'
        }}
      >
        <span style={{ fontSize: '1.2rem' }}>⚡</span> REOPEN OMNI-TERMINAL
      </button>
    );
  }

  const headerControls = (
    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
      <button className="btn-mini" onClick={fetchHistory} style={{ 
        background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', 
        color: '#fff', padding: '4px 10px', borderRadius: '4px', fontSize: '0.6rem', cursor: 'pointer' 
      }}>
        PERSISTENT HISTORY
      </button>
      <button onClick={() => setIsAnchored(!isAnchored)} style={{ 
        background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', color: '#fff', 
        cursor: 'pointer', fontSize: '0.6rem', fontWeight: 800, padding: '4px 8px', borderRadius: '4px', transition: 'all 0.2s'
      }}>
        {isAnchored ? '⏏ FLOAT' : '⚓ ANCHOR'}
      </button>
      <button onClick={() => setIsClosed(true)} style={{ 
        background: 'rgba(255,51,102,0.1)', border: '1px solid rgba(255,51,102,0.3)', color: '#ff3366', 
        cursor: 'pointer', fontSize: '0.6rem', fontWeight: 800, padding: '4px 8px', borderRadius: '4px', transition: 'all 0.2s'
      }}>
        ✖ CLOSE
      </button>
    </div>
  );

  const TerminalContent = () => (
    <>
      <div 
        onPointerDown={handlePointerDown}
        onPointerMove={handlePointerMove}
        onPointerUp={handlePointerUp}
        onPointerCancel={handlePointerUp}
        style={{
          padding: '10px 16px', background: 'rgba(0,0,0,0.4)', borderBottom: '1px solid rgba(255,255,255,0.1)',
          display: 'flex', justifyContent: 'space-between', alignItems: 'center', cursor: isAnchored ? 'default' : 'grab',
          touchAction: 'none'
        }}
      >
        <h2 style={{ margin: 0, fontSize: '0.8rem', color: '#fff', textTransform: 'uppercase', letterSpacing: '0.1em', display: 'flex', alignItems: 'center', gap: '8px' }}>
          <div style={{ width: '8px', height: '8px', background: '#8a2be2', borderRadius: '50%', boxShadow: '0 0 10px #8a2be2' }} />
          OMNI-TERMINAL (GLOBAL SCOPE)
        </h2>
        {headerControls}
      </div>

      <div style={{ flex: 1, display: 'flex', flexDirection: 'column', overflow: 'hidden' }}>
        <div ref={logsRef} style={{ 
          flex: 1, overflowY: 'auto', padding: '12px', fontFamily: 'JetBrains Mono, monospace', fontSize: '0.75rem', lineHeight: '1.4' 
        }}>
          {logs.map((log, i) => (
            <div key={i} style={{ marginBottom: '4px', color: log.type === 'ERROR' ? '#ff3366' : '#fff' }}>
              <span style={{ opacity: 0.5 }}>[{new Date().toLocaleTimeString()}]</span>
              <span style={{ color: '#8a2be2', fontWeight: 'bold', margin: '0 8px' }}>[{log.source || 'SYSTEM'}]</span>
              {log.message}
            </div>
          ))}
          {logs.length === 0 && (
            <div style={{ color: 'rgba(255,255,255,0.3)', fontStyle: 'italic', padding: '1rem' }}>No telemetry signals detected.</div>
          )}
        </div>

        <div style={{ 
          height: '100px', overflowY: 'auto', background: 'rgba(0,0,0,0.5)', borderTop: '1px solid rgba(255,255,255,0.1)', padding: '8px', fontSize: '0.65rem', fontFamily: 'JetBrains Mono, monospace', color: 'rgba(255,255,255,0.4)' 
        }}>
          {historyLogs.slice(-10).map((line, i) => <div key={i} style={{ marginBottom: '2px' }}>{line}</div>)}
        </div>
      </div>
    </>
  );

  if (isAnchored) {
    return (
      <div style={{
        marginTop: '20px',
        width: '100%',
        height: '400px',
        background: 'rgba(15, 15, 20, 0.95)',
        border: '1px solid rgba(138,43,226,0.3)',
        borderRadius: '12px',
        overflow: 'hidden',
        display: 'flex',
        flexDirection: 'column',
        boxShadow: '0 10px 40px rgba(0,0,0,0.6), inset 0 1px 0 rgba(255,255,255,0.1)'
      }}>
        <TerminalContent />
      </div>
    );
  }

  return (
    <div style={{
      position: 'fixed',
      left: pos.x,
      top: pos.y,
      width: '800px',
      height: '500px',
      background: 'rgba(15, 15, 20, 0.95)',
      backdropFilter: 'blur(20px)',
      WebkitBackdropFilter: 'blur(20px)',
      border: '1px solid rgba(138,43,226,0.5)',
      borderRadius: '12px',
      overflow: 'hidden',
      display: 'flex',
      flexDirection: 'column',
      boxShadow: '0 20px 60px rgba(0,0,0,0.8), 0 0 30px rgba(138,43,226,0.2)',
      zIndex: 9999
    }}>
      <TerminalContent />
    </div>
  );
};
