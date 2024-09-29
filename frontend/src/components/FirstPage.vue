<script setup>
import { onMounted, ref, nextTick, provide } from "vue";
import { getItems, deleteItemById } from "../libs/fetchUtils";
import { TaskManagement } from "@/libs/TaskManagement";
import TaskDetail from "./TaskDetail.vue";
import HeaderIT from "./Header.vue";
import SideBar from "./SideBar.vue";
import router from "@/router";
import buttonSlot from "./Button.vue";
import Trash from "@/assets/icons/CiTrashFull.vue";
import Edit from "@/assets/icons/CiEditPencil01.vue";
import SortDown from "@/assets/icons/SortDown.vue";
import SortUp from "@/assets/icons/SortUp.vue";
import SortDefault from "@/assets/icons/SortDefault.vue";
import { useRoute } from "vue-router";
import { get } from "@/libs/Utils";
import { BoardStore } from "@/stores/store.js";
import Toaster from "@meforma/vue-toaster/src/Toaster.vue";
import { useToast } from "vue-toast-notification";

const boardStore = BoardStore();
const taskMan = ref(new TaskManagement());
const showModalDetail = ref(false);
const statuses = ref({});
const sortedTasks = ref([]);
const sortMode = ref("default"); // 'default', 'alp', 'desc'
const sortalp = ref(false); // for toggle
const error = ref(false);
const complete = ref(false);
const classNotify = ref("");
const textNotify = ref("");
const showDeleteModal = ref(false);
const taskToDelete = ref(undefined);
const selectTask = ref({
  id: undefined,
  title: "",
  description: "",
  assignees: "",
  status: 1,
  createdOn: "",
  updatedOn: "",
});
// sem2
const token = localStorage.getItem("token");
const props = defineProps({
  boardID: {
    type: String,
    required: true,
  },
});

// GET items
onMounted(async () => {
  console.log("Received boardID :", props.boardID);
  console.log("this is firstPage");
  const taskRes = await getItems(
    `${import.meta.env.VITE_BASE_URL}/boards/${props.boardID}/tasks`,
    token
  );
  console.log(taskRes);

  taskMan.value.addtasks(taskRes);
  sortedTasks.value = taskMan.value.gettasks();
  console.log(sortedTasks.value);

  // status
  const statusRes = await getItems(
    `${import.meta.env.VITE_BASE_URL}/boards/${props.boardID}/statuses`,
    token
  );
  statuses.value = statusRes;
  statusFilter.value = statuses.value.map((status) => status.name);
  // doFilter();
});

const errorNotify = () => {
  error.value = true;
  classNotify.value = "bg-red-500";
  textNotify.value = `An error has occurred, the task does not exist.`;
  setTimeout(() => {
    error.value = false;
  }, 1000);
};

const completeNotify = (task, action) => {
  complete.value = true;
  classNotify.value = "bg-green-600";
  textNotify.value = `The task ${task} has been successfully ${action}.`;
  setTimeout(() => {
    complete.value = false;
  }, 1500);
};

const openDetails = async (id) => {
  try {
    const item = await get(
      `${import.meta.env.VITE_BASE_URL}/tasks/${id}`,
      token
    );
    selectTask.value = item;
    showModalDetail.value = true;
  } catch (error) {
    console.error("Error fetching item details:", error);
    // Handle error appropriately
  }
};

const cancel = (flag) => {
  showModalDetail.value = flag;
  selectTask.value = {
    title: "",
    description: "",
    assignees: "",
    status: 1,
  };
};
const toast = useToast()

// ADD
const saveTask = async () => {
  selectTask.value.title = selectTask.value.title.trim();
  try {
    // const res = await fetch(
    //   `${import.meta.env.VITE_BASE_URL}/boards/${props.boardID/tasks}`,
    //   {
    //     method: "POST",
    //     headers: {
    //       "Content-Type": "application/json",
    //       Authorization: `Bearer ${token}`,
    //     },
    //     body: JSON.stringify(selectTask.value),
    //   }
    // );
    // if (!res.ok) {
    //   throw new Error(
    //     `Failed to add task. Server responded with status ${res.status}`
    //   );
    // }
    const res = await boardStore.addTask(selectTask.value , props.boardID)
    const addedTask = await res.json();
    addedTask.status = addedTask.status.name;
    // sortedTasks.value.push(addedTask);
    taskMan.value.addtask(addedTask);
    sortedTasks.value = taskMan.value.gettasks();
    router.back();
    completeNotify(addedTask.title, "added");
    selectTask.value = {
      title: "",
      description: "",
      assignees: "",
      status: 1,
    };
  } catch (error){
    toast.error(error)
  }
};

// DELETE
const deleteTask = async (removeId) => {
  const removeTask = await deleteItemById(
    `${import.meta.env.VITE_BASE_URL}/tasks`,
    removeId,
    token
  );
  if (removeTask === 200) {
    completeNotify(removeId, "deleted");
    sortedTasks.value = taskMan.value.gettasks();
    sortMode.value = "default";
    taskMan.value.removetask(removeId);
  } else {
    errorNotify();
  }
};

