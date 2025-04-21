import axios from "axios";
import crewData from "../../db.json";

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

// Used for local db
/*
export const getGameCrewList = async (gameId) => {
  try {
    const crewList = crewData.crewlist[gameId];
    if (!crewList) {
      return {
        flag: false,
        code: 404,
        message: "Crew list not found",
        data: null
      };
    }
    return {
      flag: true,
      code: 200,
      message: "Find Success",
      data: crewList
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
