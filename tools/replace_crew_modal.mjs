import fs from 'fs';

const filePath = 'src/components/CrewModal.tsx';
const content = fs.readFileSync(filePath, 'utf-8');
const lines = content.split('\n');

const newCode = `  const [step, setStep] = useState<ModalStep>(activeSession ? 'controller' : 'landing');
  const [showCodeEntry, setShowCodeEntry] = useState(false);
  const [_crewActiveSessions, _setCrewActiveSessions] = useState<Record<string, CrewSession | null>>({});
  const [isLoading, setIsLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');
  const [confirmAction, setConfirmAction] = useState<'end' | 'leave' | null>(null);

  // ── Hooks ──────────────────────────────────────────────────────────────────
  const {
    discoverRadiusMi, setDiscoverRadiusMi,
    myCrews, setMyCrews,
    permanentCrews, setPermanentCrews,
    crewMemberCounts, setCrewMemberCounts,
    nearbySessions, setNearbySessions,
    isLoadingNearby, refreshNearby,
    activeSessions, setActiveSessions,
    isLoadingSessions, loadActiveSessions,
    isGettingLocation, setIsGettingLocation,
    locationLabel, setLocationLabel,
    locationCoords, setLocationCoords,
    handleDetectLocation
  } = useCrewHub(visible, step);

  const {
    selectedCrewDetail, setSelectedCrewDetail,
    crewStats, setCrewStats,
    expandedCrewId, setExpandedCrewId,
    cardMembers, setCardMembers,
    loadingCardMembersFor, setLoadingCardMembersFor,
    loadCrewMembers,
    userSearchQuery, setUserSearchQuery,
    userSearchResults, setUserSearchResults,
    selectedMembers, setSelectedMembers,
    makingOwnerFor, setMakingOwnerFor,
    isRemovingUserFor, setIsRemovingUserFor,
    isAddingMembersTo, setIsAddingMembersTo,
    newCrewName, setNewCrewName,
    newCrewIsPublic, setNewCrewIsPublic,
    newCrewColor, setNewCrewColor,
    newCrewIcon, setNewCrewIcon,
    newCrewCity, setNewCrewCity,
    newCrewState, setNewCrewState,
    newCrewDescription, setNewCrewDescription,
    newCrewPhotoUri, setNewCrewPhotoUri,
    newCrewCode, setNewCrewCode,
    newCrewHue, setNewCrewHue,
    isCreatingCrew, setIsCreatingCrew,
    createCrewError, setCreateCrewError,
    editingCrewId, setEditingCrewId,
    editCrewName, setEditCrewName,
    editCrewIsPublic, setEditCrewIsPublic,
    editCrewCity, setEditCrewCity,
    editCrewState, setEditCrewState,
    editCrewDesc, setEditCrewDesc,
    isSavingCrew, setIsSavingCrew,
    confirmingDeleteCrewId, setConfirmingDeleteCrewId,
    confirmingLeaveCrewId, setConfirmingLeaveCrewId,
  } = useCrewManage(myCrews);

  // ── Create / Schedule form local state ─────────────────────────────────────
  const [crewName, setCrewName] = useState('');
  const [displayName, setDisplayName] = useState('');
  const [_scheduledDate, _setScheduledDate] = useState<Date | null>(null);
  const [selectedCrewId, setSelectedCrewId] = useState<string | null>(null);
  const [isPublic, setIsPublic] = useState(false);
  const [schedDateTime, setSchedDateTime] = useState<Date>(() => {
    const d = new Date();
    d.setDate(d.getDate() + 1);
    d.setHours(19, 0, 0, 0);
    return d;
  });
  const [showDatePicker, setShowDatePicker] = useState(false);
  const [showTimePicker, setShowTimePicker] = useState(false);
  const [inviteCode, setInviteCode] = useState('');
  const [joiningSessionId, setJoiningSessionId] = useState<string | null>(null);
  const [_discoverRadius, setDiscoverRadius] = useState(50);
  const [_discoverSearch, setDiscoverSearch] = useState('');
  const [joiningCrewId, setJoiningCrewId] = useState<string | null>(null);

  const [currentUserId, setCurrentUserId] = useState('');

  const {
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
  );

  const pulseAnim = useRef(new Animated.Value(1)).current;

  useEffect(() => {
    const pulse = Animated.loop(
      Animated.sequence([
        Animated.timing(pulseAnim, { toValue: 0.4, duration: 800, useNativeDriver: true }),
        Animated.timing(pulseAnim, { toValue: 1, duration: 800, useNativeDriver: true }),
      ])
    );
    pulse.start();
    return () => pulse.stop();
  }, []);

  useEffect(() => {
    if (!visible) return;
    const loadUser = async () => {
      const { data: { user } } = await supabase.auth.getUser();
      if (!user) return;
      setCurrentUserId(user.id);
      try {
        const profile = await profileService.fetchOrCreateProfile();
        setDisplayName(profile?.display_name || user.email?.split('@')[0] || 'Skater');
      } catch {
        setDisplayName(user.email?.split('@')[0] || 'Skater');
      }
    };
    loadUser();
  }, [visible]);

  const handleCreate = async (scheduled?: Date) => {
    let sessionName = crewName.trim() || permanentCrews.find(c => c.id === selectedCrewId)?.name || '';
    if (!sessionName) { setErrorMsg('Pick a crew or enter a session name'); return; }

    const now = new Date();
    const dateStr = \`\${(now.getMonth() + 1).toString().padStart(2, '0')}/\${now.getDate().toString().padStart(2, '0')}\`;
    sessionName = \`\${sessionName}_\${dateStr}\`;

    setIsLoading(true); setErrorMsg('');
    try {
      const crewInfo = myCrews.find(c => c.id === selectedCrewId);
      const isSessionPublic = crewInfo ? crewInfo.is_public : false;

      const opts: Parameters<typeof crewService.createSession>[2] = {
        isPublic: isSessionPublic,
        crewId:   selectedCrewId ?? undefined,
      };
      if (locationLabel) opts.locationLabel = locationLabel;
      if (locationCoords) opts.locationCoords = locationCoords;
      if (scheduled) opts.scheduledAt = scheduled.toISOString();

      const session = await crewService.createSession(sessionName, displayName.trim(), opts);
      AppLogger.log('CREW_SESSION_CREATED', {
        sessionId: session.id, crewName: sessionName,
        hasLocation: !!locationLabel, scheduled: !!scheduled, isPublic,
      });

      if (scheduled && scheduled > new Date()) {
        const crewLabel = permanentCrews.find(c => c.id === selectedCrewId)?.name ?? sessionName;
        notificationService.sendSessionStartingSoon({
          sessionId: session.id,
          sessionName,
          crewName: crewLabel,
          scheduledAt: scheduled,
        }).catch(() => { }); 
      }

      await handleSessionJoined(session);
    } catch (e: any) {
      AppLogger.log('CREW_ERROR', { action: 'create', error: e.message });
      setErrorMsg(e.message || 'Failed to create session');
    } finally { setIsLoading(false); }
  };

  const handleJoinByCode = async () => {
    if (inviteCode.trim().length < 6) { setErrorMsg('Enter the 6-character crew invite code'); return; }
    setIsLoading(true); setErrorMsg('');
    try {
      const crew = await profileService.joinPermanentCrew(inviteCode.trim());
      AppLogger.log('CREW_SESSION_JOINED', { crewId: crew.id, crewName: crew.name, method: 'permanent_code' });
      const updatedCrews = await profileService.getMyCrew();
      setMyCrews(updatedCrews);
      setPermanentCrews(updatedCrews.map(c => ({ id: c.id, name: c.name })));
      setShowCodeEntry(false);
      setInviteCode('');
      Alert.alert(
        '🛹 Joined!',
        \`You're now a member of "\${crew.name}". When they start a session you'll see it under My Crews.\`,
        [{ text: 'Nice!' }]
      );
    } catch (e: any) {
      AppLogger.log('CREW_ERROR', { action: 'join_crew_by_code', error: e.message });
      setErrorMsg(e.message || 'Crew not found — check the code and try again.');
    } finally { setIsLoading(false); }
  };

  const handleJoinById = async (sessionId: string) => {
    setIsLoading(true); setErrorMsg('');
    try {
      const session = await crewService.joinSessionById(sessionId, displayName.trim());
      AppLogger.log('CREW_SESSION_JOINED', { sessionId: session.id, crewName: session.name, method: 'browse' });
      await handleSessionJoined(session);
    } catch (e: any) {
      AppLogger.log('CREW_ERROR', { action: 'join_id', error: e.message });
      setErrorMsg(e.message || 'Could not join that crew');
    } finally { setIsLoading(false); }
  };

  const handleLeave = () => {
    const effectiveIsLeader = currentRole === 'leader' || currentSession?.leader_user_id === currentUserId;
    if (effectiveIsLeader) {
      handleEndSession();
      return;
    }
    setConfirmAction('leave');
  };

  const handleEndSession = () => {
    setConfirmAction('end');
  };`;

const sliceBefore = lines.slice(0, 77); // up to 76
const sliceAfter = lines.slice(537); // from 537 onwards

const replacedContent = sliceBefore.join('\n') + '\n' + newCode + '\n' + sliceAfter.join('\n');

const newLines = replacedContent.split('\n');
const imports = `import { useCrewHub } from '../hooks/useCrewHub';
import { useCrewManage } from '../hooks/useCrewManage';
import { useCrewSession } from '../hooks/useCrewSession';`;

newLines.splice(33, 0, imports);

fs.writeFileSync(filePath, newLines.join('\n'));
console.log("Rewrite successful.");
