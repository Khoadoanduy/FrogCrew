import { store } from '../store/data';

export const getCrewMember = async (userId) => {
  try {
    const member = store.crewmember.find(m => m.userId === parseInt(userId));
    if (member) {
      return {
        flag: true,
        code: 200,
        message: 'Find Success',
        data: member
      };
    }
    return {
      flag: false,
      code: 404,
      message: 'Crew member not found',
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

export const getAvailableCrewMembers = async (gameId, position) => {
  try {
    // Get all crew members who are qualified for the position
    const qualifiedMembers = store.crewmember.filter(member => 
      member.positions.includes(position) &&
      member.role === 'CREW_MEMBER'
    );

    // Map them to the required format
    const availableMembers = qualifiedMembers.map(member => ({
      userId: member.userId,
      fullName: `${member.firstName} ${member.lastName}`
    }));

    if (availableMembers.length > 0) {
      return {
        flag: true,
        code: 200,
        message: "Find Success",
        data: availableMembers
      };
    }
    return {
      flag: false,
      code: 404,
      message: `No matching crew members available for ${position}`,
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

export const addCrewSchedule = async (gameId, assignments) => {
  try {
    const game = store.gameSchedule.games.find(g => g.gameId === parseInt(gameId));
    if (!game) {
      return {
        flag: false,
        code: 404,
        message: "Game not found",
        data: null
      };
    }

    // Process each assignment
    assignments.forEach(assignment => {
      const crewMember = store.crewmember.find(m => m.userId === assignment.userId);
      if (crewMember) {
        const crewedUserId = Date.now() + Math.floor(Math.random() * 1000);
        
        // Create the crew assignment
        const crewAssignment = {
          crewedUserId,
          userId: assignment.userId,
          gameId: parseInt(gameId),
          Position: assignment.position,
          fullName: `${crewMember.firstName} ${crewMember.lastName}`,
          ReportTime: assignment.reportTime,
          ReportLocation: assignment.reportLocation
        };

        // Update gameSchedule.games
        game.crewedMembers.push(crewAssignment);

        // Update crewlist
        if (!store.crewlist[gameId]) {
          store.crewlist[gameId] = {
            gameId: parseInt(gameId),
            gameStart: game.gameStart,
            gameDate: game.gameDate,
            venue: game.venue,
            opponent: game.opponent,
            crewedMembers: []
          };
        }
        store.crewlist[gameId].crewedMembers.push(crewAssignment);

        // Update CrewedUser
        if (!store.CrewedUser[gameId]) {
          store.CrewedUser[gameId] = {};
        }
        if (!store.CrewedUser[gameId][assignment.position]) {
          store.CrewedUser[gameId][assignment.position] = [];
        }
        store.CrewedUser[gameId][assignment.position].push({
          userId: assignment.userId,
          fullName: `${crewMember.firstName} ${crewMember.lastName}`
        });
      }
    });

    return {
      flag: true,
      code: 200,
      message: "Crew scheduled successfully",
      data: game
    };
  } catch (error) {
    console.error('Error in addCrewSchedule:', error);
    return {
      flag: false,
      code: 500,
      message: "Internal server error",
      data: null
    };
  }
};