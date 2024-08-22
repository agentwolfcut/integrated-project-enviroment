<script setup>
import { ref } from 'vue'
const pairs = [
  {
    image: 'https://i.imgur.com/wRtOzkV.jpeg',
    name: 'Poy 2G.',
    frole: 'Frontend Developer',
    fname: 'Nattawan Kumapao',
    role: 'FE Dev',
  },
  {
    image: 'https://i.imgur.com/vwFi6N5.jpeg',
    name: 'Gent A.',
    fname: 'Phurikorn Saengsuwan',
    frole: 'Backend Developer',
    role: 'BE Dev',
  },
  {
    image: 'https://i.imgur.com/ypeEWRe.jpeg',
    name: 'Pora W.',
    frole: 'Project Coordinator',
    fname: 'Poramade Winyunawan',
    role: 'Infra',
  },
]
const getRandomIndex = (array) => Math.floor(Math.random() * array.length)
const randomPair = ref(pairs[0])
window.addEventListener('load', () => {
  randomPair.value = pairs[getRandomIndex(pairs)]
})
const showProfileInfo = ref(false);
</script>

<template>




  <aside
    class=" bg-gradient-to-t from-cyan-900 w-64 px-6 flex h-screen flex-col items-center border-r bg-slate-900">
  <div id='profileview' class="flex items-center w-full p-5" @click="showProfileInfo = true">
    <div class="relative">
      <img
        class="w-12 h-12 rounded-full border-2 border-green-400"
        :src="randomPair.image"
        alt="User Avatar"
      />
      <span
        class="loading loading-infinity top-10 left-2 absolute w-8 h-8 bg-green-400 border-2 border-white dark:border-gray-800 rounded-full"
      ></span>
    </div>
    <div class="ml-5">
      <p class="text-white font-semibold">{{ randomPair.name }}</p>

      <p class="text-gray-400 text-sm ">{{ randomPair.role }}<p class="loading loading-ball loading-xs"></p></p>
    </div>
  </div>

  <transition name="fade-in">
    <div @click="showProfileInfo = false"  v-if="showProfileInfo" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
      <div class=" p-8 rounded-lg" >
        <img
          class="w-24 h-24 rounded-full border-2 border-grey mx-auto mb-4"
          :src="randomPair.image"
          alt="User Avatar"
        />
        <p class="text-center text-white font-semibold text-lg">{{ randomPair.fname }}</p>
        <p class="text-center text-white text-sm">{{ randomPair.frole }}</p>
        <div class="flex items-center justify-center">        <button @click="showProfileInfo = false" class="btn glass mt-4 px-4 py-2 bg-red-500 text-white" alt="X">close</button>
</div>
      </div>
    </div>
  </transition>


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

          <div
        class="group relative rounded-xl p-3 text-white transition duration-300 ease-in-out p-9"
      >
        <router-link to="/login" class="block">
          <div
            type="button" class="focus:outline-none text-white bg-red-900 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900">
            Logout
          </div>
        </router-link>
      </div>
  </aside>
</template>

<style scoped></style>
