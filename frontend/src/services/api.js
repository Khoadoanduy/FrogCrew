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
    // Format assignments to match API specification
    const formattedAssignments = assignments.map((assignment) => ({
      crewedUserId: 0, // API will assign this
      userId: assignment.userId,
      gameId: assignment.gameId,
      position: assignment.position,
    }));

    const response = await axios.post(
      `${API_URL}/crewSchedule/${gameId}`,
      formattedAssignments
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
