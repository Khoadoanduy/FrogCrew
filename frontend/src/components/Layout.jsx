import React from 'react';
import { Outlet, Link, useLocation } from 'react-router-dom';
import { useStore } from '../store/useStore';

function Layout() {
  const location = useLocation();
  const { currentUser, setCurrentUser, crewMembers } = useStore();

  const handleUserSwitch = (e) => {
    setCurrentUser(e.target.value);
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <nav className="bg-white shadow-lg">
        <div className="container mx-auto px-4">
          <div className="flex items-center h-16">
            <Link to="/" className="text-xl font-bold text-indigo-600 mr-8">
              FrogCrew
            </Link>

            <div className="flex space-x-4">
              <Link
                to="/"
                className={`px-3 py-2 rounded-md text-sm font-medium ${
                  location.pathname === '/'
                    ? 'text-indigo-600 bg-gray-100'
                    : 'text-gray-700 hover:bg-gray-100'
                }`}
              >
                Home
              </Link>
              <Link
                to="/schedule"
                className={`px-3 py-2 rounded-md text-sm font-medium ${
                  location.pathname === '/schedule'
                    ? 'text-indigo-600 bg-gray-100'
                    : 'text-gray-700 hover:bg-gray-100'
                }`}
              >
                Schedule
              </Link>
              <Link
                to="/crew"
                className={`px-3 py-2 rounded-md text-sm font-medium ${
                  location.pathname === '/crew'
                    ? 'text-indigo-600 bg-gray-100'
                    : 'text-gray-700 hover:bg-gray-100'
                }`}
              >
                Crew Members
              </Link>
            </div>

            <div className="ml-auto flex items-center space-x-4">
              <select
                value={currentUser?.userId}
                onChange={handleUserSwitch}
                className="text-sm border-gray-300 rounded-md shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
              >
                {crewMembers.map(member => (
                  <option key={member.userId} value={member.userId}>
                    View as: {member.firstName} ({member.role})
                  </option>
                ))}
              </select>

              <Link
                to={`/profile/${currentUser?.userId}`}
                className="flex items-center text-sm font-medium text-gray-500 hover:text-gray-700"
              >
                <div className="h-8 w-8 rounded-full bg-indigo-100 flex items-center justify-center">
                  <span className="text-indigo-800">
                    {currentUser?.firstName?.charAt(0)}
                  </span>
                </div>
                <span className="ml-2">{currentUser?.firstName}</span>
              </Link>
            </div>
          </div>
        </div>
      </nav>

      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <Outlet />
      </main>
    </div>
  );
}

export default Layout;