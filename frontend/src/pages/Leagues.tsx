import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';
import { leagueManagementApi, type UserLeague } from '../api/leagueManagementApi';

export default function LeaguesPage() {
  const navigate = useNavigate();
  const { fullName, setAuth, sessionToken, userId, email } = useAuthStore();
  const [leagues, setLeagues] = useState<UserLeague[]>([]);
  const [loading, setLoading] = useState(true);
  const [creating, setCreating] = useState(false);

  useEffect(() => {
    loadLeagues();
  }, []);

  const loadLeagues = async () => {
    try {
      const data = await leagueManagementApi.getUserLeagues();
      setLeagues(data);
    } catch (error) {
      console.error('Failed to load leagues:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateLeague = async () => {
    setCreating(true);
    try {
      const newLeague = await leagueManagementApi.createNewLeague();
      await loadLeagues();
      handleSelectLeague(newLeague.leagueId, newLeague.teamId);
    } catch (error) {
      console.error('Failed to create league:', error);
      setCreating(false);
    }
  };

  const handleSelectLeague = (leagueId: string, teamId: string) => {
    if (!sessionToken || !userId || !email || !fullName) return;
    
    setAuth({
      sessionToken,
      userId,
      fullName,
      email,
      activeLeagueId: leagueId,
      activeTeamId: teamId,
    });
    
    navigate('/home');
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 flex items-center justify-center">
        <p className="text-slate-400">Loading leagues...</p>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 p-8">
      <div className="max-w-5xl mx-auto">
        <div className="flex justify-between items-center mb-8">
          <div>
            <h1 className="text-4xl font-bold text-white mb-2">My Leagues</h1>
            <p className="text-slate-400">Select a league to continue or create a new one</p>
          </div>
          <button
            onClick={handleCreateLeague}
            disabled={creating}
            className="px-6 py-3 bg-gradient-to-r from-green-600 to-green-700 hover:from-green-700 hover:to-green-800 text-white font-semibold rounded-lg transition-all duration-200 shadow-lg hover:shadow-green-500/50 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {creating ? 'Creating...' : '+ New League'}
          </button>
        </div>

        {leagues.length === 0 ? (
          <div className="bg-slate-800/50 backdrop-blur-sm border border-slate-700/50 rounded-lg p-12 text-center">
            <p className="text-slate-400 mb-4">You don't have any leagues yet</p>
            <button
              onClick={handleCreateLeague}
              disabled={creating}
              className="px-6 py-3 bg-gradient-to-r from-blue-600 to-blue-700 hover:from-blue-700 hover:to-blue-800 text-white font-semibold rounded-lg transition-all duration-200 shadow-lg hover:shadow-blue-500/50"
            >
              Create Your First League
            </button>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {leagues.map((league) => (
              <button
                key={league.leagueId}
                onClick={() => handleSelectLeague(league.leagueId, league.teamId)}
                className="bg-slate-800/50 backdrop-blur-sm border border-slate-700/50 rounded-lg p-6 hover:bg-slate-700/50 hover:border-slate-600/50 transition-all text-left group"
              >
                <div className="flex items-center justify-between mb-4">
                  <div className="flex items-center gap-3">
                    <div className="w-12 h-12 rounded-full bg-gradient-to-br from-blue-500 to-purple-600 flex items-center justify-center text-white font-bold text-xl">
                      {league.divisionLevel}
                    </div>
                    <div>
                      <h3 className="text-xl font-bold text-white group-hover:text-blue-400 transition-colors">
                        {league.teamName}
                      </h3>
                      <p className="text-sm text-slate-400">Division {league.divisionLevel}</p>
                    </div>
                  </div>
                  <svg
                    className="w-6 h-6 text-slate-600 group-hover:text-blue-400 transition-colors"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M9 5l7 7-7 7"
                    />
                  </svg>
                </div>
                <div className="text-xs text-slate-500">
                  Created {new Date(league.createdAt).toLocaleDateString()}
                </div>
              </button>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
