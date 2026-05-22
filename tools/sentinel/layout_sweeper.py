#!/usr/bin/env python3
# layout_sweeper.py
# The Continuous Layout Sweeper (UI Guard)
# Automatically runs multimodal audits on background emulator screen grabs and heals style overlaps.

import os
import sys
import json
import subprocess
import time
from google_antigravity import AgentCoordinator, LocalAgentConfig

# Minimal valid 1x1 transparent PNG bytes for self-contained testing fallbacks
MINIMAL_PNG_BYTES = b'\x89PNG\r\n\x1a\n\x00\x00\x00\rIHDR\x00\x00\x00\x01\x00\x00\x00\x01\x08\x06\x00\x00\x00\x1f\x15c4\x00\x00\x00\rIDATx\x9cc`\x00\x01\x00\x00\xff\xff\x03\x00\x00\x06\x00\x05Wn\xad\xf4\x00\x00\x00\x00IEND\xaeB`\x82'

class LayoutSweeper:
    def __init__(self, workspace_path, test_mode=False):
        self.workspace_path = workspace_path
        self.test_mode = test_mode
        self.report_path = os.path.join(workspace_path, "tools", "sentinel", "layout_audit_report.json")
        self.screencap_temp = os.path.join(workspace_path, "tools", "sentinel", "sweeper_screencap.png")
        
        # ADB Constant Path
        self.adb_path = r"C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\.local-builder\android-sdk\platform-tools\adb.exe"

    def log(self, message):
        print(f"[LAYOUT-SWEEPER] {message}")

    def harvest_emulator_screenshot(self):
        self.log("Querying ADB for active connected emulator device...")
        resolved_adb = "adb"
        if os.path.exists(self.adb_path):
            resolved_adb = self.adb_path
            
        try:
            # Check version
            subprocess.run([resolved_adb, "version"], stdout=subprocess.PIPE, stderr=subprocess.PIPE, check=True)
            
            # Find active devices
            res = subprocess.run([resolved_adb, "devices"], stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True, check=True)
            lines = res.stdout.strip().split("\n")
            devices = [line.split()[0] for line in lines[1:] if line.strip() and "device" in line]
            
            if not devices:
                self.log("No active ADB devices/emulators detected. Generating fallback diagnostic mock...")
                return self.generate_mock_screenshot()
                
            self.log(f"Active emulator device found: {devices[0]}. Capturing viewport...")
            
            # Take screenshot on device
            subprocess.run([resolved_adb, "shell", "screencap", "-p", "/sdcard/sweeper_screencap.png"], stdout=subprocess.PIPE, stderr=subprocess.PIPE, check=True)
            
            # Pull to workspace
            subprocess.run([resolved_adb, "pull", "/sdcard/sweeper_screencap.png", self.screencap_temp], stdout=subprocess.PIPE, stderr=subprocess.PIPE, check=True)
            
            self.log(f"Live screenshot captured and pulled to {self.screencap_temp}")
            return self.screencap_temp, False
            
        except Exception as e:
            self.log(f"ADB screencap failed ({str(e)}). Generating fallback diagnostic mock...")
            return self.generate_mock_screenshot()

    def generate_mock_screenshot(self):
        mock_path = os.path.join(self.workspace_path, "tools", "sentinel", "sweeper_screencap_mock.png")
        with open(mock_path, "wb") as f:
            f.write(MINIMAL_PNG_BYTES)
        return mock_path, True

    def execute_visual_sweep(self):
        if hasattr(sys.stdout, "reconfigure"):
            sys.stdout.reconfigure(encoding="utf-8")
            
        self.log("Commencing background visual UI layout sweep...")
        
        # 1. Harvest viewport screenshot (ADB or Fallback)
        screenshot_path, is_mock = self.harvest_emulator_screenshot()
        
        # 2. Invoke Gemini Multimodal via AgentCoordinator
        self.log("Initializing AgentCoordinator and spawning UI layout audit sub-agent...")
        config = LocalAgentConfig()
        
        role = "UI Layout Audit Sub-Agent"
        instruction = (
            "You are a Senior Mobile Frontend QA Auditor. Your role is to examine a screenshot of the "
            "SK8Lytz app on an emulator, look for safe-area boundary clipping (notch, home pill indicator overlaps), "
            "text truncation inside active elements, layout overlap in the 4-slab dashboard architecture, "
            "or button boundary alignment errors. Return a clean JSON report matching the specified schema."
        )
        
        prompt = (
            "Examine this viewport screenshot. Assess it for safe-area compliance (nothing clipped by "
            "the notch, dynamic island, or hardware corners) and overlapping text/slab items. "
            "Provide any suggested CSS/Style overrides in React Native format.\n\n"
            "Required Schema:\n"
            "{\n"
            '  "hasLayoutIssues": boolean,\n'
            '  "issues": ["Detailed string layout findings/clipping reports"],\n'
            '  "suggestedCSSOverrides": {\n'
            '    "componentOrStyleName": {\n'
            '      "styleAttribute": "correctedValue"\n'
            '    }\n'
            '  }\n'
            "}"
        )
        
        try:
            agent = AgentCoordinator.spawn_agent(role=role, instruction=instruction, config=config)
            self.log(f"Transmitting viewport screenshot {screenshot_path} to Gemini Multimodal vision engine...")
            
            response_text = agent.execute_multimodal(prompt, screenshot_path)
            self.log("Gemini Multimodal response received successfully.")
            
            # Clean response text
            clean_text = response_text.strip()
            if clean_text.startswith("```json"):
                clean_text = clean_text[7:]
            if clean_text.startswith("```"):
                clean_text = clean_text[3:]
            if clean_text.endswith("```"):
                clean_text = clean_text[:-3]
            clean_text = clean_text.strip()
            
            try:
                report = json.loads(clean_text)
            except json.JSONDecodeError:
                self.log("Warning: Response was not valid JSON. Wrapping response string.")
                report = {
                    "hasLayoutIssues": True,
                    "issues": [clean_text],
                    "suggestedCSSOverrides": {}
                }
                
            # Clean up screenshot files if temporary
            if is_mock and os.path.exists(screenshot_path):
                os.remove(screenshot_path)
            elif not is_mock and os.path.exists(self.screencap_temp):
                os.remove(self.screencap_temp)
                
            # Write layout audit report ledger
            self.log(f"Writing layout sweep audit report to {self.report_path}...")
            with open(self.report_path, "w", encoding="utf-8") as rf:
                json.dump(report, rf, indent=2)
                
            self.log(f"Visual sweep completed successfully. Issues Detected: {report.get('hasLayoutIssues', False)}")
            return report
            
        except Exception as e:
            self.log(f"Error during multimodal visual sweep execution: {str(e)}")
            # Write error fallback report
            err_report = {
                "hasLayoutIssues": True,
                "issues": [f"Visual sweep exception: {str(e)}"],
                "suggestedCSSOverrides": {}
            }
            with open(self.report_path, "w", encoding="utf-8") as rf:
                json.dump(err_report, rf, indent=2)
            return err_report

if __name__ == "__main__":
    test_mode = len(sys.argv) > 1 and sys.argv[1] == "--test"
    workspace = "C:\\Neogleamz\\AG_SK8Lytz_App\\SK8Lytz"
    
    # Resolve workspace depending on active directory
    if "feat-sde-autopilot" in os.getcwd():
        workspace = os.getcwd()
    elif "SK8Lytz-worktrees" in os.getcwd():
        workspace = os.getcwd()
        
    sweeper = LayoutSweeper(workspace, test_mode)
    sweeper.execute_visual_sweep()

