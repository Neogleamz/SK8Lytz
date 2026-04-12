import { useState, useEffect } from 'react';
import { profileService, CrewMemberFull, PermanentCrew } from '../services/ProfileService';

export function useCrewManage(
  myCrews: PermanentCrew[]
) {
  const [selectedCrewDetail, setSelectedCrewDetail] = useState<PermanentCrew | null>(null);

  // Crew detail & Stats
  const [crewStats, setCrewStats] = useState<Record<string, { sessionCount: number; lastActive: string | null; topScene: string | null }>>({});
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
  
  const [isCreatingCrew, setIsCreatingCrew] = useState(false);
  const [createCrewError, setCreateCrewError] = useState('');

  // Edit form
  const [editingCrewId, setEditingCrewId] = useState<string | null>(null);
  const [editCrewName, setEditCrewName] = useState('');
  const [editCrewIsPublic, setEditCrewIsPublic] = useState(false);
  const [editCrewCity, setEditCrewCity] = useState('');
  const [editCrewState, setEditCrewState] = useState('');
  const [editCrewDesc, setEditCrewDesc] = useState('');
  const [isSavingCrew, setIsSavingCrew] = useState(false);

  // Confirmations
  const [confirmingDeleteCrewId, setConfirmingDeleteCrewId] = useState<string | null>(null);
  const [confirmingLeaveCrewId, setConfirmingLeaveCrewId] = useState<string | null>(null);

  // Helper to load full member details
  const loadCrewMembers = (crewId: string) => {
    if (cardMembers[crewId]) return;
    setLoadingCardMembersFor(crewId);
    profileService.getCrewMembersWithNames(crewId)
      .then(members => setCardMembers(prev => ({ ...prev, [crewId]: members })))
      .catch(() => { })
      .finally(() => setLoadingCardMembersFor(null));
  };

  // Fetch crew stats
  useEffect(() => {
    if (!selectedCrewDetail) return;
    const id = selectedCrewDetail.id;
    if (crewStats[id]) return; // already loaded
    profileService.getCrewStats(id).then(stats =>
      setCrewStats(prev => ({ ...prev, [id]: stats }))
    );
  }, [selectedCrewDetail, crewStats]);

  // Search users with debounce
  useEffect(() => {
    if (!userSearchQuery.trim()) {
      setUserSearchResults([]);
      return;
    }
    const timer = setTimeout(() => {
      profileService.searchUsers(userSearchQuery).then(setUserSearchResults);
    }, 300);
    return () => clearTimeout(timer);
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
    isCreatingCrew, setIsCreatingCrew,
    createCrewError, setCreateCrewError,

    // Edit form
    editingCrewId, setEditingCrewId,
    editCrewName, setEditCrewName,
    editCrewIsPublic, setEditCrewIsPublic,
    editCrewCity, setEditCrewCity,
    editCrewState, setEditCrewState,
    editCrewDesc, setEditCrewDesc,
    isSavingCrew, setIsSavingCrew,

    confirmingDeleteCrewId, setConfirmingDeleteCrewId,
    confirmingLeaveCrewId, setConfirmingLeaveCrewId,
  };
}
