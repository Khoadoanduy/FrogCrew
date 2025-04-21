<script setup>
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { createCrewMember } from '../services/crewMember';

const route = useRoute();
const router = useRouter();
const loading = ref(false);
const error = ref('');
const success = ref('');

const showNotification = ref(false);
const notificationMessage = ref('');

const formData = ref({
  firstName: '',
  lastName: '',
  email: '',
  phoneNumber: '',
  password: '',
  role: 'CREW_MEMBER',
  positions: []
});

const availablePositions = [
  'Producer',
  'Asst Prod',
  'Director',
  'Asst Director',
  'Technical Dir',
  'Graphics',
  'Bug Op',
  'Replay EVS',
  'EIC',
  'Video',
  'Audio',
  'Camera',
  'Utility',
  'Tech Manager',
  'TOC',
  'Observer'
];

const validatePhoneNumber = (phone) => {
  const phoneRegex = /^\d{3}-\d{3}-\d{4}$/;
  return phoneRegex.test(phone);
};

const validateEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

const formatPhoneNumber = (phone) => {
  const digits = phone.replace(/\D/g, '');
  
  if (digits.length >= 10) {
    return `${digits.slice(0,3)}-${digits.slice(3,6)}-${digits.slice(6,10)}`;
  }
  return digits;
};

const handlePhoneInput = (e) => {
  formData.value.phoneNumber = formatPhoneNumber(e.target.value);
};

const showNotificationMessage = (message) => {
  notificationMessage.value = message;
  showNotification.value = true;
  setTimeout(() => {
    showNotification.value = false;
  }, 3000);
};

const handleSubmit = async () => {
  try {
    error.value = '';
    success.value = '';
    loading.value = true;

    if (!formData.value.firstName || !formData.value.lastName || !formData.value.email || 
        !formData.value.phoneNumber || !formData.value.password || formData.value.positions.length === 0) {
      error.value = 'Please fill in all required fields';
      return;
    }

    if (!validateEmail(formData.value.email)) {
      error.value = 'Please enter a valid email address';
      return;
    }

    if (!validatePhoneNumber(formData.value.phoneNumber)) {
      error.value = 'Please enter a valid phone number (XXX-XXX-XXXX)';
      return;
    }

    const token = route.query.token;
    showNotificationMessage('Sending POST request to /crewMember...');
    
    const response = await createCrewMember(token, formData.value);

    if (response.flag) {
      success.value = 'Profile created successfully! Redirecting to login...';
      setTimeout(() => {
        router.push('/login');
      }, 2000);
    } else {
      switch (response.code) {
        case 400:
          error.value = 'Please check your input and try again';
          break;
        case 401:
          error.value = 'Invalid or expired invitation token';
          router.push('/');
          break;
        default:
          error.value = response.message || 'An error occurred while creating your profile';
      }
    }
  } catch (e) {
    error.value = 'An error occurred while creating your profile';
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="min-h-screen bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
    <!-- Request Notification -->
    <div v-if="showNotification" 
         class="fixed top-4 right-4 bg-blue-500 text-white px-6 py-3 rounded-lg shadow-lg z-50">
      {{ notificationMessage }}
    </div>

    <div class="max-w-md mx-auto">
      <div class="text-center mb-8">
        <h1 class="text-3xl font-bold">Create Your Profile</h1>
        <p class="mt-2 text-gray-600">Complete your crew member registration</p>
      </div>

      <div class="bg-white rounded-lg shadow-md p-6">
        <form @submit.prevent="handleSubmit" class="space-y-6">
          <div v-if="error" class="p-4 bg-red-100 text-red-700 rounded-lg">
            {{ error }}
          </div>
          <div v-if="success" class="p-4 bg-green-100 text-green-700 rounded-lg">
            {{ success }}
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">First Name</label>
              <input
                type="text"
                v-model="formData.firstName"
                required
                class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
                :disabled="loading"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Last Name</label>
              <input
                type="text"
                v-model="formData.lastName"
                required
                class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
                :disabled="loading"
              />
            </div>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
            <input
              type="email"
              v-model="formData.email"
              required
              class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
              :disabled="loading"
            />
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Phone Number</label>
            <input
              type="tel"
              v-model="formData.phoneNumber"
              @input="handlePhoneInput"
              placeholder="XXX-XXX-XXXX"
              required
              class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
              :disabled="loading"
            />
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Password</label>
            <input
              type="password"
              v-model="formData.password"
              required
              class="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
              :disabled="loading"
            />
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Qualified Positions</label>
            <div class="grid grid-cols-2 gap-2">
              <label v-for="position in availablePositions" 
                     :key="position" 
                     class="flex items-center space-x-2">
                <input
                  type="checkbox"
                  v-model="formData.positions"
                  :value="position"
                  class="rounded border-gray-300 text-primary-600 focus:ring-primary-500"
                  :disabled="loading"
                />
                <span class="text-sm">{{ position }}</span>
              </label>
            </div>
          </div>

          <button
            type="submit"
            class="w-full px-4 py-2 bg-primary-800 text-white hover:bg-primary-700 rounded-lg transition-colors disabled:bg-primary-300"
            :disabled="loading"
          >
            {{ loading ? 'Creating Profile...' : 'Create Profile' }}
          </button>
        </form>
      </div>
    </div>
  </div>
</template>