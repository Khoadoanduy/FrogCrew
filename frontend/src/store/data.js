import { reactive } from "vue";
import { getCrewMember } from "../services/crewMember";
import { getAvailableCrewMembers } from "../services/api";

// Initialize empty data structure
const defaultData = {
  users: [],
  crewmember: [],
  gameSchedule: {
    games: [],
  },
  crewlist: {},
  availability: [],
  invitations: [],
  CrewedUser: {},
  schedules: [],
};

// Create a reactive store
export const store = reactive({
  ...defaultData,
});

// Helper function to find and update items in arrays
export const findAndUpdate = (array, id, updates) => {
  const index = array.findIndex((item) => item.userId === id);
  if (index !== -1) {
    array[index] = { ...array[index], ...updates };
    return true;
  }
  return false;
};

// Helper function to find and remove items from arrays
export const findAndRemove = (array, id) => {
  const index = array.findIndex((item) => item.userId === id);
  if (index !== -1) {
    array.splice(index, 1);
    return true;
  }
  return false;
};

// Load crew member data from API
export const loadCrewMember = async (userId) => {
  const response = await getCrewMember(userId);
  if (response.flag) {
    store.crewmember = response.data;
  }
  return response;
};

// Load available crew members for a game
export const loadAvailableCrewMembers = async (gameId, position) => {
  const response = await getAvailableCrewMembers(gameId, position);
  if (response.flag) {
    store.CrewedUser = response.data;
  }
  return response;
};
