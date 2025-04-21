import { reactive } from 'vue';
import initialData from '../../db.json';

// Initialize missing arrays/objects if they don't exist in initial data
const defaultData = {
  users: [],
  crewmember: [],
  gameSchedule: {
    games: []
  },
  crewlist: {},
  availability: [],
  invitations: [],
  CrewedUser: {},
  schedules: []  // Add schedules array
};

// Create a reactive copy of the initial data with defaults
export const store = reactive({
  ...defaultData,
  ...initialData
});

// Helper function to find and update items in arrays
export const findAndUpdate = (array, id, updates) => {
  const index = array.findIndex(item => item.userId === id);
  if (index !== -1) {
    array[index] = { ...array[index], ...updates };
    return true;
  }
  return false;
};

// Helper function to find and remove items from arrays
export const findAndRemove = (array, id) => {
  const index = array.findIndex(item => item.userId === id);
  if (index !== -1) {
    array.splice(index, 1);
    return true;
  }
  return false;
};