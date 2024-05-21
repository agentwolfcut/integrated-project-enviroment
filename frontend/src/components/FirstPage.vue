<script setup>
import { onMounted, ref, watch, nextTick } from "vue";
import { getItemById, getItems, deleteItemById } from "../libs/fetchUtils";
import { TaskManagement } from "@/libs/TaskManagement";
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
// const taskList = taskMan.value.gettasks();
const statuses = ref({});
const sortedTasks = ref([]);
const sortMode = ref("default"); // 'default', 'alp', 'desc'
const sortalp = ref(false); // for toggle

// GET items
onMounted(async () => {
  const taskRes = await getItems(`${import.meta.env.VITE_BASE_URL}/tasks`);
  //tasks.value = taskRes // reverse and slice to show the most
  taskMan.value.addtasks(taskRes) 
  sortedTasks.value = taskMan.value.gettasks()
  tasks.value = taskRes;
  // status
  const statusRes = await getItems(`${import.meta.env.VITE_BASE_URL}/statuses`);
  statuses.value = statusRes;

});

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
  const item = await getItemById(`${import.meta.env.VITE_BASE_URL}/tasks`, id);
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
      `${import.meta.env.VITE_BASE_URL}/tasks`,
      removeId
    );
    if (status === 200) {
      sortedTasks.value.splice(
        sortedTasks.value.findIndex((item) => item.id === removeId)
      );
      taskMan.value.removetask(removeId)
      sortedTasks.value = taskMan.value.gettasks()
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
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/tasks`, {
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
    // sortedTasks.value.push(addedTask);
    taskMan.value.addtask(addedTask)
    sortedTasks.value = taskMan.value.gettasks()
    // if (sortMode.value === "default") {
    //   sortedTasks.value.push(addedTask);
    //   taskMan.value.addtask(
    //     addedTask.id,
    //     addedTask.title,
    //     addedTask.description,
    //     addedTask.assignees,
    //     addedTask.status
    //   );
    // } else if (sortMode.value === "alp") {
    //   let index = sortedTasks.value.findIndex(
    //     (task) => task.status > addedTask.status
    //   );
    //   console.log(`index =  ${index}`);
    //   sortedTasks.value.splice(index, 0, addedTask);
    //   index = undefined;
    // } else if (sortMode.value === "rev") {
    //   let index = sortedTasks.value.findIndex(
    //     (task) => task.status < addedTask.status
    //   );
    //   console.log(`index =  ${index}`);
    //   sortedTasks.value.splice(index, 0, addedTask);
    //   index = undefined;
    // }
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
    const resJson = await res.json();
    console.log(sortMode.value);
    if (sortMode.value === 'default') {
      console.log(`sort mode edit = ${sortMode.value}`);
    } else if(sortMode.value === 'alp') {
      console.log(`sort mode edit = ${sortMode.value}`);
    } else {
      console.log(`sort mode edit = ${sortMode.value}`);
    }
    taskMan.value.updatetask(resJson);
    sortedTasks.value = taskMan.value.gettasks();

    router.back();
    toaster.success(
      `The ${selectTask.value.title} task has been successfully updated`
    );
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

// Filter
const showFilter = ref(false);
const toggleFilter = () => {
  showFilter.value = !showFilter.value;
};

// Assuming statuses is an array of status objects provided as a prop or fetched from an API
const statusFilter = ref([]);

// Fetch statuses and initialize statusFilter
const fetchStatuses = async () => {
  try {
    const fetchedStatuses = await getItems(
      `${import.meta.env.VITE_BASE_URL}/statuses`
    );
    statuses.value = fetchedStatuses;
    statusFilter.value = statuses.value.map((status) => status.name);
    doFilter(); // Ensure the initial filter is applied
  } catch (error) {
    console.error("Failed to fetch statuses:", error);
  }
};

// Apply the filter whenever statusFilter changes
const doFilter = async () => {
  await nextTick(); // Ensure the next DOM update cycle is completed
  if (statusFilter.value.length > 0) {
    const statusString = statusFilter.value.join(",");
     // Logs the comma-separated string of selected statuses
    const res = await getItems(
      `${import.meta.env.VITE_BASE_URL}/tasks?statuses=${statusString}`
    );
    sortedTasks.value = res;
    if (sortMode.value) {
      switch (sortMode.value) {
        case "alp":
          sortTasksByStatusNamealp();
          break;
        case "rev":
          sortTasksByStatusNamerev();
          break;
        case "default":
          sortTasksByCreationTime();
          break;
      }
    } 
  } else {
    console.log("No statuses selected");
    sortedTasks.value = [];
  }
};

// Fetch statuses when the component is mounted
onMounted(fetchStatuses);

// Watch for changes in statusFilter to trigger filtering
watch(statusFilter, doFilter);

// SORT by STATUS

// Function to sort tasks by creation time
const sortTasksByCreationTime = () => {
  sortedTasks.value = sortedTasks.value.slice().sort((a, b) => a.id - b.id); // Assuming `id` reflects creation time
  sortMode.value = "default";
  console.log(`sort mode = ${sortMode.value}`)
};

// Function to sort tasks by status name
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

// Function to toggle sorting
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
  <!-- component -->
  <!-- img/beams.jpg -->

  <div class="flex">
    <SideBar />
    <div class="flex content flex-col items-center h-screen">
      <HeaderIT />
      <!-- Task List -->
      <div class="TaskList sm:px-20 overflow-y-scroll h-3/4 w-11/12">
        <div class="bg-white py-2 md:py-4 px-4 md:px-8 xl:px-10">
          <div class="flex items-center justify-end mb-9">
            <router-link to="/task/add">
              <div
                class="itbkk-button-add rounded-lg ml-4 sm:ml-8 flex flex-initial"
              >
                <buttonSlot size="sm" type="dark">
                  <template v-slot:title> Add Task </template>
                </buttonSlot>
              </div>
            </router-link>
          </div>

          <div
            class="task-list rounded-2xl border border-slate-100 justify-center table-fixed"
          >
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
                  <button v-if="sortalp">
                    <SortDown />
                  </button>
                  <button v-if="!sortalp">
                    <SortUp />
                  </button>
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

      <div class="flex justify-end w-11/12 mt-6 sm:px-32">
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
                  @click="doFilter"
                  class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"
                />
                <label
                  for="filter-checkbox"
                  class="ms-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                  >{{ status.name }}</label
                >
              </div>
            </div>
          </div>
        </div>
        <button
          @click="toggleFilter"
          class="inline-flex items-center px-4 py-2 text-sm font-medium text-center text-white bg-blue-700 rounded-lg hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
        >
          << Filter by Status
        </button>
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
  grid-template-columns: 5fr 4fr 3fr 2fr;
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
