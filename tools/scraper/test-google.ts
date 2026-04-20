import { GooglePlacesProvider } from './lib/providers/GooglePlacesProvider';

async function runTest() {
  console.log("🚀 Starting Google Places Dry-Run...");
  
  // 1. Search for roller rinks in a specific tiny zip code (e.g. 90210 - Beverly Hills)
  const zipCode = '90210';
  console.log(`\n1. Target: "${zipCode}"`);
  const placeIds = await GooglePlacesProvider.searchRegion(zipCode);
  
  console.log(`\n2. Found ${placeIds.length} verified Place IDs passing AntiSkateboardFilter.`);
  
  // 2. Fetch specific details for the first one found (if any)
  if (placeIds.length > 0) {
      console.log(`\n3. Fetching Premium Details for ID: ${placeIds[0]}...`);
      const details = await GooglePlacesProvider.getPlaceDetails(placeIds[0]);
      console.log('Result:', JSON.stringify(details, null, 2));
  } else {
      console.log("\n3. No matches found to pull details for. Trying a broader net, like 'Santa Monica'...");
      
      const alternativeIds = await GooglePlacesProvider.searchRegion('Santa Monica');
      console.log(`\nFound ${alternativeIds.length} verified Place IDs in Santa Monica.`);
      
      if (alternativeIds.length > 0) {
        console.log(`\nFetching Premium Details for ID: ${alternativeIds[0]}...`);
        const details = await GooglePlacesProvider.getPlaceDetails(alternativeIds[0]);
        console.log('Result:', JSON.stringify(details, null, 2));
      }
  }

  console.log("\n✅ Dry-Run Completed.");
}

runTest().catch(e => console.error(e));
