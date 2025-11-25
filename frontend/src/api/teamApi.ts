import type { TeamInfo, PlayerInfo } from '../types/team';

const API_BASE_URL = 'http://localhost:8080';

export const teamApi = {
  async getTeam(teamId: string): Promise<TeamInfo> {
    const response = await fetch(`${API_BASE_URL}/teams/${teamId}`);
    if (!response.ok) throw new Error('Failed to fetch team');
    return response.json();
  },

  async getTeamPlayers(teamId: string): Promise<PlayerInfo[]> {
    const response = await fetch(`${API_BASE_URL}/teams/${teamId}/players`);
    if (!response.ok) throw new Error('Failed to fetch players');
    return response.json();
  },
};
