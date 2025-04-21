import { store } from '../store/data';

export const getGames = async () => {
  try {
    return {
      flag: true,
      code: 200,
      message: "Find Success",
      data: store.gameSchedule.games
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

export const createSchedule = async (scheduleData) => {
  try {
    const newSchedule = {
      id: Date.now(),
      ...scheduleData,
      games: []
    };
    
    store.schedules.push(newSchedule);
    return {
      flag: true,
      code: 200,
      message: "Add Success",
      data: newSchedule
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

export const addGameToSchedule = async (scheduleId, gameData) => {
  try {
    const newGame = {
      ...gameData,
      gameId: Date.now(),
      scheduleId: parseInt(scheduleId),
      crewedMembers: []
    };

    store.gameSchedule.games.push(newGame);
    return {
      flag: true,
      code: 200,
      message: "Add Success",
      data: newGame
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