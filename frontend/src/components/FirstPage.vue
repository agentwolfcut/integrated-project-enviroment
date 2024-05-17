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
const tasksArray = ref([]);
const sortedTasks = ref([]);
const sortByStatus = ref(false); // Default is false, which means sorting by creation time

// GET items
onMounted(async () => {
  const taskRes = await getItems(import.meta.env.VITE_BASE_URL);
  taskMan.value.addtasks(taskRes);
  //tasks.value = taskRes // reverse and slice to show the most
  tasks.value = { ...taskRes };
  // Convert tasks object to array
  const clean = JSON.parse(JSON.stringify(tasks.value));
  tasksArray.value = Object.values(clean);
  // Initially sort by creation time
  sortTasksByCreationTime();
});

// SORT by STATUS

// Function to sort tasks by creation time
const sortTasksByCreationTime = () => {
  sortedTasks.value = tasksArray.value.slice().sort((a, b) => a.id - b.id); // Assuming `id` reflects creation time
};

const sortAsc = ref(true);
// Function to sort tasks by status name
const sortTasksByStatusNameAsc = () => {
  sortedTasks.value = tasksArray.value
    .slice()
    .sort((a, b) => a.status.localeCompare(b.status));
};

const sortTasksByStatusNameDsc = () => {
  sortedTasks.value = tasksArray.value
    .slice()
    .sort((a, b) => b.status.localeCompare(a.status));
};

// Function to toggle sorting
const toggleSortOrder = () => {
  sortAsc.value = !sortAsc.value;
  if (sortAsc.value) {
    sortTasksByStatusNameAsc();
  } else {
    sortTasksByStatusNameDsc();
  }
};

const checkTasksBeforeDelete = (status) => {
  const statusInUse = tasksArray.value.some(
    (task) => task.status === status.statusName
  );
  if (statusInUse) {
    console.log(`status1 : ${status}`);
  } else {
    console.log(`status2 : ${status}`);
  }
};

const taskList = taskMan.value.gettasks();

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
      taskMan.value.removetask(removeId);
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
    const addedTask = await res.json(); //respondจากbackend  ยังไม่ได้ใช้เพราะidที่ส่งมาผิด
    taskMan.value.addtask(
      addedTask.id,
      addedTask.title,
      addedTask.description,
      addedTask.assignees,
      addedTask.status.name
    );
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

const transformTaskFormat = (task) => {
  return {
    title: task.title,
    description: task.description,
    assignees: task.assignees,
    status: {
      id: task.status,
      name: task.status.name,
    },
  };
};

