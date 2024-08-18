<script setup>
import { ref, computed } from 'vue'

// List of possible images and names
const images = [
  'https://imgur.com/OMCh8M3.png',
  'https://imgur.com/0hz5ZnQ.png',
  'https://imgur.com/oNLUhPO.png',
  'https://imgur.com/yoAc3j9.png',
  'https://imgur.com/BUmixhD.png',
  'https://imgur.com/auiLii9.png',
  // Add more image URLs here
]

const names = [
  'Poy 2G.',
  'Gent A.',
  'March P.',
  'Alex W.',
  'Sulek S.',
  'Sammy D.',
  // Add more names here
]

// Function to generate a random index
const getRandomIndex = (array) => Math.floor(Math.random() * array.length)

// Reactive variables for image and name
const randomImage = ref(images[getRandomIndex(images)])
const randomName = ref(names[getRandomIndex(names)])

// Watch for page reload to change the random image and name
window.addEventListener('load', () => {
  randomImage.value = images[getRandomIndex(images)]
  randomName.value = names[getRandomIndex(names)]
})
</script>

<template>
  <aside
    class="w-64 px-6 flex h-screen flex-col items-center border-r bg-slate-900"
  >
    <div class="flex items-center w-full py-4 px-4 border-b border-gray-200">
      <img
        class="w-12 h-12 rounded-full"
        :src="randomImage"
        alt="User Avatar"
      />
      <div class="ml-5">
        <p class="text-white font-semibold">{{ randomName }}</p>
        <p class="text-gray-400 text-sm">User</p>
      </div>
    </div>

    <nav class="flex flex-1 flex-col items-center gap-y-4 pt-10">
      <div class="group relative rounded-xl p-2 text-white">
        <router-link to="/task">
          <div class="rounded-lg font-medium">Task</div>
        </router-link>
      </div>

      <div class="group relative rounded-xl p-2 text-white itbkk-manage-status">
        <router-link to="/status">
          <div class="rounded-lg font-medium">Status</div>
        </router-link>
      </div>
    </nav>
  </aside>
</template>

<style scoped></style>