const editMode = (task) => {
  selectTask.value = task;
};

const editTask = async (editedTask) => {
  try {
    selectTask.value = editedTask; // status : id
    selectTask.value.title = selectTask.value.title.trim();
    if (selectTask.value.description !== null) {
      selectTask.value.description = selectTask.value.description.trim();
    }
    if (selectTask.value.assignees !== null) {
      selectTask.value.assignees = selectTask.value.assignees.trim();
    }
    const selectedStatus = statuses.value.find(
      (status) => status.id == selectTask.value.status
    );
    if (selectedStatus) {
      selectTask.value.status = selectedStatus;
    }
    // const transformedTask = transformTaskFormat(selectTask.value);
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/tasks/${selectTask.value.id}`,
      {
        method: "PUT",
        headers: {
          "content-type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        // body: JSON.stringify({
        //   ...selectTask.value,
        // }),
        body: JSON.stringify(selectTask.value),
      }
    );
    if (!res.ok) {
      router.back();
      throw new Error(
        `Failed to update task. Server responded with status ${res.status}`
      );
    }
    const resJson = await res.json();
    // console.log(sortMode.value);
    if (sortMode.value === "default") {
      // console.log(`sort mode edit = ${sortMode.value}`);
    } else if (sortMode.value === "alp") {
      // console.log(`sort mode edit = ${sortMode.value}`);
    } else {
      // console.log(`sort mode edit = ${sortMode.value}`);
    }
    taskMan.value.updatetask(resJson);
    sortedTasks.value = taskMan.value.gettasks();

    router.back();
    completeNotify(selectTask.value.title, "updated");
    selectTask.value = {
      title: "",
      description: "",
      assignees: "",
      status: 1,
    };
  } catch (error) {
    // Navigate back
    console.error("Error editing task:", error);
    // Handle error as needed
    errorNotify();
  }
};

const cancelHandle = () => {
  selectTask.value = {
    id: undefined,
    title: "",
    description: "",
    assignees: "",
    status: 1,
    createdOn: "",
    updatedOn: "",
  };
};

const handelFail = () => {
  errorNotify();
};

// Filter
const showFilter = ref(false);
const toggleFilter = () => {
  showFilter.value = !showFilter.value;
};

// Assuming statuses is an array of status objects provided as a prop or fetched from an API
const statusFilter = ref([]);

const sortTasksByCreationTime = () => {
  sortedTasks.value = sortedTasks.value.slice().sort((a, b) => a.id - b.id); // Assuming `id` reflects creation time
  sortMode.value = "default";
};

const sortTasksByStatusNamealp = () => {
  sortedTasks.value = sortedTasks.value
    .slice()
    .sort((a, b) => a.status.localeCompare(b.status));
  sortMode.value = "alp";
};

const sortTasksByStatusNamerev = () => {
  sortedTasks.value = sortedTasks.value
    .slice()
    .sort((a, b) => b.status.localeCompare(a.status));
  sortMode.value = "rev";
};

const toggleSortOrder = () => {
  sortalp.value = !sortalp.value;
  if (sortalp.value) {
    sortMode.value = "alp";
    console.log(`sort mode = ${sortMode.value}`);
    sortTasksByStatusNamealp();
  } else {
    sortTasksByStatusNamerev();
    sortMode.value = "rev";
    console.log(`sort mode = ${sortMode.value}`);
  }
};
</script>

<template>
  <div class="flex">
    <SideBar />
    <!-- <div class="flex content flex-col items-center h-screen"> -->
    <div class="flex flex-col w-screen h-screen items-center bg-gray-200">
      <HeaderIT />
      <div class="flex justify-center overflow-y-scroll">
        <div class="sm:px-20 w-full">
          <div class="bg-gray-200 py-2 md:py-4 px-4 md:px-8 xl:px-10">
            <div class="overflow-x-auto">
              <div class="flex justify-end mb-9">
                <router-link :to="`/board/${props.boardID}/status`">
                  <div class="rounded-lg ml-4 sm:ml-8">
                    <buttonSlot size="sm" type="dark" class="itbkk-button-add">
                      <template v-slot:title> STATUS </template>
                    </buttonSlot>
                  </div>
                </router-link>
                <router-link :to="`/board/${props.boardID}/task/add`">
                  <div class="rounded-lg ml-4 sm:ml-8">
                    <buttonSlot size="sm" type="dark" class="itbkk-button-add">
                      <template v-slot:title> Add Task </template>
                    </buttonSlot>
                  </div>
                </router-link>
              </div>

              <div class="mt-7 overflow-x-auto rounded-2xl">
                <table class="w-full whitespace-nowrap">
                  <!-- head -->
                  <thead class="text-white" style="background-color: #15161a">
                    <tr class="focus:outline-none h-16 text-base">
                      <th
                        class="w-5/12 p-3 pl-12 text-base font-medium tracking-wide text-left"
                      >
                        Title
                      </th>
                      <th
                        class="p-3 text-base font-medium tracking-wide text-left"
                      >
                        Assignees
                      </th>
                      <th
                        class="p-3 text-base font-medium tracking-wide text-left"
                      >
                        <div
                          class="task-column status flex flex-row items-center"
                        >
                          <div>Status</div>
                          <button class="ml-6" @click="sortTasksByCreationTime">
                            <SortDefault />
                          </button>
                          <div
                            @click="toggleSortOrder"
                            class="itbkk-status-sort flex items-center ml-2"
                          >
                            <button v-if="sortalp">
                              <SortDown />
                            </button>
                            <button v-if="!sortalp">
                              <SortUp />
                            </button>
                          </div>
                        </div>
                      </th>
                      <th
                        class="w-24 p-3 text-base font-medium tracking-wide text-left"
                      ></th>
                    </tr>
                  </thead>

                  <!-- body content -->
                  <tbody class="container">
                    <tr
                      v-for="(task, index) in sortedTasks"
                      :key="index"
                      :class="{ 'bg-slate-100': index % 2 === 0 }"
                      class="itbkk-item h-16 box ease-in transition-colors"
                    >
                      <td
                        class="itbkk-title p-3 text-base font-medium text-slate-800 truncate"
                      >
                        <div class="flex flex-row">
                          <p class="pl-4">{{ index + 1 }}</p>
                          <button
                            @click="openDetails(task.id)"
                            class="pl-4 hover:underline text-base font-medium leading-none mr-4"
                          >
                            {{ task.title }}
                          </button>
                        </div>
                      </td>
                      <td
                        class="p-3 text-base font-medium text-slate-800 truncate"
                      >
                        <div
                          class="itbkk-assignees"
                          :class="{
                            'italic ': !task.assignees,
                          }"
                        >
                          <span v-if="task.assignees" class="truncate">{{
                            task.assignees
                          }}</span>
                          <span v-else class="text-red-700 italic">
                            Unassigned
                          </span>
                        </div>
                      </td>
                      <td
                        class="itbkk-status p-3 text-base font-medium text-slate-800 truncate"
                      >
                        {{ task.status.name }}
                      </td>
                      <td
                        class="itbkk-button-action p-3 text-base font-medium text-slate-800"
                      >
                        <router-link
                          :to="{
                            name: 'EditTask',
                            params: { taskId: task.id },
                          }"
                        >
                          <button
                            @click="editMode(task)"
                            class="pr-2 itbkk-button-edit"
                          >
                            <Edit />
                          </button>
                        </router-link>

                        <button
                          @click="
                            (showDeleteModal = true), (taskToDelete = task)
                          "
                          class="itbkk-button-delete pr-1"
                        >
                          <Trash />
                        </button>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="bg-gray-600 h-20">
        <div
          class="flex justify-end items-center w-11/12 my-6 sm:px-20 absolute bottom-0 right-20"
        >
          <div id="forFilter" v-if="showFilter">
            <div class="flex flex-row">
              <div class="flex">
                <!-- loop -->
                <div
                  class="flex items-center me-4"
                  v-for="(status, id) in statuses"
                  :key="id"
                >
                  <input
                    v-model="statusFilter"
                    id="filter-checkbox"
                    type="checkbox"
                    :value="status.name"
                    class="itbkk-status-choice w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"
                  />
                  <label
                    for="filter-checkbox"
                    class="ms-2 text-sm font-medium text-black dark:text-gray-300"
                    >{{ status.name }}</label
                  >
                </div>
              </div>
            </div>
          </div>
          <button
            @click="toggleFilter"
            class="itbkk-status-filter inline-flex items-center px-4 py-2 text-sm font-medium text-center text-white rounded-lg focus:ring-2 focus:ring-red-500"
            style="background-color: #ff5a99"
          >
            << Filter by Status
          </button>
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

  <div v-if="showDeleteModal">
    <div
      class="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-50"
    >
      <div
        class="itbkk-message bg-gray-600 border-2 border-slate-200 shadow-lg rounded-2xl p-8 relative w-1/3"
      >
        <p class="mb-4 text-base font-medium overflow-y-auto">
          Do you want to delete the task number {{ taskToDelete.id }} ,
          <span
            class="text-red-600 text-lg italic text-wrap hover:text-balance"
            >{{ taskToDelete.title }}</span
          >
          task?
        </p>

        <div class="flex justify-end">
          <button
            @click="showDeleteModal = false"
            class="itbkk-button-cancel transition-all ease-in bg-gray-300 text-gray-800 px-4 py-2 rounded mr-2 hover:bg-gray-400"
          >
            Cancel
          </button>

          <button
            @click="deleteTask(taskToDelete.id), (showDeleteModal = false)"
            class="itbkk-button-confirm transition-all ease-in bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
          >
            Confirm
          </button>
        </div>
      </div>
    </div>
  </div>

  <Teleport to="#ViewTask">
    <div v-show="showModalDetail">
      <TaskDetail :task="selectTask" @close-modal="cancel" />
    </div>
  </Teleport>

  <router-view
    :task="selectTask"
    :statusOptions="statuses"
    @saveUpdateTask="saveTask"
    @saveEdit="editTask"
    @cancelOpe="cancelHandle"
    @failEdit="handelFail"
  />
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
