import { store } from '../store/data';

export const submitAvailability = async (availabilityData) => {
  try {
    store.availability.push(availabilityData);
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