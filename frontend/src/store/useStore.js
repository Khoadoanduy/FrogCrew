import { create } from "zustand";
import { toast } from "react-hot-toast";

const API_URL = import.meta.env.VITE_API_URL;

export const useStore = create((set, get) => ({
  crewMembers: [],
  games: [],
  availability: [],
  currentUser: null,
  invites: [],

  // Initialize data
  initializeData: async () => {
    try {
      const [crewMembersRes, gamesRes, availabilityRes, invitesRes] =
        await Promise.all([
          fetch(`${API_URL}/crewMembers`),
          fetch(`${API_URL}/games`),
          fetch(`${API_URL}/availability`),
          fetch(`${API_URL}/invites`),
        ]);

      const crewMembers = await crewMembersRes.json();
      const games = await gamesRes.json();
      const availability = await availabilityRes.json();
      const invites = await invitesRes.json();

      set({
        crewMembers,
        games,
        availability,
        invites,
        currentUser: crewMembers[0], // Default to first user (Michala - Admin)
      });
    } catch (error) {
      console.error("Failed to initialize data:", error);
      toast.error("Failed to load data");
    }
  },

  // Invitation Management
  createInvite: async (email) => {
    try {
      const token =
        Math.random().toString(36).substring(2) + Date.now().toString(36);
      const invite = {
        id: Date.now().toString(),
        email,
        token,
        used: false,
        createdAt: new Date().toISOString(),
      };

      const response = await fetch(`${API_URL}/invites`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(invite),
      });

      const savedInvite = await response.json();
      set((state) => ({ invites: [...state.invites, savedInvite] }));
      return savedInvite;
    } catch (error) {
      toast.error("Failed to create invitation");
      throw error;
    }
  },

  validateInvite: async (token) => {
    try {
      const response = await fetch(
        `${API_URL}/invites?token=${token}&used=false`
      );
      const invites = await response.json();
      return invites.length > 0 ? invites[0] : null;
    } catch (error) {
      console.error("Failed to validate invite:", error);
      return null;
    }
  },

  markInviteUsed: async (inviteId) => {
    try {
      const response = await fetch(`${API_URL}/invites/${inviteId}`, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ used: true }),
      });
      const updatedInvite = await response.json();
      set((state) => ({
        invites: state.invites.map((invite) =>
          invite.id === inviteId ? updatedInvite : invite
        ),
      }));
    } catch (error) {
      console.error("Failed to mark invite as used:", error);
    }
  },

  // User Management
  setCurrentUser: (userId) => {
    const user = get().crewMembers.find((m) => m.userId === userId);
    if (user) {
      set({ currentUser: user });
      toast.success(`Switched to ${user.firstName}'s view (${user.role})`);
    }
  },

  // Crew Member Management
  addCrewMember: async (member) => {
    try {
      const response = await fetch(`${API_URL}/crewMembers`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(member),
      });
      const newMember = await response.json();
      set((state) => ({ crewMembers: [...state.crewMembers, newMember] }));
      return newMember;
    } catch (error) {
      toast.error("Failed to add crew member");
      throw error;
    }
  },

  removeCrewMember: async (id) => {
    const { games } = get();

    const hasUpcomingGames = games.some((game) => {
      const gameDate = new Date(game.gameDate);
      const today = new Date();
      return (
        gameDate > today &&
        game.crewedMembers.some((member) => member.userId === id)
      );
    });

    if (hasUpcomingGames) {
      toast.error("Cannot delete crew member with upcoming game assignments");
      return false;
    }

    try {
      await fetch(`${API_URL}/crewMembers/${id}`, { method: "DELETE" });
      set((state) => ({
        crewMembers: state.crewMembers.filter((m) => m.userId !== id),
      }));
      toast.success("Crew member deleted successfully");
      return true;
    } catch (error) {
      toast.error("Failed to delete crew member");
      console.error(error);
      return false;
    }
  },

  // Game Management
  addGame: async (game) => {
    try {
      if (!game.sport || !game.gameDate || !game.gameStart || !game.venue) {
        throw new Error("Missing required fields");
      }

      const newGame = {
        ...game,
        gameId: Math.max(...get().games.map((g) => g.gameId)) + 1,
        status: "DRAFT",
        crewedMembers: [],
      };

      const response = await fetch(`${API_URL}/games`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newGame),
      });

      const savedGame = await response.json();
      set((state) => ({ games: [...state.games, savedGame] }));
      toast.success("Game added successfully");
      return savedGame;
    } catch (error) {
      toast.error(error.message || "Failed to add game");
      throw error;
    }
  },

  updateGame: async (id, game) => {
    try {
      const response = await fetch(`${API_URL}/games/${id}`, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(game),
      });

      const updatedGame = await response.json();
      set((state) => ({
        games: state.games.map((g) => (g.gameId === id ? updatedGame : g)),
      }));
      toast.success("Game updated successfully");
    } catch (error) {
      toast.error("Failed to update game");
      throw error;
    }
  },

  publishGame: async (id) => {
    try {
      const game = get().games.find((g) => g.gameId === id);
      if (!game) {
        throw new Error("Game not found");
      }

      if (!game.sport || !game.gameDate || !game.gameStart || !game.venue) {
        throw new Error("Cannot publish game with missing required fields");
      }

      const response = await fetch(`${API_URL}/games/${id}`, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ...game, status: "PUBLISHED" }),
      });

      const updatedGame = await response.json();
      set((state) => ({
        games: state.games.map((g) => (g.gameId === id ? updatedGame : g)),
      }));
      toast.success("Game published successfully");
    } catch (error) {
      toast.error(error.message || "Failed to publish game");
      throw error;
    }
  },

  deleteGame: async (id) => {
    try {
      const game = get().games.find((g) => g.gameId === id);
      if (!game) {
        throw new Error("Game not found");
      }

      if (game.status === "PUBLISHED") {
        throw new Error("Cannot delete a published game");
      }

      await fetch(`${API_URL}/games/${id}`, { method: "DELETE" });
      set((state) => ({
        games: state.games.filter((g) => g.gameId !== id),
      }));
      toast.success("Game deleted successfully");
    } catch (error) {
      toast.error(error.message || "Failed to delete game");
      throw error;
    }
  },

  // Crew Assignment
  assignCrewMember: async (assignment) => {
    try {
      const { gameId, userId, position, reportTime, reportLocation } =
        assignment;

      if (!gameId || !userId || !position) {
        throw new Error("Missing required assignment details");
      }

      const crewMember = get().crewMembers.find((m) => m.userId === userId);
      if (!crewMember) {
        throw new Error("Crew member not found");
      }

      const game = get().games.find((g) => g.gameId === gameId);
      if (!game) {
        throw new Error("Game not found");
      }

      const newCrewedMember = {
        crewedUserId: Date.now(),
        userId,
        gameId,
        Position: position,
        fullName: `${crewMember.firstName} ${crewMember.lastName}`,
        ReportTime: reportTime,
        ReportLocation: reportLocation,
      };

      const updatedGame = {
        ...game,
        crewedMembers: [...game.crewedMembers, newCrewedMember],
      };

      const response = await fetch(`${API_URL}/games/${gameId}`, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updatedGame),
      });

      const savedGame = await response.json();
      set((state) => ({
        games: state.games.map((g) => (g.gameId === gameId ? savedGame : g)),
      }));

      return newCrewedMember;
    } catch (error) {
      toast.error(error.message || "Failed to assign crew member");
      throw error;
    }
  },

  // Availability Management
  submitAvailability: async (gameId, isAvailable, comment = "") => {
    const { currentUser, availability } = get();

    if (!currentUser) {
      toast.error("You must be logged in to submit availability");
      return;
    }

    const existingAvailability = availability.find(
      (a) => a.userId === currentUser.userId && a.gameId === gameId
    );

    if (existingAvailability) {
      toast.error("You have already submitted availability for this game");
      return;
    }

    try {
      const newAvailability = {
        id: Date.now().toString(),
        userId: currentUser.userId,
        gameId,
        availability: isAvailable ? 1 : 0,
        comment,
      };

      const response = await fetch(`${API_URL}/availability`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newAvailability),
      });

      if (!response.ok) {
        throw new Error("Failed to submit availability");
      }

      const savedAvailability = await response.json();
      set((state) => ({
        availability: [...state.availability, savedAvailability],
      }));

      toast.success("Availability submitted successfully");
    } catch (error) {
      console.error("Error submitting availability:", error);
      toast.error("Failed to submit availability");
    }
  },

  getGameAvailability: (gameId) => {
    const { currentUser, availability } = get();
    return currentUser
      ? availability.find(
          (a) => a.userId === currentUser.userId && a.gameId === gameId
        )
      : null;
  },
}));
