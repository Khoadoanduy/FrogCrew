import axios from "axios";

const API_URL = import.meta.env.VITE_API_URL;

export const getGameCrewList = async (gameId) => {
  try {
    const response = await axios.get(`${API_URL}/crewList/${gameId}`);
    return {
      flag: true,
      code: 200,
      message: "Find Success",
      data: response.data,
    };
  } catch (error) {
    if (error.response?.status === 404) {
      return {
        flag: false,
        code: 404,
        message: `Could not find game with id ${gameId}`,
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
