import { store, findAndRemove } from "../store/data";
import { updateDbFile } from "./fileOperations";

export const getCrewMembers = async () => {
  try {
    return {
      flag: true,
      code: 200,
      message: "Find Success",
      data: store.crewmember,
    };
  } catch (error) {
    return {
      flag: false,
      code: 500,
      message: "Internal server error",
      data: null,
    };
  }
};

export const createCrewMember = async (token, crewMemberData) => {
  try {
    const newUserId = Date.now();
    const newCrewMember = {
      ...crewMemberData,
      userId: newUserId,
      role: "CREW_MEMBER",
    };

    store.crewmember.push(newCrewMember);
    store.users.push({
      userId: newUserId,
      email: crewMemberData.email,
      password: crewMemberData.password,
      role: "CREW_MEMBER",
    });

    const success = await updateDbFile(store);
    if (!success) {
      throw new Error("Failed to update db.json file");
    }

    return {
      flag: true,
      code: 200,
      message: "Add Success",
      data: newCrewMember,
    };
  } catch (error) {
    return {
      flag: false,
      code: 500,
      message: "Internal server error",
      data: null,
    };
  }
};

export const deleteCrewMember = async (userId) => {
  try {
    const parsedUserId = parseInt(userId);
    const hasUpcomingGames = store.gameSchedule.games.some((game) => {
      return game.crewedMembers.some(
        (member) => member.userId === parsedUserId
      );
    });

    if (hasUpcomingGames) {
      return {
        flag: false,
        code: 409,
        message: "Cannot delete: Crew member has upcoming scheduled games",
        data: null,
      };
    }

    const removed = findAndRemove(store.crewmember, parsedUserId);
    if (!removed) {
      return {
        flag: false,
        code: 404,
        message: "Crew member not found",
        data: null,
      };
    }

    findAndRemove(store.users, parsedUserId);
    return {
      flag: true,
      code: 200,
      message: "Delete Success",
      data: null,
    };
  } catch (error) {
    return {
      flag: false,
      code: 500,
      message: "Internal server error",
      data: null,
    };
  }
};
