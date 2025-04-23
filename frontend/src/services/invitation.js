import axios from "axios";

const API_URL = import.meta.env.VITE_API_URL;

export const sendInvitations = async (emails) => {
  try {
    const response = await axios.post(`${API_URL}/invite`, { emails });
    return {
      flag: true,
      code: 200,
      message: "Invitations sent successfully",
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
