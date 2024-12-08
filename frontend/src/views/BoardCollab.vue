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
import { useUserStore } from "@/stores/UserStore";

const route = useRoute();
const boardIdRoute = route.params.boardID;
const currentUser = ref("");
const showAddModal = ref(false);
const collabStore = useCollaboratorStore();
const collaborators = ref([]);
const showDeleteModal = ref(false);
const collaboratorToRemove = ref(null);
const boardID = route.params.boardID;
const userStore = useUserStore();
const currentUserEmail = ref("");

const props = defineProps({
  boardID: {
    type: String,
    required: true,
  },
});

const fetchCollaborators = async () => {
  await collabStore.fetchCollaborators(boardIdRoute);
  collaborators.value = collabStore.collaborators;
};

onMounted(() => {
  fetchCollaborators();
  if (userStore.currentUser) {
    currentUser.value = userStore.currentUser.email;
  }
});

const openAddModal = () => {
  showAddModal.value = true;
};

const cancelAddCollab = () => {
  showAddModal.value = false;
};

const saveAddCollab = async (email, accessRight) => {
  try {
    const succuss = await collabStore.addCollaborator(
      boardIdRoute,
      email,
      accessRight
    );
    if (succuss) await fetchCollaborators();
  } catch (error) {
    console.error("Error adding collaborator:", error);
  }
  showAddModal.value = false;
};

const confirmRemoveCollab = async () => {
  if (!collaboratorToRemove.value) return;

  try {
    const success = await collabStore.removeCollaborator(
      boardIdRoute,
      collaboratorToRemove.value.oid
    );
    if (success) await fetchCollaborators();
  } catch (error) {
    console.error("Error removing collaborator:", error);
  } finally {
    showDeleteModal.value = false;
    collaboratorToRemove.value = null; // Reset
  }
};

const openDeleteModal = (collab) => {
  collaboratorToRemove.value = collab; // Store the selected collaborator
  showDeleteModal.value = true;
};

const cancelDelete = () => {
  showDeleteModal.value = false;
  collaboratorToRemove.value = null; // Reset
};
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
                          @click="openDeleteModal(collab)"
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

  <div v-if="showDeleteModal">
    <div
      class="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-50"
    >
      <div
        class="itbkk-message bg-white border-2 border-slate-200 shadow-lg rounded-2xl p-8 relative w-1/3"
      >
        <span class="text-lg font-semibold text-red-600 italic mb-12"
          >Remove Collaborator !</span
        >
        <p class="my-4 text-base font-medium overflow-y-auto">
          Do you want to remove
          <span class="text-cyan-800 italic font-bold">
            {{ collaboratorToRemove?.name }}
          </span>
          from the board ?
        </p>
        <div class="flex justify-end">
          <button
            @click="cancelDelete"
            class="itbkk-button-cancel transition-all ease-in bg-gray-300 text-gray-800 px-4 py-2 rounded mr-2 hover:bg-gray-400"
          >
            Cancel
          </button>

          <button
            @click="confirmRemoveCollab"
            class="itbkk-button-confirm transition-all ease-in bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
          >
            Confirm
          </button>
        </div>
      </div>
    </div>
  </div>

  <Teleport to="#AddCollab">
    <div v-show="showAddModal">
      <AddCollab
        :currentUserEmail="currentUserEmail"
        :boardID="boardID"
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
