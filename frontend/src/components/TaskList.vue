<script setup>
import { useRouter } from 'vue-router'
import Trash from '@/assets/icons/CiTrashFull.vue'
import Edit from '@/assets/icons/CiEditPencil01.vue'
import { computed, ref } from 'vue'
import buttonSlot from './Button.vue'

const props = defineProps({
  tasks: {
    type: Array,
    require: true,
  },

})

const previousTask = computed(() => props.task)

const emits = defineEmits([
  'showDetail',
  'deleteTask',
  'deleteC',
  'deleteConfirm',
  'addTask'
])

const showDeleteModal = ref(false)

const cancelDelete = () => {
  showDeleteModal.value = false
}

const showAlert = ref(false)
const handleTaskAdded = (statusCode) => {
  showAlert.value = true
  console.log('Task added with status code:', statusCode)
}

const taskToDelete = ref(undefined)


</script>

<template>
  <div>
    <div class="sm:px-20 w-full">
      <div class="bg-white py-2 md:py-4 px-4 md:px-8 xl:px-10">

        <div class="flex justify-end mb-9">
          <router-link to="/task/add" @taskAdded="handleTaskAdded">
            <div class="itbkk-button-add rounded-lg ml-4 sm:ml-8">
              <buttonSlot size="sm" type="dark">
                <template v-slot:title> Add Task </template>
              </buttonSlot>
            </div>
          </router-link>
        </div>


        <div class="mt-7 overflow-x-auto rounded-2xl border border-gray-100">
          <table class="w-full whitespace-nowrap rounded-md">
            <thead class="bg-slate-200 text">
              <tr class="focus:outline-none h-16  rounded text-base">
                <td>
                  <div class="flex items-center pl-5">
                    <p class="font-medium leading-none text-gray-700 ml-6">
                      Title
                    </p>
                  </div>
                </td>
                <td></td>
                <td></td>
                <td class="pl-2">
                  <div class="font-medium leading-none text-gray-700 mr-2">
                    Assignees
                  </div>
                </td>
                <td></td>
                <td>
                  <div class="font-medium leading-none text-gray-700 mr-2">
                    Status
                  </div>
                </td>
                <td></td>
              </tr>
            </thead>

            <tbody class="container">
              <tr v-for="(task, index) in tasks" :key="index"
                class="itbkk-item box h-16 border-t border-gray-100 rounded">
                <td >
                  <div class="flex items-center pl-5">
                    <div class="flex flex-row justify-start">
                      <p class="text-base font-medium leading-none text-gray-700 mr-4">
                        {{ index + 1 }}
                      </p>
                    </div>

                    <button class="itbkk-title  text-base font-medium leading-none text-gray-700 mr-4"
                      @click="$emit('showDetail', task.id)">
                      {{ task.title }}
                    </button>
                  </div>
                </td>
                <td></td>
                <td></td>
                <td class="itbkk-assignees pl-2 overflow-x-hidden ">
                  <div class="text-base font-medium leading-none text-gray-700 mr-2">
                    <span v-if="task.assignees">{{ task.assignees }}</span>
                    <span v-else class="text-slate-300 italic">
                      Unassigned
                    </span>
                  </div>
                </td>
                <td></td>
                <td class="itbkk-status ">

                  <div :class="{
                    'text-green-500 bg-green-100 ': task.status === 'Done',
                    'text-red-500 bg-red-100 ': task.status === 'To Do',
                    'text-yellow-600 bg-yellow-100': task.status === 'Doing',
                    'text-slate-700 bg-slate-300': task.status === 'No Status',
                  }" class="p-3 w-20 text-sm leading-none flex justify-center rounded-md font-semibold mr-4">
                    <!-- {{ task.status.split('_').map(words => words.charAt(0).toUpperCase() +
                      words.slice(1).toLowerCase()).join(' ') }} -->
                    {{ task.status }}

                  </div>

                </td>
                <td class="itbkk-button-action">
                  <router-link :to="{ name: 'EditTask', params: { taskId: task.id } }">
                    <button class="pr-2 itbkk-button-edit">
                      <Edit />
                    </button>
                  </router-link>

                  <button class="pr-1 itbkk-button-delete" @click="showDeleteModal = true,
                    taskToDelete = task, $emit('deleteC', task.id)
                    ">
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

  <router-view @addTask="$emit('addTask', status)" :task="previousTask" />

  <!-- Delete modal -->
  <div v-if="showDeleteModal">
    <div class="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-50">
      <div class="itbkk-message bg-white border-2 border-slate-200 shadow-lg rounded-2xl p-8 relative w-1/3">
        <p class="mb-4 text-base font-medium overflow-y-auto">
          Do you want to delete the task number {{ taskToDelete.id }} ,
          <span class="text-red-600 text-lg italic text-wrap hover:text-balance">{{ taskToDelete.title }}</span>
          task?
        </p>

        <div class="flex justify-end">
          <button @click="cancelDelete"
            class="itbkk-button-cancel transition-all ease-in bg-gray-300 text-gray-800 px-4 py-2 rounded mr-2 hover:bg-gray-400">
            Cancel
          </button>

          <button @click="$emit('deleteConfirm'), (showDeleteModal = false)"
            class="itbkk-button-confirm transition-all ease-in bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600">
            Confirm
          </button>
        </div>
      </div>
    </div>
  </div>



</template>

<style scoped>
table {
  width: 100%;
  table-layout: fixed;
}

.itbkk-title {
  white-space: initial;
  /* adjust this value to your preference */
  overflow-wrap: break-word;
  
  @media (max-width: 2000px) {
    max-width: 28rem;
  }
}

.box {
  transition: opacity 0.6s ease;
}

.container:hover> :not(:hover) {
  opacity: 0.2;
}

.itbkk-button-add {
  background-color: #260b8a;
}

.itbkk-button-add:hover {
  background-color: #c7b8ea;
  /* light purple color */
}

.itbkk-manage-status:hover {
  background-color: #eeb8c5;
}

.itbkk-manage-status {
  background-color: #ef7493;
  /* light purple color */
}
</style>