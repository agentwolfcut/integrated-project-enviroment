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
import { useBoardPermissionStore } from "@/stores/BoardPermissionStore";
import { AuthUserStore } from "@/stores/store";

const route = useRoute();
const boardIdRoute = route.params.boardID;
const currentUser = ref("");
const showAddModal = ref(false);
const collabStore = useCollaboratorStore()
const authStore = AuthUserStore()

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
  authStore.checkAccessToken()
  try {
    await collabStore.addCollaborator(boardIdRoute, email, accessRight);
  } catch (error) {
    console.error("Error adding collaborator:", error);
  }
  showAddModal.value = false;
};

const collaborators = computed(() => collabStore.collaborators);
const boardPermissionStore = useBoardPermissionStore();
const isOwner = boardPermissionStore.isOwner;
const showChangeModAcc = ref(false);

const selectedOid = ref(null);
const selectedName = ref("");
const newAccess = ref("");

const changeAccessRight = (oid, accessRight, name) => {
  selectedOid.value = oid;
  newAccess.value = accessRight === "READ" ? "WRITE" : "READ";
  // const selectedCollab = collabStore.collaborators.find((c) => c.oid === oid);
  selectedName.value = name;
  showChangeModAcc.value = true;
};

const confirmChangeAccess = async () => {
  try {
    await collabStore.updateCollaboratorAccess(
      boardIdRoute,
      selectedOid.value,
      newAccess.value
    )
    showChangeModAcc.value = false;
    collabStore.fetchCollaborators(boardIdRoute);
  } catch (error) {
    console.error("Error updating collaborator access:", error);
  }
};

const cancelButton = () => {
  showChangeModAcc.value = false;
  showRemoveModal.value = false;
  selectedOid.value = "";
  selectedName.value = "";
};

// remove
const showRemoveModal = ref(false);
const openRemoveModal = (oid, name) => {
  selectedOid.value = oid;
  selectedName.value = name;
  showRemoveModal.value = true;
};

const confirmRemove = async () => {
  const success = await collabStore.removeCollaborator(
    collabStore.boardId,
    selectedOid.value
  );
  if (success) {
    showRemoveModal.value = false;
  }
};


const showTooltip = ref(false)

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
                    :disabled="!isOwner"
                    @mouseenter="showTooltip = !isOwner"
                    @mouseleave="showTooltip = false"
                  >
                    <template v-slot:title> Add Collaborator </template>
                  </buttonSlot>
                </div>

                <span
                        v-if="showTooltip"
                        class="tooltip fixed bottom-right-tooltip"
                      >
                        You need to be board owner or have write access to
                        perform this action
                      </span>
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
                        <button
                          @click="
                            changeAccessRight(
                              collab.oid,
                              collab.accessRight,
                              collab.name
                            )
                          "
                          class="text-base disabled:cursor-not-allowed font-medium leading-none text-gray-700 bg-slate-50 p-2 rounded-lg px-4"
                          :disabled="!isOwner"
                          @mouseenter="showTooltip = !isOwner"
                          @mouseleave="showTooltip = false"
                        >
                          {{ collab.accessRight }}
                        </button>
                      </td>
                      <td class="w-2/12 p-3">
                        <div
                          class="text-base font-medium leading-none text-gray-700 mr-2"
                        >
                          <buttonSlot
                            size="sm"
                            type="light"
                            class="itbkk-collab-remove disabled:cursor-not-allowed bg-customBeige"
                            @click="openRemoveModal(collab.oid, collab.name)"
                            :disabled="!isOwner"
                            @mouseenter="showTooltip = !isOwner"
                            @mouseleave="showTooltip = false"
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

  <div v-if="showChangeModAcc">
    <div
      class="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-50"
    >
      <div
        class="itbkk-message bg-white border-2 border-slate-200 shadow-lg rounded-2xl p-8 relative w-1/3"
      >
        <span class="text-lg font-semibold text-red-600 italic mb-12"
          >Change Access Right
        </span>
        <p class="my-4 text-base font-medium overflow-y-auto">
          Do you want to change access right of
          <span class="text-cyan-800 italic font-bold">
            {{ selectedName }}
          </span>
          to
          <span class="text-cyan-800 italic font-bold"> {{ newAccess }} </span>
        </p>
        <div class="flex justify-end">
          <button
            @click="cancelButton"
            class="itbkk-button-cancel transition-all ease-in bg-gray-300 text-gray-800 px-4 py-2 rounded mr-2 hover:bg-gray-400"
          >
            Cancel
          </button>

          <button
            @click="confirmChangeAccess"
            class="itbkk-button-confirm transition-all ease-in bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
          >
            Confirm
          </button>
        </div>
      </div>
    </div>
  </div>

  <div v-if="showRemoveModal">
    <div
      class="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-50"
    >
      <div
        class="itbkk-message bg-white border-2 border-slate-200 shadow-lg rounded-2xl p-8 relative w-1/3"
      >
        <span class="text-lg font-semibold text-red-600 italic mb-12"
          >Remove collaborator
        </span>
        <p class="my-4 text-base font-medium overflow-y-auto">
          Do you want to remove
          <span class="text-cyan-800 italic font-bold">
            {{ selectedName }}
          </span>
          from the board
        </p>
        <div class="flex justify-end">
          <button
            @click="cancelButton"
            class="itbkk-button-cancel transition-all ease-in bg-gray-300 text-gray-800 px-4 py-2 rounded mr-2 hover:bg-gray-400"
          >
            Cancel
          </button>

          <button
            @click="confirmRemove"
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
.tooltip {
  background-color: rgba(0, 0, 0, 0.5); /* Semi-transparent background */
  color: #fff; /* White text */
  padding: 10px 15px;
  border-radius: 8px;
  font-size: 14px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  position: fixed;
 
  transform: translateY(20px); /* Start slightly lower */
  animation: slideUp 3.5s both; /* Trigger animation */

  
}

.bottom-right-tooltip {
  right: 107px; /* Position 20px from the right */
  bottom: 35px; /* Position 20px from the bottom */
}

@keyframes slideUp {
  0% {
    opacity: 0;
    transform: translateY(20px);
  }
  9% {
    opacity: 1;
    transform: translateY(0);
  }
  80% {
    opacity: 1;
    transform: translateY(0);
  }
  100% {
    opacity: 0;
    transform: translateY(5px);
  }
}
</style>
