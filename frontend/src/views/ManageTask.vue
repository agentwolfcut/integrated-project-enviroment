<script setup>
import { onMounted, ref, computed } from "vue";
import { deleteItemById, getItemById } from "../libs/fetchUtils";
import { TaskManagement } from "@/libs/TaskManagement";
import TaskDetail from "@/components/TaskDetail.vue";
import HeaderIT from "@/components/Header.vue";
import SideBar from "@/components/SideBar.vue";
import router from "@/router";
import buttonSlot from "@/components/Button.vue";
import Trash from "@/assets/icons/CiTrashFull.vue";
import Edit from "@/assets/icons/CiEditPencil01.vue";
import SortDown from "@/assets/icons/SortDown.vue";
import SortUp from "@/assets/icons/SortUp.vue";
import SortDefault from "@/assets/icons/SortDefault.vue";
import { put } from "@/libs/Utils";
import { useToast } from "vue-toast-notification";
import { useRoute, useRouter } from "vue-router";
import { useTaskStore } from "@/stores/TaskStore.js";
import { useStatusStore } from "@/stores/StatusStore";
import "vue-toast-notification/dist/theme-sugar.css";
import { useVisibilityStore } from "@/stores/VisibilityStore";
import { BoardStore, AuthUserStore } from "@/stores/store";
import { useBoardPermissionStore } from "@/stores/BoardPermissionStore.js";

const toast = useToast();
const taskStore = useTaskStore();
const statusStore = useStatusStore();
const boardStore = BoardStore();
const route = useRoute();
const taskMan = ref(new TaskManagement());
const showModalDetail = ref(false);
const statuses = ref([]);
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
  id: "",
  title: "",
  description: "",
  assignees: "",
  statusId: "",
});
// sem2
// const token = localStorage.getItem("token");
const props = defineProps({
  //  จาก route
  boardID: {
    type: String,
    required: true,
  },
});
const boardIdRoute = route.params.boardID;
const boardPermissionStore = useBoardPermissionStore();
const visibilitys = ref("");
const useAuthUserStore = AuthUserStore();
const showTooltip = ref(false);
useAuthUserStore.checkAccessToken();
const token = useAuthUserStore.token;

onMounted(async () => {
  await boardPermissionStore.fetchBoardById(`/boards/${boardIdRoute}`, "GET");
  if (!boardPermissionStore.hasAccess) {
    console.log(boardPermissionStore.hasAccess);
    toast.error(
      "Access denied. You do not have permission to view this board."
    );

    router.push("/test"); // Redirect if no permission
  } else {
    visibilitys.value = boardPermissionStore.boardDetails.visibility;
    try {
      await taskStore.fetchTask(boardIdRoute);
      const tasks = taskStore.tasks;
      taskMan.value.addtasks(tasks);
      sortedTasks.value = taskMan.value.gettasks();
      // status
      await statusStore.fetchStatus(boardIdRoute);
      statuses.value = statusStore.statuses;
      statusFilter.value = statuses.value.map((status) => status.name);
    } catch (error) {
      console.error(error);
    }
  }
});

const openDetails = async (id) => {
  try {
    // const task = await taskStore.getTaskById(id,boardIdRoute)
    const task = await getItemById(
      `${import.meta.env.VITE_BASE_URL}/boards/${boardIdRoute}/tasks/${id}`
    );
    selectTask.value = task;
    showModalDetail.value = true;
  } catch (error) {
    toast.error(error);
  }
};

const cancel = (flag) => {
  showModalDetail.value = flag;
  selectTask.value = {
    title: "",
    description: "",
    assignees: "",
    statusId: "",
  };
};

