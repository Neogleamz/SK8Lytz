/**
 * Shared validation utilities
 */

/**
 * Validates whether a given string is a correctly formatted email address.
 * Uses a standard regex check for <local>@<domain>.<tld>.
 */
export const isValidEmail = (str: string): boolean => {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(str.trim());
};
