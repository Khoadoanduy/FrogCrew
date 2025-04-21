<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import crewData from '../../db.json';

const router = useRouter();
const email = ref('');
const password = ref('');
const error = ref('');
const loading = ref(false);

const handleLogin = async () => {
  try {
    loading.value = true;
    error.value = '';

    // Mock authentication
    const user = crewData.users.find(u => u.email === email.value && u.password === password.value);
    
    if (user) {
      // Find the full user profile
      const userProfile = crewData.crewmember.find(m => m.userId === user.userId);
      if (userProfile) {
        // Update current user
        crewData.currentUser = userProfile;
        router.push('/');
      } else {
        error.value = 'User profile not found';
      }
    } else {
      error.value = 'Invalid email or password';
    }
  } catch (e) {
    error.value = 'An error occurred during login';
    console.error(e);
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
    <div class="sm:mx-auto sm:w-full sm:max-w-md">
      <h1 class="text-center text-3xl font-bold text-primary-900 mb-8">FrogCrew</h1>
      <div class="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
        <form @submit.prevent="handleLogin" class="space-y-6">
          <div v-if="error" class="p-4 bg-red-100 text-red-700 rounded-lg">
            {{ error }}
          </div>

          <div>
            <label for="email" class="block text-sm font-medium text-gray-700">Email</label>
            <div class="mt-1">
              <input
                id="email"
                type="email"
                v-model="email"
                required
                class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
              />
            </div>
          </div>

          <div>
            <label for="password" class="block text-sm font-medium text-gray-700">Password</label>
            <div class="mt-1">
              <input
                id="password"
                type="password"
                v-model="password"
                required
                class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
              />
            </div>
          </div>

          <div>
            <button
              type="submit"
              :disabled="loading"
              class="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-primary-800 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 disabled:bg-primary-300"
            >
              {{ loading ? 'Signing in...' : 'Sign in' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>