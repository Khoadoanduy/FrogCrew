import axios from "axios";

const API_URL = import.meta.env.VITE_API_URL;

export const getCrewMember = async (userId) => {
  try {
    const response = await axios.get(`${API_URL}/crewmember/${userId}`);
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
        message: `Could not find user with id ${userId}`,
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

export const createCrewMember = async (token, crewMemberData) => {
  try {
    const response = await axios.post(`${API_URL}/crewmember`, crewMemberData);
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

export const deleteCrewMember = async (userId) => {
  try {
    const response = await axios.delete(`${API_URL}/template/${userId}`);
    return {
      flag: true,
      code: 200,
      message: "Delete Success",
      data: null,
    };
  } catch (error) {
    if (error.response?.status === 404) {
      return {
        flag: false,
        code: 404,
        message: "Crew member not found",
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
