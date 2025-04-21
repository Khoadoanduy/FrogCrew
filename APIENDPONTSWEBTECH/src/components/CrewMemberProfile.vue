<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getCrewMember } from '../services/crewMember';

const route = useRoute();
const router = useRouter();
const crewMember = ref(null);
const error = ref('');
const loading = ref(true);

onMounted(async () => {
  try {
    const userId = route.params.id;
    const response = await getCrewMember(userId);
    
    if (response.flag) {
      crewMember.value = response.data;
    } else {
      error.value = response.message;
    }
  } catch (e) {
    error.value = 'An error occurred while fetching the crew member profile';
    console.error(e);
  } finally {
    loading.value = false;
  }
});

const goBack = () => {
  router.back();
};
</script>

<template>
  <div class="max-w-3xl mx-auto px-4 py-8">
    <div class="mb-8">
      <button @click="goBack" class="px-4 py-2 bg-slate-100 hover:bg-slate-200 rounded-lg transition-colors">
        Back
      </button>
    </div>
    
    <div v-if="loading" class="text-center py-8">Loading...</div>
    <div v-else-if="error" class="text-center py-8 text-red-600">{{ error }}</div>
    <div v-else-if="crewMember" class="space-y-8">
      <div class="flex items-center gap-4">
        <h1 class="text-3xl font-bold">{{ crewMember.firstName }} {{ crewMember.lastName }}</h1>
        <span class="px-4 py-2 bg-slate-100 rounded-lg">{{ crewMember.role }}</span>
      </div>

      <div class="space-y-6">
        <div>
          <h2 class="text-xl font-semibold mb-4">Contact Information</h2>
          <div class="space-y-2">
            <div class="flex gap-4 items-center">
              <span class="min-w-20 font-medium">Email:</span>
              <a :href="`mailto:${crewMember.email}`" class="text-blue-600 hover:text-blue-800">
                {{ crewMember.email }}
              </a>
            </div>
            <div class="flex gap-4 items-center">
              <span class="min-w-20 font-medium">Phone:</span>
              <a :href="`tel:${crewMember.phoneNumber}`" class="text-blue-600 hover:text-blue-800">
                {{ crewMember.phoneNumber }}
              </a>
            </div>
          </div>
        </div>

        <div>
          <h2 class="text-xl font-semibold mb-4">Positions</h2>
          <div class="flex flex-wrap gap-2">
            <span v-for="position in crewMember.positions" 
                  :key="position"
                  class="px-4 py-2 bg-slate-100 rounded-lg">
              {{ position }}
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>