<script setup>
import NavBar from './components/NavBar.vue';
</script>

<template>
  <div class="min-h-screen bg-gray-50 font-roboto-slab">
    <NavBar />
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <router-view></router-view>
    </div>
  </div>
</template>

<style>
body {
  @apply antialiased font-roboto-slab;
}
</style>