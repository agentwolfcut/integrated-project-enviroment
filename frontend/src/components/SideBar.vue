<script setup>
import { ref } from 'vue'
const pairs = [
  {
    image: 'https://i.imgur.com/wRtOzkV.jpeg',
    name: 'Poy 2G.',
    role: 'FE Dev',
  },
  {
    image: 'https://i.imgur.com/vwFi6N5.jpeg',
    name: 'Gent A.',
    role: 'BE Dev',
  },
  {
    image: 'https://i.imgur.com/ypeEWRe.jpeg',
    name: 'Pora W.',
    role: 'Infra',
  },
]
const getRandomIndex = (array) => Math.floor(Math.random() * array.length)
const randomPair = ref(pairs[0])
window.addEventListener('load', () => {
  randomPair.value = pairs[getRandomIndex(pairs)]
})
</script>

<template>
  <aside
    class="w-64 px-6 flex h-screen flex-col items-center border-r bg-slate-900"
  >
    <div class="flex items-center w-full py-4 px-4 border-b border-gray-200">
      <div class="relative">
        <img
          class="w-12 h-12 rounded-full"
          :src="randomPair.image"
          alt="User Avatar"
        />
        <span
          class="top-8 left-9 absolute w-3.5 h-3.5 bg-green-400 border-2 border-white dark:border-gray-800 rounded-full"
        ></span>
      </div>
      <div class="ml-5">
        <p class="text-white font-semibold">{{ randomPair.name }}</p>

        <p class="text-gray-400 text-sm">{{ randomPair.role }}</p>
      </div>
    </div>

    <nav class="flex flex-1 flex-col items-center gap-y-4 pt-10">
      <div
        class="group relative rounded-xl p-3 text-white transition duration-300 ease-in-out"
      >
        <router-link to="/task" class="block">
          <div
            class="rounded-lg font-medium text-lg text-center group-hover:scale-105 transform transition duration-300"
          >
            Task View
          </div>
        </router-link>
      </div>

      <div
        class="group relative rounded-xl p-3 text-white transition duration-300 ease-in-out"
      >
        <router-link to="/status" class="block">
          <div
            class="rounded-lg font-medium text-lg text-center group-hover:scale-105 transform transition duration-300"
          >
            Status View
          </div>
        </router-link>
      </div>
    </nav>
  </aside>
</template>

<style scoped></style>
