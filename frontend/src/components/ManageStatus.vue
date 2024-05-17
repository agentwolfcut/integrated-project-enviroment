<script setup>
import { onMounted, ref } from "vue";
import HeaderIT from "./Header.vue";
import {
  getItems,
  transferTasksAndDeleteStatus,
  deleteItemById,
  editItem,
} from "../libs/fetchUtils";
import { StatusManagement } from "@/libs/StatusManagement";
import SideBar from "./SideBar.vue";
import buttonSlot from "./Button.vue";
import Trash from "@/assets/icons/CiTrashFull.vue";
import Edit from "@/assets/icons/CiEditPencil01.vue";
import router from "@/router";
import { createToaster } from "../../node_modules/@meforma/vue-toaster";

const toaster = createToaster({
  /* options */
});
const statusMan = ref(new StatusManagement());

// GET
onMounted(async () => {
  const statusRes = await getItems(import.meta.env.VITE_BASE_URL2);
  statusMan.value.addStatuses(statusRes);
});

const statusList = ref(statusMan.value.getStatuses());

// ADD
const editingStatus = ref({ id: undefined, name: "", description: "" });
const addStatus = async () => {
  try {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL2}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(editingStatus.value),
    });
    if (!res.ok) {
      throw new Error(
        `Failed to add status. Server responded with status ${res.status}`
      );
    }
    const addedItem = await res.json(); //respondจากbackend  ยังไม่ได้ใช้เพราะidที่ส่งมาผิด
    editingStatus.value = { id: undefined, name: "", description: "" };
    // console.log(previousTask.value);

    statusMan.value.addStatus(
      addedItem.id,
      addedItem.name,
      addedItem.description
    );
    router.back();
    toaster.success(`The ${addedItem.name} status has been added`);
  } catch (error) {
    // Navigate back
    console.error("Error adding status:", error);
    // Handle error as needed
    toaster.error(`An error has occurred, the status could not be added.`);
  }
};

// DELETE
const destinationStatusId = ref(null);
const hasTasksToTransfer = ref(false);
const confirmDelete = ref(false);
const statusDelete = ref(undefined);
const confirmTransfer = ref(false);

const tasks = ref("");

const checkTasksBeforeDelete = async (status) => {
  console.log(status.name);
  const res = await getItems(import.meta.env.VITE_BASE_URL);
  tasks.value = { ...res };
  // console.log(tasks.value);
  const clean = JSON.parse(JSON.stringify(tasks.value));
  // console.log(clean);
  const tasksArray = Object.values(clean);
  // console.log(tasksArray);
  const statusToCheck = status.name;
  const filteredTask = tasksArray.filter(
    (item) => item.status === statusToCheck
  );
  const count = filteredTask.length;
  if (count > 0) {
    console.log(`makkwa = ${count}`);
    confirmTransfer.value = true;
  } else {
    console.log(`object = ${count}`);
    confirmDelete.value = true;
  }
};

const deleteStatus = async () => {
  // back
  const removeId = statusDelete.value.id;
  const removeStatus = await deleteItemById(
    import.meta.env.VITE_BASE_URL2,
    removeId
  );
  // front
  if (removeStatus === 200) {
    statusMan.value.removeStatus(removeId);
    toaster.success(`The ${statusDelete.value.name} status has been deleted`);
  } else {
    toaster.error(
      `An error has occurred, The status could not be delete , please refresh page.`
    );
  }
  confirmDelete.value = false;
};

const transferAndDeleteStatus = async () => {
  try {
    const removeId = statusDelete.value.id;
    const destinationId = destinationStatusId.value;
    const result = await transferTasksAndDeleteStatus(
      import.meta.env.VITE_BASE_URL2,
      removeId,
      destinationId
    );
    if (result === 200) {
      statusMan.value.removeStatus(removeId);
      toaster.success(
        `The tasks have been transferred and the status has been deleted.`
      );
    } else if (result === 404) {
      toaster.error(`An error has occurred, the status does not exist.`);
    }
  } catch (error) {
    console.error("Error transferring tasks and deleting status:", error);
    toaster.error(
      `Failed to transfer tasks and delete status. Please try again later.`
    );
  }
  confirmTransfer.value = false;
  destinationStatusId.value = null;
};

// UPDATE

const updateStatus = async (editStatus) => {
  try {
    const editedItem = await editItem(
      import.meta.env.VITE_BASE_URL2,
      editStatus.id,
      editStatus
    );
    statusMan.value.updateStatus(
      editStatus.id,
      editStatus.name,
      editStatus.description
    );
    toaster.success(`The ${editStatus.name} status has been updated`);
    // Update parent component's data
    editingStatus.value = { id: undefined, name: "", description: "" };
    router.back(-1);
  } catch (error) {
    // Handle error
    console.error("Error updating status:", error);
    toaster.error("Failed to update status. Please try again later.");
  }
};

const openToEdit = (status) => {
  editingStatus.value = status;
};
const clearEdit = () => {
  editingStatus.value = { id: undefined, name: "", description: "" };
};
</script>

