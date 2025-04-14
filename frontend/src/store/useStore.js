import { create } from 'zustand';
import { toast } from 'react-hot-toast';

const mockData = {
  crewMembers: [
    {
      userId: '1',
      firstName: 'Michala',
      lastName: 'Rogers',
      email: 'michala.rogers@example.com',
      phoneNumber: '1234567890',
      role: 'ADMIN',
      positions: ['DIRECTOR', 'PRODUCER', 'TECHNICAL_DIR'],
      experience: 'senior'
    },
    {
      userId: '2',
      firstName: 'Jane',
      lastName: 'Smith',
      email: 'jane.smith@example.com',
      phoneNumber: '0987654321',
      role: 'CREW',
      positions: ['CAMERA', 'UTILITY'],
      experience: 'experienced'
    },
    {
      userId: '3',
      firstName: 'Bob',
      lastName: 'Johnson',
      email: 'bob.johnson@example.com',
      phoneNumber: '5556667777',
      role: 'CREW',
      positions: ['AUDIO', 'VIDEO'],
      experience: 'new'
    }
  ],
  games: [
    {
      gameId: 1,
      gameStart: "12:00",
      gameDate: "2024-09-07",
      venue: "Amon G. Carter Stadium",
      opponent: "Texas Longhorns",
      sport: "Football",
      status: "PUBLISHED",
      crewedMembers: [
        {
          crewedUserId: 1,
          userId: "1",
          gameId: 1,
          Position: "DIRECTOR",
          fullName: "Michala Rogers",
          ReportTime: "10:00",
          ReportLocation: "CONTROL ROOM"
        }
      ]
    },
    {
      gameId: 2,
      gameStart: "15:30",
      gameDate: "2024-09-14",
      venue: "Schollmaier Arena",
      opponent: "UCF",
      sport: "Basketball",
      status: "DRAFT",
      crewedMembers: []
    }
  ],
  availability: [
    {
      userId: '1',
      gameId: 1,
      availability: 1,
      comment: "Available all day"
    },
    {
      userId: '2',
      gameId: 1,
      availability: 0,
      comment: "Out of town"
    }
  ]
};

