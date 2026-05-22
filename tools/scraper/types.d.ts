declare module 'node-fetch' {
  import { Agent } from 'http';
  
  export interface RequestInit {
    method?: string;
    headers?: any;
    body?: any;
    redirect?: 'follow' | 'manual' | 'error';
    follow?: number;
    timeout?: number;
    compress?: boolean;
    size?: number;
    agent?: Agent | ((parsedUrl: URL) => Agent);
    signal?: any;
  }

  export interface Response {
    readonly ok: boolean;
    readonly status: number;
    readonly statusText: string;
    readonly headers: any;
    text(): Promise<string>;
    json(): Promise<any>;
    buffer(): Promise<Buffer>;
    arrayBuffer(): Promise<ArrayBuffer>;
    body: any;
  }

  function fetch(url: string | any, init?: RequestInit): Promise<Response>;
  export default fetch;
}

declare module 'pdf-parse' {
  interface PDFInfo {
    text: string;
    numpages: number;
    numrender: number;
    info: any;
    metadata: any;
    version: string;
  }
  function pdfParse(dataBuffer: Buffer, options?: any): Promise<PDFInfo>;
  export default pdfParse;
}

interface LocalSpot {
  id: string;
  name: string;
  lat?: number | null;
  lng?: number | null;
  city?: string | null;
  state?: string | null;
  zip?: string | null;
  street_address?: string | null;
  phone_number?: string | null;
  website?: string | null;
  google_place_id?: string | null;
  google_maps_url?: string | null;
  business_status?: string | null;
  rating?: number | null;
  user_ratings_total?: number | null;
  opening_hours?: string | null;
  operator_description?: string | null;
  facility_type?: string | null;
  is_published?: number;
  verification_status?: string | null;
  has_adult_night?: number;
  has_pro_shop?: number;
  is_deep_crawled?: number;
  raw_knowledge_panel?: string | null;
  photos?: string | null;
  candidate_photos?: string | null;
  candidate_links?: string | null;
  ai_metadata?: string | null;
  last_attempted_at?: string | null;
  last_enriched_at?: string | null;
  retry_count?: number;
  created_at?: string;
  surface_type?: string | null;
  is_indoor?: number;
  adult_night_details?: string | null;
  source?: string | null;
  is_verified?: number;
  updated_at?: string | null;
  updated_by?: string | null;
  is_featured?: number;
  has_lights?: number | null;
  has_fee?: number | null;
  operator_name?: string | null;
  has_rental?: number | null;
  is_wheelchair_accessible?: number | null;
  has_wifi?: number | null;
  has_toilets?: number | null;
  has_food?: number | null;
  has_ac?: number | null;
  has_lockers?: number | null;
  capacity?: number | null;
  hosts_derby?: number;
  surface_quality?: string | null;
  vibe_score?: number | null;
  cultural_metadata?: string | null;
  instagram_url?: string | null;
  facebook_url?: string | null;
  tiktok_url?: string | null;
  schedule_url?: string | null;
  pricing_data?: string | null;
  special_events?: string | null;
  adult_night_schedule?: string | null;
  yelp_url?: string | null;
  email_addresses?: string | null;
  facade_exterior?: string | null;
  skate_floor?: string | null;
  rental_shop?: string | null;
  pro_shop?: string | null;
  snack_bar?: string | null;
  seating_area?: string | null;
  logo_url?: string | null;
  cover_photo_url?: string | null;
  price_range?: string | null;
  [key: string]: any;
}

interface ScrapedLink {
  text: string;
  href: string;
}

