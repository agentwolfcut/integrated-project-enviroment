<script setup>
import SideBar from "@/components/SideBar.vue";
import HeaderIT from "@/components/Header.vue";
import { onMounted, computed } from "vue";
import buttonSlot from "@/components/Button.vue";
import "vue-toast-notification/dist/theme-sugar.css";
import { BoardStore, AuthUserStore } from "@/stores/store";
import router from "@/router";

const boardStore = BoardStore();

// Add this line to set the current board ID in the store
const setCurrentBoardId = (id) => {
  boardStore.setCurrentBoardID(id);
};

onMounted(async () => {
  await boardStore.getBoards();
});

const ownedBoards = computed(() => boardStore.ownedBoards);
const collabBoards = computed(() => boardStore.collabBoards);

</script>

<template>
  <div class="flex">
    <SideBar />
    <div class="flex flex-col w-screen h-screen items-center bg-customBg">
      <HeaderIT />
      <div class="flex justify-center overflow-y-scroll">
        <div class="sm:px-20 w-full">
          <div class="py-2 md:py-4 px-4 md:px-8 xl:px-10">
            <div>
              <!-- button add -->
              <div class="flex justify-end mb-9">
                
                <div>
                  <router-link to="/board/add" v-if="ownedBoards.length === 0">
                    <div class="rounded-lg ml-4 sm:ml-8 w-60">
                      <buttonSlot
                        size="sm"
                        type="dark"
                        class="itbkk-button-create"
                      >
                        <template v-slot:title>
                          Create personal board
                        </template>
                      </buttonSlot>
                    </div>
                  </router-link>
                </div>
              </div>

              <div class="personal-board py-3">
                
                  <div
                    class="flex justify-center font-extrabold text-2xl text-black"
                  >
                    Personal Boards
                  </div>
                

                <div class="mt-7 overflow-x-auto rounded-2xl">
                  <table class="w-full whitespace-nowrap bg">
                    <!-- head -->
                    <thead
                      class="text-slate-50 bg-customPink"
                      
                    >
                      <tr class="focus:outline-none h-16 text-base text-white">
                        <th class="w-3/12 p-3 pl-12 text-base font-bold tracking-wide text-left">No</th>
                        <th class="p-3 text-base font-bold tracking-wide text-left">Name</th>
                        <th class="p-3 w-2/12 text-base font-bold tracking-wide text-left">Visibility</th>
                      </tr>
                    </thead>
                    <!-- body content -->
                    <tbody class="container">
                      <tr
                        v-if="ownedBoards.length"
                        v-for="(board, index) in ownedBoards"
                        :key="index"
                        class="itbkk-item itbkk-personal-item h-16 box ease-in transition-colors"
                      >
                        <td class="w-3/12 p-3 pl-12 text-black text-base font-medium tracking-wide text-left">
                          {{ index + 1 }}
                        </td>
                        <td class="w-3/12 p-3 pl-5">
                          <router-link
                            :to="`/board/${board.id}`"
                            @click="setCurrentBoardId(board.id)"
                          >
                            <div class="itbkk-board-name text-base truncate font-medium leading-none text-gray-900 mr-2">
                              {{ board.name }}
                            </div>
                          </router-link>
                        </td>
                        <td class="w-2/12 p-3">
                          <div class="text-base font-medium leading-none text-gray-700 mr-2">
                            {{ board.visibility }}
                          </div>
                        </td>
                      </tr>
                      <tr v-else>
                        <td colspan="3" class="text-center text-xl font-bold py-6 text-slate-400">
                          NO BOARD PROVIDED
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>

              <div class="collab-board py-10">
                <div>
                  <div
                    class="flex justify-center font-extrabold text-2xl text-black"
                  >
                    Collab Boards
                  </div>
                </div>
                <div class="mt-7 overflow-x-auto rounded-2xl">
                  <table class="w-full whitespace-nowrap">
                    <!-- head -->
                    <thead
                      class="text-slate-50 bg-customYellow"
                    >
                      <tr class="focus:outline-none h-16 text-base text-black">
                        <th class="w-2/12 p-3 pl-12 text-base font-bold tracking-wide text-left">No</th>
                        <th class="p-3 text-base font-bold tracking-wide text-left">Name</th>
                        <th class="p-3 w-2/12 text-base font-bold tracking-wide text-left">Owner</th>
                        <th class="p-3 w-2/12 text-base font-bold tracking-wide text-left">Access Right</th>
                        <th class="p-3 w-2/12 text-base font-bold tracking-wide text-left">Action</th>
                      </tr>
                    </thead>
                    <!-- body content -->
                    <tbody class="container">
                      <tr v-if="collabBoards.length" v-for="(board, index) in collabBoards" :key="index" class="box">
                        <td class="w-2/12 p-3 pl-12 text-black text-base font-medium tracking-wide text-left">
                          {{ index + 1 }}
                        </td>
                        <td class="w-3/12 p-3 pl-5">
                          <router-link
                            :to="`/board/${board.boardId}`"
                            @click="setCurrentBoardId(board.id)">
                              <div class="itbkk-board-name text-base truncate font-medium leading-none text-gray-900 mr-2">
                                {{ board.name }}
                              </div>
                          </router-link>
                        </td>
                        <td class="w-2/12 p-3">
                          <div class="itbkk-owner-name text-base font-medium leading-none text-gray-700 mr-2">
                            Action
                          </div>
                        </td>
                        <td class="w-3/12 p-3">
                          <div class="itbkk-access-right text-base font-medium leading-none text-gray-700 mr-2">
                            READ
                          </div>
                        </td>
                        <td class="w-2/12 p-3">
                          <button
                            class="itbkk-leave-board btn-secondary p-3 font-normal bg-sky-800 text-white rounded-md"
                          >
                            leave
                          </button>
                        </td>
                      </tr>
                      <tr v-else>
                        <td colspan="5" class="text-center text-xl font-bold py-6 text-slate-400">
                          NO BOARD PROVIDED
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <router-view />
</template>

<style scoped>
.itbkk-button-create {
  background-color: #260b8a;
}

.itbkk-button-create:hover {
  background-color: #c7b8ea;
  /* light purple color */
}
.itbkk-leave-board {
  background-color: #1b527a;
}

.itbkk-leave-board:hover {
  background-color: #31a6dc;
  /* light purple color */
}

table {
  width: 100%;
  table-layout: fixed;
}

.box {
  transition: opacity 0.6s ease;
}

.box:hover {
  opacity: 0.4;
  background-color: rgb(203, 203, 203);
}

.container:hover > :not(:hover) {
  opacity: 0.2;
}
</style>