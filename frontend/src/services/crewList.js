import axios from "axios";

const API_URL = import.meta.env.VITE_API_URL;

export const getGameCrewList = async (gameId) => {
  try {
    const response = await axios.get(`${API_URL}/crewlist/${gameId}`);
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
