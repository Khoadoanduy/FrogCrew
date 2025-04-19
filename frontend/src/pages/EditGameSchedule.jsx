import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useStore } from "../store/useStore";
import { toast } from "react-hot-toast";

function EditGameSchedule() {
  const { gameId } = useParams();
  const navigate = useNavigate();
  const { games, updateGame } = useStore();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [formData, setFormData] = useState({
    sport: "",
    gameDate: "",
    gameStart: "",
    venue: "",
    opponent: "",
    requiredPositions: [
      "PRODUCER",
      "DIRECTOR",
      "TECHNICAL_DIR",
      "AUDIO",
      "CAMERA",
    ],
  });

  useEffect(() => {
    const game = games.find((g) => g.gameId === parseInt(gameId));
    if (!game) {
      toast.error("Game not found");
      navigate("/schedule");
      return;
    }
    setFormData({
      sport: game.sport || "",
      gameDate: game.gameDate || "",
      gameStart: game.gameStart || "",
      venue: game.venue || "",
      opponent: game.opponent || "",
      requiredPositions: game.requiredPositions || [
        "PRODUCER",
        "DIRECTOR",
        "TECHNICAL_DIR",
        "AUDIO",
        "CAMERA",
      ],
    });
  }, [gameId, games, navigate]);

  const sportOptions = [
    "Football",
    "Basketball",
    "Baseball",
    "Soccer",
    "Volleyball",
    "Other",
  ];

  const commonVenues = [
    "Amon G. Carter Stadium",
    "Schollmaier Arena",
    "Lupton Stadium",
    "Garvey-Rosenthal Soccer Stadium",
  ];

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      if (
        !formData.sport ||
        !formData.gameDate ||
        !formData.gameStart ||
        !formData.venue
      ) {
        throw new Error("Please fill in all required fields");
      }

      await updateGame(parseInt(gameId), formData);
      toast.success("Game schedule updated successfully");
      navigate("/schedule");
    } catch (error) {
      toast.error(error.message || "Failed to update game schedule");
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  return (
    <div className="space-y-6">
      <header className="flex items-center justify-between">
        <h1 className="text-3xl font-bold text-gray-900">Edit Game Schedule</h1>
      </header>

      <div className="bg-white shadow rounded-lg">
        <form onSubmit={handleSubmit} className="space-y-6 p-6">
          <div className="grid grid-cols-1 gap-6 sm:grid-cols-2">
            <div>
              <label
                htmlFor="sport"
                className="block text-sm font-medium text-gray-700"
              >
                Sport Type *
              </label>
              <select
                id="sport"
                name="sport"
                value={formData.sport}
                onChange={handleChange}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                required
              >
                <option value="">Select a sport</option>
                {sportOptions.map((sport) => (
                  <option key={sport} value={sport}>
                    {sport}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label
                htmlFor="gameDate"
                className="block text-sm font-medium text-gray-700"
              >
                Game Date *
              </label>
              <div className="mt-1 relative rounded-md shadow-sm">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none"></div>
                <input
                  type="date"
                  id="gameDate"
                  name="gameDate"
                  value={formData.gameDate}
                  onChange={handleChange}
                  className="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  required
                />
              </div>
            </div>

            <div>
              <label
                htmlFor="gameStart"
                className="block text-sm font-medium text-gray-700"
              >
                Game Time *
              </label>
              <div className="mt-1 relative rounded-md shadow-sm">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none"></div>
                <input
                  type="time"
                  id="gameStart"
                  name="gameStart"
                  value={formData.gameStart}
                  onChange={handleChange}
                  className="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  required
                />
              </div>
            </div>

            <div>
              <label
                htmlFor="venue"
                className="block text-sm font-medium text-gray-700"
              >
                Venue *
              </label>
              <div className="mt-1 space-y-2">
                <div className="relative rounded-md shadow-sm">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none"></div>
                  <input
                    type="text"
                    id="venue"
                    name="venue"
                    value={formData.venue}
                    onChange={handleChange}
                    className="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                    placeholder="Enter or select venue"
                    required
                    list="venue-suggestions"
                  />
                  <datalist id="venue-suggestions">
                    {commonVenues.map((venue) => (
                      <option key={venue} value={venue} />
                    ))}
                  </datalist>
                </div>
                {commonVenues.length > 0 && (
                  <div className="flex flex-wrap gap-2">
                    {commonVenues.map((venue) => (
                      <button
                        key={venue}
                        type="button"
                        onClick={() =>
                          setFormData((prev) => ({ ...prev, venue }))
                        }
                        className="inline-flex items-center px-2.5 py-1.5 border border-gray-300 shadow-sm text-xs font-medium rounded text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                      >
                        {venue}
                      </button>
                    ))}
                  </div>
                )}
              </div>
            </div>

            <div>
              <label
                htmlFor="opponent"
                className="block text-sm font-medium text-gray-700"
              >
                Opponent
              </label>
              <input
                type="text"
                id="opponent"
                name="opponent"
                value={formData.opponent}
                onChange={handleChange}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                placeholder="Enter opponent name"
              />
            </div>
          </div>

          <div className="flex items-center justify-end space-x-3">
            <button
              type="button"
              onClick={() => navigate("/schedule")}
              className="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={isSubmitting}
              className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50"
            >
              {isSubmitting ? "Updating..." : "Update Schedule"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default EditGameSchedule;
