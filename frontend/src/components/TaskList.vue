<script setup>
import { useRouter } from 'vue-router'
import Trash from '@/assets/icons/CiTrashFull.vue'
import Edit from '@/assets/icons/CiEditPencil01.vue'
import { ref } from 'vue'

defineProps({
  tasks: {
    type: Array,
    require: true,
  },
})

const emits = defineEmits([
  'showDetail',
  'deleteTask',
  'deleteC',
  'deleteConfirm',
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
    <div class="sm:px-6 w-full">
      <div class="bg-white py-4 md:py-7 px-4 md:px-8 xl:px-10">
        <!-- head -->
        <div class="sm:flex items-center justify-between">
          <div class=" flex items-center button-filter">
            <div class="rounded-full focus:outline-none focus:ring-2 focus:bg-indigo-50 focus:ring-indigo-800">
              <div class="py-2 px-8 bg-indigo-100 text-indigo-700 rounded-full">
                <button>All</button>
              </div>
            </div>
            <div
              class="rounded-full focus:outline-none focus:ring-2 focus:bg-indigo-50 focus:ring-indigo-800 ml-4 sm:ml-8">
              <div class="py-2 px-8 text-gray-600 hover:text-indigo-700 hover:bg-indigo-100 rounded-full">
                <button>Do</button>
              </div>
            </div>
            <div
              class="rounded-full focus:outline-none focus:ring-2 focus:bg-indigo-50 focus:ring-indigo-800 ml-4 sm:ml-8">
              <div class="py-2 px-8 text-gray-600 hover:text-indigo-700 hover:bg-indigo-100 rounded-full">
                <button>Doing</button>
              </div>
            </div>
            <div
              class="rounded-full focus:outline-none focus:ring-2 focus:bg-indigo-50 focus:ring-indigo-800 ml-4 sm:ml-8">
              <div class="py-2 px-8 text-gray-600 hover:text-indigo-700 hover:bg-indigo-100 rounded-full">
                <button>Done</button>
              </div>
            </div>
          </div>

          <!-- ADD button  -->
          <router-link to="/task/add" @taskAdded="handleTaskAdded">
            <button class="itbkk-button-add mt-4 
                sm:mt-0 inline-flex px-6 py-3 relative items-center justify-center
              overflow-hidden bg-blue-600 font-medium text-white shadow-2xl
               transition-all duration-300 before:absolute before:inset-0 before:border-0 before:border-white 
               before:duration-100 before:ease-linear hover:bg-blue-50 hover:text-black hover:shadow-blue-600 rounded-md
               ">
              <span class="relative  text-sm font-medium ">Add Task</span>
            </button>
          </router-link>
        </div>

        <div class="mt-7 overflow-x-auto">
          <table class="w-full whitespace-nowrap rounded-md">
            <thead class="bg-slate-200 text">
              <tr class="focus:outline-none h-16 border border-gray-100 rounded text-base">
                <td>
                  <div class="flex items-center pl-5">
                    <p class="font-medium leading-none text-gray-700 ml-6">
                      Title
                    </p>
                  </div>
                </td>

                <td>
                  <div class="font-medium leading-none text-gray-700 mr-2">
                    Assignees
                  </div>
                </td>
                <td>
                  <div class="font-medium leading-none text-gray-700 mr-2">
                    Status
                  </div>
                </td>
                <td></td>
              </tr>
            </thead>
            <!-- อันนี้อันใหม่แก้บัคเลข -->

            <tbody class="container">

              <tr v-for="(task, index) in tasks" :key="index"
                class="itbkk-item box h-16 border border-gray-100 rounded">
                <td>
                  <div class="flex items-center pl-5">
                    <div class="flex flex-row justify-start">
                      <p class="text-base font-medium leading-none text-gray-700 mr-4">
                        {{ index + 1 }}
                      </p>
                    </div>

                    <button class="itbkk-title text-base font-medium leading-none text-gray-700 mr-4"
                      @click="$emit('showDetail', task.id)">
                      {{ task.title }}
                    </button>
                  </div>
                </td>

                <td class="itbkk-assignees" >
                  <div class="text-base font-medium leading-none text-gray-700 mr-2">
                    <span v-if="task.assignees">{{ task.assignees }}</span>
                    <span v-else class="text-slate-300 italic"> Unassigned </span>
                  </div>
                </td>

                <td class="itbkk-status">
                  <div :class="{
                    'text-green-500 bg-green-100 ': task.status === 'Done',
                    'text-red-500 bg-red-100 ': task.status === 'To Do',
                    'text-yellow-600 bg-yellow-100': task.status === 'Doing',
                    'text-slate-700 bg-slate-300 w-20':
                      task.status === 'No Status',
                  }" class="p-3 text-sm leading-none w-16 rounded-md font-semibold mr-4">
                    {{ task.status }}
                  </div>
                </td>

                <td class="itbkk-button-action">

                  <button class="pr-2 itbkk-button-edit">
                    <router-link :to="{ name: 'EditTask', params: { taskId: task.id } }">
                      <Edit />
                    </router-link>
                  </button>


                  <button class="pr-1 itbkk-button-delete" @click="
                      ; (showDeleteModal = true),
                    (taskToDelete = task),
                    $emit('deleteC', task.id)
                    ">
                    <Trash />
                  </button>
                </td>
              </tr>

            </tbody>

            <!-- //อันนี้อันเก่าที่บัคเรื่องเลขนะ
            <tbody>
                            <tr v-for="task in tasks" :key="task.id"
                                class="itbkk-item focus:outline-none h-16 border border-gray-100 rounded ">
                                <td>
                                    <div class="flex items-center pl-5">
                                        <div class="flex flex-row justify-start">
                                            <p class="text-base font-medium leading-none text-gray-700 mr-4">
                                                {{ task.id }}
                                            </p>

                                        </div>

                                        <button
                                            class="itbkk-title text-base font-medium leading-none text-gray-700 mr-4"
                                            @click="openModalDetail, $emit('showDetail', task.id)">
                                            {{ task.title }}
                                        </button>

                                    </div>
                                </td>

                                <td class="itbkk-assignees" :class="{ 'italic': task.assignees === '' }">
                                    <div class="text-base font-medium leading-none text-gray-700 mr-2">
                                        <span v-if="task.assignees">{{ task.assignees }}</span>
                                        <span v-else class="text-slate-300">Unassigned</span>
                                    </div>
                                </td>

                                <td class="itbkk-status">
                                    <div :class="{
                                        'text-green-500 bg-green-100 ': task.status === 'Done',
                                        'text-red-500 bg-red-100 ': task.status === 'To Do',
                                        'text-yellow-600 bg-yellow-100': task.status === 'Doing',
                                        'text-slate-700 bg-slate-300 w-20': task.status === 'No Status'
                                    }" class="p-3 text-sm  leading-none w-16 rounded-md font-semibold mr-4">
                                        {{ task.status }}
                                    </div>
                                </td>

                                <td>
                                    <button class="pr-2">
                                        <Edit />
                                    </button>
                                    <button class="pr-1" @click="showDeleteModal = true, taskToDelete=task , $emit('deleteC', task.id)">
                                        <Trash />
                                    </button>
                                </td>
                            </tr>
                        </tbody> -->
          </table>
        </div>
      </div>
    </div>
  </div>


  <!-- Delete modal -->
  <div v-if="showDeleteModal">
    <div class="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-50">
      <div class="itbkk-message bg-white border-2 border-slate-200 shadow-lg rounded-2xl p-8 relative w-1/3">
        <p class="mb-4 text-base font-medium overflow-y-auto">
          Do you want to delete the task  number {{ taskToDelete.id }} , 
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
.box {
  transition: opacity 0.6s ease;
  ;
}

.container:hover> :not(:hover) {
  opacity: 0.2;

}
</style>
