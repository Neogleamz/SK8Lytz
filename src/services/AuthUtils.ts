/**
 * AuthUtils.ts
 * Password security + profanity utilities for SK8Lytz
 */
import * as Crypto from 'expo-crypto';

// ─── TOP 100 WORST PASSWORDS ──────────────────────────────────────────────────
const BAD_PASSWORDS = new Set([
  '123456','password','123456789','12345678','12345','1234567','1234567890',
  'qwerty','abc123','111111','123123','admin','letmein','welcome','monkey',
  'dragon','master','login','sunshine','princess','shadow','superman',
  'michael','football','baseball','soccer','charlie','donald','password1',
  'iloveyou','qwerty123','1q2w3e4r','123qwe','zxcvbn','trustno1','batman',
  'hunter2','starwars','hello','freedom','whatever','qazwsx','pass',
  'asdfgh','zxcvbnm','passw0rd','p@ssword','p@ss123','pa$$word','test',
  'guest','admin123','root','toor','changeme','secret','qwertyu',
  'qwertyui','qwertyuiop','1234','12341234','abc','abc1234','letmein1',
  'mother','jennifer','thomas','jessica','asshole','fuckyou','696969',
  '121212','7777777','123321','555555','654321','lovely','welcome1',
  'access','flower','master1','superman1','aaaaaa','000000','1111','2000',
  'phoenix','maverick','cheese','pepper','ginger','thunder','ranger',
  '1qaz2wsx','q1w2e3r4','qwerty1','pass123','123abc','abc123456',
]);

// ─── PROFANITY LIST ───────────────────────────────────────────────────────────
const PROFANITY = new Set([
  'fuck','shit','ass','bitch','cunt','dick','cock','pussy','nigger','nigga',
  'faggot','fag','whore','slut','bastard','damn','crap','piss','twat','wank',
  'wanker','asshole','arsehole','motherfucker','fucker','bullshit','horseshit',
  'retard','spic','kike','chink','gook','wetback','tranny','dyke',
]);

// ─── PASSWORD COMPLEXITY ──────────────────────────────────────────────────────
export interface PasswordStrength {
  score: number;        // 0–4
  label: string;        // 'Weak' | 'Fair' | 'Good' | 'Strong'
  color: string;
  errors: string[];
}

export function checkPasswordComplexity(password: string): PasswordStrength {
  const errors: string[] = [];
  if (password.length < 8)       errors.push('At least 8 characters');
  if (!/[A-Z]/.test(password))   errors.push('One uppercase letter');
  if (!/[0-9]/.test(password))   errors.push('One number');
  if (!/[^A-Za-z0-9]/.test(password)) errors.push('One special character (!@#$...)');

  const score = Math.max(0, 4 - errors.length);
  const labels = ['Weak', 'Fair', 'Fair', 'Good', 'Strong'];
  const colors = ['#FF3B30', '#FF9500', '#FF9500', '#34C759', '#00C853'];

  return { score, label: labels[score], color: colors[score], errors };
}

export function isCommonPassword(password: string): boolean {
  return BAD_PASSWORDS.has(password.toLowerCase());
}

// ─── HAVE I BEEN PWNED ────────────────────────────────────────────────────────
export async function checkHIBP(password: string): Promise<{ pwned: boolean; count: number }> {
  try {
    const hash = await Crypto.digestStringAsync(
      Crypto.CryptoDigestAlgorithm.SHA1,
      password,
      { encoding: Crypto.CryptoEncoding.HEX }
    );
    const prefix = hash.slice(0, 5).toUpperCase();
    const suffix = hash.slice(5).toUpperCase();

    const response = await fetch(`https://api.pwnedpasswords.com/range/${prefix}`, {
      headers: { 'Add-Padding': 'true' }
    });
    if (!response.ok) return { pwned: false, count: 0 };

    const text = await response.text();
    const lines = text.split('\r\n');
    const match = lines.find(line => line.split(':')[0] === suffix);
    if (match) {
      const count = parseInt(match.split(':')[1], 10);
      return { pwned: true, count };
    }
    return { pwned: false, count: 0 };
  } catch {
    // Network error — fail open (don't block login)
    return { pwned: false, count: 0 };
  }
}

// ─── PROFANITY CHECK ─────────────────────────────────────────────────────────
export function containsProfanity(text: string): boolean {
  const words = text.toLowerCase().replace(/[^a-z0-9 ]/g, ' ').split(/\s+/);
  return words.some(w => PROFANITY.has(w));
}
