const API_BASE_URL = 'http://localhost:8080';

export type InviteRequest = {
  inviteeName: string;
  inviteeEmail: string;
};

export type InviteValidation = {
  valid: boolean;
  inviterName: string;
  inviteeName: string;
  leagueId: string;
  errorMessage?: string;
};

export type AvailableTeam = {
  id: string;
  name: string;
  colorPrimary: string;
  colorSecondary: string;
};

export type AcceptInviteRequest = {
  token: string;
  fullName: string;
  email: string;
  password: string;
  passwordConfirmation: string;
  selectedTeamId: string;
};

export type AuthResponse = {
  sessionToken: string;
  userId: string;
  fullName: string;
  email: string;
  activeLeagueId: string;
  activeTeamId: string;
};

const getAuthHeader = () => {
  const stored = localStorage.getItem('auth-storage');
  if (!stored) return {};
  const data = JSON.parse(stored);
  const token = data.state?.sessionToken;
  return token ? { Authorization: `Bearer ${token}` } : {};
};

export const inviteApi = {
  sendInvite: async (data: InviteRequest): Promise<void> => {
    const response = await fetch(`${API_BASE_URL}/invites/send`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        ...getAuthHeader(),
      },
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      if (response.status === 400) {
        throw new Error('Invalid invite data. Please check the email address.');
      }
      throw new Error('Failed to send invite');
    }
  },

  validateToken: async (token: string): Promise<InviteValidation> => {
    const response = await fetch(`${API_BASE_URL}/invites/validate?token=${encodeURIComponent(token)}`);

    if (!response.ok) {
      throw new Error('Failed to validate invite token');
    }

    return response.json();
  },

  getAvailableTeams: async (leagueId: string): Promise<AvailableTeam[]> => {
    const response = await fetch(`${API_BASE_URL}/invites/available-teams?leagueId=${leagueId}`);

    if (!response.ok) {
      throw new Error('Failed to fetch available teams');
    }

    return response.json();
  },

  acceptInvite: async (data: AcceptInviteRequest): Promise<AuthResponse> => {
    const response = await fetch(`${API_BASE_URL}/invites/accept`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      if (response.status === 400) {
        throw new Error('Invalid registration data');
      }
      if (response.status === 401) {
        throw new Error('Invalid password for existing account');
      }
      throw new Error('Failed to accept invite');
    }

    return response.json();
  },
};