const editTask = async () => {
  const transformedTask = transformTaskFormat(selectTask.value);
  try {
    const res = await fetch(
      `${import.meta.env.VITE_BASE_URL}/${selectTask.value.id}`,
      {
        method: "PUT",
        headers: {
          "content-type": "application/json",
        },
        body: JSON.stringify({
          ...transformedTask,
        }),
      }
    );
    if (!res.ok) {
      throw new Error(
        `Failed to update task. Server responded with status ${res.status}`
      );
    }
    const editTask = await res.json();
    taskMan.value.updatetask(
      editTask.id,
      editTask.title,
      editTask.description,
      editTask.assignees,
      editTask.status
    );
    router.back();
    toaster.success(`The ${editTask.title} task has been successfully updated`);
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
          <div class="flex justify-end mb-9">
            
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
                  <button v-if="sortAsc"><SortDown /></button>
                  <button v-if="!sortAsc"><SortUp /></button>
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
                  class="text-gray-700 truncate"
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
              <div class="task-column text-gray-700">{{ task.status }}</div>
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
    @saveUpdateTask="saveTask"
    @saveEdit="editTask"
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
  grid-template-columns: 6fr 3fr 2fr 1fr;
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
.ts-control {
  border: 1px solid #d0d0d0;
  padding: 8px 8px;
  width: 100%;
  overflow: hidden;
  position: relative;
  z-index: 1;
  box-sizing: border-box;
  box-shadow: none;
  border-radius: 3px;
  display: flex;
  flex-wrap: wrap;
}
.ts-wrapper.multi.has-items .ts-control {
  padding: calc(8px - 2px - 0) 8px calc(8px - 2px - 3px - 0);
}
.full .ts-control {
  background-color: #fff;
}
.disabled .ts-control,
.disabled .ts-control * {
  cursor: default !important;
}
.focus .ts-control {
  box-shadow: none;
}
.ts-control > * {
  vertical-align: baseline;
  display: inline-block;
}
.ts-wrapper.multi .ts-control > div {
  cursor: pointer;
  margin: 0 3px 3px 0;
  padding: 2px 6px;
  background: #f2f2f2;
  color: #303030;
  border: 0 solid #d0d0d0;
}
.ts-wrapper.multi .ts-control > div.active {
  background: #e8e8e8;
  color: #303030;
  border: 0 solid #cacaca;
}
.ts-wrapper.multi.disabled .ts-control > div,
.ts-wrapper.multi.disabled .ts-control > div.active {
  color: #7d7d7d;
  background: white;
  border: 0 solid white;
}
.ts-control > input {
  flex: 1 1 auto;
  min-width: 7rem;
  display: inline-block !important;
  padding: 0 !important;
  min-height: 0 !important;
  max-height: none !important;
  max-width: 100% !important;
  margin: 0 !important;
  text-indent: 0 !important;
  border: 0 none !important;
  background: none !important;
  line-height: inherit !important;
  -webkit-user-select: auto !important;
  -moz-user-select: auto !important;
  -ms-user-select: auto !important;
  user-select: auto !important;
  box-shadow: none !important;
}
.ts-control > input::-ms-clear {
  display: none;
}
.ts-control > input:focus {
  outline: none !important;
}
.has-items .ts-control > input {
  margin: 0 4px !important;
}
.ts-control.rtl {
  text-align: right;
}
.ts-control.rtl.single .ts-control:after {
  left: 15px;
  right: auto;
}
.ts-control.rtl .ts-control > input {
  margin: 0 4px 0 -2px !important;
}
.disabled .ts-control {
  opacity: 0.5;
  background-color: #fafafa;
}
.input-hidden .ts-control > input {
  opacity: 0;
  position: absolute;
  left: -10000px;
}

.ts-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  width: 100%;
  z-index: 10;
  border: 1px solid #d0d0d0;
  background: #fff;
  margin: 0.25rem 0 0;
  border-top: 0 none;
  box-sizing: border-box;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  border-radius: 0 0 3px 3px;
}
.ts-dropdown [data-selectable] {
  cursor: pointer;
  overflow: hidden;
}
.ts-dropdown [data-selectable] .highlight {
  background: rgba(125, 168, 208, 0.2);
  border-radius: 1px;
}
.ts-dropdown .option,
.ts-dropdown .optgroup-header,
.ts-dropdown .no-results,
.ts-dropdown .create {
  padding: 5px 8px;
}
.ts-dropdown .option,
.ts-dropdown [data-disabled],
.ts-dropdown [data-disabled] [data-selectable].option {
  cursor: inherit;
  opacity: 0.5;
}
.ts-dropdown [data-selectable].option {
  opacity: 1;
  cursor: pointer;
}
.ts-dropdown .optgroup:first-child .optgroup-header {
  border-top: 0 none;
}
.ts-dropdown .optgroup-header {
  color: #303030;
  background: #fff;
  cursor: default;
}
.ts-dropdown .active {
  background-color: #f5fafd;
  color: #495c68;
}
.ts-dropdown .active.create {
  color: #495c68;
}
.ts-dropdown .create {
  color: rgba(48, 48, 48, 0.5);
}
.ts-dropdown .spinner {
  display: inline-block;
  width: 30px;
  height: 30px;
  margin: 5px 8px;
}
.ts-dropdown .spinner::after {
  content: " ";
  display: block;
  width: 24px;
  height: 24px;
  margin: 3px;
  border-radius: 50%;
  border: 5px solid #d0d0d0;
  border-color: #d0d0d0 transparent #d0d0d0 transparent;
  animation: lds-dual-ring 1.2s linear infinite;
}
@keyframes lds-dual-ring {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.ts-dropdown-content {
  overflow: hidden auto;
  max-height: 200px;
  scroll-behavior: smooth;
}

.ts-wrapper.plugin-drag_drop .ts-dragging {
  color: transparent !important;
}
.ts-wrapper.plugin-drag_drop .ts-dragging > * {
  visibility: hidden !important;
}

.plugin-checkbox_options:not(.rtl) .option input {
  margin-right: 0.5rem;
}

.plugin-checkbox_options.rtl .option input {
  margin-left: 0.5rem;
}

/* stylelint-disable function-name-case */
.plugin-clear_button {
  --ts-pr-clear-button: 1em;
}
.plugin-clear_button .clear-button {
  opacity: 0;
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  right: calc(8px - 6px);
  margin-right: 0 !important;
  background: transparent !important;
  transition: opacity 0.5s;
  cursor: pointer;
}
.plugin-clear_button.form-select .clear-button,
.plugin-clear_button.single .clear-button {
  right: max(var(--ts-pr-caret), 8px);
}
.plugin-clear_button.focus.has-items .clear-button,
.plugin-clear_button:not(.disabled):hover.has-items .clear-button {
  opacity: 1;
}

.ts-wrapper .dropdown-header {
  position: relative;
  padding: 10px 8px;
  border-bottom: 1px solid #d0d0d0;
  background: color-mix(#fff, #d0d0d0, 85%);
  border-radius: 3px 3px 0 0;
}
.ts-wrapper .dropdown-header-close {
  position: absolute;
  right: 8px;
  top: 50%;
  color: #303030;
  opacity: 0.4;
  margin-top: -12px;
  line-height: 20px;
  font-size: 20px !important;
}
.ts-wrapper .dropdown-header-close:hover {
  color: black;
}

.plugin-dropdown_input.focus.dropdown-active .ts-control {
  box-shadow: none;
  border: 1px solid #d0d0d0;
}
.plugin-dropdown_input .dropdown-input {
  border: 1px solid #d0d0d0;
  border-width: 0 0 1px;
  display: block;
  padding: 8px 8px;
  box-shadow: none;
  width: 100%;
  background: transparent;
}
.plugin-dropdown_input .items-placeholder {
  border: 0 none !important;
  box-shadow: none !important;
  width: 100%;
}
.plugin-dropdown_input.has-items .items-placeholder,
.plugin-dropdown_input.dropdown-active .items-placeholder {
  display: none !important;
}

.ts-wrapper.plugin-input_autogrow.has-items .ts-control > input {
  min-width: 0;
}
.ts-wrapper.plugin-input_autogrow.has-items.focus .ts-control > input {
  flex: none;
  min-width: 4px;
}
.ts-wrapper.plugin-input_autogrow.has-items.focus
  .ts-control
  > input::-ms-input-placeholder {
  color: transparent;
}
.ts-wrapper.plugin-input_autogrow.has-items.focus
  .ts-control
  > input::placeholder {
  color: transparent;
}

.ts-dropdown.plugin-optgroup_columns .ts-dropdown-content {
  display: flex;
}
.ts-dropdown.plugin-optgroup_columns .optgroup {
  border-right: 1px solid #f2f2f2;
  border-top: 0 none;
  flex-grow: 1;
  flex-basis: 0;
  min-width: 0;
}
.ts-dropdown.plugin-optgroup_columns .optgroup:last-child {
  border-right: 0 none;
}
.ts-dropdown.plugin-optgroup_columns .optgroup::before {
  display: none;
}
.ts-dropdown.plugin-optgroup_columns .optgroup-header {
  border-top: 0 none;
}

.ts-wrapper.plugin-remove_button .item {
  display: inline-flex;
  align-items: center;
}
.ts-wrapper.plugin-remove_button .item .remove {
  color: inherit;
  text-decoration: none;
  vertical-align: middle;
  display: inline-block;
  padding: 0 6px;
  border-radius: 0 2px 2px 0;
  box-sizing: border-box;
}
.ts-wrapper.plugin-remove_button .item .remove:hover {
  background: rgba(0, 0, 0, 0.05);
}
.ts-wrapper.plugin-remove_button.disabled .item .remove:hover {
  background: none;
}
.ts-wrapper.plugin-remove_button .remove-single {
  position: absolute;
  right: 0;
  top: 0;
  font-size: 23px;
}

.ts-wrapper.plugin-remove_button:not(.rtl) .item {
  padding-right: 0 !important;
}
.ts-wrapper.plugin-remove_button:not(.rtl) .item .remove {
  border-left: 1px solid #d0d0d0;
  margin-left: 6px;
}
.ts-wrapper.plugin-remove_button:not(.rtl) .item.active .remove {
  border-left-color: #cacaca;
}
.ts-wrapper.plugin-remove_button:not(.rtl).disabled .item .remove {
  border-left-color: white;
}

.ts-wrapper.plugin-remove_button.rtl .item {
  padding-left: 0 !important;
}
.ts-wrapper.plugin-remove_button.rtl .item .remove {
  border-right: 1px solid #d0d0d0;
  margin-right: 6px;
}
.ts-wrapper.plugin-remove_button.rtl .item.active .remove {
  border-right-color: #cacaca;
}
.ts-wrapper.plugin-remove_button.rtl.disabled .item .remove {
  border-right-color: white;
}

:root {
  --ts-pr-clear-button: 0;
  --ts-pr-caret: 0;
  --ts-pr-min: 0.75rem;
}

.ts-wrapper.single .ts-control,
.ts-wrapper.single .ts-control input {
  cursor: pointer;
}

.ts-control:not(.rtl) {
  padding-right: max(
    var(--ts-pr-min),
    var(--ts-pr-clear-button) + var(--ts-pr-caret)
  ) !important;
}

.ts-control.rtl {
  padding-left: max(
    var(--ts-pr-min),
    var(--ts-pr-clear-button) + var(--ts-pr-caret)
  ) !important;
}

.ts-wrapper {
  position: relative;
}

.ts-dropdown,
.ts-control,
.ts-control input {
  color: #303030;
  font-family: inherit;
  font-size: 13px;
  line-height: 18px;
}

.ts-control,
.ts-wrapper.single.input-active .ts-control {
  background: #fff;
  cursor: text;
}

.ts-hidden-accessible {
  border: 0 !important;
  clip: rect(0 0 0 0) !important;
  -webkit-clip-path: inset(50%) !important;
  clip-path: inset(50%) !important;
  overflow: hidden !important;
  padding: 0 !important;
  position: absolute !important;
  width: 1px !important;
  white-space: nowrap !important;
}
</style>
