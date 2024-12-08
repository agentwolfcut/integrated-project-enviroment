<script setup>
import router from "@/router";
import { useRoute } from "vue-router";
import SideBar from "@/components/SideBar.vue";
import HeaderIT from "@/components/Header.vue";
import buttonSlot from "@/components/Button.vue";
import { onMounted, ref, computed } from "vue";
import VueJwtDecode from "vue-jwt-decode";
import AddCollab from "@/components/AddCollab.vue";
import { useCollaboratorStore } from "@/stores/CollaboratorStore";
import GoBack from "@/assets/icons/GoBack.vue";

const route = useRoute();
const boardIdRoute = route.params.boardID;
const currentUser = ref("");
const showAddModal = ref(false);
const collabStore = useCollaboratorStore();

onMounted(() => {
  collabStore.fetchCollaborators(boardIdRoute);
});

const openAddModal = () => {
  showAddModal.value = true;
};

const cancelAddCollab = () => {
  showAddModal.value = false;
};

const saveAddCollab = async (email, accessRight) => {
  try {
    await collabStore.addCollaborator(boardIdRoute, email, accessRight);
  } catch (error) {
    console.error("Error adding collaborator:", error);
  }
  showAddModal.value = false;
};

// Computed property to get the list of collaborators
// const collaborators = computed(() => store.collaborators);
const collaborators = collabStore.collaborators;
</script>

<template>
  <div class="flex">
    <SideBar :user="currentUser" />
    <div class="flex flex-col w-screen h-screen items-center bg-customBg">
      <HeaderIT />
      <div class="flex justify-center overflow-y-scroll">
        <div class="sm:px-20 w-full">
          <div class="py-2 md:py-4 px-4 md:px-8 xl:px-10">
            <div>
              <!-- Header and Add Collaborator button -->
              <div class="flex justify-between mb-9">
                <header
                  class="flex justify-center items-center text-2xl font-bold text-cyan-700"
                >
                  <div class="text-black text-xl font-bold">BOARD NAME</div>
                </header>

                <div class="rounded-lg ml-4 sm:ml-8" @click="openAddModal">
                  <buttonSlot
                    size="sm"
                    type="dark"
                    class="itbkk-collaborator-add disabled:cursor-not-allowed"
                  >
                    <template v-slot:title> Add Collaborator </template>
                  </buttonSlot>
                </div>
              </div>

              <!-- Table with scroll behavior -->
              <div class="mt-7 overflow-auto max-h-[500px] rounded-2xl">
                <table class="w-full whitespace-nowrap">
                  <!-- Table head -->
                  <thead class="text-black font-bold bg-customYellow">
                    <tr class="focus:outline-none h-16 text-base">
                      <th class="w-1/12 p-3 pl-12 tracking-wide text-left">
                        No
                      </th>
                      <th class="w-2/12 p-3 tracking-wide text-left">Name</th>
                      <th class="p-3 w-4/12 tracking-wide text-left">Email</th>
                      <th class="p-3 w-2/12 tracking-wide text-left">
                        Access Right
                      </th>
                      <th class="p-3 w-2/12 tracking-wide text-left">Action</th>
                    </tr>
                  </thead>
                  <!-- Table body -->
                  <tbody class="container">
                    <tr
                      v-for="(collab, index) in collaborators"
                      :key="index"
                      :class="{ 'bg-white': index % 2 === 0 }"
                      class="itbkk-item itbkk-collab-item h-16 box ease-in transition-colors"
                    >
                      <td
                        class="w-1/12 p-3 pl-12 text-black text-base font-medium tracking-wide text-left"
                      >
                        {{ index + 1 }}
                      </td>
                      <td class="w-2/12 p-3">
                        <div
                          class="itbkk-name text-base truncate font-medium leading-none text-gray-900 mr-2"
                        >
                          {{ collab.name }}
                        </div>
                      </td>
                      <td class="w-4/12 p-3">
                        <div
                          class="itbkk-email text-base font-medium leading-none text-gray-700 mr-2"
                        >
                          {{ collab.email }}
                        </div>
                      </td>
                      <td class="w-3/12 p-3">
                        <div
                          class="text-base font-medium leading-none text-gray-700 bg-slate-50 p-2 rounded-lg px-4"
                        >
                          {{ collab.accessRight }}
                        </div>
                      </td>
                      <td class="w-2/12 p-3">
                        <div
                          class="text-base font-medium leading-none text-gray-700 mr-2"
                        >
                          <buttonSlot
                            size="sm"
                            type="light"
                            class="itbkk-collab-remove disabled:cursor-not-allowed bg-customBeige"
                          >
                            <template v-slot:title> remove </template>
                          </buttonSlot>
                        </div>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>

              <!-- Go Back Button -->
              <div class="flex justify-start mt-6">
                <button
                  class="itbkk-go-back px-4 py-2 transition-transform transform hover:scale-125 active:scale-95 focus:outline-none"
                  @click="router.back()"
                >
                  <GoBack />
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <Teleport to="#AddCollab">
    <div v-show="showAddModal">
      <AddCollab
        @cancleAddCollab="cancelAddCollab"
        @saveAddCollab="saveAddCollab"
      />
    </div>
  </Teleport>
</template>

<style scoped>
.itbkk-collaborator-add {
  background-color: #db3069;
}
.itbkk-collaborator-add:hover {
  background-color: #e6abbe;
}

.itbkk-collab-remove:hover {
  background-color: #4e4e46;
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
