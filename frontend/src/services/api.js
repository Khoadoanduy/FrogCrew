import axios from "axios";

const API_URL = import.meta.env.VITE_API_URL;

export const getAvailableCrewMembers = async (gameId, position) => {
  try {
    const response = await axios.get(
      `${API_URL}/CrewedUser/${gameId}/${position}`
    );
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

export const addCrewSchedule = async (gameId, assignments) => {
  try {
    const response = await axios.post(
      `${API_URL}/crewSchedule/${gameId}`,
      assignments
    );
    return {
      flag: true,
      code: 200,
      message: "Crew scheduled successfully",
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
