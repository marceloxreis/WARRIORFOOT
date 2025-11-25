import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';
import { apiClient } from '../api/client';

export function Home() {
  const navigate = useNavigate();
  const { fullName, email, sessionToken, clearAuth } = useAuthStore();

  const handleLogout = async () => {
    if (sessionToken) {
      await apiClient.logout(sessionToken);
    }
    clearAuth();
    navigate('/login');
  };

  return (
    <div className="min-h-screen">
      <nav className="navbar">
        <div className="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
          <h1 className="logo-text">WARRIORFOOT</h1>
          <div className="flex items-center space-x-4">
            <div className="text-right">
              <p className="text-sm font-medium text-gray-100">{fullName}</p>
              <p className="text-xs text-gray-400">{email}</p>
            </div>
            <button
              onClick={handleLogout}
              className="btn-danger"
            >
              Logout
            </button>
          </div>
        </div>
      </nav>

      <div className="max-w-7xl mx-auto px-4 py-8">
        <div className="card p-8">
          <div className="mb-6">
            <h2 className="text-3xl font-bold mb-2 bg-gradient-to-r from-green-400 to-blue-500 bg-clip-text text-transparent">
              Welcome, {fullName}!
            </h2>
            <p className="text-gray-400">Manager Dashboard</p>
          </div>
          
          <div className="alert-success mb-6">
            <div className="flex items-start">
              <svg className="w-5 h-5 mr-3 mt-0.5" fill="currentColor" viewBox="0 0 20 20">
                <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
              </svg>
              <div>
                <p className="font-semibold">League Created Successfully!</p>
                <p className="text-sm mt-1">Your league has been generated with 32 teams across 4 divisions. You have been assigned a team in Division 4.</p>
              </div>
            </div>
          </div>

          <div className="bg-slate-900/30 border border-slate-700 rounded-lg p-6">
            <h3 className="text-xl font-semibold mb-4 text-blue-400">🚀 Coming Soon</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              <div className="flex items-center space-x-2 text-gray-300">
                <span className="text-blue-400">▸</span>
                <span>View your team and players</span>
              </div>
              <div className="flex items-center space-x-2 text-gray-300">
                <span className="text-blue-400">▸</span>
                <span>Manage lineup and tactics</span>
              </div>
              <div className="flex items-center space-x-2 text-gray-300">
                <span className="text-blue-400">▸</span>
                <span>Simulate matches</span>
              </div>
              <div className="flex items-center space-x-2 text-gray-300">
                <span className="text-blue-400">▸</span>
                <span>View league standings</span>
              </div>
              <div className="flex items-center space-x-2 text-gray-300">
                <span className="text-blue-400">▸</span>
                <span>Invite friends to your league</span>
              </div>
              <div className="flex items-center space-x-2 text-gray-300">
                <span className="text-blue-400">▸</span>
                <span>Transfer market</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
