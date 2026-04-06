$file = 'src\components\LogViewerModal.tsx'
$content = [System.IO.File]::ReadAllText((Resolve-Path $file).Path)
$marker = "PERFORMANCE_METRIC:      { icon: 'chart-line',   color: '#FFD700', label: 'Metric' },"
$idx = $content.IndexOf($marker)
if ($idx -lt 0) { Write-Host "marker not found"; exit 1 }
$insertAt = $idx + $marker.Length
$addition = @'

  CREW_SESSION_CREATED:    { icon: 'account-group', color: '#FFAA00', label: 'Crew Created' },
  CREW_SESSION_JOINED:     { icon: 'account-plus',  color: '#00AAFF', label: 'Crew Joined' },
  CREW_SESSION_LEFT:       { icon: 'account-minus', color: '#FF6B6B', label: 'Crew Left' },
  CREW_SESSION_ENDED:      { icon: 'stop-circle',   color: '#FF6B6B', label: 'Session Ended' },
  CREW_LEADERSHIP_TRANSFERRED: { icon: 'crown',     color: '#FFD700', label: 'Leader Handoff' },
  CREW_SCENE_BROADCAST:    { icon: 'broadcast',     color: '#FFAA00', label: 'Scene Broadcast' },
  CREW_SCENE_RECEIVED:     { icon: 'download-circle', color: '#00AAFF', label: 'Scene Received' },
  CREW_AUTO_REJOINED:      { icon: 'refresh-circle', color: '#00E676', label: 'Auto-Rejoined' },
  CREW_ERROR:              { icon: 'account-alert', color: '#FF4444', label: 'Crew Error' },
  STREET_MODE_ACTIVATED:   { icon: 'run-fast',      color: '#FF8C00', label: 'Street Mode ON' },
  STREET_MODE_DEACTIVATED: { icon: 'walk',          color: '#888888', label: 'Street Mode OFF' },
  STREET_JERK_DETECTED:    { icon: 'gesture-swipe', color: '#FFC107', label: 'Jerk Detected' },
  STREET_SENSITIVITY_CHANGED: { icon: 'tune-vertical', color: '#AADDFF', label: 'Sensitivity' },
  PUSH_TOKEN_REGISTERED:   { icon: 'bell-ring',     color: '#00E676', label: 'Push Registered' },
  PUSH_NOTIFICATION_TAPPED:{ icon: 'bell-check',    color: '#00AAFF', label: 'Notif Tapped' },
  PUSH_NOTIFICATION_SENT:  { icon: 'send',          color: '#FFAA00', label: 'Notif Sent' },
  PUSH_TOKEN_UNREGISTERED: { icon: 'bell-off',      color: '#888888', label: 'Push Unregistered' },
  PROFILE_UPDATED:         { icon: 'account-edit',  color: '#c255ff', label: 'Profile Updated' },
  CREW_PERMANENT_CREATED:  { icon: 'star-circle',   color: '#FFAA00', label: 'Perm Crew Created' },
  CREW_PERMANENT_JOINED:   { icon: 'star-plus',     color: '#00AAFF', label: 'Perm Crew Joined' },
  CREW_PERMANENT_LEFT:     { icon: 'star-minus',    color: '#FF6B6B', label: 'Perm Crew Left' },
'@
$updated = $content.Substring(0, $insertAt) + $addition + $content.Substring($insertAt)
[System.IO.File]::WriteAllText((Resolve-Path $file).Path, $updated, [System.Text.UTF8Encoding]::new($false))
Write-Host "Done"
