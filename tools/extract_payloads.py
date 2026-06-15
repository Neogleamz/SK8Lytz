import json
import os

conversations = {
    "DATA_LAYER": "a3ec9b75-25fe-4ad9-88f9-86a72ac1889e",
    "NOTIFICATIONS_&_ROUTING": "a416323c-14a3-408e-9fc5-923c91820f49",
    "THEME_&_ASSETS": "5c672120-57bb-4fdc-806d-8cf2fd19d37b",
    "OS_PERMISSIONS": "f52e390c-6f7c-4b5a-a6e6-88f8ad738ddd",
    "DEPENDENCY_AUDIT": "a58e4cc4-9e0c-452b-adb2-bfe64fd411c9",
    "UI_CONTROLS": "1f24b64b-bf54-470e-a78f-5589574fea91",
    "SESSION_TRACKING": "dd41db12-caef-450b-b225-b0dd24134bb5",
    "SIMULATION_&_MOCKS": "4196191e-0f13-455b-984c-5bdaa2cb0fd9",
    "IDENTITY": "06930f88-5592-4a86-9b36-e94a58abf642",
    "HARDWARE_PROTOCOLS": "5a530b26-725e-4ab6-9973-af2edb6a59f6",
    "UTILS": "5103ed48-651d-431e-be12-afa9a1cc2013",
    "BUILD_CONFIG_&_OTA": "7be4a4cb-f1b4-4a18-9b59-6b25853e1ebc",
    "CLOUD_FUNCTIONS": "6a46ef76-76cc-47a9-bd30-ea40575e5106",
    "BLE_CORE": "d2161e81-72cf-445e-b0cb-c9eb278d4760",
    "NATIVE_&_WATCH": "69d5005e-0db4-45a9-a76c-38d48d4a1e31",
    "GROUP_SYNC": "459d1d1f-4a8f-47ff-b1e8-133dbc266b5e"
}

out_dir = os.path.join(os.path.expanduser("~"), ".gemini", "antigravity", "brain", "scratch_payloads")
if not os.path.exists(out_dir):
    os.makedirs(out_dir)

for name, cid in conversations.items():
    path = os.path.join(os.path.expanduser("~"), ".gemini", "antigravity", "brain", cid, ".system_generated", "logs", "transcript.jsonl")
    if not os.path.exists(path):
        print(f"Missing {path}")
        continue
    
    with open(path, "r", encoding="utf-8") as f:
        lines = f.readlines()
        
    last_msg = ""
    for line in reversed(lines):
        try:
            data = json.loads(line)
            if data.get("type") == "PLANNER_RESPONSE":
                last_msg = data.get("content", "")
                if last_msg:
                    break
        except:
            pass
            
    if not last_msg:
        print(f"No message found for {name}")
        continue
        
    out_path = os.path.join(out_dir, f"{name}.md")
    with open(out_path, "w", encoding="utf-8") as f:
        f.write(last_msg)
    print(f"Wrote {out_path}")
