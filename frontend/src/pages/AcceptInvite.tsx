import { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { inviteApi, type InviteValidation, type AvailableTeam } from '../api/inviteApi';
import { useAuthStore } from '../store/authStore';

export default function AcceptInvitePage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { setAuth } = useAuthStore();

  const [loading, setLoading] = useState(true);
  const [validation, setValidation] = useState<InviteValidation | null>(null);
  const [teams, setTeams] = useState<AvailableTeam[]>([]);
  const [selectedTeamId, setSelectedTeamId] = useState<string>('');

  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [passwordConfirmation, setPasswordConfirmation] = useState('');

  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const token = searchParams.get('token');
    if (!token) {
      setError('Invalid invite link');
      setLoading(false);
      return;
    }

    validateAndLoadData(token);
  }, [searchParams]);

  const validateAndLoadData = async (token: string) => {
    try {
      const validationResult = await inviteApi.validateToken(token);

      if (!validationResult.valid) {
        setError(validationResult.errorMessage || 'Invalid invite');
        setLoading(false);
        return;
      }

      setValidation(validationResult);

      const availableTeams = await inviteApi.getAvailableTeams(validationResult.leagueId);
      if (availableTeams.length === 0) {
        setError('No available teams in this league. All teams are taken.');
        setLoading(false);
        return;
      }

      setTeams(availableTeams);
      setLoading(false);
    } catch (err) {
      setError('Failed to load invite information');
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    if (!fullName.trim()) {
      setError('Please enter your full name');
      return;
    }

    if (!email.trim() || !email.includes('@')) {
      setError('Please enter a valid email address');
      return;
    }

    if (password.length < 6) {
      setError('Password must be at least 6 characters');
      return;
    }

    if (password !== passwordConfirmation) {
      setError('Passwords do not match');
      return;
    }

    if (!selectedTeamId) {
      setError('Please select a team');
      return;
    }

    const token = searchParams.get('token');
    if (!token) return;

    setSubmitting(true);
    try {
      const authResponse = await inviteApi.acceptInvite({
        token,
        fullName,
        email,
        password,
        passwordConfirmation,
        selectedTeamId,
      });

      setAuth({
        sessionToken: authResponse.sessionToken,
        userId: authResponse.userId,
        fullName: authResponse.fullName,
        email: authResponse.email,
        activeLeagueId: authResponse.activeLeagueId,
        activeTeamId: authResponse.activeTeamId,
      });

      navigate('/home');
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to accept invite');
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-b-4 border-blue-500 mx-auto mb-4"></div>
          <p className="text-slate-400">Loading invite...</p>
        </div>
      </div>
    );
  }

  if (error && !validation) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 flex items-center justify-center p-4">
        <div className="bg-slate-800/50 backdrop-blur-sm border border-slate-700/50 rounded-lg shadow-xl p-8 max-w-md w-full text-center">
          <div className="w-16 h-16 rounded-full bg-red-500/20 flex items-center justify-center mx-auto mb-4">
            <svg className="w-8 h-8 text-red-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
          </div>
          <h2 className="text-2xl font-bold text-white mb-2">Invalid Invite</h2>
          <p className="text-slate-400 mb-6">{error}</p>
          <button
            onClick={() => navigate('/login')}
            className="px-6 py-3 bg-gradient-to-r from-blue-600 to-blue-700 hover:from-blue-700 hover:to-blue-800 text-white font-semibold rounded-lg transition-all duration-200 shadow-lg hover:shadow-blue-500/50"
          >
            Go to Login
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 p-8">
      <div className="max-w-3xl mx-auto">
        <div className="bg-slate-800/50 backdrop-blur-sm border border-slate-700/50 rounded-lg shadow-xl p-8">
          {/* Welcome Message */}
          <div className="text-center mb-8">
            <h1 className="text-4xl font-bold text-white mb-2">
              Olá, {validation?.inviteeName}!
            </h1>
            <p className="text-xl text-slate-300 mb-1">Bem-vindo ao WARRIORFOOT!</p>
            <p className="text-slate-400">
              Você foi convidado por <span className="text-blue-400 font-semibold">{validation?.inviterName}</span>
            </p>
          </div>

          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Personal Information */}
            <div className="bg-slate-900/30 border border-slate-700 rounded-lg p-6">
              <h3 className="text-lg font-semibold text-white mb-4">Your Information</h3>

              <div className="space-y-4">
                <div>
                  <label htmlFor="fullName" className="block text-sm font-medium text-slate-300 mb-2">
                    Full Name
                  </label>
                  <input
                    type="text"
                    id="fullName"
                    value={fullName}
                    onChange={(e) => setFullName(e.target.value)}
                    disabled={submitting}
                    className="w-full px-4 py-2.5 bg-slate-900/50 border border-slate-700 rounded-lg text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent disabled:opacity-50"
                    placeholder="Enter your full name"
                  />
                </div>

                <div>
                  <label htmlFor="email" className="block text-sm font-medium text-slate-300 mb-2">
                    Email Address
                  </label>
                  <input
                    type="email"
                    id="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    disabled={submitting}
                    className="w-full px-4 py-2.5 bg-slate-900/50 border border-slate-700 rounded-lg text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent disabled:opacity-50"
                    placeholder="your@email.com"
                  />
                </div>

                <div>
                  <label htmlFor="password" className="block text-sm font-medium text-slate-300 mb-2">
                    Password
                  </label>
                  <input
                    type="password"
                    id="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    disabled={submitting}
                    className="w-full px-4 py-2.5 bg-slate-900/50 border border-slate-700 rounded-lg text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent disabled:opacity-50"
                    placeholder="Minimum 6 characters"
                  />
                </div>

                <div>
                  <label htmlFor="passwordConfirmation" className="block text-sm font-medium text-slate-300 mb-2">
                    Confirm Password
                  </label>
                  <input
                    type="password"
                    id="passwordConfirmation"
                    value={passwordConfirmation}
                    onChange={(e) => setPasswordConfirmation(e.target.value)}
                    disabled={submitting}
                    className="w-full px-4 py-2.5 bg-slate-900/50 border border-slate-700 rounded-lg text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent disabled:opacity-50"
                    placeholder="Re-enter your password"
                  />
                </div>
              </div>
            </div>

            {/* Team Selection */}
            <div className="bg-slate-900/30 border border-slate-700 rounded-lg p-6">
              <h3 className="text-lg font-semibold text-white mb-4">Select Your Team</h3>
              <p className="text-slate-400 text-sm mb-4">Choose a Division 4 team to manage</p>

              <div className="grid grid-cols-2 md:grid-cols-3 gap-3">
                {teams.map((team) => (
                  <button
                    key={team.id}
                    type="button"
                    onClick={() => setSelectedTeamId(team.id)}
                    disabled={submitting}
                    className={`p-4 border-2 rounded-lg transition-all duration-200 disabled:opacity-50 ${
                      selectedTeamId === team.id
                        ? 'border-blue-500 bg-blue-500/20 shadow-lg shadow-blue-500/50'
                        : 'border-slate-700 hover:border-slate-600 bg-slate-800/50'
                    }`}
                  >
                    <div className="flex items-center justify-center gap-2 mb-2">
                      <div
                        className="w-4 h-4 rounded-full"
                        style={{ backgroundColor: team.colorPrimary }}
                      />
                      <div
                        className="w-4 h-4 rounded-full"
                        style={{ backgroundColor: team.colorSecondary }}
                      />
                    </div>
                    <p className="text-white font-semibold text-sm text-center">{team.name}</p>
                  </button>
                ))}
              </div>
            </div>

            {/* Error Message */}
            {error && (
              <div className="p-4 bg-red-500/10 border border-red-500/50 rounded-lg">
                <p className="text-red-400 text-sm">{error}</p>
              </div>
            )}

            {/* Submit Button */}
            <button
              type="submit"
              disabled={submitting}
              className="w-full py-3 bg-gradient-to-r from-blue-600 to-blue-700 hover:from-blue-700 hover:to-blue-800 text-white font-semibold rounded-lg transition-all duration-200 shadow-lg hover:shadow-blue-500/50 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {submitting ? 'Accepting Invite...' : 'Accept Invite & Join League'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
