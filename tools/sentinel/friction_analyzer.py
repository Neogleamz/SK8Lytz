#!/usr/bin/env python3
# friction_analyzer.py
# Parses transcript.jsonl for command and tool errors, and calls a SpawnerAgent to propose rule changes.

import os
import sys
import json
from google_antigravity import AgentCoordinator, LocalAgentConfig

def find_latest_transcript(brain_dir="C:\\Users\\Magma\\.gemini\\antigravity-ide\\brain"):
    if not os.path.exists(brain_dir):
        return None
    
    # Locate all transcript.jsonl files recursively
    transcripts = []
    for root, dirs, files in os.walk(brain_dir):
        if "transcript.jsonl" in files:
            path = os.path.join(root, "transcript.jsonl")
            transcripts.append((path, os.path.getmtime(path)))
            
    if not transcripts:
        return None
        
    # Return the path of the most recently modified transcript.jsonl
    transcripts.sort(key=lambda x: x[1], reverse=True)
    return transcripts[0][0]

def analyze_transcript(transcript_path):
    print(f"Reading logs from: {transcript_path}")
    friction_events = []
    
    try:
        with open(transcript_path, "r", encoding="utf-8") as f:
            for line in f:
                if not line.strip():
                    continue
                step = json.loads(line)
                
                # Check for failed commands (non-zero exit codes or common compile crashes)
                if step.get("type") == "RUN_COMMAND" and step.get("status") == "DONE":
                    content = step.get("content", "")
                    if "failed with exit code" in content or "error TS" in content or "ESLint" in content:
                        friction_events.append({
                            "type": "command_failure",
                            "step_index": step.get("step_index"),
                            "content": content[-1000:] # Grab tail end of output
                        })
                        
                # Check for tool call execution errors
                elif step.get("status") == "ERROR":
                    friction_events.append({
                        "type": "tool_error",
                        "step_index": step.get("step_index"),
                        "content": step.get("content", "")
                    })
    except Exception as e:
        print(f"Error parsing transcript: {str(e)}")
        
    return friction_events

def run_friction_analysis():
    print("Initializing Sentinel Friction Analyzer...")
    
    transcript_path = find_latest_transcript()
    if not transcript_path:
        print("No active conversation logs found. Scanning aborted.")
        return
        
    events = analyze_transcript(transcript_path)
    if not events:
        print("Zero friction events (compilation, lints, or tool errors) found in this session. Workspace is healthy! [SUCCESS]")
        return
        
    print(f"Detected {len(events)} friction occurrences. Spawning Friction Analyst Agent...")
    
    # Format events for Gemini
    formatted_logs = ""
    for idx, e in enumerate(events[-5:]): # Only analyze last 5 events to prevent context bloat
        formatted_logs += f"\n--- EVENT {idx+1} (Step {e['step_index']}, Type: {e['type']}) ---\n"
        formatted_logs += e['content'] + "\n"
        
    # Verify key exists
    config = LocalAgentConfig()
    is_offline = False
    if not config.api_key:
        print("WARNING: GEMINI_API_KEY is not set. Running Rule Evolution in OFFLINE heuristic mode...")
        is_offline = True
        
    analyst = AgentCoordinator.spawn_agent(
        role="Friction Resolution Expert",
        instruction=(
            "You analyze logs of failed commands, TypeScript compilation errors, or tool crashes during a development session. "
            "Your goal is to write a precise, actionable addition to 'agent-behavior.md' to prevent these exact failures from happening again. "
            "Be extremely specific. Do not use generic statements. Write your response strictly as a markdown section titled '### evolved rules'."
        )
    )
    
    prompt = (
        "Here are the recent friction logs from our development session:\n"
        f"{formatted_logs}\n"
        "Please analyze these errors and output a surgical rule amendment to prevent this from repeating. "
        "Format it exactly as: \n"
        "### evolved rules\n"
        "- **Rule Title**: Detailed rule description explaining how the agent must check/fix this before running commands."
    )
    
    try:
        amendment = analyst.execute(prompt)
        print("\n=== Proposed Rule Amendments (SDE Self-Evolution) ===")
        print(amendment)
        print("====================================================\n")
        
        # Save amendment for review
        output_file = "C:\\Neogleamz\\AG_SK8Lytz_App\\SK8Lytz\\.agents\\rules\\friction-rule-amendments.md"
        with open(output_file, "w", encoding="utf-8") as f:
            f.write(amendment)
        print(f"Proposals successfully written to: {output_file} [SUCCESS]")
    except Exception as e:
        print(f"Friction Agent failed: {str(e)} [ERROR]")


if __name__ == "__main__":
    run_friction_analysis()
