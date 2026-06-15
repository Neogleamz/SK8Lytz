import { useEffect, useState } from 'react';
import { CrewMemberFull, PermanentCrew, profileService } from '../services/ProfileService';
import { AppLogger } from '../services/appLogger';

const SEARCH_DEBOUNCE_MS = 300;

export function useCrewManage(
  myCrews: PermanentCrew[]
) {
  const [selectedCrewDetail, setSelectedCrewDetail] = useState<PermanentCrew | null>(null);

  // Crew detail & Stats
  const [crewStats, setCrewStats] = useState<Record<string, { sessionCount: number; lastActive: string | null; topScene: string | null; totalDistanceMiles?: number; avgSpeedMph?: number; peakSpeedMph?: number; peakGForce?: number; totalDurationSec?: number }>>({});
  const [expandedCrewId, setExpandedCrewId] = useState<string | null>(null);

  // Members Card info
  const [cardMembers, setCardMembers] = useState<Record<string, CrewMemberFull[]>>({});
  const [loadingCardMembersFor, setLoadingCardMembersFor] = useState<string | null>(null);

  // User Search
  const [userSearchQuery, setUserSearchQuery] = useState('');
  const [userSearchResults, setUserSearchResults] = useState<{ user_id: string, username: string | null, display_name: string | null }[]>([]);
  const [selectedMembers, setSelectedMembers] = useState<{ user_id: string, username: string | null, display_name: string | null }[]>([]);

  // Actions
  const [makingOwnerFor, setMakingOwnerFor] = useState<string | null>(null);
  const [isRemovingUserFor, setIsRemovingUserFor] = useState<string | null>(null);
  const [isAddingMembersTo, setIsAddingMembersTo] = useState<string | null>(null);

  // Create form
  const [newCrewName, setNewCrewName] = useState('');
  const [newCrewIsPublic, setNewCrewIsPublic] = useState(false);
  const [newCrewColor, setNewCrewColor] = useState('#FFAA00');
  const [newCrewIcon, setNewCrewIcon] = useState('roller-skate');
  const [newCrewCity, setNewCrewCity] = useState('');
  const [newCrewState, setNewCrewState] = useState('');
  const [newCrewDescription, setNewCrewDescription] = useState('');
  const [newCrewPhotoUri, setNewCrewPhotoUri] = useState<string | null>(null);
  const [newCrewCode, setNewCrewCode] = useState(() => Math.random().toString(36).substring(2, 8).toUpperCase());
  const [newCrewHue, setNewCrewHue] = useState(40);
  type ManageStatus = 'idle' | 'creating' | 'saving' | 'error' | 'success';
  const [status, setStatus] = useState<ManageStatus>('idle');
  const isCreatingCrew = status === 'creating';
  const [createCrewError, setCreateCrewError] = useState('');

  // Edit form
  const [editingCrewId, setEditingCrewId] = useState<string | null>(null);
  const [editCrewName, setEditCrewName] = useState('');
  const [editCrewIsPublic, setEditCrewIsPublic] = useState(false);
  const [editCrewCity, setEditCrewCity] = useState('');
  const [editCrewState, setEditCrewState] = useState('');
  const [editCrewDesc, setEditCrewDesc] = useState('');
  const isSavingCrew = status === 'saving';

  // Confirmations
  const [confirmingDeleteCrewId, setConfirmingDeleteCrewId] = useState<string | null>(null);
  const [confirmingLeaveCrewId, setConfirmingLeaveCrewId] = useState<string | null>(null);

  // Helper to load full member details
  const loadCrewMembers = (crewId: string) => {
    if (cardMembers[crewId]) return;
    setLoadingCardMembersFor(crewId);
    profileService.getCrewMembersWithNames(crewId)
      .then(members => setCardMembers(prev => ({ ...prev, [crewId]: members })))
      .catch((e) => AppLogger.warn('[CrewManage] loadCrewMembers failed', { crewId, error: String(e), payload_size: 0, ssi: 0 }))
      .finally(() => setLoadingCardMembersFor(null));
  };

  // Fetch crew stats
  useEffect(() => {
    if (!selectedCrewDetail) return;
    const id = selectedCrewDetail.id;
    if (crewStats[id]) return; // already loaded
    profileService.getCrewStats(id)
      .then(stats => setCrewStats(prev => ({ ...prev, [id]: stats })))
      .catch((e) => AppLogger.warn('[CrewManage] getCrewStats failed', { id, error: String(e), payload_size: 0, ssi: 0 }));
  }, [selectedCrewDetail, crewStats]);

  // Search users with debounce
  useEffect(() => {
    if (!userSearchQuery.trim()) {
      setUserSearchResults([]);
      return;
    }
    let active = true;
    const timer = setTimeout(() => {
      profileService.searchUsers(userSearchQuery)
        .then((res) => {
          if (active) setUserSearchResults(res);
        })
        .catch((e) => {
          if (active) AppLogger.warn('[CrewManage] searchUsers failed', { query: '[REDACTED]', error: String(e), payload_size: 0, ssi: 0 });
        });
    }, SEARCH_DEBOUNCE_MS);
    return () => {
      active = false;
      clearTimeout(timer);
    };
  }, [userSearchQuery]);

  return {
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

    // Create form
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
    isCreatingCrew, setIsCreatingCrew: (v: boolean) => setStatus(v ? 'creating' : 'idle'),
    createCrewError, setCreateCrewError,

    // Edit form
    editingCrewId, setEditingCrewId,
    editCrewName, setEditCrewName,
    editCrewIsPublic, setEditCrewIsPublic,
    editCrewCity, setEditCrewCity,
    editCrewState, setEditCrewState,
    editCrewDesc, setEditCrewDesc,
    isSavingCrew, setIsSavingCrew: (v: boolean) => setStatus(v ? 'saving' : 'idle'),
    status, setStatus,

    confirmingDeleteCrewId, setConfirmingDeleteCrewId,
    confirmingLeaveCrewId, setConfirmingLeaveCrewId,
  };
}
