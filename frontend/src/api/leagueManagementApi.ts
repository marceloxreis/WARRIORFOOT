const API_BASE_URL = 'http://localhost:8080';

export type UserLeague = {
  leagueId: string;
  teamId: string;
  teamName: string;
  divisionLevel: number;
  createdAt: string;
};

const getAuthHeader = () => {
  const stored = localStorage.getItem('auth-storage');
  if (!stored) return {};
  const data = JSON.parse(stored);
  const token = data.state?.sessionToken;
  return token ? { Authorization: `Bearer ${token}` } : {};
};

export const leagueManagementApi = {
  getUserLeagues: async (): Promise<UserLeague[]> => {
    const response = await fetch(`${API_BASE_URL}/leagues/user/list`, {
      headers: getAuthHeader(),
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
      headers: getAuthHeader(),
    });

    if (!response.ok) {
      throw new Error('Failed to create new league');
    }

    return response.json();
  },
};
