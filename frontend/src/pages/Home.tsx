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
    <div className="min-h-screen bg-gray-100">
      <nav className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
          <h1 className="text-2xl font-bold">WARRIORFOOT</h1>
          <div className="flex items-center space-x-4">
            <span className="text-sm text-gray-600">{fullName}</span>
            <button
              onClick={handleLogout}
              className="bg-red-600 text-white px-4 py-2 rounded-md hover:bg-red-700"
            >
              Logout
            </button>
          </div>
        </div>
      </nav>

      <div className="max-w-7xl mx-auto px-4 py-8">
        <div className="bg-white rounded-lg shadow-md p-6">
          <h2 className="text-2xl font-semibold mb-4">Welcome, {fullName}!</h2>
          <p className="text-gray-600 mb-4">Email: {email}</p>
          
          <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded">
            Your league has been created with 32 teams across 4 divisions.
            You have been assigned a team in Division 4.
          </div>

          <div className="mt-6">
            <h3 className="text-xl font-semibold mb-2">Coming Soon:</h3>
            <ul className="list-disc list-inside text-gray-600 space-y-1">
              <li>View your team and players</li>
              <li>Manage lineup and tactics</li>
              <li>Simulate matches</li>
              <li>View league standings</li>
              <li>Invite friends to join your league</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
}
