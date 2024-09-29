<script setup>
import { defineProps, inject, ref , onMounted} from 'vue'
import VueJwtDecode from 'vue-jwt-decode';


let currentUser = ref('')
const token = localStorage.getItem('token')

if (token) {
    const decoded = VueJwtDecode.decode(token);
    currentUser.value = decoded.name;
}

</script>

<template>
  <aside
    class=" bg-gradient-to-t from-cyan-900 w-64 px-6 flex h-screen flex-col items-center  bg-slate-900">
  <div id='profileview' class="flex items-center w-full py-8 px-5" >
    <div class="relative">
      <!-- <img
        class="w-12 h-12 rounded-full border-2 border-green-400"
        :src="randomPair.image"
        alt="User Avatar"
      />
      <span
        class="loading loading-infinity top-10 left-2 absolute w-8 h-8 bg-green-400 border-2 border-white dark:border-gray-800 rounded-full"
      ></span> -->
    </div>
    <div class="ml-5">
      <p class="itbkk-fullname text-white font-semibold"> {{ currentUser }} </p>

      <p class="text-gray-400 text-sm ">  Welcome <p class="loading loading-ring loading-xs h-3"></p></p>
    </div>
  </div>

  <!-- <transition name="fade-in">
    <div @click="showProfileInfo = false"  v-if="showProfileInfo" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
      <div class=" p-8 rounded-lg" >
        <img
          class="w-24 h-24 rounded-full border-2 border-grey mx-auto mb-4"
          :src="randomPair.image"
          alt="User Avatar"
        />
        <p class="text-center text-white font-semibold text-lg">{{ randomPair.fname }}</p>
        <p class="text-center text-white text-sm">{{ randomPair.frole }}</p>
        <div class="flex items-center justify-center">        
          
                    <div
        class="group relative rounded-xl p-3 text-white transition duration-300 ease-in-out p-9"
      >
        <router-link to="/login" class="block">
          <div
            type="button" class="btn glass mt-4 px-4 py-0 bg-red-500 text-white">
            Logout
          </div>
        </router-link>
      </div>
          
</div>
      </div>
    </div>
  </transition> -->

    <nav class="flex flex-1 flex-col items-center gap-y-4 pt-10">
      <!-- <div
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
      </div> -->

            <div
        class="group relative rounded-xl p-3 text-white transition duration-300 ease-in-out"
      >
        <router-link to="/board" class="block">
          <div
            class="rounded-lg font-medium text-lg text-center group-hover:scale-105 transform transition duration-300"
          >
            Board View
          </div>
        </router-link>
      </div>

            <div
        class="group relative rounded-xl p-3 text-white transition duration-300 ease-in-out"
      >
<span class="loading loading-ball loading-xs"></span>
<span class="loading loading-ball loading-sm"></span>
<span class="loading loading-ball loading-md"></span>
<span class="loading loading-ball loading-lg"></span>
      </div>
    </nav>

  </aside>
</template>

<style scoped></style>