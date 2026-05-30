import fetch from 'node-fetch';
import { PhotoCategory } from '../Photographer';

export interface VisionAnalysisResult {
  valid: boolean;
  category?: PhotoCategory;
  score?: number; // 1-10
}

export interface VisionLLMOptions {
  model?: string;
  temperature?: number;
  host?: string;   // e.g. 'localhost' or 'host.docker.internal'
  port?: number;    // e.g. 1234
}

/**
 * Validates and scores a photo using a local multimodal LLM (e.g. LM Studio with Llama 3.2 Vision).
 * Assumes the LLM provides an OpenAI-compatible endpoint.
 * @param base64Image - Base64-encoded JPEG image
 * @param systemPrompt - The analysis prompt (configurable from the dashboard)
 * @param opts - Model, temperature, and host overrides from the publisher config
 */
export async function analyzePhotoWithVision(base64Image: string, systemPrompt: string, opts: VisionLLMOptions = {}): Promise<VisionAnalysisResult> {
  const host = opts.host || process.env.LM_STUDIO_HOST || 'localhost';
  const port = opts.port || 1234;
  const endpoint = `http://${host}:${port}/v1/chat/completions`;

  const defaultAccept: VisionAnalysisResult = { valid: true };

  try {
    const payload = {
      model: opts.model || 'local-model',
      messages: [
        {
          role: 'user',
          content: [
            { type: 'text', text: systemPrompt },
            { type: 'image_url', image_url: { url: `data:image/jpeg;base64,${base64Image}` } }
          ]
        }
      ],
      temperature: opts.temperature ?? 0.1,
      max_tokens: 80
    };

    const response = await fetch(endpoint, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    });

    if (!response.ok) {
      console.warn(`[VisionLLM] Endpoint returned ${response.status}: ${await response.text()}`);
      return defaultAccept;
    }

    const data: any = await response.json();
    let reply = data.choices?.[0]?.message?.content?.trim() || '';
    
    // Attempt to parse JSON from the reply
    try {
      // Sometimes models wrap JSON in markdown blocks
      reply = reply.replace(/```json/g, '').replace(/```/g, '').trim();
      const parsed = JSON.parse(reply);
      
      const validCategories: PhotoCategory[] = ['exterior', 'interior', 'floor', 'pro_shop', 'action', 'logo', 'flyer', 'unknown'];
      
      const isValid = parsed.valid === true || String(parsed.category).toLowerCase() !== 'reject';
      
      if (!isValid || parsed.category === 'reject') {
        return { valid: false };
      }

      let cat = String(parsed.category).toLowerCase() as PhotoCategory;
      if (!validCategories.includes(cat)) {
        cat = 'unknown';
      }

      let score = parseInt(parsed.score, 10);
      if (isNaN(score)) score = 5; // default middle score
      if (score < 1) score = 1;
      if (score > 10) score = 10;

      return {
        valid: true,
        category: cat,
        score
      };

    } catch (parseError) {
      console.warn(`[VisionLLM] Failed to parse JSON from LLM: ${reply}`);
      // If it explicitly said NO or REJECT in raw text, reject it
      if (reply.toUpperCase().includes('REJECT') || reply.toUpperCase().includes('"VALID": FALSE')) {
        return { valid: false };
      }
      return defaultAccept;
    }

  } catch (error: any) {
    console.warn(`[VisionLLM] Failed to connect to Vision API: ${error.message}. Defaulting to TRUE (Accept).`);
    // If LM Studio is not running, just accept the photo so the pipeline doesn't crash.
    return defaultAccept;
  }
}
