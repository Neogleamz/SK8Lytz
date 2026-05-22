#!/usr/bin/env python3
# layout_auditor.py
# Multimodal Visual AI Layout Auditor powered by Gemini API & Antigravity SDK.
# Audits React Native/Expo UI viewports for safe-area boundaries, dynamic island clipping, and viewport overlaps.

import os
import sys
import json
import argparse
from google_antigravity import AgentCoordinator, LocalAgentConfig

# A minimal valid 1x1 transparent PNG for self-contained testing if no screenshot is provided
MINIMAL_PNG_BYTES = b'\x89PNG\r\n\x1a\n\x00\x00\x00\rIHDR\x00\x00\x00\x01\x00\x00\x00\x01\x08\x06\x00\x00\x00\x1f\x15c4\x00\x00\x00\rIDATx\x9cc`\x00\x01\x00\x00\xff\xff\x03\x00\x00\x06\x00\x05Wn\xad\xf4\x00\x00\x00\x00IEND\xaeB`\x82'

def parse_args():
    parser = argparse.ArgumentParser(description="Visual AI Layout Auditor")
    parser.add_argument(
        "--image", 
        type=str, 
        help="Path to the viewport screenshot to audit"
    )
    parser.add_argument(
        "--output", 
        type=str, 
        default="layout_audit_report.json",
        help="Path to save the generated JSON report"
    )
    parser.add_argument(
        "--test", 
        action="store_true", 
        help="Run a self-contained diagnostic test with a mock screenshot"
    )
    return parser.parse_args()

def main():
    args = parse_args()
    
    # 1. Resolve or generate target screenshot
    target_image = args.image
    is_mock = False
    
    if args.test or not target_image:
        print("[AUDITOR] No screenshot path provided or --test flag passed. Generating mock test image...")
        mock_path = "mock_screenshot_temp.png"
        try:
            with open(mock_path, "wb") as f:
                f.write(MINIMAL_PNG_BYTES)
            target_image = mock_path
            is_mock = True
            print(f"[AUDITOR] Mock image generated successfully at {mock_path}")
        except Exception as e:
            print(f"[ERROR] Failed to write mock image: {str(e)}")
            sys.exit(1)
            
    if not os.path.exists(target_image):
        print(f"[ERROR] Specified screenshot path does not exist: {target_image}")
        sys.exit(1)
        
    print(f"[AUDITOR] Commencing visual layout audit on: {target_image}")
    
    # 2. Spawn Visual Auditor Agent via Google Antigravity SDK
    try:
        config = LocalAgentConfig()
        
        # Auditor Role and System Instructions
        role = "Visual AI Layout Auditor"
        instruction = (
            "You are a elite Frontend SDE and Multimodal UI Auditor. You specialize in auditing "
            "React Native and Expo mobile application viewports. You review screenshots to catch safe-area "
            "clipping (notch, dynamic island, home indicator overlap), flex box overflows, text truncation, "
            "button clipping, and layout asymmetry. You must analyze the image and return a STRICT JSON "
            "response detailing the issues and supplying direct React Native style/CSS overrides to fix them."
        )
        
        agent = AgentCoordinator.spawn_agent(role=role, instruction=instruction, config=config)
        
        # Formulate highly specific prompt instructing JSON layout output
        prompt = (
            "Analyze this screenshot of the SK8Lytz React Native / Expo application viewport. "
            "Review the layout for notch/dynamic-island safe area padding breaches, text wrapping constraints, "
            "flex viewport overflows, cut-off bounds, and symmetry. "
            "Return a strict JSON object mapping selectors or component names to corrected styles. "
            "Do not include markdown code block wrapper unless requested, just return the raw JSON object.\n\n"
            "Required Schema:\n"
            "{\n"
            '  "hasLayoutIssues": boolean,\n'
            '  "issues": ["List of identified layout, padding, alignment, or overflow bugs"],\n'
            '  "suggestedCSSOverrides": {\n'
            '    "componentNameOrSelector": {\n'
            '      "styleAttribute": "correctedValue"\n'
            '    }\n'
            "  }\n"
            "}"
        )
        
        # Check if the API key is present; otherwise run offline fallback
        if not config.api_key:
            print("[AUDITOR] Warning: GEMINI_API_KEY not configured. Falling back to offline heuristic mock audit...")
            
        print("[AUDITOR] Transmitting payload to Gemini Multimodal Engine...")
        response_text = agent.execute_multimodal(prompt, target_image)
        
        # 3. Parse and clean response JSON
        print("[AUDITOR] Response received. Parsing results...")
        
        # Strip markdown ```json codeblock delimiters if present
        clean_text = response_text.strip()
        if clean_text.startswith("```json"):
            clean_text = clean_text[7:]
        if clean_text.startswith("```"):
            clean_text = clean_text[3:]
        if clean_text.endswith("```"):
            clean_text = clean_text[:-3]
        clean_text = clean_text.strip()
        
        try:
            audit_report = json.loads(clean_text)
        except json.JSONDecodeError:
            # Create graceful fallback structure if text is not valid JSON
            print("[WARNING] Gemini returned non-JSON text. Creating wrapper report...")
            audit_report = {
                "hasLayoutIssues": True,
                "issues": [clean_text],
                "suggestedCSSOverrides": {}
            }
            
        # 4. Save and present report
        with open(args.output, "w", encoding="utf-8") as rf:
            json.dump(audit_report, rf, indent=2)
            
        print(f"\n[AUDITOR] === AUDIT RESULT SAVED TO {args.output} ===")
        print(json.dumps(audit_report, indent=2))
        print("==================================================\n")
        
        # Clean up temporary mock image
        if is_mock and os.path.exists(mock_path):
            os.remove(mock_path)
            
    except Exception as e:
        print(f"[ERROR] Layout Auditor execution failed: {str(e)}")
        # Clean up mock
        if is_mock and os.path.exists(mock_path):
            os.remove(mock_path)
        sys.exit(1)

if __name__ == "__main__":
    main()
