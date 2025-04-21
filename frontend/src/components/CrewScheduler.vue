<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getAvailableCrewMembers, addCrewSchedule } from '../services/api';
import crewData from '../../db.json';

const route = useRoute();
const router = useRouter();
const game = ref(null);
const selectedPosition = ref('');
const qualifiedMembers = ref([]);
const error = ref('');
const success = ref('');
const loading = ref(false);
const showAddForm = ref(false);
const showNotification = ref(false);

const availablePositions = [
  'Producer',
  'Director',
  'Technical Dir',
  'Graphics',
  'Replay EVS',
  'Audio',
  'Camera',
  'Video'
];

const newCrewMember = ref({
  userId: null,
  reportTime: '',
  reportLocation: ''
});

const loadGame = async () => {
  try {
    const gameId = parseInt(route.params.id);
    const gameData = crewData.gameSchedule.games.find(g => g.gameId === gameId);
    
    if (gameData) {
      game.value = gameData;
    } else {
      error.value = 'Game not found';
    }
  } catch (e) {
    error.value = 'Error loading game data';
    console.error(e);
  }
};

const updateQualifiedMembers = async () => {
  if (selectedPosition.value && game.value) {
    try {
      const response = await getAvailableCrewMembers(game.value.gameId, selectedPosition.value);
      
      if (response.flag) {
        qualifiedMembers.value = response.data;
        showAddForm.value = true;
      } else {
        qualifiedMembers.value = [];
        error.value = response.message;
        showAddForm.value = false;
      }
    } catch (e) {
      error.value = 'Failed to fetch available crew members';
      qualifiedMembers.value = [];
      showAddForm.value = false;
      console.error(e);
    }
    newCrewMember.value.userId = null;
  }
};

const handleSubmit = async () => {
  try {
    error.value = '';
    success.value = '';
    loading.value = true;

    if (!newCrewMember.value.userId || !selectedPosition.value || !newCrewMember.value.reportLocation) {
      error.value = 'Please complete all crew member assignments including location';
      return;
    }

    const assignment = {
      userId: newCrewMember.value.userId,
      gameId: game.value.gameId,
      position: selectedPosition.value,
      reportTime: newCrewMember.value.reportTime || '',
      reportLocation: newCrewMember.value.reportLocation
    };

    showNotification.value = true;
    setTimeout(() => {
      showNotification.value = false;
    }, 3000);

    const response = await addCrewSchedule(game.value.gameId, [assignment]);

    if (response.flag) {
      success.value = 'Crew member assigned successfully!';
      
      // Reset form
      selectedPosition.value = '';
      qualifiedMembers.value = [];
      newCrewMember.value = {
        userId: null,
        reportTime: '',
        reportLocation: ''
      };
      showAddForm.value = false;
      
      // Reload game data to show updated crew list
      await loadGame();
    } else {
      error.value = response.message;
    }
  } catch (e) {
    error.value = 'An error occurred while assigning crew member';
    console.error(e);
  } finally {
    loading.value = false;
  }
};

onMounted(loadGame);
</script>

<template>
  <div class="max-w-7xl mx-auto px-4 py-8">
    <div v-if="showNotification" 
         class="fixed top-4 right-4 bg-blue-500 text-white px-6 py-3 rounded-lg shadow-lg z-50">
      Sending POST request to /crewSchedule/{{ game?.gameId }}...
    </div>

    <div class="mb-8">
      <button @click="router.back()" class="px-4 py-2 bg-primary-100 hover:bg-primary-200 rounded-lg transition-colors text-primary-900">
        Back
      </button>
    </div>

    <div v-if="error" class="mb-8 p-4 bg-red-100 text-red-700 rounded-lg">
      {{ error }}
    </div>
    <div v-if="success" class="mb-8 p-4 bg-green-100 text-green-700 rounded-lg">
      {{ success }}
    </div>

    <div v-if="game" class="space-y-8">
      <!-- Game Details -->
      <div class="bg-white rounded-lg shadow-md p-6">
        <h1 class="text-2xl font-bold mb-4">Schedule Crew</h1>
        <div class="space-y-2">
          <p class="text-lg">{{ game.opponent }}</p>
          <p>Date: {{ new Date(game.gameDate).toLocaleDateString() }}</p>
          <p>Time: {{ game.gameStart }}</p>
          <p>Venue: {{ game.venue }}</p>
        </div>
      </div>

      <!-- Current Crew List -->
      <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-xl font-semibold mb-6">Current Crew Members</h2>
        <div v-if="game.crewedMembers && game.crewedMembers.length > 0" class="space-y-4">
          <div v-for="member in game.crewedMembers" 
               :key="member.crewedUserId"
               class="p-4 bg-gray-50 rounded-lg">
            <div class="flex justify-between items-center">
              <div>
                <p class="font-medium">{{ member.fullName }}</p>
                <p class="text-sm text-gray-600">Position: {{ member.Position }}</p>
                <div class="text-sm text-gray-600">
                  <p>Report Time: {{ member.ReportTime }}</p>
                  <p>Location: {{ member.ReportLocation }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="text-center text-gray-500 py-4">
          No crew members assigned yet.
        </div>
      </div>

      <!-- Add Crew Member -->
      <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-xl font-semibold mb-6">Add Crew Member</h2>

        <!-- Position Selection -->
        <div class="mb-6">
          <label class="block text-sm font-medium text-gray-700 mb-2">Select Position</label>
          <select
            v-model="selectedPosition"
            @change="updateQualifiedMembers"
            class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
          >
            <option value="">Select Position</option>
            <option v-for="position in availablePositions" 
                    :key="position"
                    :value="position">
              {{ position }}
            </option>
          </select>
        </div>

        <!-- Crew Member Form -->
        <form v-if="showAddForm" @submit.prevent="handleSubmit" class="space-y-6">
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Select Crew Member</label>
              <select
                v-model="newCrewMember.userId"
                class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
                required
              >
                <option value="">Select Member</option>
                <option v-for="member in qualifiedMembers"
                        :key="member.userId"
                        :value="member.userId">
                  {{ member.fullName }}
                </option>
              </select>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Report Time</label>
              <input
                type="time"
                v-model="newCrewMember.reportTime"
                class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
                required
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Report Location</label>
              <input
                type="text"
                v-model="newCrewMember.reportLocation"
                placeholder="Enter report location"
                class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
                required
              />
            </div>
          </div>

          <div class="flex justify-end">
            <button
              type="submit"
              class="px-6 py-2 bg-primary-800 text-white hover:bg-primary-700 rounded-lg transition-colors disabled:bg-primary-300"
              :disabled="loading"
            >
              {{ loading ? 'Assigning...' : 'Assign Crew Member' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>