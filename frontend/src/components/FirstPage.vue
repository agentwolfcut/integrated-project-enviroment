<script setup>
import { computed, onMounted, ref, toRaw } from "vue";
import {
  addItem,
  editItem,
  getItemById,
  getItems,
  deleteItemById,
} from "../libs/fetchUtils";
import { TaskManagement } from "@/libs/TaskManagement";
import TaskList from "./TaskList.vue";
import TaskDetail from "./TaskDetail.vue";
import { createToaster } from "../../node_modules/@meforma/vue-toaster";
import HeaderIT from "./Header.vue";
import SideBar from "./SideBar.vue";
import router from "@/router";
import buttonSlot from "./Button.vue";
import Trash from "@/assets/icons/CiTrashFull.vue";
import Edit from "@/assets/icons/CiEditPencil01.vue";
import SortDown from "@/assets/icons/SortDown.vue";
import SortUp from "@/assets/icons/SortUp.vue";
import SortDefault from "@/assets/icons/SortDefault.vue";

const toaster = createToaster({
  /* options */
});

const taskMan = ref(new TaskManagement());
const showModalDetail = ref(false);
const tasks = ref({});
const taskList = taskMan.value.gettasks()
const statuses = ref({});
const tasksArray = ref([]);
const statusArray = ref([]);
const sortedTasks = ref([]);
const sortMode = ref("default"); // 'default', 'Alp', 'desc'

// GET items
onMounted(async () => {
  const taskRes = await getItems(import.meta.env.VITE_BASE_URL);
  //tasks.value = taskRes // reverse and slice to show the most
  tasks.value = taskRes;
  // Convert tasks object to array
  const clean = JSON.parse(JSON.stringify(tasks.value));
  tasksArray.value = Object.values(clean);
  // Initially sort by creation time
  sortTasksByCreationTime();

  const statusRes = await getItems(import.meta.env.VITE_BASE_URL2);
  statuses.value = statusRes;
  const status = JSON.parse(JSON.stringify(statuses.value));
  statusArray.value = Object.values(status);
  // console.log(statuses.value);
});

// SORT by STATUS

// Function to sort tasks by creation time
const sortTasksByCreationTime = () => {
  sortedTasks.value = tasksArray.value.slice().sort((a, b) => a.id - b.id); // Assuming `id` reflects creation time
  sortMode.value = "default";
  sortAlp.value = false;
  console.log(`sort mode = ${sortMode.value}`);
};
const sortAlp = ref(false);
// Function to sort tasks by status name
const sortTasksByStatusNameAlp = () => {
  sortedTasks.value = tasksArray.value
    .slice()
    .sort((a, b) => a.status.localeCompare(b.status));
};

const sortTasksByStatusNameRev = () => {
  sortedTasks.value = tasksArray.value
    .slice()
    .sort((a, b) => b.status.localeCompare(a.status));
};

// Function to toggle sorting
const toggleSortOrder = () => {
  sortAlp.value = !sortAlp.value;
  if (sortAlp.value) {
    sortMode.value = "Alp";
    console.log(sortMode.value);
    sortTasksByStatusNameAlp();
  } else {
    sortTasksByStatusNameRev();
    sortMode.value = "Rev";
    console.log(sortMode.value);
  }
};


// for modal
const selectTask = ref({
  id: undefined,
  title: "",
  description: "",
  assignees: "",
  status: 1,
  createdOn: "",
  updatedOn: "",
});

const openDetails = async (id) => {
  //console.log(id);
  const item = await getItemById(import.meta.env.VITE_BASE_URL, id);
  selectTask.value = item;
  // selectTask.value.status = selectTask.value.status.split('_').map(words => words.charAt(0).toUpperCase() + words.slice(1).toLowerCase()).join(' ')
  showModalDetail.value = true;
  // router.push(`/task/${id}`)
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

// DELETE
const showDeleteModal = ref(false);
const taskToDelete = ref(undefined);

const deleteTask = async (removeId) => {
  try {
    const status = await deleteItemById(
      import.meta.env.VITE_BASE_URL,
      removeId
    );
    if (status === 200) {
      sortedTasks.value.splice(
        sortedTasks.value.findIndex((item) => item.id === removeId)
      );
      toaster.success(`The task has been deleted`);
    } else {
      toaster.error(`The task does not exist            
            , please refresh page`);
    }
  } catch (error) {
    console.error("Error deleting task:", error);
    // Handle error as needed
    toaster.error(`Failed to delete task. An error occurred.`);
  }
};

// ADD
const saveTask = async () => {
  selectTask.value.title = selectTask.value.title.trim();
  if (selectTask.value.description !== null) {
    selectTask.value.description = selectTask.value.description.trim();
  }
  if (selectTask.value.assignees !== null) {
    selectTask.value.assignees = selectTask.value.assignees.trim();
  }
  try {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(selectTask.value),
    });
    if (!res.ok) {
      throw new Error(
        `Failed to add task. Server responded with status ${res.status}`
      );
    }
    const addedTask = await res.json();
    addedTask.status = addedTask.status.name;

    if (sortMode.value === "default") {
      sortedTasks.value.push(addedTask);
      taskMan.value.addtask(addedTask.id,addedTask.title,addedTask.description,addedTask.assignees,addedTask.status)
    } else if (sortMode.value === "Alp") {
      let index = sortedTasks.value.findIndex(
        (task) => task.status > addedTask.status
      );
      sortedTasks.value.splice(index, 0, addedTask);
      index = undefined;
    } else if (sortMode.value === "Rev") {
      let index = sortedTasks.value.findIndex(
        (task) => task.status < addedTask.status
      );
      sortedTasks.value.splice(index, 0, addedTask);
      index = undefined;
    } else {
      sortedTasks.value.push(addedTask);
    }

    // console.log(previousTask.value);
    router.back();
    toaster.success(`The ${addedTask.title} task has been successfully added`);
    selectTask.value = {
      title: "",
      description: "",
      assignees: "",
      status: 1,
    };
  } catch (error) {
    // Navigate back
    console.error("Error adding task:", error);
    // Handle error as needed
    toaster.error(`The task can't add please try again`);
  }
};

