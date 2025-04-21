import axios from 'axios';
import crewData from '../../db.json';

const API_URL = import.meta.env.VITE_API_URL;

export const getGames = async () => {
  try {
    const response = await axios.get(`${API_URL}/gameSchedule/games`);
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

export const createSchedule = async (scheduleData) => {
  try {
    const response = await axios.post(`${API_URL}/gameSchedule`, scheduleData);
    return {
      flag: true,
      code: 200,
      message: "Add Success",
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

export const addGameToSchedule = async (scheduleId, gameData) => {
  try {
    const response = await axios.post(`${API_URL}/gameSchedule/${scheduleId}/games`, gameData);
    return {
      flag: true,
      code: 200,
      message: "Add Success",
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
export const getGames = async () => {
  try {
    return {
      flag: true,
      code: 200,
      message: "Find Success",
      data: crewData.gameSchedule.games
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

export const createSchedule = async (scheduleData) => {
  try {
    const newSchedule = {
      id: crewData.schedules.length + 1,
      ...scheduleData,
      games: []
    };
    crewData.schedules.push(newSchedule);
    return {
      flag: true,
      code: 200,
      message: "Add Success",
      data: newSchedule
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

export const addGameToSchedule = async (scheduleId, gameData) => {
  try {
    const schedule = crewData.schedules.find(s => s.id === scheduleId);
    if (!schedule) {
      return {
        flag: false,
        code: 404,
        message: "Schedule not found",
        data: null
      };
    }

    const newGame = {
      gameId: Date.now(),
      ...gameData,
      crewedMembers: []
    };
    schedule.games.push(newGame);
    return {
      flag: true,
      code: 200,
      message: "Add Success",
      data: newGame
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