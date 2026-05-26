import { executeDetective } from './tools/scraper/core/DetectiveEngine';

const targetUrl = process.argv[2] || 'https://skateflorence.com';
const targetName = process.argv[3] || 'Skate Center Florence';
const targetCity = process.argv[4] || 'Florence';

const ctx = {
  website: targetUrl,
  name: targetName,
  city: targetCity,
  state: 'SC',
  surface_type: null, vibe_score: null, is_indoor: null
};
const aiConfig = { detective_model: 'qwen2.5-7b-instruct-1m' };
(async () => {
  console.log("Starting test...");
  const res = await executeDetective(ctx, aiConfig, true, console.log);
  console.log("\n\nFINAL RESULT:");
  console.log("Hours:", res.mappedFields.opening_hours);
  console.log("Pricing:", res.mappedFields.pricing_data);
  process.exit(0);
})();
