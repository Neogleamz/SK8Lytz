export interface ValidationResult {
  valid: boolean;
  cleanValue?: any;
  reason?: string;
}

export function validateAndCleanField(raw: any, rule: string): ValidationResult {
  if (raw === null || raw === undefined || raw === '') {
    return { valid: false, reason: 'Field is empty' };
  }

  // If no validation rule is set, just accept it
  if (!rule || rule === 'NONE') {
    return { valid: true, cleanValue: raw };
  }

  switch (rule) {
    case 'PHONE':
      return validatePhone(raw);
    case 'URL':
      return validateURL(raw);
    case 'JSON_ARRAY':
      return validateJSONArray(raw);
    case 'STATE_CODE':
      return validateStateCode(raw);
    case 'ZIP_CODE':
      return validateZipCode(raw);
    default:
      // Unknown rule, pass it through
      return { valid: true, cleanValue: raw };
  }
}

function validatePhone(raw: any): ValidationResult {
  const str = String(raw);
  // Strip everything but digits
  const digits = str.replace(/\D/g, '');
  if (digits.length < 10) {
    return { valid: false, reason: `Phone number too short: ${str}` };
  }
  // If it's 10 digits, assume US and prepend +1
  if (digits.length === 10) {
    return { valid: true, cleanValue: `+1${digits}` };
  }
  if (digits.length === 11 && digits.startsWith('1')) {
    return { valid: true, cleanValue: `+${digits}` };
  }
  // Otherwise just prepend + (might be international)
  return { valid: true, cleanValue: `+${digits}` };
}

function validateURL(raw: any): ValidationResult {
  let str = String(raw).trim();
  if (str.toLowerCase() === 'n/a' || str.toLowerCase() === 'none') {
      return { valid: false, reason: `Invalid URL placeholder: ${str}` };
  }
  if (!str.startsWith('http://') && !str.startsWith('https://')) {
    str = 'https://' + str;
  }
  try {
    const url = new URL(str);
    if (!url.hostname.includes('.')) {
      return { valid: false, reason: `Invalid hostname in URL: ${str}` };
    }
    return { valid: true, cleanValue: url.toString() };
  } catch (e) {
    return { valid: false, reason: `Malformed URL: ${str}` };
  }
}

function validateJSONArray(raw: any): ValidationResult {
  try {
    let arr = raw;
    if (typeof raw === 'string') {
      arr = JSON.parse(raw);
    }
    if (!Array.isArray(arr)) {
      return { valid: false, reason: `Value is not a JSON array` };
    }
    if (arr.length === 0) {
      return { valid: false, reason: `JSON array is empty` };
    }
    return { valid: true, cleanValue: JSON.stringify(arr) };
  } catch (e) {
    return { valid: false, reason: `Failed to parse JSON array` };
  }
}

function validateStateCode(raw: any): ValidationResult {
  const str = String(raw).trim().toUpperCase();
  // We can add a simple mapping or just check length
  if (str.length === 2 && /^[A-Z]{2}$/.test(str)) {
    return { valid: true, cleanValue: str };
  }
  return { valid: false, reason: `Invalid state code: ${str}. Must be 2 letters.` };
}

function validateZipCode(raw: any): ValidationResult {
  const str = String(raw).trim();
  if (/^\d{5}(-\d{4})?$/.test(str)) {
    return { valid: true, cleanValue: str };
  }
  return { valid: false, reason: `Invalid US zip code: ${str}` };
}
