import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface AuthState {
  sessionToken: string | null;
  userId: string | null;
  fullName: string | null;
  email: string | null;
  activeLeagueId: string | null;
  activeTeamId: string | null;
  setAuth: (data: {
    sessionToken: string;
    userId: string;
    fullName: string;
    email: string;
    activeLeagueId: string;
    activeTeamId: string;
  }) => void;
  clearAuth: () => void;
  isAuthenticated: () => boolean;
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      sessionToken: null,
      userId: null,
      fullName: null,
      email: null,
      activeLeagueId: null,
      activeTeamId: null,
      setAuth: (data) => set(data),
      clearAuth: () =>
        set({
          sessionToken: null,
          userId: null,
          fullName: null,
          email: null,
          activeLeagueId: null,
          activeTeamId: null,
        }),
      isAuthenticated: () => get().sessionToken !== null,
    }),
    {
      name: 'auth-storage',
    }
  )
);
