import axios from 'axios';
import crewData from '../../db.json';

const API_URL = import.meta.env.VITE_API_URL;

export const getCrewMember = async (userId) => {
  try {
    const response = await axios.get(`${API_URL}/crewmember/${userId}`);
    return {
      flag: true,
      code: 200,
      message: "Find Success",
      data: response.data
    };
  } catch (error) {
    if (error.response?.status === 404) {
      return {
        flag: false,
        code: 404,
        message: `Could not find user with id ${userId}`,
        data: null
      };
    }
    return {
      flag: false,
      code: error.response?.status || 500,
      message: error.response?.data?.message || "Internal server error",
      data: null
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

export const deleteCrewMember = async (userId) => {
  try {
    const response = await axios.delete(`${API_URL}/template/${userId}`);
    return {
      flag: true,
      code: 200,
      message: "Delete Success",
      data: null
    };
  } catch (error) {
    if (error.response?.status === 404) {
      return {
        flag: false,
        code: 404,
        message: "Crew member not found",
        data: null
      };
    }
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
export const getCrewMember = async (userId) => {
  try {
    const member = crewData.crewmember.find(m => m.userId === parseInt(userId));
    if (!member) {
      return {
        flag: false,
        code: 404,
        message: `Could not find user with id ${userId}`,
        data: null
      };
    }
    return {
      flag: true,
      code: 200,
      message: "Find Success",
      data: member
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

export const createCrewMember = async (token, crewMemberData) => {
  try {
    const invitation = crewData.invitations.find(inv => inv.token === token);
    if (!invitation) {
      return {
        flag: false,
        code: 401,
        message: "Invalid or expired invitation token",
        data: null
      };
    }

    const newUserId = crewData.users.length + 1;
    const newUser = {
      userId: newUserId,
      email: crewMemberData.email,
      password: crewMemberData.password,
      role: "CREW_MEMBER"
    };

    const newCrewMember = {
      userId: newUserId,
      ...crewMemberData,
      role: "CREW_MEMBER"
    };

    crewData.users.push(newUser);
    crewData.crewmember.push(newCrewMember);

    return {
      flag: true,
      code: 200,
      message: "Add Success",
      data: newCrewMember
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

export const deleteCrewMember = async (userId) => {
  try {
    const memberIndex = crewData.crewmember.findIndex(m => m.userId === userId);
    if (memberIndex === -1) {
      return {
        flag: false,
        code: 404,
        message: "Crew member not found",
        data: null
      };
    }

    const hasUpcomingGames = crewData.gameSchedule.games.some(game => {
      const gameDate = new Date(game.gameDate);
      const today = new Date();
      return gameDate > today && game.crewedMembers.some(crew => crew.userId === userId);
    });

    if (hasUpcomingGames) {
      return {
        flag: false,
        code: 409,
        message: "Cannot delete: Crew member has upcoming games",
        data: null
      };
    }

    crewData.crewmember.splice(memberIndex, 1);
    const userIndex = crewData.users.findIndex(u => u.userId === userId);
    if (userIndex !== -1) {
      crewData.users.splice(userIndex, 1);
    }

    return {
      flag: true,
      code: 200,
      message: "Delete Success",
      data: null
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