export const useStore = create((set, get) => ({
  crewMembers: mockData.crewMembers,
  games: mockData.games,
  availability: mockData.availability,
  schedules: [],
  currentUser: mockData.crewMembers[0], // Default to Michala (Admin)

  // User Management
  setCurrentUser: (userId) => {
    const user = mockData.crewMembers.find(m => m.userId === userId);
    if (user) {
      set({ currentUser: user });
      toast.success(`Switched to ${user.firstName}'s view (${user.role})`);
    }
  },

  // Crew Member Management
  setCrewMembers: (members) => set({ crewMembers: members }),
  addCrewMember: (member) =>
    set((state) => ({ crewMembers: [...state.crewMembers, member] })),
  removeCrewMember: async (id) => {
    const { games } = get();
    
    const hasUpcomingGames = games.some(game => {
      const gameDate = new Date(game.gameDate);
      const today = new Date();
      return gameDate > today && game.crewedMembers.some(member => member.userId === id);
    });

    if (hasUpcomingGames) {
      toast.error('Cannot delete crew member with upcoming game assignments');
      return false;
    }

    try {
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      set((state) => ({
        crewMembers: state.crewMembers.filter((m) => m.userId !== id),
      }));
      
      toast.success('Crew member deleted successfully');
      return true;
    } catch (error) {
      toast.error('Failed to delete crew member');
      console.error(error);
      return false;
    }
  },
  updateCrewMember: (id, member) =>
    set((state) => ({
      crewMembers: state.crewMembers.map((m) =>
        m.userId === id ? { ...m, ...member } : m
      ),
    })),

  // Game Management
  setGames: (games) => set({ games }),
  addGame: async (game) => {
    try {
      if (!game.sport || !game.gameDate || !game.gameStart || !game.venue) {
        throw new Error('Missing required fields');
      }

      await new Promise(resolve => setTimeout(resolve, 1000));

      const newGame = {
        ...game,
        gameId: Math.max(...get().games.map(g => g.gameId)) + 1,
        status: 'DRAFT',
        crewedMembers: []
      };

      set((state) => ({ 
        games: [...state.games, newGame]
      }));

      toast.success('Game added successfully');
      return newGame;
    } catch (error) {
      toast.error(error.message || 'Failed to add game');
      throw error;
    }
  },
  updateGame: async (id, game) => {
    try {
      await new Promise(resolve => setTimeout(resolve, 1000));

      set((state) => ({
        games: state.games.map((g) => (g.gameId === id ? { ...g, ...game } : g)),
      }));

      toast.success('Game updated successfully');
    } catch (error) {
      toast.error('Failed to update game');
      throw error;
    }
  },
  publishGame: async (id) => {
    try {
      const game = get().games.find(g => g.gameId === id);
      if (!game) {
        throw new Error('Game not found');
      }

      if (!game.sport || !game.gameDate || !game.gameStart || !game.venue) {
        throw new Error('Cannot publish game with missing required fields');
      }

      await new Promise(resolve => setTimeout(resolve, 1000));

      set((state) => ({
        games: state.games.map((g) => 
          g.gameId === id ? { ...g, status: 'PUBLISHED' } : g
        ),
      }));

      toast.success('Game published successfully');
    } catch (error) {
      toast.error(error.message || 'Failed to publish game');
      throw error;
    }
  },
  deleteGame: async (id) => {
    try {
      const game = get().games.find(g => g.gameId === id);
      if (!game) {
        throw new Error('Game not found');
      }

      if (game.status === 'PUBLISHED') {
        throw new Error('Cannot delete a published game');
      }

      await new Promise(resolve => setTimeout(resolve, 1000));

      set((state) => ({
        games: state.games.filter((g) => g.gameId !== id),
      }));

      toast.success('Game deleted successfully');
    } catch (error) {
      toast.error(error.message || 'Failed to delete game');
      throw error;
    }
  },

  // Crew Assignment
  assignCrewMember: async (assignment) => {
    try {
      const { gameId, userId, position, reportTime, reportLocation } = assignment;
      
      if (!gameId || !userId || !position) {
        throw new Error('Missing required assignment details');
      }

      await new Promise(resolve => setTimeout(resolve, 1000));

      const crewMember = get().crewMembers.find(m => m.userId === userId);
      if (!crewMember) {
        throw new Error('Crew member not found');
      }

      const newCrewedMember = {
        crewedUserId: Date.now(),
        userId,
        gameId,
        Position: position,
        fullName: `${crewMember.firstName} ${crewMember.lastName}`,
        ReportTime: reportTime,
        ReportLocation: reportLocation
      };

      set((state) => ({
        games: state.games.map((game) => {
          if (game.gameId === gameId) {
            return {
              ...game,
              crewedMembers: [...game.crewedMembers, newCrewedMember]
            };
          }
          return game;
        })
      }));

      return newCrewedMember;
    } catch (error) {
      toast.error(error.message || 'Failed to assign crew member');
      throw error;
    }
  },
  removeCrewAssignment: async (gameId, userId) => {
    try {
      await new Promise(resolve => setTimeout(resolve, 1000));

      set((state) => ({
        games: state.games.map((game) => {
          if (game.gameId === gameId) {
            return {
              ...game,
              crewedMembers: game.crewedMembers.filter(
                member => member.userId !== userId
              )
            };
          }
          return game;
        })
      }));

      return true;
    } catch (error) {
      toast.error('Failed to remove crew assignment');
      throw error;
    }
  },

  // Availability Management
  submitAvailability: async (gameId, isAvailable, comment = "") => {
    const { currentUser, availability } = get();
    
    const existingAvailability = availability.find(
      a => a.userId === currentUser.userId && a.gameId === gameId
    );

    if (existingAvailability) {
      toast.error("You have already submitted availability for this game");
      return;
    }

    try {
      const newAvailability = {
        userId: currentUser.userId,
        gameId,
        availability: isAvailable ? 1 : 0,
        comment
      };

      set((state) => ({
        availability: [...state.availability, newAvailability]
      }));

      toast.success("Availability submitted successfully");
    } catch (error) {
      toast.error("Failed to submit availability");
      console.error(error);
    }
  },
  getGameAvailability: (gameId) => {
    const { currentUser, availability } = get();
    return availability.find(
      a => a.userId === currentUser.userId && a.gameId === gameId
    );
  },

  // Other Management
  setSchedules: (schedules) => set({ schedules })
}));