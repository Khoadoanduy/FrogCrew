<script setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import crewData from "../../db.json";

const router = useRouter();
const currentUser = ref(crewData.currentUser);
const crewMembers = ref(crewData.crewmember);

const isAdmin = computed(() => currentUser.value?.role === "ADMIN");

const navigateTo = (path) => {
  router.push(path);
};

const handleLogout = () => {
  crewData.currentUser = null;
  router.push("/login");
};

const switchUser = (userId) => {
  const newUser = crewMembers.value.find((member) => member.userId === userId);
  if (newUser) {
    currentUser.value = newUser;
    crewData.currentUser = newUser;
  }
};
</script>

<!-- Template for NavBar -->
<template>
  <nav class="bg-primary-900">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex items-center justify-between h-16">
        <div class="flex items-center">
          <div
            class="text-2xl font-bold text-white cursor-pointer font-roboto-slab"
            @click="navigateTo('/')"
          >
            FrogCrew
          </div>
          <div class="hidden md:block ml-10">
            <div class="flex items-baseline space-x-4">
              <button
                @click="navigateTo('/')"
                class="px-3 py-2 rounded-md text-sm font-medium text-white hover:bg-primary-800 transition-colors"
              >
                Home
              </button>
              <button
                @click="navigateTo('/schedule')"
                class="px-3 py-2 rounded-md text-sm font-medium text-white hover:bg-primary-800 transition-colors"
              >
                Schedule
              </button>
              <button
                v-if="isAdmin"
                @click="navigateTo('/admin')"
                class="px-3 py-2 rounded-md text-sm font-medium text-white hover:bg-primary-800 transition-colors"
              >
                Admin Dashboard
              </button>
              <button
                v-if="isAdmin"
                @click="navigateTo('/admin/schedules')"
                class="px-3 py-2 rounded-md text-sm font-medium text-white hover:bg-primary-800 transition-colors"
              >
                Game Schedules
              </button>
            </div>
          </div>
        </div>
        <div class="flex items-center space-x-4">
          <span
            v-if="currentUser"
            class="text-sm text-gray-300 font-roboto-slab"
          >
            {{ currentUser.firstName }} {{ currentUser.lastName }} ({{
              currentUser.role
            }})
          </span>
          <select
            v-if="currentUser"
            @change="switchUser(parseInt($event.target.value))"
            class="bg-primary-800 text-white text-sm rounded-md border-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-primary-900 focus:ring-white"
          >
            <option
              v-for="member in crewMembers"
              :key="member.userId"
              :value="member.userId"
              :selected="member.userId === currentUser.userId"
            >
              {{ member.firstName }} ({{ member.role }})
            </option>
          </select>
          <button
            v-if="currentUser"
            @click="handleLogout"
            class="px-3 py-2 rounded-md text-sm font-medium text-white hover:bg-primary-800 transition-colors"
          >
            Logout
          </button>
        </div>
      </div>
    </div>
  </nav>
</template>
