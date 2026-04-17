import axios from 'axios';

const OSM_TEST_QUERY = `
[out:json][timeout:90];
area["ISO3166-2"="US-CA"]->.searchArea;
nwr["sport"~"roller_skating|skateboard"](area.searchArea);
out count;
`;

async function testSkateparks() {
  try {
    const response = await axios.post('https://overpass-api.de/api/interpreter', `data=${encodeURIComponent(OSM_TEST_QUERY)}`, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    });
    console.log(response.data.elements[0].tags);
  } catch (e: any) {
    console.error('Failed:', e.message);
  }
}
testSkateparks();