// EDIT
const editMode = (task) => {
  selectTask.value = task;
};

const updateStatusId = () => {
  const selectedStatus = statusArray.value.find(
    (status) => status.name === selectTask.value.status
  );
  if (selectedStatus) {
    selectTask.value.status = selectedStatus;
  }
};

const editTask = async () => {
  selectTask.value.title = selectTask.value.title.trim();
  if (selectTask.value.description !== null) {
    selectTask.value.description = selectTask.value.description.trim();
  }
  if (selectTask.value.assignees !== null) {
    selectTask.value.assignees = selectTask.value.assignees.trim();
  }
  updateStatusId();
  // const transformedTask = transformTaskFormat(selectTask.value);
  try {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/${selectTask.value.id}`,
      {
        method: "PUT",
        headers: {
          "content-type": "application/json",
        },
        // body: JSON.stringify({
        //   ...selectTask.value,
        // }),
        body: JSON.stringify(selectTask.value),
      }
    );
    if (!res.ok) {
      throw new Error(
        `Failed to update task. Server responded with status ${res.status}`
      );
    }
    
    router.back();
    toaster.success(`The ${selectTask.value.title} task has been successfully updated`);
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
    toaster.error(`The task can't update please try again`);
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
  toaster.error(`An error has occurred, the status does not exist.`);
};

</script>

<template>
  <!-- component -->
  <!-- img/beams.jpg -->

  <div class="flex">
    <SideBar />

    <div class="flex content flex-col items-center h-screen">
      <HeaderIT />

      <!-- <TaskList :tasks="taskMan.gettasks()" @showDetail="openDetails" @deleteC="deleteIdConfirm"
                @deleteConfirm="deleteTask"   /> -->

      <!-- Task List -->
      <div class="TaskList sm:px-20 w-full overflow-y-scroll h-5/6">
        <div class="bg-white py-2 md:py-4 px-4 md:px-8 xl:px-10">
          <div class="flex items-center mb-9">
            <div class="flex flex-1">filter</div>
            <router-link to="/task/add">
              <div class="itbkk-button-add rounded-lg ml-4 sm:ml-8">
                <buttonSlot size="sm" type="dark">
                  <template v-slot:title> Add Task </template>
                </buttonSlot>
              </div>
            </router-link>
          </div>

          <div class="task-list rounded-2xl border border-slate-100 w-full">
            <div
              class="task-list-header font-medium leading-none text-gray-700"
            >
              <div class="task-column title bg-slate-200 rounded-tl-2xl">
                <p class="mr-5">&nbsp; &nbsp; &nbsp; Title</p>
              </div>
              <div class="task-column assignees bg-slate-200">Assignees</div>
              <div
                class="task-column status bg-slate-200 flex flex-row items-center"
              >
                <div>Status</div>
                <button class="ml-6" @click="sortTasksByCreationTime">
                  <SortDefault />
                </button>
                <div @click="toggleSortOrder" class="flex items-center ml-2">
                  <button v-if="sortAlp"><SortDown /></button>
                  <button v-if="!sortAlp"><SortUp /></button>
                </div>
              </div>
              <div
                class="task-column actions bg-slate-200 rounded-tr-2xl"
              ></div>
            </div>

            <div
              class="task-list-item rounded text-base font-medium leading-none"
              v-for="(task, index) in sortedTasks"
              :key="index"
            >
              <div class="task-column flex flex-row">
                <p class="text-gray-500 mr-5">{{ index + 1 }}</p>
                <button
                  @click="openDetails(task.id)"
                  class="text-gray-700 truncate max-w-lg"
                >
                  {{ task.title }}
                </button>
              </div>
              <div class="task-column text-gray-700">
                <span v-if="task.assignees" class="truncate">{{
                  task.assignees
                }}</span>
                <span v-else class="text-slate-300 italic"> Unassigned </span>
              </div>
              <div class="task-column text-gray-700 truncate">
                {{ task.status }}
              </div>
              <div class="task-column text-gray-700">
                <router-link
                  :to="{ name: 'EditTask', params: { taskId: task.id } }"
                >
                  <button
                    @click="editMode(task)"
                    class="pr-2 itbkk-button-edit"
                  >
                    <Edit />
                  </button>
                </router-link>
                <button
                  @click="(showDeleteModal = true), (taskToDelete = task)"
                  class="pr-1 itbkk-button-delete"
                >
                  <Trash />
                </button>
              </div>
            </div>
            <!-- Repeat .task-list-item for more tasks -->
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
.task-list {
  display: grid;
  grid-template-columns: 6fr 5fr 3fr 1fr;
  width: 100%;
}

.task-list-header,
.task-list-item {
  display: contents;
}

.task-column {
  padding: 10px;
  padding-top: 1.5rem /* 8px */;
  padding-bottom: 1.5rem /* 8px */;
  box-sizing: border-box;
  border-top-width: 1px;
  --tw-border-opacity: 1;
  border-color: rgb(243 244 246 / var(--tw-border-opacity));
}
</style>
