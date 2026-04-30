"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g = Object.create((typeof Iterator === "function" ? Iterator : Object).prototype);
    return g.next = verb(0), g["throw"] = verb(1), g["return"] = verb(2), typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (g && (g = 0, op[0] && (_ = 0)), _) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
var __rest = (this && this.__rest) || function (s, e) {
    var t = {};
    for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p) && e.indexOf(p) < 0)
        t[p] = s[p];
    if (s != null && typeof Object.getOwnPropertySymbols === "function")
        for (var i = 0, p = Object.getOwnPropertySymbols(s); i < p.length; i++) {
            if (e.indexOf(p[i]) < 0 && Object.prototype.propertyIsEnumerable.call(s, p[i]))
                t[p[i]] = s[p[i]];
        }
    return t;
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
var supabase_js_1 = require("@supabase/supabase-js");
var LocalDB_1 = require("./core/LocalDB");
var dotenv_1 = __importDefault(require("dotenv"));
var path_1 = __importDefault(require("path"));
// Load environment variables
dotenv_1.default.config({ path: path_1.default.resolve(__dirname, '../../.env') });
var supabaseUrl = process.env.EXPO_PUBLIC_SUPABASE_URL;
var supabaseKey = process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY;
if (!supabaseUrl || !supabaseKey) {
    console.error("❌ Missing Supabase URL or Key in environment variables.");
    process.exit(1);
}
var supabase = (0, supabase_js_1.createClient)(supabaseUrl, supabaseKey);
function migrateData() {
    return __awaiter(this, void 0, void 0, function () {
        var _a, configData, configErr, id, updated_at, payload, keys, _b, blocklistData_1, blocklistErr, insertBlocklist_1, _c, keywordsData_1, keywordsErr, insertKeyword_1, _d, fieldRegistryData_1, fieldRegistryErr, insertFieldRegistry_1, e_1;
        return __generator(this, function (_e) {
            switch (_e.label) {
                case 0:
                    console.log("🚀 Starting data migration from Supabase to Local SQLite...");
                    _e.label = 1;
                case 1:
                    _e.trys.push([1, 6, , 7]);
                    // 1. Migrate scraper_config
                    console.log("Fetching scraper_config...");
                    return [4 /*yield*/, supabase.from('scraper_config').select('*').eq('id', 1).single()];
                case 2:
                    _a = _e.sent(), configData = _a.data, configErr = _a.error;
                    if (configErr && configErr.code !== 'PGRST116') { // PGRST116 means no rows
                        console.error("Error fetching scraper_config:", configErr);
                    }
                    else if (configData) {
                        console.log("Found scraper_config. Migrating...");
                        id = configData.id, updated_at = configData.updated_at, payload = __rest(configData, ["id", "updated_at"]);
                        keys = Object.keys(payload);
                        if (keys.length > 0) {
                            LocalDB_1.db.prepare("DROP TABLE IF EXISTS scraper_config").run();
                            LocalDB_1.db.prepare("CREATE TABLE IF NOT EXISTS scraper_config (id INTEGER PRIMARY KEY, config_json TEXT, updated_at TEXT DEFAULT CURRENT_TIMESTAMP)").run();
                            LocalDB_1.db.prepare("INSERT INTO scraper_config (id, config_json) VALUES (1, ?)").run(JSON.stringify(payload));
                            console.log("\u2705 Migrated scraper_config.");
                        }
                    }
                    else {
                        console.log("\u2139\uFE0F No scraper_config found in Supabase.");
                    }
                    // 2. Migrate scraper_blocklist
                    console.log("Fetching scraper_blocklist...");
                    return [4 /*yield*/, supabase.from('scraper_blocklist').select('*')];
                case 3:
                    _b = _e.sent(), blocklistData_1 = _b.data, blocklistErr = _b.error;
                    if (blocklistErr) {
                        console.error("Error fetching scraper_blocklist:", blocklistErr);
                    }
                    else if (blocklistData_1 && blocklistData_1.length > 0) {
                        console.log("Found ".concat(blocklistData_1.length, " records in scraper_blocklist. Migrating..."));
                        insertBlocklist_1 = LocalDB_1.db.prepare("INSERT INTO scraper_blocklist (pattern, match_type, reason, created_at) VALUES (@pattern, @match_type, @reason, @created_at)");
                        LocalDB_1.db.transaction(function () {
                            for (var _i = 0, blocklistData_2 = blocklistData_1; _i < blocklistData_2.length; _i++) {
                                var item = blocklistData_2[_i];
                                insertBlocklist_1.run({
                                    pattern: item.pattern,
                                    match_type: item.match_type || 'name',
                                    reason: item.reason || '',
                                    created_at: item.created_at || new Date().toISOString()
                                });
                            }
                        })();
                        console.log("\u2705 Migrated ".concat(blocklistData_1.length, " records into scraper_blocklist."));
                    }
                    else {
                        console.log("\u2139\uFE0F No records found in scraper_blocklist.");
                    }
                    // 3. Migrate scraper_blocklist_keywords
                    console.log("Fetching scraper_blocklist_keywords...");
                    return [4 /*yield*/, supabase.from('scraper_blocklist_keywords').select('*')];
                case 4:
                    _c = _e.sent(), keywordsData_1 = _c.data, keywordsErr = _c.error;
                    if (keywordsErr) {
                        console.error("Error fetching scraper_blocklist_keywords:", keywordsErr);
                    }
                    else if (keywordsData_1 && keywordsData_1.length > 0) {
                        console.log("Found ".concat(keywordsData_1.length, " records in scraper_blocklist_keywords. Migrating..."));
                        insertKeyword_1 = LocalDB_1.db.prepare("INSERT INTO scraper_blocklist_keywords (keyword, created_at) VALUES (@keyword, @created_at)");
                        LocalDB_1.db.transaction(function () {
                            for (var _i = 0, keywordsData_2 = keywordsData_1; _i < keywordsData_2.length; _i++) {
                                var item = keywordsData_2[_i];
                                insertKeyword_1.run({
                                    keyword: item.keyword,
                                    created_at: item.created_at || new Date().toISOString()
                                });
                            }
                        })();
                        console.log("\u2705 Migrated ".concat(keywordsData_1.length, " records into scraper_blocklist_keywords."));
                    }
                    else {
                        console.log("\u2139\uFE0F No records found in scraper_blocklist_keywords.");
                    }
                    // 4. Migrate pipeline_field_registry
                    console.log("Fetching pipeline_field_registry...");
                    return [4 /*yield*/, supabase.from('pipeline_field_registry').select('*')];
                case 5:
                    _d = _e.sent(), fieldRegistryData_1 = _d.data, fieldRegistryErr = _d.error;
                    if (fieldRegistryErr) {
                        console.error("Error fetching pipeline_field_registry:", fieldRegistryErr);
                    }
                    else if (fieldRegistryData_1 && fieldRegistryData_1.length > 0) {
                        console.log("Found ".concat(fieldRegistryData_1.length, " records in pipeline_field_registry. Migrating..."));
                        LocalDB_1.db.prepare("DROP TABLE IF EXISTS pipeline_field_registry").run();
                        // Ensure table is created properly with TEXT id before inserting
                        LocalDB_1.db.prepare("\n          CREATE TABLE IF NOT EXISTS pipeline_field_registry (\n            id TEXT PRIMARY KEY,\n            field_name TEXT,\n            phase_id INTEGER,\n            display_label TEXT,\n            data_type TEXT,\n            sort_order INTEGER,\n            created_at TEXT DEFAULT CURRENT_TIMESTAMP\n          )\n        ").run();
                        insertFieldRegistry_1 = LocalDB_1.db.prepare("\n            INSERT INTO pipeline_field_registry (id, field_name, phase_id, display_label, data_type, sort_order, created_at) \n            VALUES (@id, @field_name, @phase_id, @display_label, @data_type, @sort_order, @created_at)\n            ON CONFLICT(id) DO NOTHING\n        ");
                        LocalDB_1.db.transaction(function () {
                            for (var _i = 0, fieldRegistryData_2 = fieldRegistryData_1; _i < fieldRegistryData_2.length; _i++) {
                                var item = fieldRegistryData_2[_i];
                                insertFieldRegistry_1.run({
                                    id: item.id,
                                    field_name: item.field_name,
                                    phase_id: item.phase_id,
                                    display_label: item.display_label,
                                    data_type: item.data_type,
                                    sort_order: item.sort_order,
                                    created_at: item.created_at || new Date().toISOString()
                                });
                            }
                        })();
                        console.log("\u2705 Migrated ".concat(fieldRegistryData_1.length, " records into pipeline_field_registry."));
                    }
                    else {
                        console.log("\u2139\uFE0F No records found in pipeline_field_registry.");
                    }
                    console.log("🎉 Migration completed successfully.");
                    return [3 /*break*/, 7];
                case 6:
                    e_1 = _e.sent();
                    console.error("❌ Fatal error during migration:", e_1);
                    return [3 /*break*/, 7];
                case 7: return [2 /*return*/];
            }
        });
    });
}
migrateData();
