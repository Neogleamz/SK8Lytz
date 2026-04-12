import fs from 'fs';

const filePath = 'src/components/CrewModal.tsx';
let content = fs.readFileSync(filePath, 'utf-8');

// 1. Add CrewProvider import
if (!content.includes('CrewProvider')) {
  content = content.replace(
    "import { useCrewSession } from '../hooks/useCrewSession';",
    "import { useCrewSession } from '../hooks/useCrewSession';\nimport { CrewProvider } from '../context/CrewContext';"
  );
}

// 2. Wrap hooks
content = content.replace(
  '  const {\n    discoverRadiusMi,',
  '  const hub = useCrewHub(visible, step);\n  const {\n    discoverRadiusMi,'
);
content = content.replace(
  '  } = useCrewHub(visible, step);',
  '  } = hub;'
);

content = content.replace(
  '  const {\n    selectedCrewDetail,',
  '  const manage = useCrewManage(hub.myCrews);\n  const {\n    selectedCrewDetail,'
);
content = content.replace(
  '  } = useCrewManage(myCrews);',
  '  } = manage;'
);

content = content.replace(
  `  const {
    currentSession, setCurrentSession,
    currentRole, setCurrentRole,
    members, setMembers, loadMembers,
    isHandoffMode, setIsHandoffMode,
    handleSessionJoined,
    executeEndSession,
    executeLeaveSession,
    handleHandoffLeadership
  } = useCrewSession(
    activeSession, activeRole, currentUserId,
    onSessionReady, onSessionLeft, onSessionEnded,
    refreshNearby, 
    () => setStep('landing'), 
    setErrorMsg
  );`,
  `  const session = useCrewSession(
    activeSession, activeRole, currentUserId,
    onSessionReady, onSessionLeft, onSessionEnded,
    refreshNearby, 
    () => setStep('landing'), 
    setErrorMsg
  );
  const {
    currentSession, setCurrentSession,
    currentRole, setCurrentRole,
    members, setMembers, loadMembers,
    isHandoffMode, setIsHandoffMode,
    handleSessionJoined,
    executeEndSession,
    executeLeaveSession,
    handleHandoffLeadership
  } = session;`
);

// 3. Define contextValue
const contextValueCode = `  const contextValue = {
    step, setStep,
    showCodeEntry, setShowCodeEntry,
    isLoading, setIsLoading,
    errorMsg, setErrorMsg,
    confirmAction, setConfirmAction,
    hub, manage, session
  };

  const pulseAnim = useRef(new Animated.Value(1)).current;`;

content = content.replace('  const pulseAnim = useRef(new Animated.Value(1)).current;', contextValueCode);

// 4. Wrap the Modal contents in CrewProvider
content = content.replace(
  '<KeyboardAvoidingView',
  '<CrewProvider value={contextValue}>\n      <KeyboardAvoidingView'
);
content = content.replace(
  '      </KeyboardAvoidingView>\n    </Modal>',
  '      </KeyboardAvoidingView>\n      </CrewProvider>\n    </Modal>'
);

fs.writeFileSync(filePath, content);
console.log("Context injection successful.");
