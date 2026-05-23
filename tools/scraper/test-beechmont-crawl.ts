import puppeteer from 'puppeteer';
import http from 'http';

const REQUIRED_SCHEMA = {
  hours: 'Complete weekly public skating schedule for ALL 7 DAYS {Monday: time_range, Tuesday: time_range, ...}. Include every day even if closed.',
  pricing: 'All admission fees {adult,child,senior,spectator,skate_rental}.',
  has_fee: 'boolean or null — return null if no pricing/fee information was found. DO NOT assume free.',
  has_adult_night: 'boolean or null — return null if no adult night information was found.',
  adult_night_schedule: 'If adult nights: {day:time_range}. Null if none.'
};

const COMBINED_SCHEMA = {
  surface_type: 'Floor: wood/maple/concrete/asphalt/sport_court/synthetic.',
  surface_quality: 'Condition 3-5 words.',
  vibe_score: '0-100.',
  is_indoor: 'boolean',
  has_rental: 'boolean',
  has_pro_shop: 'boolean',
  has_food: 'boolean',
  has_lights: 'boolean',
  has_lockers: 'boolean',
  has_ac: 'boolean',
  has_wifi: 'boolean',
  has_toilets: 'boolean',
  wheelchair: 'boolean',
  derby: 'boolean',
  capacity: 'integer.',
  special_events: 'Array.',
  operator_name: 'Owner name.',
  operator_description: '1-2 sentences.',
  cultural_meta: 'Significance or null.',
  adult_night_details: 'Details or null.',
  instagram_url: 'URL or null.',
  facebook_url: 'URL or null.',
  tiktok_url: 'URL or null.',
  schedule_url: 'URL or null.',
  yelp_url: 'URL or null.',
  price_range: '$ to $$$$ or null.',
  logo_url: 'Logo URL or null.',
  email_addresses: 'Array of ALL contact email addresses found for this venue. Include info, events, parties, management, booking — every unique email. Return [] if none found.'
};

async function callLMStudio(systemMessage: string, userMessage: string): Promise<any> {
  const payload = {
    model: 'qwen2.5-7b-instruct-1m',
    messages: [
      { role: 'system', content: systemMessage },
      { role: 'user', content: userMessage }
    ],
    temperature: 0.1,
    stream: false
  };

  const postData = JSON.stringify(payload);

  return new Promise((resolve, reject) => {
    const req = http.request({
      hostname: 'localhost',
      port: 1234,
      path: '/v1/chat/completions',
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Content-Length': Buffer.byteLength(postData)
      }
    }, (res) => {
      let data = '';
      res.on('data', (chunk) => { data += chunk; });
      res.on('end', () => {
        try {
          const parsed = JSON.parse(data);
          const content = parsed.choices?.[0]?.message?.content || '';
          resolve(content);
        } catch (e) {
          resolve(data);
        }
      });
    });

    req.on('error', (err) => reject(err));
    req.write(postData);
    req.end();
  });
}

function condenseWebText(text: string): string {
  return text
    .replace(/\s+/g, ' ')
    .replace(/\n+/g, '\n')
    .trim()
    .slice(0, 15000);
}

async function run() {
  console.log('Launching browser...');
  const browser = await puppeteer.launch({ headless: 'new' });
  const page = await browser.newPage();
  
  const urls = [
    'https://beechmontrollarena.com/',
    'https://beechmontrollarena.com/open-skating-hours',
    'https://beechmontrollarena.com/pro-shop',
    'https://beechmontrollarena.com/concession'
  ];

  let cText = '';
  let aText = '';

  for (const url of urls) {
    console.log(`Crawling -> ${url}`);
    await page.goto(url, { waitUntil: 'domcontentloaded' });
    const text = await page.evaluate(() => document.body.innerText);
    const condensed = condenseWebText(text);
    
    console.log(`   Fetched ${text.length} chars (condensed to ${condensed.length})`);
    
    const isOpsUrl = url.includes('open-skating-hours') || url === 'https://beechmontrollarena.com/';
    const isAmenityUrl = url.includes('pro-shop') || url.includes('concession') || url === 'https://beechmontrollarena.com/';

    if (isOpsUrl) cText += `\n\n[PAGE: ${url}]\n${condensed}`;
    if (isAmenityUrl) aText += `\n\n[PAGE: ${url}]\n${condensed}`;
  }

  await browser.close();

  console.log('\n--- PASS 1 CRAWL TEXT LENGTHS ---');
  console.log('Ops Text Length:', cText.length);
  console.log('Amenities Text Length:', aText.length);

  const systemPrompt = `You are a data extraction agent for [Beechmont Rollarena] in [Cincinnati].\nONLY this location. Valid JSON only.\nCRITICAL BOOLEAN RULE: For ALL boolean fields, you MUST return null if the information was not explicitly found in the text. Do NOT assume false. Do NOT infer. Return null to indicate unknown.`;

  console.log('\nCalling LM Studio Pass 1...');
  const p1Res = await callLMStudio(
    systemPrompt + `\nSCHEMA:\n${JSON.stringify(REQUIRED_SCHEMA, null, 2)}`,
    `Website Text:\n${cText}`
  );
  console.log('LM Studio Pass 1 Result:\n', p1Res);

  console.log('\nCalling LM Studio Pass 2...');
  const p2Res = await callLMStudio(
    systemPrompt + `\nSCHEMA:\n${JSON.stringify(COMBINED_SCHEMA, null, 2)}`,
    `Website Text:\n${aText}`
  );
  console.log('LM Studio Pass 2 Result:\n', p2Res);
}

run();
