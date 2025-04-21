import axios from 'axios';
import crewData from '../../db.json';

const API_URL = import.meta.env.VITE_API_URL;

export const sendInvitations = async (emails) => {
  try {
    const response = await axios.post(`${API_URL}/invite`, { emails });
    return {
      flag: true,
      code: 200,
      message: "Invitations sent successfully",
      data: response.data
    };
  } catch (error) {
    return {
      flag: false,
      code: error.response?.status || 500,
      message: error.response?.data?.message || "Internal server error",
      data: null
    };
  }
};

// Used for local db
/*
export const sendInvitations = async (emails) => {
  try {
    const existingEmails = crewData.users.map(u => u.email);
    const duplicateEmails = emails.filter(email => existingEmails.includes(email));

    if (duplicateEmails.length > 0) {
      return {
        flag: false,
        code: 409,
        message: "Some email addresses are already registered",
        data: null
      };
    }

    const invitations = emails.map(email => ({
      email,
      token: Math.random().toString(36).substring(2),
      createdAt: new Date().toISOString()
    }));

    crewData.invitations.push(...invitations);
    return {
      flag: true,
      code: 200,
      message: "Invitations sent successfully",
      data: invitations
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