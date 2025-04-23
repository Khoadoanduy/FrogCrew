import axios from "axios";

const API_URL = import.meta.env.VITE_API_URL;

export const getAvailability = async (gameId, position) => {
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
    if (error.response?.status === 404) {
      return {
        flag: false,
        code: 404,
        message: `No matching crew members available for ${position}`,
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

export const submitAvailability = async (availabilityData) => {
  try {
    const response = await axios.post(
      `${API_URL}/availability`,
      availabilityData
    );
    return {
      flag: true,
      code: 200,
      message: "Availability submitted successfully",
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
