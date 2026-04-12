const fs = require('fs');
const path = require('path');

const fileContent = fs.readFileSync('src/components/CrewModal.tsx', 'utf8');

const regexes = [
  { name: 'CrewLandingScreen', startMarker: 'const renderLanding = () => {', endMarker: 'const renderCreate = () => (' },
  { name: 'CrewCreateScreen', startMarker: 'const renderCreate = () => (', endMarker: 'const renderSchedule = () => (' },
  { name: 'CrewScheduleScreen', startMarker: 'const renderSchedule = () => (', endMarker: 'const renderJoin = () => (' },
  { name: 'CrewJoinScreen', startMarker: 'const renderJoin = () => (', endMarker: 'const renderController = () => {' },
  { name: 'CrewControllerScreen', startMarker: 'const renderController = () => {', endMarker: 'const renderManage = () => {' },
  { name: 'CrewManageScreen', startMarker: 'const renderManage = () => {', endMarker: 'const renderCrewDetail = () => {' },
  { name: 'CrewDetailScreen', startMarker: 'const renderCrewDetail = () => {', endMarker: '// MAIN RENDER' }
];

const sharedImports = `import React, { useRef, useEffect } from 'react';
import { View, Text, TouchableOpacity, ScrollView, Animated, ActivityIndicator, Alert, Share, TextInput, Image, RefreshControl } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Colors } from '../../theme';
import { createStyles } from './CrewStyles';
import { useCrewContext } from '../../context/CrewContext';
import { profileService } from '../../services/profileService';
// TODO: any other specific imports check manually
import { Picker } from '@react-native-picker/picker';

const styles = createStyles(Colors);
`;

for (const screen of regexes) {
  let startIndex = fileContent.indexOf(screen.startMarker);
  let endIndex = fileContent.indexOf(screen.endMarker);
  
  if (startIndex === -1 || endIndex === -1) {
    console.error("COULD NOT FIND", screen.name);
    continue;
  }
  
  // Extract block
  let block = fileContent.substring(startIndex, endIndex);

  // Strip the wrapper, we know it's either () => ( or () => {
  if (block.startsWith(screen.startMarker)) {
    block = block.substring(screen.startMarker.length).trim();
  }
  
  // Strip the closing bracket/paren for the function
  block = block.replace(/[\}\)]\s*;\s*$/, '').trim();

  // For the ones that used simple parenthesis return (like renderCreate = () => ( ... )), we wrap it in a return.
  if (screen.startMarker.includes('=> (')) {
     block = 'return (\n' + block + '\n);';
  }

  // Create component wrapper
  const componentContent = `${sharedImports}

export function ${screen.name}() {
  const context = useCrewContext();
  const { hub, manage, session, setStep, step, confirmAction, setConfirmAction, currentUserId, displayName, errorMsg, setErrorMsg, isLoading, setIsLoading, showCodeEntry, setShowCodeEntry } = context;
  const { activeSessions, myCrews, permanentCrews, isLoadingNearby, refreshNearby, nearbySessions, discoverRadiusMi, setDiscoverRadiusMi, locationLabel, handleDetectLocation, isGettingLocation } = hub;
  const { selectedCrewDetail, setSelectedCrewDetail, expandedCrewId, setExpandedCrewId, cardMembers, setCardMembers, loadingCardMembersFor, makingOwnerFor, setMakingOwnerFor, confirmingDeleteCrewId, setConfirmingDeleteCrewId, confirmingLeaveCrewId, setConfirmingLeaveCrewId, createCrewError, setCreateCrewError, isCreatingCrew, newCrewName, setNewCrewName, newCrewDesc, newCrewIsPublic, setNewCrewIsPublic, newCrewCity, setNewCrewCity, newCrewState, setNewCrewState } = manage;
  const { currentSession, isHandoffMode, executeLeaveSession, executeEndSession, handleHandoffLeadership } = session;
  
  // NOTE: You will need to bring in local state that wasn't context-ified
  // or convert them. For now, the structure guarantees safe parsing context injection.

  ${block}
}
`;

  fs.writeFileSync(path.join('src/components/crew', screen.name + '.tsx'), componentContent);
  console.log('Generated ' + screen.name + '.tsx');
}
