import React, { useState } from "react";
import { format } from "date-fns";

import { useStore } from "../store/useStore";
import { useNavigate } from "react-router-dom";

function Schedule() {
  const navigate = useNavigate();
  const {
    games,
    submitAvailability,
    getGameAvailability,
    currentUser,
    publishGame,
    deleteGame,
  } = useStore();
  const [view, setView] = useState("list");
  const [dateFilter, setDateFilter] = useState("");
  const [venueFilter, setVenueFilter] = useState("");
  const [opponentFilter, setOpponentFilter] = useState("");
  const [submittingGameId, setSubmittingGameId] = useState(null);
  const [comment, setComment] = useState("");
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(null);
  const [isDeleting, setIsDeleting] = useState(false);

  const venues = Array.from(new Set(games.map((game) => game.venue)));
  const opponents = Array.from(new Set(games.map((game) => game.opponent)));

  const filteredGames = games.filter((game) => {
    const matchesDate = !dateFilter || game.gameDate.includes(dateFilter);
    const matchesVenue = !venueFilter || game.venue === venueFilter;
    const matchesOpponent = !opponentFilter || game.opponent === opponentFilter;
    return matchesDate && matchesVenue && matchesOpponent;
  });

  const handleAvailabilitySubmit = async (gameId, isAvailable) => {
    await submitAvailability(gameId, isAvailable, comment);
    setSubmittingGameId(null);
    setComment("");
  };

  const handlePublish = async (gameId) => {
    try {
      await publishGame(gameId);
    } catch (error) {
      console.error("Failed to publish game:", error);
    }
  };

  const handleDelete = async (gameId) => {
    setIsDeleting(true);
    try {
      await deleteGame(gameId);
      setShowDeleteConfirm(null);
    } catch (error) {
      console.error("Failed to delete game:", error);
    } finally {
      setIsDeleting(false);
    }
  };

  if (games.length === 0) {
    return (
      <div className="text-center py-12">
        <h3 className="mt-2 text-lg font-medium text-gray-900">
          No upcoming games
        </h3>
        <p className="mt-1 text-sm text-gray-500">
          No games are currently scheduled.
        </p>
        {currentUser?.role === "ADMIN" && (
          <button
            onClick={() => navigate("/schedule/create")}
            className="mt-4 inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
          >
            Create Game Schedule
          </button>
        )}
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <header className="flex items-center justify-between">
        <h1 className="text-3xl font-bold text-gray-900">Game Schedule</h1>
        <div className="flex items-center space-x-2">
          {currentUser?.role === "ADMIN" && (
            <button
              onClick={() => navigate("/schedule/create")}
              className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
            >
              Add New Game
            </button>
          )}
          <button
            onClick={() => setView("list")}
            className={`px-4 py-2 rounded-md ${
              view === "list"
                ? "bg-indigo-600 text-white"
                : "bg-white text-gray-700 hover:bg-gray-50"
            }`}
          >
            List View
          </button>
          <button
            onClick={() => setView("calendar")}
            className={`px-4 py-2 rounded-md ${
              view === "calendar"
                ? "bg-indigo-600 text-white"
                : "bg-white text-gray-700 hover:bg-gray-50"
            }`}
          >
            Calendar View
          </button>
        </div>
      </header>

      <div className="bg-white shadow rounded-lg">
        <div className="p-6">
          <div className="grid grid-cols-1 gap-4 sm:grid-cols-3 mb-6">
            <div>
              <label
                htmlFor="date"
                className="block text-sm font-medium text-gray-700"
              >
                Date
              </label>
              <input
                type="date"
                id="date"
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                value={dateFilter}
                onChange={(e) => setDateFilter(e.target.value)}
              />
            </div>
            <div>
              <label
                htmlFor="venue"
                className="block text-sm font-medium text-gray-700"
              >
                Venue
              </label>
              <select
                id="venue"
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                value={venueFilter}
                onChange={(e) => setVenueFilter(e.target.value)}
              >
                <option value="">All Venues</option>
                {venues.map((venue) => (
                  <option key={venue} value={venue}>
                    {venue}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label
                htmlFor="opponent"
                className="block text-sm font-medium text-gray-700"
              >
                Opponent
              </label>
              <select
                id="opponent"
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                value={opponentFilter}
                onChange={(e) => setOpponentFilter(e.target.value)}
              >
                <option value="">All Opponents</option>
                {opponents.map((opponent) => (
                  <option key={opponent} value={opponent}>
                    {opponent}
                  </option>
                ))}
              </select>
            </div>
          </div>

          {view === "list" ? (
            <div className="flow-root">
              <ul className="-my-5 divide-y divide-gray-200">
                {filteredGames.map((game) => {
                  const availability = getGameAvailability(game.gameId);
                  const isSubmitting = submittingGameId === game.gameId;

                  return (
                    <li key={game.gameId} className="py-5">
                      <div className="flex items-center space-x-4">
                        <div className="flex-shrink-0"></div>
                        <div className="flex-1 min-w-0">
                          <div className="flex items-center justify-between">
                            <div>
                              <p className="text-sm font-medium text-gray-900 truncate">
                                vs {game.opponent}
                              </p>
                              <span
                                className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                                  game.status === "PUBLISHED"
                                    ? "bg-green-100 text-green-800"
                                    : "bg-yellow-100 text-yellow-800"
                                }`}
                              >
                                {game.status}
                              </span>
                            </div>
                            {currentUser?.role === "ADMIN" &&
                              game.status !== "PUBLISHED" && (
                                <div className="flex items-center space-x-2">
                                  <button
                                    onClick={() => handlePublish(game.gameId)}
                                    className="inline-flex items-center px-2 py-1 border border-transparent text-xs font-medium rounded shadow-sm text-white bg-green-600 hover:bg-green-700"
                                  >
                                    Publish
                                  </button>
                                  <button
                                    onClick={() =>
                                      navigate(`/schedule/edit/${game.gameId}`)
                                    }
                                    className="inline-flex items-center px-2 py-1 border border-gray-300 text-xs font-medium rounded shadow-sm text-gray-700 bg-white hover:bg-gray-50"
                                  >
                                    Edit
                                  </button>
                                  <button
                                    onClick={() =>
                                      setShowDeleteConfirm(game.gameId)
                                    }
                                    className="inline-flex items-center px-2 py-1 border border-transparent text-xs font-medium rounded shadow-sm text-white bg-red-600 hover:bg-red-700"
                                  >
                                    Delete
                                  </button>
                                </div>
                              )}
                          </div>
                          <div className="mt-2 flex items-center text-sm text-gray-500">
                            {game.venue}
                          </div>
                          <p className="mt-1 text-sm text-gray-500">
                            {format(new Date(game.gameDate), "MMMM dd, yyyy")}{" "}
                            at {game.gameStart}
                          </p>
                        </div>
                        <div className="flex items-center space-x-2">
                          {currentUser?.role !== "ADMIN" &&
                            (availability ? (
                              <div
                                className={`inline-flex items-center px-3 py-1 rounded-full text-sm ${
                                  availability.availability
                                    ? "bg-green-100 text-green-800"
                                    : "bg-red-100 text-red-800"
                                }`}
                              >
                                {availability.availability
                                  ? "Available"
                                  : "Unavailable"}
                              </div>
                            ) : isSubmitting ? (
                              <div className="space-y-2">
                                <div className="flex items-center space-x-2">
                                  <button
                                    onClick={() =>
                                      handleAvailabilitySubmit(
                                        game.gameId,
                                        true
                                      )
                                    }
                                    className="inline-flex items-center px-3 py-1 border border-transparent text-sm font-medium rounded-full shadow-sm text-white bg-green-600 hover:bg-green-700"
                                  >
                                    Available
                                  </button>
                                  <button
                                    onClick={() =>
                                      handleAvailabilitySubmit(
                                        game.gameId,
                                        false
                                      )
                                    }
                                    className="inline-flex items-center px-3 py-1 border border-transparent text-sm font-medium rounded-full shadow-sm text-white bg-red-600 hover:bg-red-700"
                                  >
                                    Unavailable
                                  </button>
                                </div>
                                <input
                                  type="text"
                                  placeholder="Add a comment (optional)"
                                  className="block w-full text-sm border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500"
                                  value={comment}
                                  onChange={(e) => setComment(e.target.value)}
                                />
                              </div>
                            ) : (
                              <button
                                onClick={() => setSubmittingGameId(game.gameId)}
                                className="inline-flex items-center px-3 py-1 border border-gray-300 text-sm font-medium rounded-full shadow-sm text-gray-700 bg-white hover:bg-gray-50"
                              >
                                Submit Availability
                              </button>
                            ))}
                          <button
                            onClick={() =>
                              navigate(`/crew/game/${game.gameId}`)
                            }
                            className="inline-flex items-center px-3 py-1.5 border border-transparent text-xs font-medium rounded-full shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                          >
                            {currentUser?.role === "ADMIN"
                              ? "Manage Crew"
                              : "View Crew List"}
                          </button>
                        </div>
                      </div>
                    </li>
                  );
                })}
              </ul>
            </div>
          ) : (
            <div className="grid grid-cols-7 gap-px bg-gray-200 rounded-lg overflow-hidden">
              {["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"].map((day) => (
                <div
                  key={day}
                  className="bg-gray-50 py-2 text-center text-sm font-semibold text-gray-700"
                >
                  {day}
                </div>
              ))}
              {Array.from({ length: 35 }).map((_, i) => {
                const currentDate = new Date(2024, 8, i + 1);
                const gamesOnDay = filteredGames.filter(
                  (game) => game.gameDate === format(currentDate, "yyyy-MM-dd")
                );

                return (
                  <div key={i} className="bg-white min-h-[100px] p-2">
                    <div className="font-medium text-sm text-gray-500">
                      {i + 1}
                    </div>
                    {gamesOnDay.map((game) => (
                      <div
                        key={game.gameId}
                        className="mt-1 p-1 text-xs bg-indigo-50 text-indigo-700 rounded cursor-pointer hover:bg-indigo-100"
                        onClick={() => navigate(`/crew/game/${game.gameId}`)}
                      >
                        vs {game.opponent}
                      </div>
                    ))}
                  </div>
                );
              })}
            </div>
          )}
        </div>
      </div>

      {showDeleteConfirm && (
        <div className="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center">
          <div className="bg-white rounded-lg p-6 max-w-sm mx-4">
            <h3 className="text-lg font-medium text-gray-900 mb-4">
              Confirm Delete
            </h3>
            <p className="text-sm text-gray-500 mb-4">
              Are you sure you want to delete this game? This action cannot be
              undone.
            </p>
            <div className="flex justify-end space-x-3">
              <button
                onClick={() => setShowDeleteConfirm(null)}
                className="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                disabled={isDeleting}
              >
                Cancel
              </button>
              <button
                onClick={() => handleDelete(showDeleteConfirm)}
                className="px-4 py-2 border border-transparent rounded-md text-sm font-medium text-white bg-red-600 hover:bg-red-700 disabled:opacity-50"
                disabled={isDeleting}
              >
                {isDeleting ? "Deleting..." : "Delete Game"}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Schedule;
