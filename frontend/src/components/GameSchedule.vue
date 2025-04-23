<script setup>
import { ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import { getGames } from "../services/gameSchedule";
import { submitAvailability } from "../services/availability";
import crewData from "../../db.json";

const router = useRouter();
const games = ref([]);
const error = ref("");
const loading = ref(true);
const showForm = ref(false);
const formError = ref("");
const currentUser = ref(crewData.currentUser);
const showNotification = ref(false);
const notificationMessage = ref("");
const formData = ref({
  userId: currentUser.value.userId,
  gameId: null,
  availability: null,
  comment: "",
});

const isCrewMember = computed(() => currentUser.value.role === "CREW_MEMBER");

const hasSubmittedAvailability = (gameId) => {
  return crewData.availability?.some(
    (a) => a.userId === currentUser.value.userId && a.gameId === gameId
  );
};

const getAvailabilityStatus = (gameId) => {
  const availability = crewData.availability?.find(
    (a) => a.userId === currentUser.value.userId && a.gameId === gameId
  );

  if (!availability) return null;
  return availability.availability === 1 ? "Available" : "Unavailable";
};

const showNotificationMessage = (message) => {
  notificationMessage.value = message;
  showNotification.value = true;
  setTimeout(() => {
    showNotification.value = false;
  }, 3000);
};

onMounted(async () => {
  try {
    const response = await getGames();
    if (response.flag) {
      games.value = response.data;
    } else {
      error.value = response.message;
    }
  } catch (e) {
    error.value = "An error occurred while fetching the game schedule";
  } finally {
    loading.value = false;
  }
});

const viewCrewList = (gameId) => {
  router.push(`/crew-list/${gameId}`);
};

const openAvailabilityForm = (game) => {
  formData.value = {
    userId: currentUser.value.userId,
    gameId: game.gameId,
    availability: null,
    comment: "",
  };
  formError.value = "";
  showForm.value = true;
};

const handleSubmit = async () => {
  try {
    formError.value = "";

    if (formData.value.availability === null) {
      formError.value = "Please select your availability";
      return;
    }

    showNotificationMessage("Sending POST request to /availability...");

    const response = await submitAvailability(formData.value);

    if (response.flag) {
      showForm.value = false;
      formData.value = {
        userId: currentUser.value.userId,
        gameId: null,
        availability: null,
        comment: "",
      };
      // Refresh the games list to show updated availability
      const gamesResponse = await getGames();
      if (gamesResponse.flag) {
        games.value = gamesResponse.data;
      }
    } else {
      switch (response.code) {
        case 400:
          formError.value = "Please fill in all required fields.";
          break;
        case 404:
          formError.value = "Game or user not found.";
          break;
        case 409:
          formError.value =
            "You have already submitted availability for this game.";
          break;
        default:
          formError.value =
            response.message ||
            "An error occurred while submitting availability.";
      }
    }
  } catch (e) {
    formError.value = "Failed to submit availability. Please try again.";
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
      <h1 class="text-3xl font-bold text-gray-900">Game Schedule</h1>
    </div>

    <div v-if="loading" class="text-center py-8">Loading...</div>
    <div v-else-if="error" class="text-center py-8 text-red-600">
      {{ error }}
    </div>
    <div v-else-if="games.length === 0" class="text-center py-8">
      No upcoming games available at this time.
    </div>
    <div v-else class="space-y-4">
      <div
        v-for="game in games"
        :key="game.gameId"
        class="bg-white rounded-lg shadow-md p-6"
      >
        <div class="flex justify-between items-center">
          <div class="space-y-2">
            <div class="flex gap-4 items-center">
              <span class="font-bold text-lg">{{
                new Date(game.gameDate).toLocaleDateString()
              }}</span>
              <span class="text-lg">vs {{ game.opponent }}</span>
            </div>
            <div class="flex gap-6 text-gray-600">
              <span>Start Time: {{ game.gameStart }}</span>
              <span>Venue: {{ game.venue }}</span>
              <span
                >Status: {{ game.isFinalized ? "Finalized" : "Pending" }}</span
              >
              <span
                v-if="isCrewMember && hasSubmittedAvailability(game.gameId)"
                :class="{
                  'text-green-600':
                    getAvailabilityStatus(game.gameId) === 'Available',
                  'text-red-600':
                    getAvailabilityStatus(game.gameId) === 'Unavailable',
                }"
              >
                Status: {{ getAvailabilityStatus(game.gameId) }}
              </span>
            </div>
          </div>
          <div class="flex gap-4">
            <button
              @click="viewCrewList(game.gameId)"
              class="px-4 py-2 bg-primary-100 hover:bg-primary-200 rounded-lg transition-colors text-primary-900"
            >
              View Crew List
            </button>
            <button
              v-if="isCrewMember && !hasSubmittedAvailability(game.gameId)"
              @click="openAvailabilityForm(game)"
              class="px-4 py-2 bg-primary-800 text-white hover:bg-primary-700 rounded-lg transition-colors"
            >
              Submit Availability
            </button>
            <span
              v-else-if="isCrewMember"
              class="px-4 py-2 bg-gray-100 text-gray-600 rounded-lg"
            >
              Availability Submitted
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- Availability Form Modal -->
    <div
      v-if="showForm"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center"
    >
      <div class="bg-white rounded-lg p-8 max-w-lg w-full mx-4">
        <h2 class="text-2xl font-bold mb-6">Submit Availability</h2>
        <form @submit.prevent="handleSubmit" class="space-y-6">
          <div v-if="formError" class="p-4 bg-red-100 text-red-700 rounded-lg">
            {{ formError }}
          </div>

          <div>
            <label class="block mb-2 font-medium">Availability:</label>
            <div class="space-x-6">
              <label class="inline-flex items-center">
                <input
                  type="radio"
                  v-model="formData.availability"
                  :value="1"
                  required
                  class="form-radio text-primary-600"
                />
                <span class="ml-2">Available</span>
              </label>
              <label class="inline-flex items-center">
                <input
                  type="radio"
                  v-model="formData.availability"
                  :value="0"
                  required
                  class="form-radio text-red-600"
                />
                <span class="ml-2">Unavailable</span>
              </label>
            </div>
          </div>

          <div>
            <label for="comment" class="block mb-2 font-medium"
              >Comment (optional):</label
            >
            <textarea
              id="comment"
              v-model="formData.comment"
              rows="3"
              class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
              placeholder="Add any additional information..."
            ></textarea>
          </div>

          <div class="flex justify-end gap-4">
            <button
              type="button"
              @click="showForm = false"
              class="px-4 py-2 bg-gray-100 hover:bg-gray-200 rounded-lg transition-colors"
            >
              Cancel
            </button>
            <button
              type="submit"
              class="px-4 py-2 bg-primary-800 text-white hover:bg-primary-700 rounded-lg transition-colors"
            >
              Submit
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
