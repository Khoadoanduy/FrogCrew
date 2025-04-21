<script setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import { store } from "../store/data";
import { createSchedule, addGameToSchedule } from "../services/gameSchedule";

const router = useRouter();
const showScheduleForm = ref(false);
const showGameForm = ref(false);
const selectedSchedule = ref(null);
const showNotification = ref(false);
const notificationMessage = ref("");
const scheduleFormData = ref({
  sport: "",
  season: "",
});
const gameFormData = ref({
  gameDate: "",
  gameStart: "",
  venue: "",
  opponent: "",
  isFinalized: false,
});
const error = ref("");
const success = ref("");
const loading = ref(false);

const showNotificationMessage = (message) => {
  notificationMessage.value = message;
  showNotification.value = true;
  setTimeout(() => {
    showNotification.value = false;
  }, 3000);
};

// Use computed property to access schedules from store
const schedules = computed(() => store.schedules || []);

const openScheduleForm = () => {
  showScheduleForm.value = true;
  scheduleFormData.value = {
    sport: "",
    season: "",
  };
  error.value = "";
};

const closeScheduleForm = () => {
  showScheduleForm.value = false;
};

const openGameForm = (schedule) => {
  selectedSchedule.value = schedule;
  showGameForm.value = true;
  gameFormData.value = {
    gameDate: "",
    gameStart: "",
    venue: "",
    opponent: "",
    isFinalized: false,
  };
  error.value = "";
};

const closeGameForm = () => {
  showGameForm.value = false;
  selectedSchedule.value = null;
};

const scheduleCrew = (game) => {
  router.push(`/admin/schedule-crew/${game.gameId}`);
};

const handleScheduleCreate = async () => {
  try {
    error.value = "";
    success.value = "";
    loading.value = true;

    if (!scheduleFormData.value.sport || !scheduleFormData.value.season) {
      error.value = "Please fill in all required fields";
      return;
    }

    const response = await createSchedule(scheduleFormData.value);

    if (response.flag) {
      success.value = "Schedule created successfully!";
      closeScheduleForm();
    } else {
      error.value = response.message || "Failed to create schedule";
    }
  } catch (e) {
    error.value = "An error occurred while creating the schedule";
    console.error(e);
  } finally {
    loading.value = false;
  }
};

