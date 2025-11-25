import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';
import { teamApi, Team as TeamData, Player } from '../api/teamApi';
import { apiClient } from '../api/client';

export function Team() {
  const navigate = useNavigate();
  const { fullName, activeTeamId, sessionToken, clearAuth } = useAuthStore();
  const [team, setTeam] = useState<TeamData | null>(null);
  const [players, setPlayers] = useState<Player[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!activeTeamId) return;

    const loadTeamData = async () => {
      try {
        const [teamData, playersData] = await Promise.all([
          teamApi.getTeam(activeTeamId),
          teamApi.getTeamPlayers(activeTeamId),
        ]);
        setTeam(teamData);
        setPlayers(playersData);
      } catch (err) {
        setError('Failed to load team data');
      } finally {
        setLoading(false);
      }
    };

    loadTeamData();
  }, [activeTeamId]);

  const handleLogout = async () => {
    if (sessionToken) {
      await apiClient.logout(sessionToken);
    }
    clearAuth();
    navigate('/login');
  };

  const getPositionColor = (position: string) => {
    switch (position) {
      case 'GK': return 'text-yellow-400';
      case 'DF': return 'text-blue-400';
      case 'MF': return 'text-green-400';
      case 'FW': return 'text-red-400';
      default: return 'text-gray-400';
    }
  };

  const formatCurrency = (value: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 0,
    }).format(value);
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-gray-400">Loading team data...</div>
      </div>
    );
  }

  if (error || !team) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-red-400">{error || 'Team not found'}</div>
      </div>
    );
  }

  const playersByPosition = {
    GK: players.filter(p => p.position === 'GK'),
    DF: players.filter(p => p.position === 'DF'),
    MF: players.filter(p => p.position === 'MF'),
    FW: players.filter(p => p.position === 'FW'),
  };

  return (
    <div className="min-h-screen">
      <nav className="bg-slate-900/80 backdrop-blur-md border-b border-slate-700/50 shadow-lg">
        <div className="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
          <h1 className="text-2xl font-black bg-gradient-to-r from-blue-400 to-cyan-400 bg-clip-text text-transparent">
            WARRIORFOOT
          </h1>
          <div className="flex items-center space-x-4">
            <button
              onClick={() => navigate('/home')}
              className="text-gray-300 hover:text-white transition-colors"
            >
              Dashboard
            </button>
            <div className="text-right">
              <p className="text-sm font-medium text-gray-100">{fullName}</p>
              <p className="text-xs text-gray-400">{team.name}</p>
            </div>
            <button
              onClick={handleLogout}
              className="bg-gradient-to-r from-red-600 to-red-700 hover:from-red-700 hover:to-red-800 text-white font-semibold py-2 px-4 rounded-lg transition-all duration-200 shadow-lg hover:shadow-red-500/50"
            >
              Logout
            </button>
          </div>
        </div>
      </nav>

      <div className="max-w-7xl mx-auto px-4 py-8">
        <div className="bg-slate-800/50 backdrop-blur-sm border border-slate-700/50 rounded-lg shadow-xl p-6 mb-6">
          <div className="flex items-center justify-between">
            <div>
              <h2 className="text-3xl font-bold mb-2" style={{ color: team.colorPrimary }}>
                {team.name}
              </h2>
              <div className="flex items-center space-x-4 text-sm text-gray-400">
                <span>Division {team.divisionLevel}</span>
                <span className="flex items-center">
                  <span className="w-4 h-4 rounded mr-2" style={{ backgroundColor: team.colorPrimary }}></span>
                  <span className="w-4 h-4 rounded" style={{ backgroundColor: team.colorSecondary }}></span>
                </span>
              </div>
            </div>
            <div className="text-right">
              <p className="text-gray-400 text-sm">Squad Size</p>
              <p className="text-3xl font-bold text-white">{players.length}</p>
            </div>
          </div>
        </div>

        {(['GK', 'DF', 'MF', 'FW'] as const).map((position) => (
          <div key={position} className="mb-6">
            <h3 className={`text-xl font-bold mb-3 ${getPositionColor(position)}`}>
              {position === 'GK' && 'Goalkeepers'}
              {position === 'DF' && 'Defenders'}
              {position === 'MF' && 'Midfielders'}
              {position === 'FW' && 'Forwards'}
              <span className="ml-2 text-gray-500 text-sm">({playersByPosition[position].length})</span>
            </h3>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {playersByPosition[position].map((player) => (
                <div
                  key={player.id}
                  className="bg-slate-800/50 backdrop-blur-sm border border-slate-700/50 rounded-lg p-4 hover:border-slate-600 transition-colors"
                >
                  <div className="flex justify-between items-start mb-2">
                    <div>
                      <h4 className="font-semibold text-white">{player.name}</h4>
                      <p className="text-xs text-gray-400">{player.age} years old</p>
                    </div>
                    <div className="text-right">
                      <div className="text-2xl font-bold text-blue-400">{player.overall}</div>
                      <div className={`text-xs font-semibold ${getPositionColor(player.position)}`}>
                        {player.position}
                      </div>
                    </div>
                  </div>
                  <div className="text-sm text-gray-400">
                    Value: <span className="text-green-400 font-semibold">{formatCurrency(player.marketValue)}</span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
