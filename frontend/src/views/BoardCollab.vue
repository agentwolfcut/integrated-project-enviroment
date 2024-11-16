<script setup>
import router from "@/router";
import { useRoute } from "vue-router";
import SideBar from "@/components/SideBar.vue";
import HeaderIT from "@/components/Header.vue";
import buttonSlot from "@/components/Button.vue";
import { onMounted, ref } from "vue";
import VueJwtDecode from "vue-jwt-decode";
import AddCollab from "@/components/AddCollab.vue";

const route = useRoute();
const boardIdRoute = route.params.boardID;
const currentUser = ref("");
const showAddModal = ref(false)
const openAddModal = () => {
  showAddModal.value = true
  console.log(showAddModal.value);
}

const cancleAddCollab = () => {
  showAddModal.value = false
}

const saveAddCollab = () => {
  console.log('ok');
  // api 
  showAddModal.value = false
}

</script>

<template>
  <div class="flex">
    <SideBar :user="currentUser" />
    <div class="flex flex-col w-screen h-screen items-center bg-gray-200">
      <HeaderIT />
      <div class="flex justify-center overflow-y-scroll">
        <div class="sm:px-20 w-full">
          <div class="py-2 md:py-4 px-4 md:px-8 xl:px-10 bg-gray-200">
            <div>
              <!-- button add -->
              <div class="flex justify-between mb-9">
                <header
                  class="flex justify-center items-center text-2xl font-bold text-cyan-700"
                >
                  <div>BOARD NAME</div>
                </header>

                <div class="rounded-lg ml-4 sm:ml-8" @click=openAddModal>
                  <buttonSlot
                    size="sm"
                    type="dark"
                    class="itbkk-collaborator-add disabled:cursor-not-allowed"
                  >
                    <template v-slot:title> Add Collaborator </template>
                  </buttonSlot>
                </div>
              </div>

              <div class="mt-7 overflow-x-auto rounded-2xl">
                <table class="w-full whitespace-nowrap">
                  <!-- head -->
                  <thead
                    class="text-slate-50 text"
                    style="background-color: #15161a"
                  >
                    <tr class="focus:outline-none h-16 text-base">
                      <th
                        class="w-1/12 p-3 pl-12 text-base font-medium tracking-wide text-left"
                      >
                        No
                      </th>
                      <th
                        class="w-2/12 p-3 text-base font-medium tracking-wide text-left"
                      >
                        Name
                      </th>

                      <th
                        class="p-3 w-4/12 text-base font-medium tracking-wide text-left"
                      >
                        Email
                      </th>
                      <th
                        class="p-3 w-2/12 text-base font-medium tracking-wide text-left"
                      >
                        Access Right
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
                    <!-- <tr
                        class="itbkk-item itbkk-collab-item h-16 box ease-in transition-colors"
                        v-for="(board, index) in boards"
                        :key="index"
                      > -->
                    <tr
                      class="itbkk-item itbkk-collab-item h-16 box ease-in transition-colors"
                    >
                      <td
                        class="w-1/12 p-3 pl-12 text-black text-base font-medium tracking-wide text-left"
                      >
                        <!-- {{ index + 1 }} -->
                        1
                      </td>

                      <td class="w-2/12 p-3">
                        <div
                          class="itbkk-name text-base truncate font-medium leading-none text-gray-900 mr-2"
                        >
                          <!-- {{ board.name }} -->
                          ITBKK SIAM
                        </div>
                      </td>

                      <td class="w-4/12 p-3">
                        <div
                          class="itbkk-email text-base font-medium leading-none text-gray-700 mr-2"
                        >
                          itbkk.siam@ad.sit.kmutt.ac.th
                        </div>
                      </td>
                      <td class="w-3/12 p-3">
                        <select
                          class="itbkk-access-right text-base font-medium leading-none text-gray-700 bg-slate-50 mr-2 p-2 rounded-lg px-4"
                        >
                          <option value="read" selected>Read</option>
                          <option value="write">Write</option>
                        </select>
                      </td>
                      <td class="w-2/12 p-3">
                        <div
                          class="text-base font-medium leading-none text-gray-700 mr-2"
                        >
                          <buttonSlot
                            size="sm"
                            type="light"
                            class="itbkk-collab-remove disabled:cursor-not-allowed bg-blue-200"
                          >
                            <template v-slot:title> remove </template>
                          </buttonSlot>
                        </div>
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
  <Teleport to="#AddCollab">
    <div v-show="showAddModal">
        <AddCollab @cancleAddCollab="cancleAddCollab" @saveAddCollab="saveAddCollab"/>
    </div>
  </Teleport>
</template>

<style scoped>
.itbkk-collaborator-add {
  background-color: #260b8a;
}

.itbkk-collaborator-add:hover {
  background-color: #c7b8ea;
  /* light purple color */
}

.itbkk-collab-remove:hover {
  background-color: #0b4b8b;
  /* light purple color */
}

table {
  width: 100%;
  table-layout: fixed;
}

.box {
  transition: opacity 0.6s ease;
}

.container:hover > :not(:hover) {
  opacity: 0.2;
}
</style>
