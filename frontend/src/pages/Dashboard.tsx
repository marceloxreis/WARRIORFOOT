import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';
import { leagueApi } from '../api/leagueApi';
import type { LeagueInfo, TeamBasicInfo } from '../types/league';

export default function Dashboard() {
  const navigate = useNavigate();
  const { activeLeagueId } = useAuthStore();
  const [leagueData, setLeagueData] = useState<LeagueInfo | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!activeLeagueId) {
      navigate('/home');
      return;
    }

    leagueApi
      .getLeagueDashboard(activeLeagueId)
      .then(setLeagueData)
      .catch(() => navigate('/home'))
      .finally(() => setLoading(false));
  }, [activeLeagueId, navigate]);

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 flex items-center justify-center">
        <p className="text-slate-400">Loading...</p>
      </div>
    );
  }

  if (!leagueData) return null;

  const divisionNames: Record<number, string> = {
    1: 'Division 1',
    2: 'Division 2',
    3: 'Division 3',
    4: 'Division 4',
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 p-8">
      <div className="max-w-7xl mx-auto">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-4xl font-bold text-white">League Dashboard</h1>
          <button
            onClick={() => navigate('/home')}
            className="px-6 py-2 bg-slate-700/50 backdrop-blur-sm text-white rounded-lg hover:bg-slate-600/50 transition-all"
          >
            Back
          </button>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {Object.entries(leagueData.divisions)
            .sort(([a], [b]) => Number(a) - Number(b))
            .map(([divLevel, teams]) => (
              <DivisionCard
                key={divLevel}
                divisionName={divisionNames[Number(divLevel)]}
                teams={teams}
                onTeamClick={(teamId) => navigate(`/team/${teamId}`)}
              />
            ))}
        </div>
      </div>
    </div>
  );
}

function DivisionCard({
  divisionName,
  teams,
  onTeamClick,
}: {
  divisionName: string;
  teams: TeamBasicInfo[];
  onTeamClick: (teamId: string) => void;
}) {
  return (
    <div className="bg-slate-800/40 backdrop-blur-sm rounded-2xl p-6 border border-slate-700/50">
      <h2 className="text-2xl font-bold text-white mb-4">{divisionName}</h2>
      <div className="space-y-2">
        {teams.map((team) => (
          <button
            key={team.id}
            onClick={() => onTeamClick(team.id)}
            className="w-full p-4 bg-slate-700/30 backdrop-blur-sm rounded-lg hover:bg-slate-600/30 transition-all text-left flex items-center gap-4"
          >
            <div className="flex gap-2">
              <div
                className="w-6 h-6 rounded"
                style={{ backgroundColor: team.colorPrimary }}
              />
              <div
                className="w-6 h-6 rounded"
                style={{ backgroundColor: team.colorSecondary }}
              />
            </div>
            <span className="text-white font-medium">{team.name}</span>
          </button>
        ))}
      </div>
    </div>
  );
}
