import axios from "axios";

const API_URL = import.meta.env.VITE_API_URL;

export const getGames = async () => {
  try {
    const response = await axios.get(`${API_URL}/gameSchedule/games`);
    return {
      flag: true,
      code: 200,
      message: "Find Success",
      data: response.data,
    };
  } catch (error) {
    return {
      flag: false,
      code: error.response?.status || 500,
      message: error.response?.data?.message || "Internal server error",
      data: null,
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
      data: response.data,
    };
  } catch (error) {
    return {
      flag: false,
      code: error.response?.status || 500,
      message: error.response?.data?.message || "Internal server error",
      data: null,
    };
  }
};

export const addGameToSchedule = async (scheduleId, gameData) => {
  try {
    const response = await axios.post(
      `${API_URL}/gameSchedule/${scheduleId}/games`,
      gameData
    );
    return {
      flag: true,
      code: 200,
      message: "Add Success",
      data: response.data,
    };
  } catch (error) {
    if (error.response?.status === 400) {
      return {
        flag: false,
        code: 400,
        message: "Provided arguments are invalid, see data for details.",
        data: error.response.data,
      };
    }
    if (error.response?.status === 404) {
      return {
        flag: false,
        code: 404,
        message: `Could not find schedule with id ${scheduleId}`,
        data: null,
      };
    }
    return {
      flag: false,
      code: error.response?.status || 500,
      message: error.response?.data?.message || "Internal server error",
      data: null,
    };
  }
};
