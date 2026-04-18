import axios from 'axios';
import fs from 'fs';

const OVERPASS_URL = 'https://overpass-api.de/api/interpreter';

// Query to get roller skating facilities in the US (Kansas City for Demo)
const QUERY = `
[out:json][timeout:180][bbox:38.7,-95.1,39.4,-94.2];
(
  node["sport"~"roller_skating"];
  way["sport"~"roller_skating"];
  relation["sport"~"roller_skating"];
  node["leisure"="ice_rink"]["sport"="roller_skating"];
  way["leisure"="ice_rink"]["sport"="roller_skating"];
);
out center;
`;

async function seed() {
  console.log('Fetching skate spots from Overpass API (US)...');
  try {
    const response = await axios.post(OVERPASS_URL, `data=${encodeURIComponent(QUERY)}`, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    });

    const elements = response.data.elements;
    console.log(`Found ${elements.length} raw results.`);

    const spots = elements.map((el: any) => {
      const lat = el.lat || el.center?.lat;
      const lon = el.lon || el.center?.lon;
      const tags = el.tags || {};
      
      let name = tags.name || tags.brand || 'Unknown Skate Spot';
      const city = tags['addr:city'] || null;
      const state = tags['addr:state'] || null;
      const zip = tags['addr:postcode'] || null;
      const phone = tags.phone || tags['contact:phone'] || null;
      
      let indoor = tags.indoor === 'yes';
      let surface = tags.surface || 'unknown';
      if (surface.includes('wood')) surface = 'wood';
      else if (surface.includes('concrete')) surface = 'concrete';
      else if (surface.includes('asphalt')) surface = 'asphalt';

      const has_lights = tags.lit === 'yes';
      const has_fee = tags.fee === 'yes';
      const has_rental = tags['skates:rental'] === 'yes';
      const is_wheelchair_accessible = tags.wheelchair === 'yes';
      const has_wifi = tags.internet_access === 'wlan' || tags.internet_access === 'yes';
      const has_toilets = tags.toilets === 'yes';
      const operator_name = tags.operator || null;
      const operator_description = tags.description || null;

      const has_food = tags.food === 'yes' || tags.snack_bar === 'yes' || tags.fast_food === 'yes';
      const has_ac = tags.air_conditioning === 'yes';
      const has_lockers = tags.lockers === 'yes';
      const capacity = tags.capacity ? parseInt(tags.capacity, 10) : null;
      const hosts_derby = tags.roller_derby === 'yes' || (tags.club && tags.club.includes('derby')) || null;

      return {
        id: el.id,
        name,
        lat,
        lng: lon,
        city,
        state,
        zip,
        phone,
        surface_type: surface,
        is_indoor: indoor,
        has_lights,
        has_fee,
        has_rental,
        is_wheelchair_accessible,
        has_wifi,
        has_toilets,
        operator_name,
        operator_description,
        has_food,
        has_ac,
        has_lockers,
        capacity,
        hosts_derby
      };
    }).filter((s: any) => s.name !== 'Unknown Skate Spot');

    console.log(`Filtered down to ${spots.length} named locations.`);

    fs.writeFileSync('seed.json', JSON.stringify(spots, null, 2));
    console.log('Saved to seed.json');

  } catch (err: any) {
    console.error('Failed to fetch from Overpass:', err.message);
  }
}

seed();
