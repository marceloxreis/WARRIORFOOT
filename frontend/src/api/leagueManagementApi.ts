const API_BASE_URL = 'http://localhost:8080';

export type UserLeague = {
  leagueId: string;
  teamId: string;
  teamName: string;
  divisionLevel: number;
  createdAt: string;
  isCreator: boolean;
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

  deleteLeague: async (leagueId: string): Promise<void> => {
    const response = await fetch(`${API_BASE_URL}/leagues/${leagueId}`, {
      method: 'DELETE',
      headers: getAuthHeader(),
    });

    if (!response.ok) {
      if (response.status === 400) {
        throw new Error('Only league creator can delete the league');
      }
      throw new Error('Failed to delete league');
    }
  },

  leaveLeague: async (leagueId: string): Promise<void> => {
    const response = await fetch(`${API_BASE_URL}/leagues/${leagueId}/leave`, {
      method: 'POST',
      headers: getAuthHeader(),
    });

    if (!response.ok) {
      if (response.status === 409) {
        throw new Error('League creator cannot leave. Delete the league instead.');
      }
      throw new Error('Failed to leave league');
    }
  },
};
