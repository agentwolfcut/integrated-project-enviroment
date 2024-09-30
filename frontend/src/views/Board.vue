<script setup>
import SideBar from "@/components/SideBar.vue";
import HeaderIT from "@/components/Header.vue";
import { useRoute } from "vue-router";
import { ref, onMounted, provide, computed } from "vue";
import VueJwtDecode from "vue-jwt-decode";
import buttonSlot from "@/components/Button.vue";
import { getItems } from "../libs/fetchUtils";
import { useToast } from "vue-toast-notification";
import "vue-toast-notification/dist/theme-sugar.css";
import { BoardManagement } from "@/libs/BoardManagement";
import { BoardStore } from "@/stores/store.js";

const boardStore = BoardStore();

const toast = useToast();
const route = useRoute();
const error = ref(false);
const complete = ref(false);
const classNotify = ref("");
const textNotify = ref("");

const boardMan = ref(new BoardManagement());
const boardList = ref(boardMan.value.getBoards());

onMounted(() => {
  boardStore.getBoard();
});

const boards = computed(() => boardStore.getBoards);
</script>

<template>
  <div class="flex">
    <SideBar/>
    <div class="flex flex-col w-screen h-screen items-center bg-gray-200">
      <HeaderIT />
      <div class="flex justify-center overflow-y-scroll">
        <div class="sm:px-20 w-full">
          <div class="py-2 md:py-4 px-4 md:px-8 xl:px-10 bg-gray-200">
            <div>
              <!-- button add -->
              <div class="flex justify-end mb-9">
                <input
                  type="text"
                  placeholder="Filter by status"
                  class="input input-bordered w-full max-w-xs"
                />
                <div>
                  <router-link to="/board/add" v-if="boards.length === 0">
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

              <div class="mt-7 overflow-x-auto rounded-2xl">
                <table v-if="boards.length" class="w-full whitespace-nowrap">
                  <!-- head -->
                  <thead
                    class="text-slate-50 text"
                    style="background-color: #15161a"
                  >
                    <tr class="focus:outline-none h-16 text-base">
                      <th
                        class="w-3/12 p-3 pl-12 text-base font-medium tracking-wide text-left"
                      >
                        No
                      </th>
                      <th
                        class="p-3 text-base font-medium tracking-wide text-left"
                      >
                        Name
                      </th>

                      <th
                        class="p-3 w-2/12 text-base font-medium tracking-wide text-left"
                      >
                        Action
                      </th>
                    </tr>
                  </thead>
                  <!-- body content -->
                  <tbody class="container">
                    <tr
                      class="itbkk-item h-16 box ease-in transition-colors"
                      v-for="(board, index) in boards"
                      :key="index"
                    >
                      <td
                        class="w-3/12 p-3 pl-12 text-black text-base font-medium tracking-wide text-left"
                      >
                        {{ index + 1 }}
                      </td>

                      <td class="w-3/12 p-3 pl-5">
                        <router-link :to="`/board/${board.boardID}`">
                          <div
                            class="itbkk-status-description text-base truncate font-medium leading-none text-gray-900 mr-2"
                          >
                            {{ board.boardName }}
                          </div>
                        </router-link>
                      </td>

                      <td class="w-2/12 p-3">
                        <div
                          class="text-base font-medium leading-none text-gray-700 mr-2"
                        >
                          Action
                        </div>
                      </td>
                    </tr>
                  </tbody>
                </table>

                <div
                  v-else
                  class="flex justify-center mt-64 text-3xl font-bold"
                >
                  No board provided
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div
        v-show="error || complete"
        :class="[
          'itbkk-message absolute bottom-0 right-0 text-white font-semibold py-3 px-6 rounded-lg shadow-xl m-12',
          classNotify,
        ]"
        v-text="textNotify"
      ></div>
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

table {
  width: 100%;
  table-layout: fixed;
}

.box {
  transition: opacity 0.6s ease;
}

.box:hover{
  opacity: 0.4;
  background-color:rgb(203, 203, 203);
}

.container:hover > :not(:hover) {
  opacity: 0.2;
}
</style>
