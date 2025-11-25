import type { LeagueInfo } from '../types/league';

const API_BASE_URL = 'http://localhost:8080';

export const leagueApi = {
  getLeagueDashboard: async (leagueId: string): Promise<LeagueInfo> => {
    const response = await fetch(`${API_BASE_URL}/leagues/${leagueId}`, {
      credentials: 'include',
    });

    if (!response.ok) {
      throw new Error('Failed to fetch league dashboard');
    }

    return response.json();
  },
};
