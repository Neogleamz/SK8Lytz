import { parseSitemap } from './core/SitemapParser';

async function run() {
  const origin = 'http://beechmontrollarena.com/';
  const res = await fetch(origin);
  const html = await res.text();
  
  // count regex links
  const linkRegex = /<a\s+[^>]*href=["']([^"'#]+)["'][^>]*>([\s\S]*?)<\/a>/gi;
  let m;
  const links: string[] = [];
  while ((m = linkRegex.exec(html)) !== null) {
    links.push(m[1]);
  }
  console.log('Plain HTML fetch links count:', links.length);
  console.log('Links found in plain HTML:', links);
}
run();
