import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';
import { leagueManagementApi, type UserLeague } from '../api/leagueManagementApi';
import { ConfirmDialog } from '../components/ConfirmDialog';

type ConfirmDialogState = {
  isOpen: boolean;
  title: string;
  message: string;
  confirmText: string;
  variant: 'danger' | 'warning';
  leagueId: string;
  actionType: 'delete' | 'leave';
};

export default function LeaguesPage() {
  const navigate = useNavigate();
  const { fullName, setAuth, sessionToken, userId, email, activeLeagueId } = useAuthStore();
  const [leagues, setLeagues] = useState<UserLeague[]>([]);
  const [loading, setLoading] = useState(true);
  const [creating, setCreating] = useState(false);
  const [confirmDialog, setConfirmDialog] = useState<ConfirmDialogState>({
    isOpen: false,
    title: '',
    message: '',
    confirmText: '',
    variant: 'danger',
    leagueId: '',
    actionType: 'delete',
  });

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
    const leagueName = prompt('Enter a name for your new league:');
    if (!leagueName || leagueName.trim() === '') {
      return;
    }

    setCreating(true);
    try {
      const newLeague = await leagueManagementApi.createNewLeague(leagueName.trim());
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

  const handleDeleteClick = (league: UserLeague, e: React.MouseEvent) => {
    e.stopPropagation();
    setConfirmDialog({
      isOpen: true,
      title: 'Delete League',
      message: `Are you sure you want to delete this league? This will permanently remove the league and all its data for all participants. This action cannot be undone.`,
      confirmText: 'Delete League',
      variant: 'danger',
      leagueId: league.leagueId,
      actionType: 'delete',
    });
  };

  const handleLeaveClick = (league: UserLeague, e: React.MouseEvent) => {
    e.stopPropagation();
    setConfirmDialog({
      isOpen: true,
      title: 'Leave League',
      message: `Are you sure you want to leave this league? You will no longer be able to access this league or your team.`,
      confirmText: 'Leave League',
      variant: 'warning',
      leagueId: league.leagueId,
      actionType: 'leave',
    });
  };

  const handleConfirm = async () => {
    try {
      if (confirmDialog.actionType === 'delete') {
        await leagueManagementApi.deleteLeague(confirmDialog.leagueId);
      } else {
        await leagueManagementApi.leaveLeague(confirmDialog.leagueId);
      }

      // Refresh leagues list
      await loadLeagues();

      // If the deleted/left league was the active one, clear it
      if (activeLeagueId === confirmDialog.leagueId) {
        if (!sessionToken || !userId || !email || !fullName) return;
        setAuth({
          sessionToken,
          userId,
          fullName,
          email,
          activeLeagueId: undefined,
          activeTeamId: undefined,
        });
      }

      setConfirmDialog({ ...confirmDialog, isOpen: false });
    } catch (error) {
      console.error('Failed to perform action:', error);
      alert(error instanceof Error ? error.message : 'An error occurred');
      setConfirmDialog({ ...confirmDialog, isOpen: false });
    }
  };

  const handleCancel = () => {
    setConfirmDialog({ ...confirmDialog, isOpen: false });
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
              <div
                key={league.leagueId}
                className="relative bg-slate-800/50 backdrop-blur-sm border border-slate-700/50 rounded-lg p-6 hover:bg-slate-700/50 hover:border-slate-600/50 transition-all group"
              >
                <button
                  onClick={() => handleSelectLeague(league.leagueId, league.teamId)}
                  className="w-full text-left"
                >
                  <div className="mb-3">
                    <h2 className="text-2xl font-bold text-white group-hover:text-blue-400 transition-colors mb-1">
                      {league.leagueName}
                    </h2>
                    <p className="text-xs text-slate-500">
                      Created {new Date(league.createdAt).toLocaleDateString()}
                    </p>
                  </div>
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 rounded-full bg-gradient-to-br from-green-500 to-emerald-600 flex items-center justify-center text-white font-bold text-lg">
                        {league.divisionLevel}
                      </div>
                      <div>
                        <p className="text-sm text-slate-300">{league.teamName}</p>
                        <p className="text-xs text-slate-500">Division {league.divisionLevel}</p>
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
                </button>

                {/* Delete/Leave Button */}
                <button
                  onClick={(e) => league.isCreator ? handleDeleteClick(league, e) : handleLeaveClick(league, e)}
                  className={`absolute top-4 right-4 p-2 rounded-lg transition-all ${
                    league.isCreator
                      ? 'text-red-400 hover:bg-red-500/20 hover:text-red-300'
                      : 'text-yellow-400 hover:bg-yellow-500/20 hover:text-yellow-300'
                  }`}
                  title={league.isCreator ? 'Delete League' : 'Leave League'}
                >
                  {league.isCreator ? (
                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
                      />
                    </svg>
                  ) : (
                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"
                      />
                    </svg>
                  )}
                </button>
              </div>
            ))}
          </div>
        )}

        <ConfirmDialog
          isOpen={confirmDialog.isOpen}
          title={confirmDialog.title}
          message={confirmDialog.message}
          confirmText={confirmDialog.confirmText}
          cancelText="Cancel"
          onConfirm={handleConfirm}
          onCancel={handleCancel}
          variant={confirmDialog.variant}
        />
      </div>
    </div>
  );
}
