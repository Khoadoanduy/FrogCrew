import axios from "axios";
import fs from "fs";
import path from "path";

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
    // Generate random positions if none provided
    if (!crewMemberData.positions || crewMemberData.positions.length === 0) {
      const availablePositions = [
        "Producer",
        "Asst Prod",
        "Director",
        "Asst Director",
        "Technical Dir",
        "Graphics",
        "Bug Op",
        "Replay EVS",
        "EIC",
        "Video",
        "Audio",
        "Camera",
        "Utility",
        "Tech Manager",
        "TOC",
        "Observer",
      ];
      crewMemberData.positions = getRandomPositions(availablePositions);
    }

    // Set role to CREW_MEMBER if not specified
    if (!crewMemberData.role) {
      crewMemberData.role = "CREW_MEMBER";
    }

    // Generate a unique userId
    crewMemberData.userId = Date.now();

    // Make API call
    const response = await axios.post(`${API_URL}/crewmember`, crewMemberData);

    if (response.flag) {
      // Update db.json
      const dbUpdated = updateDbJson(crewMemberData);
      if (!dbUpdated) {
        console.warn("Failed to update db.json, but API call was successful");
      }
    }

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

// Helper function to get random positions
const getRandomPositions = (availablePositions) => {
  const numPositions = Math.floor(Math.random() * 3) + 1; // 1-3 positions
  const shuffled = [...availablePositions].sort(() => 0.5 - Math.random());
  return shuffled.slice(0, numPositions);
};

// Helper function to update db.json
const updateDbJson = (newUser) => {
  try {
    const dbPath = path.resolve(__dirname, "../../db.json");
    const dbData = JSON.parse(fs.readFileSync(dbPath, "utf8"));

    // Add to users array
    dbData.users.push({
      userId: newUser.userId,
      email: newUser.email,
      password: newUser.password,
    });

    // Add to crewmember array
    dbData.crewmember.push(newUser);

    // Write back to file
    fs.writeFileSync(dbPath, JSON.stringify(dbData, null, 2));
    return true;
  } catch (error) {
    console.error("Error updating db.json:", error);
    return false;
  }
};
