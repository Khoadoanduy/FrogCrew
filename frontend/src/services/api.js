import axios from 'axios';
import crewData from '../../db.json';

const API_URL = import.meta.env.VITE_API_URL;

export const getAvailableCrewMembers = async (gameId, position) => {
  try {
    const response = await axios.get(`${API_URL}/CrewedUser/${gameId}/${position}`);
    return {
      flag: true,
      code: 200,
      message: "Find Success",
      data: response.data
    };
  } catch (error) {
    return {
      flag: false,
      code: error.response?.status || 500,
      message: error.response?.data?.message || "Internal server error",
      data: null
    };
  }
};

export const addCrewSchedule = async (gameId, assignments) => {
  try {
    const response = await axios.post(`${API_URL}/crewSchedule/${gameId}`, assignments);
    return {
      flag: true,
      code: 200,
      message: "Crew scheduled successfully",
      data: response.data
    };
  } catch (error) {
    return {
      flag: false,
      code: error.response?.status || 500,
      message: error.response?.data?.message || "Internal server error",
      data: null
    };
  }
};

// Used for local db
/*
export const getAvailableCrewMembers = async (gameId, position) => {
  try {
    const crewedUsers = crewData.CrewedUser[gameId]?.[position] || [];
    return {
      flag: true,
      code: 200,
      message: "Find Success",
      data: crewedUsers
    };
  } catch (error) {
    return {
      flag: false,
      code: 500,
      message: "Internal server error",
      data: null
    };
  }
};

export const addCrewSchedule = async (gameId, assignments) => {
  try {
    const game = crewData.gameSchedule.games.find(g => g.gameId === gameId);
    if (!game) {
      return {
        flag: false,
        code: 404,
        message: "Game not found",
        data: null
      };
    }

    assignments.forEach(assignment => {
      game.crewedMembers.push({
        crewedUserId: Date.now() + Math.floor(Math.random() * 1000),
        ...assignment
      });
    });

    return {
      flag: true,
      code: 200,
      message: "Crew scheduled successfully",
      data: game
    };
  } catch (error) {
    return {
      flag: false,
      code: 500,
      message: "Internal server error",
      data: null
    };
  }
};
*/