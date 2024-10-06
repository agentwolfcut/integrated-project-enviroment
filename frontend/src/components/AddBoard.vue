<script setup>
import { ref, provide } from "vue";
import router from "@/router";
import VueJwtDecode from "vue-jwt-decode";
import { useToast } from "vue-toast-notification";
import "vue-toast-notification/dist/theme-sugar.css";
import { BoardStore } from "@/stores/Store.js";

const boardStore = BoardStore();
const newBoardName = ref('')
const toast = useToast();
const currentUser = ref("");
const token = localStorage.getItem("token");
const decoded = VueJwtDecode.decode(token);
currentUser.value = decoded.name;

provide("currentUser", currentUser);
newBoardName.value =  `${currentUser.value} personal Board`
console.log(newBoardName.value);

const addBoard = () => {
  boardStore.addBoard(newBoardName.value);
  // moackup
  localStorage.setItem('isPrivate', 'true');
  router.push('/board')
};


// can't add
</script>

<template>
  <div
    class="itbkk-modal-new absolute left-0 right-0 top-1/4 m-auto flex flex-wrap justify-center items-center"
  >
    <div
      class="px-3 lg:flex-none fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-70"
    >
      <div class="bg-white w-1/2 h-auto p-2 rounded-2xl shadow-xl">
        <p class="font-bold text-3xl text-black flex justify-center m-5">
          New Board
        </p>

        <!-- head -->
        <div class="m-4">
          <label for="title" class="font-medium text-base">Name</label>
          <input
            class="itbkk-board-name p-2 w-full bg-slate-100 flex font-medium text-xl text-black rounded-md border-slate-600 text-slate-600"
            type="text"
            maxlength="50"
            placeholder="Enter board name"
            v-model="newBoardName"
          />

          <p class="text-end text-sm font-semibold text-blue-600"></p>
        </div>

        <!-- bottom -->
        <div class="m-3">
          <div class="buttons flex gap-2">
            <button :disabled="!newBoardName"
              @click="addBoard"
              class="itbkk-button-ok disabled border border-slate-800 hover:bg-green-500 hover:text-white transition-all ease-out p-3 font-medium text-base text-green-800 bg-green-300 rounded-md px-3 disabled:opacity-50 disabled:cursor-not-allowed disabled:bg-slate-600 disabled:text-slate-900"
            >
              save
            </button>
            <button 
              @click="router.back"
              class="itbkk-button-cancel border border-slate-800 hover:bg-slate-400 hover:text-white transition-all ease-out p-3 font-medium text-base text-slate-800 bg-slate-300 rounded-md px-3"
            >
              cancel
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
