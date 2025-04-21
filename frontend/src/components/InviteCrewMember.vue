<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import crewData from '../../db.json';

const router = useRouter();
const emails = ref(['']);
const error = ref('');
const success = ref('');
const loading = ref(false);

const addEmailField = () => {
  emails.value.push('');
};

const removeEmailField = (index) => {
  emails.value.splice(index, 1);
};

const validateEmail = (email) => {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
};

const handleSubmit = async () => {
  try {
    error.value = '';
    success.value = '';
    loading.value = true;

    // Validate emails
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

    const response = await fetch('/invite', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ emails: validEmails }),
    });

    const result = await response.json();

    if (result.flag) {
      success.value = 'Invitations sent successfully!';
      emails.value = [''];
    } else {
      error.value = result.message || 'Failed to send invitations';
    }
  } catch (e) {
    error.value = 'An error occurred while sending invitations';
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="max-w-3xl mx-auto px-4 py-8">
    <div class="mb-8">
      <button @click="router.push('/')" class="px-4 py-2 bg-slate-100 hover:bg-slate-200 rounded-lg transition-colors">
        Back to Home
      </button>
    </div>

    <div class="bg-white rounded-lg shadow-md p-6">
      <h1 class="text-2xl font-bold mb-6">Invite Crew Members</h1>

      <form @submit.prevent="handleSubmit" class="space-y-6">
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
              class="flex-1 px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
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
            class="px-4 py-2 bg-slate-100 hover:bg-slate-200 rounded-lg transition-colors"
            :disabled="loading"
          >
            Add Another Email
          </button>
          <button
            type="submit"
            class="px-6 py-2 bg-blue-600 text-white hover:bg-blue-700 rounded-lg transition-colors disabled:bg-blue-300"
            :disabled="loading"
          >
            {{ loading ? 'Sending...' : 'Send Invitations' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>