<template>
  <div class="flex">
    <SideBar />
    <div class="flex flex-col w-screen h-screen items-center">
      <HeaderIT />
      <div class="flex justify-center">
        <div class="sm:px-20 w-full">
          <div class="bg-white py-2 md:py-4 px-4 md:px-8 xl:px-10">
            <div class="overflow-x-auto">
              <!-- button add -->
              <div class="flex justify-end mb-9">
                <router-link to="/status/add">
                  <div class="rounded-lg ml-4 sm:ml-8">
                    <buttonSlot size="sm" type="dark" class="itbkk-button-add">
                      <template v-slot:title> Add Status </template>
                    </buttonSlot>
                  </div>
                </router-link>
              </div>

              <div
                class="mt-7 overflow-x-auto rounded-2xl border border-gray-100"
              >
                <table class="w-full whitespace-nowrap">
                  <!-- head -->
                  <thead class="bg-slate-200 text">
                    <tr class="focus:outline-none h-16 text-base">
                      <td>
                        <div class="flex items-center pl-5">
                          <p
                            class="font-medium leading-none text-gray-700 ml-6"
                          >
                            Name
                          </p>
                        </div>
                      </td>
                      <td></td>
                      <td>
                        <div
                          class="font-medium leading-none text-gray-700 mr-2"
                        >
                          Description
                        </div>
                      </td>
                      <td></td>
                      <td>
                        <div
                          class="px-10 font-medium leading-none text-gray-700 mr-2"
                        >
                          Action
                        </div>
                      </td>
                    </tr>
                  </thead>

                  <!-- body content -->
                  <tbody class="container">
                    <tr
                      v-for="(status, index) in statusList"
                      :key="index"
                      class="itbkk-item box h-16 border-t border-gray-100 rounded"
                    >
                      <td class="min-w-60">
                        <div class="flex items-center pl-5">
                          <div class="flex flex-row justify-start">
                            <p
                              class="text-base font-medium leading-none text-gray-700 mr-4"
                            >
                              {{ index + 1 }}
                            </p>
                          </div>

                          <button
                            class="itbkk-title text-base font-medium leading-none text-gray-700 mr-4"
                          >
                            {{ status.name }}
                          </button>
                        </div>
                      </td>
                      <td></td>
                      <td class="itbkk-status-description">
                        <div
                          v-if="status.description"
                          class="text-base font-medium leading-none text-gray-700 mr-2"
                        >
                          {{ status.description }}
                        </div>
                        <div
                          v-else
                          class="text-base font-normal italic leading-none text-gray-400 mr-2"
                        >
                          No Description Provided
                        </div>
                      </td>

                      <td></td>
                      <td class="itbkk-status-action px-10">
                        <div
                          class="text-base font-medium leading-none text-gray-700 mr-2"
                        >
                          <router-link
                            :to="{
                              name: 'EditStatus',
                              params: { id: status.id },
                            }"
                          >
                            <button
                              class="pr-2 itbkk-button-edit"
                              @click="openToEdit(status)"
                            >
                              <Edit />
                            </button>
                          </router-link>

                          <button
                            class="pr-1 itbkk-button-delete"
                            @click="
                              (statusDelete = status),
                                checkTasksBeforeDelete(status)
                            "
                          >
                            <Trash />
                          </button>
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

  <router-view
    :status="editingStatus"
    @saveStatus="addStatus"
    @saveEdit="updateStatus"
    @cancelEdit="clearEdit"
  />

  <div v-if="confirmDelete">
    <div
      class="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-50"
    >
      <div
        class="itbkk-message bg-white border-2 border-slate-200 shadow-lg rounded-2xl p-8 relative w-1/3"
      >
        <div class="mb-4 text-base font-medium overflow-y-auto">
          <p>Do you want to delete the status "{{ statusDelete.name }}"?</p>
        </div>

        <div class="flex justify-end mt-4">
          <button
            @click="confirmDelete = false"
            class="itbkk-button-cancel transition-all ease-in bg-gray-300 text-gray-800 px-4 py-2 rounded mr-2 hover:bg-gray-400"
          >
            Cancel
          </button>
          <button
            @click="deleteStatus"
            class="itbkk-button-confirm transition-all ease-in bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 disabled:cursor-not-allowed disabled:bg-slate-600 disabled:text-slate-900"
          >
            Confirm
          </button>
        </div>
      </div>
    </div>
  </div>

  <div v-if="confirmTransfer">
    <div
      class="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-50"
    >
      <div
        class="itbkk-message bg-white border-2 border-slate-200 shadow-lg rounded-2xl p-8 relative w-1/3"
      >
        <div class="mb-4 text-base font-medium overflow-y-auto">
          <p>
            There are tasks in
            <span class="text-red-600 italic">{{ statusDelete.name }}</span>
            status. To delete this status, please transfer tasks to an other
            status.
          </p>
        </div>
        <div>
          <select
            v-model="destinationStatusId"
            class="w-full p-2 border rounded"
          >
            <option value="" selected disabled>Select destination status</option>
            <option
              :disabled="(statusDelete.name === status.name)"
              v-for="status in statusList"
              :key="status.id"
              :value="status.id"
            >
              {{ status.name }}
            </option>
          </select>
        </div>
        <div class="flex justify-end mt-4">
          <button
            @click="confirmTransfer = false"
            class="itbkk-button-cancel transition-all ease-in bg-gray-300 text-gray-800 px-4 py-2 rounded mr-2 hover:bg-gray-400"
          >
            Cancel
          </button>
          <button
            @click="transferAndDeleteStatus(destinationStatusId)"
            :disabled="hasTasksToTransfer && !destinationStatusId"
            class="itbkk-button-confirm transition-all ease-in bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 disabled:cursor-not-allowed disabled:bg-slate-600 disabled:text-slate-900"
          >
            Transfer and Delete
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.itbkk-button-add {
  background-color: #260b8a;
}

.itbkk-button-add:hover {
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

.container:hover > :not(:hover) {
  opacity: 0.2;
}
</style>