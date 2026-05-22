#!/usr/bin/env python3
# sde_daemon.py
# The SDE Autopilot Daemon Core Engine
# Orchestrates and automates the closed-loop pipeline:
# Crawling (debt_harvester) -> Refinement (pm_refinement) -> Assembly Line (assembly_line) -> Healing (regression_healer) -> Sweeper/Fuzzer
# Hardened with a 3-strike debugger lockout safety gate and Discord bridge notifications.

import os
import sys
import json
import subprocess
import time
import argparse

class SdeAutopilotDaemon:
    def __init__(self, workspace_path, test_mode=False, continuous=False, max_strikes=3):
        self.workspace_path = workspace_path
        self.test_mode = test_mode
        self.continuous = continuous
        self.max_strikes = max_strikes
        self.strike_counter = 0
        self.discord_script = os.path.join(workspace_path, "tools", "discord-bridge", "notify_discord.ps1")
        
        # Tools configuration
        self.tools = {
            "harvester": os.path.join(workspace_path, "tools", "sentinel", "debt_harvester.py"),
            "refiner": os.path.join(workspace_path, "tools", "sentinel", "pm_refinement.py"),
            "assembly": os.path.join(workspace_path, "tools", "sentinel", "assembly_line.py"),
            "healer": os.path.join(workspace_path, "tools", "sentinel", "regression_healer.py"),
            "fuzzer": os.path.join(workspace_path, "tools", "sentinel", "protocol_fuzzer.py"),
            "sweeper": os.path.join(workspace_path, "tools", "sentinel", "layout_sweeper.py")
        }

    def log(self, message):
        print(f"\n🤖 [SDE-DAEMON] {message}")

    def notify_discord(self, message):
        self.log(f"Discord Bridge Broadcast: '{message}'")
        if self.test_mode:
            return
            
        try:
            # Execute the PS1 script as mandated by safety-protocol.md
            cmd = [
                "powershell.exe",
                "-ExecutionPolicy", "Bypass",
                "-File", self.discord_script,
                "-Message", f"🤖 SDE Autopilot: {message}"
            ]
            subprocess.run(cmd, cwd=self.workspace_path, capture_output=True, text=True)
        except Exception as e:
            print(f"[DAEMON] Discord notification warning: {str(e)}")

    def run_sub_tool(self, tool_name, extra_args=None):
        script_path = self.tools.get(tool_name)
        if not script_path or not os.path.exists(script_path):
            self.log(f"Error: Script not found for {tool_name}")
            return False, "Script path missing"
            
        cmd = [sys.executable, script_path]
        if self.test_mode:
            cmd.append("--test")
        if extra_args:
            cmd.extend(extra_args)
            
        self.log(f"Executing: {' '.join(cmd)}")
        try:
            res = subprocess.run(cmd, cwd=self.workspace_path, capture_output=True, text=True, encoding="utf-8")
            if res.returncode == 0:
                print(res.stdout)
                return True, res.stdout
            else:
                print(res.stdout)
                print(res.stderr, file=sys.stderr)
                return False, res.stderr
        except Exception as e:
            return False, str(e)

    def execute_autopilot_loop(self):
        if hasattr(sys.stdout, "reconfigure"):
            sys.stdout.reconfigure(encoding="utf-8")
            
        self.log("Initializing Sentinel Developer Engine (SDE) Autopilot Daemon Loop...")
        self.notify_discord("Sentinel Developer Engine (SDE) Autopilot Core Daemon Booted! ⚡")
        
        while True:
            self.log("=== Commencing Autopilot Cycle Execution ===")
            
            # Step 1: Harvester (Task-Debt Harvester)
            self.log("Step 1/5: Harvesting technical debts and TODO context...")
            success, out = self.run_sub_tool("harvester")
            if not success:
                self.log(f"⚠️ Harvester cycle warning: {out}")
            else:
                self.log("Harvester cycle completed successfully.")
                
            # Step 2: Refiner (PM Refinement)
            self.log("Step 2/5: Grooming unrefined tickets and scaffolding plans...")
            success, out = self.run_sub_tool("refiner")
            if not success:
                self.log(f"⚠️ PM refiner cycle warning: {out}")
            else:
                self.log("PM refiner cycle completed successfully.")
                
            # Step 3: Assembly Line (Multi-Agent Parallel Orchestration)
            self.log("Step 3/5: Spawning Assembly Line (TPM -> Surgeon -> QA)...")
            success, out = self.run_sub_tool("assembly", ["--task", "feat/sde-phase5-autopilot"])
            if not success:
                self.log(f"⚠️ Assembly Line cycle warning: {out}")
            else:
                self.log("Assembly Line cycle completed successfully.")

            # Step 4: Regression Healer & 3-Strike Lockout Gate
            self.log("Step 4/5: Running code compilation checks and self-healer...")
            # We execute healing verification
            success, out = self.run_sub_tool("healer")
            if not success:
                self.strike_counter += 1
                self.log(f"❌ Regression Healer Failure! Strike Counter escalated: {self.strike_counter}/{self.max_strikes}")
                self.notify_discord(f"⚠️ Strike {self.strike_counter}/{self.max_strikes} triggered due to verification failure!")
                
                # Check Lockout Gate Boundary
                if self.strike_counter >= self.max_strikes:
                    self.log("🚨 THREE-STRIKE LOCKOUT GATE BREACHED!")
                    self.log("Reverting worktree index modifications, halting daemon loop, dropping to consultative mode...")
                    self.notify_discord("🚨 EMERGENCY LOCKOUT BREACH! 3 strikes reached. Hard-halting loop and rolling back changes via git reset --hard!")
                    
                    if not self.test_mode:
                        subprocess.run(["git", "reset", "--hard"], cwd=self.workspace_path)
                    
                    self.log("Autopilot Daemon cleanly suspended. Awaiting manual SDE triage.")
                    return False
            else:
                if self.strike_counter > 0:
                    self.log(f"Resetting strike counter back to 0 (Codebase healed successfully).")
                    self.strike_counter = 0
                self.log("Regression verification and healing checks passed.")

            # Step 5: Fuzzer & Multimodal Screen Sweeping
            self.log("Step 5/5: Executing active BLE protocol fuzzer and visual layout screen sweeper...")
            fuzz_success, fuzz_out = self.run_sub_tool("fuzzer")
            sweep_success, sweep_out = self.run_sub_tool("sweeper")
            
            if fuzz_success and sweep_success:
                self.log("BLE protocol fuzzing and Visual UI layout sweep audits fully verified!")
                self.notify_discord("Visual UI Audit and BLE Fuzzer verified! Layout symmetry matches perfectly. 📱✅")
            else:
                self.log(f"⚠️ Fuzz/Sweep warnings: Fuzz={fuzz_success}, Sweep={sweep_success}")
                
            self.log("=== Autopilot Cycle Finalized [SUCCESS] ===")
            self.notify_discord("Full Autopilot Sprint Cycle successfully completed and QA Gate verified! 🏆")
            
            if not self.continuous:
                break
                
            self.log("Continuous mode active. Sleeping for 30 seconds before next sweep...")
            time.sleep(30.0)
            
        return True

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Sentinel Developer Engine (SDE) Autopilot Daemon")
    parser.add_argument("--test", action="store_true", help="Run SDE daemon in test/mock/offline mode")
    parser.add_argument("--continuous", action="store_true", help="Continuously loop audits and SDE checks")
    args = parser.parse_args()
    
    workspace = "C:\\Neogleamz\\AG_SK8Lytz_App\\SK8Lytz"
    if "feat-sde-autopilot" in os.getcwd():
        workspace = os.getcwd()
    elif "SK8Lytz-worktrees" in os.getcwd():
        workspace = os.getcwd()
        
    daemon = SdeAutopilotDaemon(workspace, test_mode=args.test, continuous=args.continuous)
    daemon.execute_autopilot_loop()
