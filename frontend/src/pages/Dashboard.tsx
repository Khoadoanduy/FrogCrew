import React from 'react';
import { Calendar as CalendarIcon, Users, Clock } from 'lucide-react';
import { useStore } from '../store/useStore';
import { format } from 'date-fns';

function Dashboard() {
  const { games, crewMembers, currentUser } = useStore();

  const upcomingGames = games
    .filter((game) => new Date(game.date) > new Date())
    .slice(0, 3);

  return (
    <div className="space-y-6">
      <header>
        <h1 className="text-3xl font-bold text-gray-900">
          Welcome back, {currentUser?.name}!
        </h1>
      </header>

      <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
        <div className="bg-white overflow-hidden shadow rounded-lg">
          <div className="p-5">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <CalendarIcon className="h-6 w-6 text-gray-400" />
              </div>
              <div className="ml-5 w-0 flex-1">
                <dl>
                  <dt className="text-sm font-medium text-gray-500 truncate">
                    Upcoming Games
                  </dt>
                  <dd className="text-3xl font-semibold text-gray-900">
                    {upcomingGames.length}
                  </dd>
                </dl>
              </div>
            </div>
          </div>
        </div>

        <div className="bg-white overflow-hidden shadow rounded-lg">
          <div className="p-5">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <Users className="h-6 w-6 text-gray-400" />
              </div>
              <div className="ml-5 w-0 flex-1">
                <dl>
                  <dt className="text-sm font-medium text-gray-500 truncate">
                    Active Crew Members
                  </dt>
                  <dd className="text-3xl font-semibold text-gray-900">
                    {crewMembers.length}
                  </dd>
                </dl>
              </div>
            </div>
          </div>
        </div>

        <div className="bg-white overflow-hidden shadow rounded-lg">
          <div className="p-5">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <Clock className="h-6 w-6 text-gray-400" />
              </div>
              <div className="ml-5 w-0 flex-1">
                <dl>
                  <dt className="text-sm font-medium text-gray-500 truncate">
                    Next Game
                  </dt>
                  <dd className="text-lg font-semibold text-gray-900">
                    {upcomingGames[0]
                      ? format(new Date(upcomingGames[0].date), 'MMM dd, yyyy')
                      : 'No upcoming games'}
                  </dd>
                </dl>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="bg-white shadow sm:rounded-lg">
        <div className="px-4 py-5 sm:p-6">
          <h3 className="text-lg leading-6 font-medium text-gray-900">
            Upcoming Games
          </h3>
          <div className="mt-5">
            <div className="flow-root">
              <ul className="-my-5 divide-y divide-gray-200">
                {upcomingGames.map((game) => (
                  <li key={game.id} className="py-4">
                    <div className="flex items-center space-x-4">
                      <div className="flex-1 min-w-0">
                        <p className="text-sm font-medium text-gray-900 truncate">
                          {game.homeTeam} vs {game.awayTeam}
                        </p>
                        <p className="text-sm text-gray-500">
                          {format(new Date(game.date), 'MMMM dd, yyyy')} at{' '}
                          {game.time}
                        </p>
                        <p className="text-sm text-gray-500">{game.location}</p>
                      </div>
                      <div>
                        <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                          {game.assignedCrew.length} crew assigned
                        </span>
                      </div>
                    </div>
                  </li>
                ))}
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;