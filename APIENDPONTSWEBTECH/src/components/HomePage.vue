<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import crewData from '../../db.json';

const router = useRouter();
const currentUser = ref(crewData.currentUser);

const isAdmin = computed(() => currentUser.value?.role === 'ADMIN');

const viewSchedule = () => {
  router.push('/schedule');
};

const viewAdminDashboard = () => {
  router.push('/admin');
};

const viewGameSchedules = () => {
  router.push('/admin/schedules');
};

const viewProfile = () => {
  router.push(`/crew-member/${currentUser.value.userId}`);
};
</script>

<template>
  <div>
    <!-- Admin View -->
    <div v-if="isAdmin" class="space-y-8">
      <div class="flex justify-between items-center">
        <h1 class="text-3xl font-bold text-gray-900">Admin Dashboard</h1>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div class="bg-white rounded-xl shadow-lg p-8 hover:shadow-xl transition-shadow">
          <h2 class="text-2xl font-semibold text-primary-900 mb-4">Crew Management</h2>
          <p class="text-gray-600 mb-6">Manage crew members, send invitations, and handle crew assignments.</p>
          <button 
            @click="viewAdminDashboard"
            class="w-full px-6 py-3 bg-primary-800 text-white hover:bg-primary-700 rounded-lg transition-colors"
          >
            Go to Crew Management
          </button>
        </div>

        <div class="bg-white rounded-xl shadow-lg p-8 hover:shadow-xl transition-shadow">
          <h2 class="text-2xl font-semibold text-primary-900 mb-4">Game Schedules</h2>
          <p class="text-gray-600 mb-6">Create and manage game schedules, add games, and assign crew members.</p>
          <button 
            @click="viewGameSchedules"
            class="w-full px-6 py-3 bg-primary-800 text-white hover:bg-primary-700 rounded-lg transition-colors"
          >
            Manage Game Schedules
          </button>
        </div>
      </div>
    </div>

    <!-- Crew Member View -->
    <div v-else class="max-w-3xl mx-auto">
      <div class="mb-8 flex justify-between items-center">
        <h1 class="text-3xl font-bold text-gray-900">Welcome, {{ currentUser.firstName }}!</h1>
        <div class="space-x-4">
          <button 
            @click="viewSchedule" 
            class="px-4 py-2 bg-primary-800 text-white hover:bg-primary-700 rounded-lg transition-colors"
          >
            View Game Schedule
          </button>
          <button 
            @click="viewProfile" 
            class="px-4 py-2 bg-primary-100 text-primary-800 hover:bg-primary-200 rounded-lg transition-colors"
          >
            View Profile
          </button>
        </div>
      </div>

      <div class="bg-white rounded-xl shadow-lg p-8">
        <div class="space-y-6">
          <div>
            <h2 class="text-xl font-semibold text-gray-900 mb-4">Your Positions</h2>
            <div class="flex flex-wrap gap-2">
              <span 
                v-for="position in currentUser.positions" 
                :key="position"
                class="px-3 py-1 bg-primary-100 text-primary-800 rounded-full text-sm"
              >
                {{ position }}
              </span>
            </div>
          </div>

          <div>
            <h2 class="text-xl font-semibold text-gray-900 mb-4">Contact Information</h2>
            <div class="space-y-2 text-gray-600">
              <p>Email: {{ currentUser.email }}</p>
              <p>Phone: {{ currentUser.phoneNumber }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>