<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getGameCrewList } from '../services/crewList';

const route = useRoute();
const router = useRouter();
const game = ref(null);
const error = ref('');
const loading = ref(true);

onMounted(async () => {
  try {
    const gameId = route.params.id;
    const response = await getGameCrewList(gameId);
    
    if (response.flag) {
      game.value = response.data;
    } else {
      error.value = response.message || 'Failed to load crew list';
    }
  } catch (e) {
    error.value = 'An error occurred while fetching the crew list';
    console.error(e);
  } finally {
    loading.value = false;
  }
});

const goBack = () => {
  router.push('/schedule');
};

const viewProfile = (userId) => {
  router.push(`/crew-member/${userId}`);
};
</script>

<template>
  <div class="max-w-7xl mx-auto px-4 py-8">
    <div class="mb-8">
      <button @click="goBack" class="px-4 py-2 bg-slate-100 hover:bg-slate-200 rounded-lg transition-colors">
        Back to Schedule
      </button>
    </div>

    <div v-if="loading" class="text-center py-8">Loading...</div>
    <div v-else-if="error" class="text-center py-8 text-red-600">{{ error }}</div>
    <div v-else-if="game" class="space-y-8">
      <div class="space-y-4">
        <h1 class="text-3xl font-bold">{{ game.opponent }} - Crew List</h1>
        <div class="flex gap-8 text-slate-600">
          <span>Date: {{ new Date(game.gameDate).toLocaleDateString() }}</span>
          <span>Start Time: {{ game.gameStart }}</span>
          <span>Venue: {{ game.venue }}</span>
        </div>
      </div>

      <div class="space-y-4">
        <h2 class="text-2xl font-semibold">Assigned Crew Members</h2>
        <div class="space-y-4">
          <div v-if="!game.crewedMembers?.length" class="text-center py-8 text-gray-500">
            No crew members assigned yet.
          </div>
          <div v-else v-for="member in game.crewedMembers" 
               :key="member.crewedUserId"
               class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow cursor-pointer"
               @click="viewProfile(member.userId)">
            <div class="space-y-4">
              <div class="flex gap-4 items-center">
                <span class="font-bold text-lg">{{ member.fullName }}</span>
                <span class="px-4 py-2 bg-slate-100 rounded-lg">{{ member.Position }}</span>
              </div>
              <div class="flex gap-8 text-slate-600">
                <span>Report Time: {{ member.ReportTime }}</span>
                <span>Location: {{ member.ReportLocation }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>