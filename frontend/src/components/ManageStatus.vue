<script setup>
import { onMounted, ref } from 'vue';
import HeaderIT from './Header.vue'
import { getItems } from '../libs/fetchUtils';
import { StatusManagement } from '@/libs/StatusManagement';
import SideBar from './SideBar.vue'
import buttonSlot from './Button.vue'
import Trash from '@/assets/icons/CiTrashFull.vue'
import Edit from '@/assets/icons/CiEditPencil01.vue'


const statusMan = ref(new StatusManagement())

onMounted(async () => {
    const statusRes = await getItems(import.meta.env.VITE_BASE_URL2)
    statusMan.value.addStatuses(statusRes)
})

const statusList = ref(statusMan.value.getStatuses())

const showAddModal = ref(false)

</script>

<template>
    <div class="flex">
        <SideBar />

        <div class="flex flex-col w-screen h-screen">
            <HeaderIT />

            <div class="flex justify-center">
                <div class="sm:px-20 w-full">
                    <div class="bg-white py-2 md:py-4 px-4 md:px-8 xl:px-10 max-w-screen-2xl">
                        <div class="overflow-x-auto ">

                            <!-- button add -->
                            <div class="flex justify-end mb-9" @click="showAddModal = true">

                                <div class="rounded-lg ml-4 sm:ml-8">
                                    <buttonSlot size='sm' type="dark" class="itbkk-button-add">
                                        <template v-slot:title>
                                            Add Status
                                        </template>
                                    </buttonSlot>
                                </div>

                            </div>

                            <div class="mt-7 overflow-x-auto">
                                <table class="w-full whitespace-nowrap rounded-md">
                                    <!-- head -->
                                    <thead class="bg-slate-200 text">
                                        <tr class="focus:outline-none h-16 border border-gray-100 rounded text-base">
                                            <td>
                                                <div class="flex items-center pl-5">
                                                    <p class="font-medium leading-none text-gray-700 ml-6">
                                                        Name
                                                    </p>
                                                </div>
                                            </td>

                                            <td>
                                                <div class="font-medium leading-none text-gray-700 mr-2">
                                                    Description
                                                </div>
                                            </td>

                                            <td>
                                                <div class="font-medium leading-none text-gray-700 mr-2 ">
                                                    Action
                                                </div>
                                            </td>


                                        </tr>
                                    </thead>

                                    <!-- body content -->
                                    <tbody class="container">
                                        <tr v-for="(status, index) in statusList" :key="index"
                                            class="itbkk-item box h-16 border border-gray-100 rounded">
                                            <td class="overflow-hidden max-w-96">
                                                <div class="flex items-center pl-5">
                                                    <div class="flex flex-row justify-start">
                                                        <p
                                                            class="text-base font-medium leading-none text-gray-700 mr-4">
                                                            {{ index + 1 }}
                                                        </p>
                                                    </div>

                                                    <button
                                                        class="itbkk-title text-base font-medium leading-none text-gray-700 mr-4">
                                                        {{ status.name }}
                                                    </button>
                                                </div>
                                            </td>


                                            <td class="itbkk-status-description">
                                                <div class="text-base font-medium leading-none text-gray-700 mr-2">
                                                    {{ status.description }}
                                                </div>
                                            </td>


                                            <!-- <td class="itbkk-button-action flex flex-row">
                                            
                                            <button class="pr-2 itbkk-button-edit">
                                                <Edit />
                                            </button>

                                            <button class="pr-1 itbkk-button-delete">
                                                <Trash />
                                            </button>
                                        </td> -->

                                            <td class="itbkk-status-description">
                                                <div class="text-base font-medium leading-none text-gray-700 mr-2">
                                                    <button class="pr-2 itbkk-button-edit">
                                                        <Edit />
                                                    </button>

                                                    <button class="pr-1 itbkk-button-delete">
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

    <Teleport to="#AddStatus">
        <div v-show="showAddModal">
            <AddStatus />
        </div>

    </Teleport>

</template>

<style scoped>
.itbkk-button-add {
    background-color: #260b8a;
}

.itbkk-button-add:hover {
    background-color: #c7b8ea;
    /* light purple color */
}
</style>