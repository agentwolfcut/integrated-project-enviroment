<script setup>
import { useRouter } from 'vue-router'


defineProps({
    tasks: {
        type: Array,
        require: true
    }
})

defineEmits(['showDetail', 'showAdd'])


</script>

<template>
    <div>
        <div class="sm:px-6 w-full ">
            <div class="bg-white py-4 md:py-7 px-4 md:px-8 xl:px-10">
                <!-- head -->
                <div class="sm:flex items-center justify-between">
                    <div class="flex items-center">
                        <a
                            class="rounded-full focus:outline-none focus:ring-2  focus:bg-indigo-50 focus:ring-indigo-800">
                            <div class="py-2 px-8 bg-indigo-100 text-indigo-700 rounded-full">
                                <p>All</p>
                            </div>
                        </a>
                        <a class="rounded-full focus:outline-none focus:ring-2 focus:bg-indigo-50 focus:ring-indigo-800 ml-4 sm:ml-8"
                            href="javascript:void(0)">
                            <div
                                class="py-2 px-8 text-gray-600 hover:text-indigo-700 hover:bg-indigo-100 rounded-full ">
                                <p>Do</p>
                            </div>
                        </a>
                        <a class="rounded-full focus:outline-none focus:ring-2 focus:bg-indigo-50 focus:ring-indigo-800 ml-4 sm:ml-8"
                            href="javascript:void(0)">
                            <div
                                class="py-2 px-8 text-gray-600 hover:text-indigo-700 hover:bg-indigo-100 rounded-full ">
                                <p>Doing</p>
                            </div>
                        </a>
                        <a class="rounded-full focus:outline-none focus:ring-2 focus:bg-indigo-50 focus:ring-indigo-800 ml-4 sm:ml-8"
                            href="javascript:void(0)">
                            <div
                                class="py-2 px-8 text-gray-600 hover:text-indigo-700 hover:bg-indigo-100 rounded-full ">
                                <p>Done</p>
                            </div>
                        </a>
                    </div>

                    <!-- button -->
                    <button @click="$emit('showAdd')"
                        class="focus:ring-2 focus:ring-offset-2 focus:ring-indigo-600 mt-4 sm:mt-0 inline-flex items-start justify-start px-6 py-3 bg-indigo-700 hover:bg-indigo-600 focus:outline-none rounded">
                        <p class="text-sm font-medium leading-none text-white">Add Task modal</p>
                    </button>

                    <router-link to="/task/add">
                        <button @closeModal="close()">
                            <div class="focus:ring-2 focus:ring-offset-2 focus:ring-indigo-600 mt-4 sm:mt-0 inline-flex
                        items-start justify-start px-6 py-3 bg-indigo-700 hover:bg-indigo-600 focus:outline-none
                        rounded">
                                <p class="text-sm font-medium leading-none text-white">Add Task link</p>
                            </div>
                        </button>
                    </router-link>

                </div>



                <div class="mt-7 overflow-x-auto ">
                    <table class="w-full whitespace-nowrap rounded-md">
                        <thead class="bg-slate-200 text">
                            <tr class=" focus:outline-none h-16 border border-gray-100 rounded text-base">
                                <td>
                                    <div class="flex items-center pl-5">
                                        <p class=" font-medium leading-none text-gray-700 ml-6">
                                            Title
                                        </p>
                                    </div>
                                </td>

                                <td>
                                    <div class=" font-medium leading-none text-gray-700 mr-2">
                                        Assignees
                                    </div>
                                </td>
                                <td>
                                    <div class=" font-medium leading-none text-gray-700 mr-2">
                                        Status
                                    </div>
                                </td>
                            </tr>
                        </thead>

                        <tbody>

                            <tr v-for="task in tasks" :key="task.id"
                                class="itbkk-item focus:outline-none h-16 border border-gray-100 rounded ">
                                <td>
                                    <div class="flex items-center pl-5">
                                        <p class="text-base font-medium leading-none text-gray-700 mr-4">
                                            {{ task.id }}
                                        </p>

                                        <button
                                            class="itbkk-title text-base font-medium leading-none text-gray-700 mr-4"
                                            @click="openModalDetail, $emit('showDetail', task.id)">
                                            {{ task.title }}
                                        </button>

                                        <!-- <router-link :to="{ name: 'TaskDetail', params: { id: task.id } }"
                                            :props="{ task: $emit('showDetail') }">
                                            <button @click="$emit('showDetail', task.id)"> {{ task.title }}</button>
                                        </router-link> -->


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
                                        'bg-slate-200': task.status === ''
                                    }" class="p-3 text-sm  leading-none w-16 rounded-md font-semibold mr-4">
                                        {{ task.status }}
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>


            </div>
        </div>
    </div>
</template>

<style scoped></style>