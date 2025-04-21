import axios from "axios";
import crewData from "../../db.json";

const API_URL = import.meta.env.VITE_API_URL;

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

// Used for local db
/*
export const submitAvailability = async (availabilityData) => {
  try {
    const existingAvailability = crewData.availability.find(
      a => a.userId === availabilityData.userId && a.gameId === availabilityData.gameId
    );

    if (existingAvailability) {
      return {
        flag: false,
        code: 409,
        message: "Availability already submitted for this game",
        data: null
      };
    }

    crewData.availability.push(availabilityData);
    return {
      flag: true,
      code: 200,
      message: "Availability submitted successfully",
      data: availabilityData
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
