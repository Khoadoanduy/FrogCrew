import React from "react";
import { useStore } from "../store/useStore";

function Dashboard() {
  const { currentUser } = useStore();

  return (
    <div className="max-w-4xl mx-auto text-center">
      <div className="bg-white rounded-lg shadow-lg p-8">
        <h1 className="text-4xl font-bold text-gray-900 mb-4">
          Welcome to FrogCrew, {currentUser?.firstName}!
        </h1>

        <p className="text-lg text-gray-600 mb-8">
          Your central hub for managing sports broadcast crew assignments and
          schedules.
        </p>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div className="bg-indigo-50 rounded-lg p-6">
            <h2 className="text-xl font-semibold text-indigo-900 mb-2">
              Quick Links
            </h2>
            <ul className="space-y-2 text-indigo-700">
              <li>
                <a href="/schedule" className="hover:text-indigo-900">
                  View Game Schedule
                </a>
              </li>
              <li>
                <a href="/crew" className="hover:text-indigo-900">
                  Browse Crew Members
                </a>
              </li>
              {currentUser?.role === "ADMIN" && (
                <li>
                  <a href="/schedule/create" className="hover:text-indigo-900">
                    Create New Game
                  </a>
                </li>
              )}
            </ul>
          </div>

          <div className="bg-indigo-50 rounded-lg p-6">
            <h2 className="text-xl font-semibold text-indigo-900 mb-2">
              Your Role
            </h2>
            <p className="text-indigo-700">
              You are currently logged in as a{" "}
              <span className="font-semibold">{currentUser?.role}</span>
            </p>
            <p className="text-sm text-indigo-600 mt-2">
              {currentUser?.role === "ADMIN"
                ? "You have access to all administrative features."
                : "You can view schedules and manage your availability."}
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
