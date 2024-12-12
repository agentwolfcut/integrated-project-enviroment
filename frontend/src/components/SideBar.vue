<script setup>
import { defineProps, ref, onMounted } from 'vue'
import VueJwtDecode from 'vue-jwt-decode';
import buttonSlot from "./Button.vue";
import router from "@/router"
import { AuthUserStore } from '@/stores/store';
import Logout from '@/assets/icons/Logout.vue';

const authUserStore = AuthUserStore()
const token = localStorage.getItem('token')
let currentUser = ref('')

if (token) {
  authUserStore.checkAccessToken()
  const decoded = VueJwtDecode.decode(token);
  if (!decoded.name) {
    currentUser.value = 'Guest user'
  }
  currentUser.value = decoded.name;

} else {
  currentUser.value = 'Guest user'
}

const signout = () => {
  currentUser.value = ''
  router.push('/login') 
  authUserStore.clearTokens
}
</script>

<template>
  <aside class="w-64 px-6 flex h-screen flex-col items-center bg-customBlue">
    <div id='profileview' class="flex items-center w-full py-8 px-5">
      <div class="ml-5">
        <p class="itbkk-fullname text-white font-semibold"> {{ currentUser }} </p>
        <p class="text-white text-sm"> Welcome
          <p class="loading loading-ring loading-xs h-3"></p>
        </p>
      </div>
    </div>

    <nav class="flex flex-1 flex-col items-center gap-y-4 pt-10">
      <div class="group relative rounded-xl p-3 text-white transition duration-300 ease-in-out">
        <router-link to="/board" class="block">
          <div class="rounded-lg font-medium text-lg text-center group-hover:scale-105 transform transition duration-300">
            Board View
          </div>
        </router-link>
      </div>

      <div class="group relative rounded-xl p-3 text-white transition duration-300 ease-in-out">
        <span class="loading loading-ball loading-xs"></span>
        <span class="loading loading-ball loading-sm"></span>
        <span class="loading loading-ball loading-md"></span>
        <span class="loading loading-ball loading-lg"></span>
      </div>
    </nav>

    <button @click="signout" class="pb-10 flex flex-row justify-center transition-transform transform hover:scale-105 active:scale-95 focus:outline-none">
      <Logout/> <span class="text-white px-3">sign out</span>
    </button>
  </aside>
</template>

<style scoped>
</style>