const addGame = async () => {
  try {
    error.value = "";
    success.value = "";
    loading.value = true;

    if (!selectedSchedule.value) {
      error.value = "No schedule selected";
      return;
    }

    const response = await addGameToSchedule(
      selectedSchedule.value.id,
      gameFormData.value
    );

    if (response.flag) {
      // Update the local schedule's games array
      const schedule = schedules.value.find(
        (s) => s.id === selectedSchedule.value.id
      );
      if (schedule) {
        schedule.games.push(response.data);
      }
      success.value = "Game added successfully!";
      closeGameForm();
    } else {
      error.value = response.message || "Failed to add game";
    }
  } catch (e) {
    error.value = "An error occurred while adding the game";
    console.error(e);
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="max-w-7xl mx-auto px-4 py-8">
    <!-- Request Notification -->
    <div
      v-if="showNotification"
      class="fixed top-4 right-4 bg-blue-500 text-white px-6 py-3 rounded-lg shadow-lg z-50"
    >
      {{ notificationMessage }}
    </div>

    <div class="flex justify-between items-center mb-8">
      <h1 class="text-2xl font-bold">Game Schedules</h1>
      <button
        @click="openScheduleForm"
        class="px-4 py-2 bg-slate-800 text-white hover:bg-slate-700 rounded-lg transition-colors"
      >
        Create New Schedule
      </button>
    </div>

    <!-- Schedule List -->
    <div class="space-y-4">
      <div v-if="schedules.length === 0" class="text-center py-8 text-gray-500">
        No schedules created yet.
      </div>
      <div
        v-else
        v-for="schedule in schedules"
        :key="schedule.id"
        class="bg-white rounded-lg shadow-md p-6"
      >
        <div class="flex justify-between items-center mb-4">
          <div>
            <h2 class="text-xl font-semibold">{{ schedule.sport }}</h2>
            <p class="text-slate-600">Season: {{ schedule.season }}</p>
          </div>
          <button
            @click="openGameForm(schedule)"
            class="px-4 py-2 bg-slate-100 hover:bg-slate-200 rounded-lg transition-colors"
          >
            Add Games
          </button>
        </div>

        <!-- Games List -->
        <div
          v-if="schedule.games && schedule.games.length > 0"
          class="mt-4 space-y-3"
        >
          <h3 class="text-lg font-medium">Games</h3>
          <div
            v-for="game in schedule.games"
            :key="game.gameId"
            class="p-4 bg-slate-50 rounded-lg"
          >
            <div class="flex justify-between items-center">
              <div>
                <p class="font-medium">vs {{ game.opponent }}</p>
                <div class="text-sm text-slate-600">
                  <p>
                    Date: {{ new Date(game.gameDate).toLocaleDateString() }}
                  </p>
                  <p>Time: {{ game.gameStart }}</p>
                  <p>Venue: {{ game.venue }}</p>
                </div>
              </div>
              <div class="flex items-center gap-4">
                <button
                  @click="scheduleCrew(game)"
                  class="px-4 py-2 bg-slate-800 text-white hover:bg-slate-700 rounded-lg transition-colors"
                >
                  Schedule Crew
                </button>
                <span
                  class="px-3 py-1 text-sm rounded-full"
                  :class="
                    game.isFinalized
                      ? 'bg-green-100 text-green-800'
                      : 'bg-yellow-100 text-yellow-800'
                  "
                >
                  {{ game.isFinalized ? "Finalized" : "Pending" }}
                </span>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="mt-4 text-center py-4 text-gray-500">
          No games added to this schedule yet.
        </div>
      </div>
    </div>

    <!-- Create Schedule Modal -->
    <div
      v-if="showScheduleForm"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center"
    >
      <div class="bg-white rounded-lg p-6 max-w-md w-full mx-4">
        <h2 class="text-xl font-bold mb-6">Create New Schedule</h2>

        <form @submit.prevent="handleScheduleCreate" class="space-y-6">
          <div v-if="error" class="p-4 bg-red-100 text-red-700 rounded-lg">
            {{ error }}
          </div>
          <div
            v-if="success"
            class="p-4 bg-green-100 text-green-700 rounded-lg"
          >
            {{ success }}
          </div>

          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1"
                >Sport</label
              >
              <input
                type="text"
                v-model="scheduleFormData.sport"
                placeholder="Enter sport name"
                class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-slate-500 focus:border-slate-500"
                :disabled="loading"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1"
                >Season</label
              >
              <input
                type="text"
                v-model="scheduleFormData.season"
                placeholder="Enter season (e.g., 2024-2025)"
                class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-slate-500 focus:border-slate-500"
                :disabled="loading"
              />
            </div>
          </div>

          <div class="flex justify-end gap-4">
            <button
              type="button"
              @click="closeScheduleForm"
              class="px-4 py-2 bg-slate-100 hover:bg-slate-200 rounded-lg transition-colors"
              :disabled="loading"
            >
              Cancel
            </button>
            <button
              type="submit"
              class="px-4 py-2 bg-slate-800 text-white hover:bg-slate-700 rounded-lg transition-colors"
              :disabled="loading"
            >
              {{ loading ? "Creating..." : "Create Schedule" }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Add Game Modal -->
    <div
      v-if="showGameForm"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center"
    >
      <div class="bg-white rounded-lg p-6 max-w-md w-full mx-4">
        <h2 class="text-xl font-bold mb-6">Add Game to Schedule</h2>

        <form @submit.prevent="addGame" class="space-y-6">
          <div v-if="error" class="p-4 bg-red-100 text-red-700 rounded-lg">
            {{ error }}
          </div>
          <div
            v-if="success"
            class="p-4 bg-green-100 text-green-700 rounded-lg"
          >
            {{ success }}
          </div>

          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1"
                >Game Date</label
              >
              <input
                type="date"
                v-model="gameFormData.gameDate"
                class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-slate-500 focus:border-slate-500"
                :disabled="loading"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1"
                >Start Time</label
              >
              <input
                type="time"
                v-model="gameFormData.gameStart"
                class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-slate-500 focus:border-slate-500"
                :disabled="loading"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1"
                >Venue</label
              >
              <input
                type="text"
                v-model="gameFormData.venue"
                placeholder="Enter venue name"
                class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-slate-500 focus:border-slate-500"
                :disabled="loading"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1"
                >Opponent</label
              >
              <input
                type="text"
                v-model="gameFormData.opponent"
                placeholder="Enter opponent name"
                class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-slate-500 focus:border-slate-500"
                :disabled="loading"
              />
            </div>
            <div class="flex items-center gap-2">
              <input
                type="checkbox"
                id="isFinalized"
                v-model="gameFormData.isFinalized"
                class="rounded border-gray-300 text-slate-600 focus:ring-slate-500"
                :disabled="loading"
              />
              <label for="isFinalized" class="text-sm font-medium text-gray-700"
                >Finalize Game</label
              >
            </div>
          </div>

          <div class="flex justify-end gap-4">
            <button
              type="button"
              @click="closeGameForm"
              class="px-4 py-2 bg-slate-100 hover:bg-slate-200 rounded-lg transition-colors"
              :disabled="loading"
            >
              Cancel
            </button>
            <button
              type="submit"
              class="px-4 py-2 bg-slate-800 text-white hover:bg-slate-700 rounded-lg transition-colors"
              :disabled="loading"
            >
              {{ loading ? "Adding..." : "Add Game" }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
