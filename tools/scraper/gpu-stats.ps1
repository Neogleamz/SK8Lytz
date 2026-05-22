$v = (Get-Counter '\GPU Adapter Memory(*)\Dedicated Usage' -ErrorAction SilentlyContinue).CounterSamples |
  Sort-Object CookedValue -Descending | Select-Object -First 1
$u = (Get-Counter '\GPU Engine(*engtype_3D*)\Utilization Percentage' -ErrorAction SilentlyContinue).CounterSamples |
  Where-Object { $_.CookedValue -gt 0 } | Measure-Object CookedValue -Sum
$vram = if ($v) { [math]::Round($v.CookedValue / 1MB, 1) } else { 0 }
$util = if ($u) { [math]::Round($u.Sum, 1) } else { 0 }
Write-Output "$vram|$util"
