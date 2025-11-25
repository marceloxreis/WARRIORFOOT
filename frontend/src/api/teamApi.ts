const API_BASE_URL = 'http://localhost:8080';

export interface Team {
  id: string;
  name: string;
  colorPrimary: string;
  colorSecondary: string;
  divisionLevel: number;
}

export interface Player {
  id: string;
  name: string;
  age: number;
  position: string;
  overall: number;
  marketValue: number;
}

export const teamApi = {
  async getTeam(teamId: string): Promise<Team> {
    const response = await fetch(`${API_BASE_URL}/teams/${teamId}`);
    if (!response.ok) throw new Error('Failed to fetch team');
    return response.json();
  },

  async getTeamPlayers(teamId: string): Promise<Player[]> {
    const response = await fetch(`${API_BASE_URL}/teams/${teamId}/players`);
    if (!response.ok) throw new Error('Failed to fetch players');
    return response.json();
  },
};
