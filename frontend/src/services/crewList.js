import { store } from '../store/data';

export const getGameCrewList = async (gameId) => {
  try {
    const crewList = store.crewlist[gameId];
    if (crewList) {
      return {
        flag: true,
        code: 200,
        message: "Find Success",
        data: crewList
      };
    }
    return {
      flag: false,
      code: 404,
      message: "Crew list not found",
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