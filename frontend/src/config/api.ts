/**
 * API Configuration
 */
export const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

/**
 * Get authorization header with Bearer token
 */
export function getAuthHeader(): Record<string, string> {
  const stored = localStorage.getItem('auth-storage');
  if (!stored) return {};

  try {
    const data = JSON.parse(stored);
    const token = data.state?.sessionToken;
    return token ? { Authorization: `Bearer ${token}` } : {};
  } catch {
    return {};
  }
}

/**
 * Create API fetch with base URL and auth headers
 */
export async function apiFetch(
  endpoint: string,
  options: RequestInit = {}
): Promise<Response> {
  const url = `${API_BASE_URL}${endpoint}`;
  const headers = {
    ...getAuthHeader(),
    ...options.headers,
  };

  return fetch(url, {
    ...options,
    headers,
  });
}