// ADD wait for API
const saveTask = async () => {
  selectTask.value.title = selectTask.value.title.trim();
  try {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/boards/${boardIdRoute}/tasks`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(selectTask.value),
      }
    );
    if (!res.ok) {
      throw new Error(
        `Failed to add task. Server responded with status ${res.status}`
      );
    }
    // const res = await boardStore.addTask(selectTask.value, props.id);
    const addedTask = await res.json();
    // {"title":"zxzxzx","description":null,"assignees":null,"statusId":328}
    const status = statusStore.getStatusById(addedTask.statusId);
    addedTask.statusId = status;
    // sortedTasks.value.push(addedTask);
    taskMan.value.addtask(addedTask);
    sortedTasks.value = taskMan.value.gettasks();
    router.back();
    toast.success(`${addedTask.title} added`);
    selectTask.value = {
      title: "",
      description: "",
      assignees: "",
      statusId: "",
    };
  } catch (error) {
    toast.error(`this is FirstPage : ${error}`);
  }
};

const deleteTask = async (removeId) => {
  // taskStore.fetchTask(boardIdRoute);
  const removeTask = await deleteItemById(
    `${import.meta.env.VITE_BASE_URL}/boards/${boardIdRoute}/tasks`,
    removeId,
    token
  );
  if (removeTask === 200) {
    toast.success(`The task ${removeId} has been successfully deleted`);
    sortedTasks.value = taskMan.value.gettasks();
    sortMode.value = "default";
    taskMan.value.removetask(removeId);
  } else {
    toast.error("An error has occurred, the task does not exist.");
  }
};

const editMode = (task) => {
  selectTask.value = task;
};

const editTask = async (editedTask) => {
  try {
    // don't trim yet
    selectTask.value = editedTask; // status : id
    selectTask.value.title = selectTask.value.title.trim();

    const selectedStatus = statuses.value.find(
      (status) => status.id == selectTask.value.status
    );
    // ตรวจสอบว่าพบ status ที่ถูกเลือก และตั้งค่า statusId ให้ตรงกับ backend
    if (selectedStatus) {
      selectTask.value.statusId = selectedStatus.id;
    }
    // ลบค่า status และ id เดิม เพื่อไม่ให้ส่งไปใน payload
    delete selectTask.value.status;
    // ส่งข้อมูลที่มี statusId ไปยัง backend
    const res = await put(
      `${import.meta.env.VITE_BASE_URL}/boards/${boardIdRoute}/tasks/${
        editedTask.id
      }`,
      selectTask.value,
      token
    );

    // const resJson = await res.json();
    console.log(res);
    taskMan.value.updatetask(res);
    sortedTasks.value = taskMan.value.gettasks();
    router.back();
    toast.success(
      `The task ${selectTask.value.title} has been successfully updated`
    );

    // รีเซ็ต selectTask
    selectTask.value = {
      title: "",
      description: "",
      assignees: "",
      statusId: "",
    };
  } catch (error) {
    toast.error(error);
  }
};

const cancelHandle = () => {
  selectTask.value = {
    id: undefined,
    title: "",
    description: "",
    assignees: "",
    statusId: "",
  };
};

const handelFail = () => {
  toast.error("this is error");
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
    sortTasksByStatusNamealp();
  } else {
    sortTasksByStatusNamerev();
    sortMode.value = "rev";
  }
};

// visibility
// const isPrivate = ref(true); // ค่าเริ่มต้นของ visibility (true = private, false = public)
const isPrivate = computed(() => visibilitys.value === "PRIVATE");
const showModalVis = ref(false);
const visibilityStore = useVisibilityStore();
const isOwner = boardPermissionStore.isOwner;

const toggleVisibility = async () => {
  if (!boardPermissionStore.isOwner) {
    toast.error("You do not have permission to change visibility.");
    return;
  }
  showModalVis.value = true;
  console.log(
    `visible is ${visibilitys.value} and isPrivate is ${isPrivate.value}`
  );
};

const confirmChange = async () => {
  const newVisibility = visibilitys.value === "PRIVATE" ? "PUBLIC" : "PRIVATE";
  try {
    await boardPermissionStore.updateVisibility(boardIdRoute, newVisibility);
    visibilitys.value = newVisibility;
  } catch (error) {
    toast.error("Failed to update visibility.");
  }
  showModalVis.value = false;
};
</script>

<template>
  <div class="flex">
    <SideBar />
    <!-- <div class="flex content flex-col items-center h-screen"> -->
    <div class="flex flex-col w-screen h-screen items-center bg-customBg">
      <HeaderIT />
      <div class="flex justify-center overflow-y-scroll">
        <div class="sm:px-20 w-full">
          <div class="py-2 md:py-4 px-4 md:px-8 xl:px-10">
            <div class="overflow-x-auto">
              <div class="for-button mb-9 flex flex-row justify-between">
                <button
                  @click="toggleVisibility"
                  :disabled="!isOwner"
                  @mouseenter="showTooltip = !isOwner"
                  @mouseleave="showTooltip = false"
                  class="itbkk-board-visibility flex items-center disabled:cursor-not-allowed rounded-full w-10 h-10 justify-center"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 24 24"
                    fill="currentColor"
                    class="size-6"
                  >
                    <path
                      fill-rule="evenodd"
                      d="M12 1.5a5.25 5.25 0 0 0-5.25 5.25v3a3 3 0 0 0-3 3v6.75a3 3 0 0 0 3 3h10.5a3 3 0 0 0 3-3v-6.75a3 3 0 0 0-3-3v-3c0-2.9-2.35-5.25-5.25-5.25Zm3.75 8.25v-3a3.75 3.75 0 1 0-7.5 0v3h7.5Z"
                      clip-rule="evenodd"
                    />
                  </svg>
                </button>

                <div class="flex flex-row">
                  <!-- รอ component  -->


                  <router-link :to="`/board/${boardIdRoute}/collab`">
                    <div class="rounded-lg ml-4 sm:ml-8">
                      <buttonSlot
                        size="sm"
                        type="light"
                        class="itbkk-manage-collaborator"
                      >
                        <template v-slot:title> COLLABORATOR </template>
                      </buttonSlot>
                    </div>
                  </router-link>

                  <router-link :to="`/board/${boardIdRoute}/status`">
                    <div class="rounded-lg ml-4 sm:ml-8">
                      <buttonSlot 
                        size="sm"
                        type="dark"
                        class="itbkk-manage-status"
                      >
                        <template v-slot:title> STATUS </template>
                      </buttonSlot>
                    </div>
                  </router-link>

                  <router-link :to="`/board/${boardIdRoute}/task/add`">
                    <div class="rounded-lg ml-4 sm:ml-8">
                      <buttonSlot
                        size="sm"
                        type="dark"
                        class="itbkk-button-add disabled:cursor-not-allowed"
                        :disabled="!isOwner"
                        @mouseenter="showTooltip = !isOwner"
                        @mouseleave="showTooltip = false"
                      >
                        <template v-slot:title> Add Task </template>
                      </buttonSlot>
                      <span v-if="showTooltip" class="tooltip"
                        >You need to be the board owner to perform this
                        action.</span
                      >
                    </div>
                  </router-link>
                </div>
              </div>
              <div class="text-black text-xl font-bold">BOARD NAME</div>

              <div class="mt-7 overflow-x-auto rounded-2xl">
                <table class="w-full whitespace-nowrap">
                  <!-- head -->
                  <thead class="bg-customYellow text-black">
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
                  <tbody class="container bg-white">
                    <tr
                      v-for="(task, index) in sortedTasks"
                      :key="index"
                      :class="{ 'bg-customBg': index % 2 === 0 }"
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
                            class="itbkk-button-edit pr-2 disabled:cursor-not-allowed"
                            :disabled="!isOwner"
                            @mouseenter="showTooltip = !isOwner"
                            @mouseleave="showTooltip = false"
                          >
                            <Edit />
                          </button>
                        </router-link>

                        <button
                          @click="
                            (showDeleteModal = true), (taskToDelete = task)
                          "
                          class="itbkk-button-delete pr-1 disabled:cursor-not-allowed"
                          :disabled="!isOwner"
                          @mouseenter="showTooltip = !isOwner"
                          @mouseleave="showTooltip = false"
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

  <div v-if="showDeleteModal" class="itbkk-modal-alert">
    <div
      class="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-50"
    >
      <div
        class="itbkk-message bg-gray-400 border-2 border-slate-200 shadow-lg rounded-2xl p-8 relative w-1/3"
      >
        <p class="mb-4 text-base text-black font-medium overflow-y-auto">
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

  <div v-if="showModalVis">
    <div
      class="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-50"
    >
      <div
        class="itbkk-message bg-white border-2 border-slate-200 shadow-lg rounded-2xl p-8 relative w-1/3"
      >
        <span class="text-lg font-semibold text-red-600 italic mb-12"
          >Board Visibility change !</span
        >
        <p class="my-4 text-base font-medium overflow-y-auto">
          Do you want to change board visibility to
          <span class="text-cyan-800 italic font-bold">
            {{ isPrivate ? "Public" : "Private" }}</span
          >
          ?
        </p>
        <div class="flex justify-end">
          <button
            @click="showModalVis = false"
            class="itbkk-button-cancel transition-all ease-in bg-gray-300 text-gray-800 px-4 py-2 rounded mr-2 hover:bg-gray-400"
          >
            Cancel
          </button>

          <button
            @click="confirmChange"
            class="itbkk-button-confirm transition-all ease-in bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
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
    @saveUpdateTask="saveTask"
    @saveEdit="editTask"
    @cancelOpe="cancelHandle"
    @failEdit="handelFail"
  />
</template>

<style scoped>
.itbkk-button-add {
  background-color: #808782;
}

.itbkk-button-add:hover {
  background-color: #d5d7d5;
  /* light purple color */
}

/* itbkk-manage-status */
.itbkk-manage-status {
  background-color: #db3069;
}

.itbkk-manage-status:hover {
  background-color: #e6abbe;
  /* light purple color */
}

.itbkk-manage-collaborator {
  background-color: #ebebd3;
}

.itbkk-manage-collaborator:hover {
  background-color: #4e4e46;
}

.itbkk-board-visibility {
  background-color: #f33939;
  transition: all 0.3s;
  color: white;
}

.itbkk-board-visibility:hover {
  background-color: #ff6868;
  transform: scale(0.8);
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
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
  position: absolute;
  background: #333;
  color: #fff;
  padding: 5px;
  border-radius: 4px;
}
</style>