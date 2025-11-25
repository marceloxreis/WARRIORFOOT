import type { PlayerDetails } from '../types/player';

const API_BASE_URL = 'http://localhost:8080';

export const playerApi = {
  getPlayerDetails: async (playerId: string): Promise<PlayerDetails> => {
    const response = await fetch(`${API_BASE_URL}/players/${playerId}`, {
      credentials: 'include',
    });

    if (!response.ok) {
      throw new Error('Failed to fetch player details');
    }

    return response.json();
  },
};
