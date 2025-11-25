const API_BASE_URL = 'http://localhost:8080';

export type UserLeague = {
  leagueId: string;
  teamId: string;
  teamName: string;
  divisionLevel: number;
  createdAt: string;
};

export const leagueManagementApi = {
  getUserLeagues: async (): Promise<UserLeague[]> => {
    const response = await fetch(`${API_BASE_URL}/leagues/user/list`, {
      credentials: 'include',
    });

    if (!response.ok) {
      throw new Error('Failed to fetch user leagues');
    }

    return response.json();
  },

  createNewLeague: async (): Promise<{
    leagueId: string;
    teamId: string;
    teamName: string;
    divisionLevel: number;
  }> => {
    const response = await fetch(`${API_BASE_URL}/leagues/create`, {
      method: 'POST',
      credentials: 'include',
    });

    if (!response.ok) {
      throw new Error('Failed to create new league');
    }

    return response.json();
  },
};
