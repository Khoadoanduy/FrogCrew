<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { sendInvitations } from '../services/invitation';
import { deleteCrewMember } from '../services/crewMember';
import crewData from '../../db.json';

const router = useRouter();
const emails = ref(['']);
const error = ref('');
const success = ref('');
const loading = ref(false);
const showDeleteModal = ref(false);
const selectedMember = ref(null);
const crewMembers = ref(crewData.crewmember.filter(member => member.role === 'CREW_MEMBER'));
const searchQuery = ref('');
const selectedPosition = ref('');
const selectedAvailability = ref('');
const showNotification = ref(false);
const notificationMessage = ref('');

// Get unique positions from all crew members
const availablePositions = computed(() => {
  const positions = new Set();
  crewMembers.value.forEach(member => {
    member.positions.forEach(position => positions.add(position));
  });
  return Array.from(positions).sort();
});

// Filter crew members based on search, position, and availability
const filteredCrewMembers = computed(() => {
  return crewMembers.value.filter(member => {
    const matchesSearch = member.firstName.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
                         member.lastName.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
                         member.email.toLowerCase().includes(searchQuery.value.toLowerCase());
    
    const matchesPosition = !selectedPosition.value || member.positions.includes(selectedPosition.value);
    
    const isAvailable = !selectedAvailability.value || 
                       (selectedAvailability.value === 'available' && !hasUpcomingGames(member.userId)) ||
                       (selectedAvailability.value === 'unavailable' && hasUpcomingGames(member.userId));
    
    return matchesSearch && matchesPosition && isAvailable;
  });
});

const showNotificationMessage = (message) => {
  notificationMessage.value = message;
  showNotification.value = true;
  setTimeout(() => {
    showNotification.value = false;
  }, 3000);
};

const validateEmail = (email) => {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
};

const handleInvite = async () => {
  try {
    error.value = '';
    success.value = '';
    loading.value = true;

    const validEmails = emails.value.filter(email => email.trim() !== '');
    const invalidEmails = validEmails.filter(email => !validateEmail(email));

    if (invalidEmails.length > 0) {
      error.value = 'Please enter valid email addresses';
      return;
    }

    if (validEmails.length === 0) {
      error.value = 'Please enter at least one email address';
      return;
    }

    showNotificationMessage('Sending POST request to /invite...');

    const response = await sendInvitations(validEmails);

    if (response.flag) {
      success.value = 'Invitations sent successfully!';
      emails.value = [''];
    } else {
      switch (response.code) {
        case 400:
          error.value = 'Some email addresses are invalid. Please check and try again.';
          break;
        case 409:
          error.value = 'Some email addresses are already registered in the system.';
          break;
        default:
          error.value = response.message || 'Failed to send invitations';
      }
    }
  } catch (e) {
    error.value = 'An error occurred while sending invitations';
  } finally {
    loading.value = false;
  }
};

const addEmailField = () => {
  emails.value.push('');
};

const removeEmailField = (index) => {
  emails.value.splice(index, 1);
};

const openDeleteModal = (member) => {
  selectedMember.value = member;
  showDeleteModal.value = true;
};

const closeDeleteModal = () => {
  selectedMember.value = null;
  showDeleteModal.value = false;
};

const handleDelete = async () => {
  try {
    error.value = '';
    loading.value = true;

    showNotificationMessage(`Sending DELETE request to /template/${selectedMember.value.userId}...`);

    const response = await deleteCrewMember(selectedMember.value.userId);

    if (response.flag) {
      success.value = 'Crew member deleted successfully!';
      crewMembers.value = crewMembers.value.filter(member => member.userId !== selectedMember.value.userId);
      closeDeleteModal();
    } else {
      switch (response.code) {
        case 404:
          error.value = 'Crew member not found';
          break;
        case 409:
          error.value = 'Cannot delete: Crew member has upcoming scheduled games';
          break;
        default:
          error.value = response.message || 'Failed to delete crew member';
      }
    }
  } catch (e) {
    error.value = 'An error occurred while deleting the crew member';
  } finally {
    loading.value = false;
  }
};

const hasUpcomingGames = (memberId) => {
  return crewData.gameSchedule.games.some(game => {
    const gameDate = new Date(game.gameDate);
    const today = new Date();
    return gameDate > today && game.crewedMembers.some(crew => crew.userId === memberId);
  });
};

const clearFilters = () => {
  searchQuery.value = '';
  selectedPosition.value = '';
  selectedAvailability.value = '';
};
</script>

