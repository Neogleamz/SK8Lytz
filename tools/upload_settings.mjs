import fs from 'fs';
fetch('https://qefmeivpjyaukbwadgaz.supabase.co/storage/v1/object/sk8lytz-settings/sk8lytz-picks.json', {
  method: 'PUT',
  headers: {
    'Authorization': 'Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFlZm1laXZwanlhdWtid2FkZ2F6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzM0MzUyMjAsImV4cCI6MjA4OTAxMTIyMH0.TtBAAL7RPk-w8Q_IGbhPouBjcdjyCRXKy_D5YS4FQss',
    'apikey': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFlZm1laXZwanlhdWtid2FkZ2F6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzM0MzUyMjAsImV4cCI6MjA4OTAxMTIyMH0.TtBAAL7RPk-w8Q_IGbhPouBjcdjyCRXKy_D5YS4FQss',
    'Content-Type': 'application/json',
    'x-upsert': 'true'
  },
  body: fs.readFileSync('c:\\Users\\Magma\\Downloads\\sk8lytz-picks.json')
}).then(res => res.json()).then(console.log).catch(console.error);
