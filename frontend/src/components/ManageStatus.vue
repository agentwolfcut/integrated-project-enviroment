<script setup>
import { onMounted, ref } from 'vue';
import HeaderIT from './Header.vue'
import { getItems } from '../libs/fetchUtils';
import { StatusManagement } from '@/libs/StatusManagement';
import SideBar from './SideBar.vue'
import buttonSlot from './Button.vue'


const statusMan = ref(new StatusManagement())

onMounted(async () => {
    const statusRes = await getItems(import.meta.env.VITE_BASE_URL2    )
    statusMan.value.addStatuses(statusRes)
})

const statusList = ref(statusMan.value.getStatuses())


</script>

<template>
    <div class="flex">
        <SideBar />

        <div class="flex flex-col w-screen h-screen">
            <HeaderIT />

            <div class="flex justify-center">
                <div class="sm:px-20 w-full">
                    <div class="bg-white py-2 md:py-4 px-4 md:px-8 xl:px-10">
                        <div class="mt-7 overflow-x-auto">

                            <!-- button add -->

                            <div class="my-2">
                                <router-link to="/status/add">
                                    <div class="rounded-lg">
                                        <buttonSlot size='sm' type="dark" class="itbkk-button-add">
                                            <template v-slot:title>
                                                Add Status
                                            </template>
                                        </buttonSlot>
                                    </div>
                                </router-link>
                            </div>

                            <table class="w-full whitespace-nowrap rounded-md">
                                <!-- head -->
                                <thead class="bg-slate-200 text">
                                    <tr class="focus:outline-none h-16 border border-gray-100 rounded text-base">
                                        <td></td>
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
                                            <div class="font-medium leading-none text-gray-700 mr-2">
                                                Action
                                            </div>
                                        </td>

                                    </tr>
                                </thead>

                                <!-- body content -->
                                <tbody class="container">
                                    <tr v-for="(status, index) in statusList" :key="index"
                                        class="itbkk-item box h-16 border border-gray-100 rounded">
                                        <td>
                                            <div class="flex items-center pl-5">
                                                <div class="flex flex-row justify-start">
                                                    <p class="text-base font-medium leading-none text-gray-700 mr-4">
                                                        {{ index + 1 }}
                                                    </p>
                                                </div>

                                                <button
                                                    class="itbkk-status-name text-base font-medium leading-none text-gray-700 mr-4">
                                                    {{ status.statusName }}
                                                </button>
                                            </div>
                                        </td>
                                        <td></td>

                                        <td class="itbkk-status-description">
                                            <div class="text-base font-medium leading-none text-gray-700 mr-2">
                                                {{ status.description }}

                                            </div>
                                        </td>



                                        <td class="itbkk-button-action">
                                            <button class="itbkk-button-edit">edit</button>
                                            <button class="itbkk-button-delete">delete</button>
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