<template>
  <div class="max-w-7xl mx-auto px-4 py-8">
    <!-- Request Notification -->
    <div v-if="showNotification" 
         class="fixed top-4 right-4 bg-blue-500 text-white px-6 py-3 rounded-lg shadow-lg z-50">
      {{ notificationMessage }}
    </div>

    <!-- Invite Section -->
    <div class="bg-white rounded-lg shadow-md p-6 mb-8">
      <h2 class="text-2xl font-bold mb-6">Invite Crew Members</h2>

      <form @submit.prevent="handleInvite" class="space-y-6">
        <div v-if="error" class="p-4 bg-red-100 text-red-700 rounded-lg">
          {{ error }}
        </div>
        <div v-if="success" class="p-4 bg-green-100 text-green-700 rounded-lg">
          {{ success }}
        </div>

        <div class="space-y-4">
          <div v-for="(email, index) in emails" :key="index" class="flex gap-2">
            <input
              type="email"
              v-model="emails[index]"
              placeholder="Enter email address"
              class="flex-1 px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
              :disabled="loading"
            />
            <button
              type="button"
              @click="removeEmailField(index)"
              class="px-3 py-2 bg-red-100 text-red-600 hover:bg-red-200 rounded-lg transition-colors"
              :disabled="emails.length === 1 || loading"
            >
              Remove
            </button>
          </div>
        </div>

        <div class="flex gap-4">
          <button
            type="button"
            @click="addEmailField"
            class="px-4 py-2 bg-primary-100 hover:bg-primary-200 rounded-lg transition-colors text-primary-900"
            :disabled="loading"
          >
            Add Another Email
          </button>
          <button
            type="submit"
            class="px-6 py-2 bg-primary-800 text-white hover:bg-primary-700 rounded-lg transition-colors disabled:bg-primary-300"
            :disabled="loading"
          >
            {{ loading ? 'Sending...' : 'Send Invitations' }}
          </button>
        </div>
      </form>
    </div>

    <!-- Crew Members List -->
    <div class="bg-white rounded-lg shadow-md p-6">
      <h2 class="text-2xl font-bold mb-6">Manage Crew Members</h2>
      
      <!-- Filters -->
      <div class="mb-6 space-y-4">
        <div class="flex gap-4 items-end">
          <div class="flex-1">
            <label class="block text-sm font-medium text-gray-700 mb-1">Search</label>
            <input
              type="text"
              v-model="searchQuery"
              placeholder="Search by name or email"
              class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Position</label>
            <select
              v-model="selectedPosition"
              class="px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
            >
              <option value="">All Positions</option>
              <option v-for="position in availablePositions" :key="position" :value="position">
                {{ position }}
              </option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Availability</label>
            <select
              v-model="selectedAvailability"
              class="px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
            >
              <option value="">All</option>
              <option value="available">Available</option>
              <option value="unavailable">Has Upcoming Games</option>
            </select>
          </div>
          <button
            @click="clearFilters"
            class="px-4 py-2 text-primary-600 hover:text-primary-800"
          >
            Clear Filters
          </button>
        </div>
      </div>

      <!-- Crew Members Grid -->
      <div class="space-y-4">
        <div v-if="filteredCrewMembers.length === 0" class="text-center py-8 text-gray-500">
          No crew members found matching the current filters.
        </div>
        <div v-else v-for="member in filteredCrewMembers" 
             :key="member.userId"
             class="flex justify-between items-center p-4 border rounded-lg">
          <div>
            <h3 class="font-semibold">{{ member.firstName }} {{ member.lastName }}</h3>
            <p class="text-sm text-primary-600">{{ member.email }}</p>
            <div class="flex gap-2 mt-2">
              <span v-for="position in member.positions" 
                    :key="position"
                    class="px-2 py-1 bg-primary-100 rounded-full text-xs text-primary-800">
                {{ position }}
              </span>
            </div>
          </div>
          <button
            @click="openDeleteModal(member)"
            class="px-4 py-2 bg-red-600 text-white hover:bg-red-700 rounded-lg transition-colors"
            :disabled="hasUpcomingGames(member.userId)"
            :title="hasUpcomingGames(member.userId) ? 'Cannot delete: Member has upcoming games' : ''"
          >
            Delete
          </button>
        </div>
      </div>
    </div>

    <!-- Delete Confirmation Modal -->
    <div v-if="showDeleteModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
      <div class="bg-white rounded-lg p-6 max-w-md w-full mx-4">
        <h3 class="text-xl font-bold mb-4">Confirm Deletion</h3>
        <p class="mb-6">
          Are you sure you want to delete {{ selectedMember?.firstName }} {{ selectedMember?.lastName }}? 
          This action cannot be undone.
        </p>
        <div class="flex justify-end gap-4">
          <button
            @click="closeDeleteModal"
            class="px-4 py-2 bg-primary-100 hover:bg-primary-200 rounded-lg transition-colors text-primary-900"
          >
            Cancel
          </button>
          <button
            @click="handleDelete"
            class="px-4 py-2 bg-red-600 text-white hover:bg-red-700 rounded-lg transition-colors"
            :disabled="loading"
          >
            {{ loading ? 'Deleting...' : 'Delete' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>