<script setup>
import { onMounted, ref } from 'vue';
import HeaderIT from './Header.vue'
import { getItems, addItem, deleteItemById, editItem } from '../libs/fetchUtils';
import { StatusManagement } from '@/libs/StatusManagement';
import SideBar from './SideBar.vue'
import buttonSlot from './Button.vue'
import Trash from '@/assets/icons/CiTrashFull.vue'
import Edit from '@/assets/icons/CiEditPencil01.vue'
import router from '@/router';
import { createToaster } from '../../node_modules/@meforma/vue-toaster'

const toaster = createToaster({ /* options */ })
const statusMan = ref(new StatusManagement())

// GET 
onMounted(async () => {
    const statusRes = await getItems(import.meta.env.VITE_BASE_URL2)
    statusMan.value.addStatuses(statusRes)
})

const statusList = ref(statusMan.value.getStatuses())

// ADD
const editingStatus = ref({ id: undefined, name: '', description: '' })
const addStatus = async () => {
    try {
        const res = await fetch(`${import.meta.env.VITE_BASE_URL2}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(editingStatus.value)
        });
        if (!res.ok) {
            throw new Error(`Failed to add status. Server responded with status ${res.status}`);
        }
        const addedItem = await res.json(); //respondจากbackend  ยังไม่ได้ใช้เพราะidที่ส่งมาผิด      
        editingStatus.value = { id: undefined, name: '', description: '' }
        // console.log(previousTask.value);

        statusMan.value.addStatus(addedItem.id, addedItem.name, addedItem.description)
        router.back();
        toaster.success(`The ${addedItem.name} status has been added`)
    }    // Navigate back
    catch (error) {
        console.error('Error adding status:', error);
        // Handle error as needed
        toaster.error(`An error has occurred, the status could not be added.`)
    }
}

// DELETE
const confirmDelete = ref(false)
const statusDelete = ref(undefined)
const emits = defineEmits(['deleteC', 'deleteConfirm'])

const deleteStatus = async () => {
    // back
    const removeId = statusDelete.value.id
    const removeStatus = await deleteItemById(import.meta.env.VITE_BASE_URL2,
        removeId)
    // front
    if (removeStatus === 200) {
        statusMan.value.removeStatus(removeId)
        toaster.success(`The ${statusDelete.value.name} status has been deleted`)
    } else {
        toaster.error(`An error has occurred, the status could not be delete , plz refresh page.`)
    }
    confirmDelete.value = false
}


const updateStatus = async (editStatus) => {

    const editedItem = await editItem(import.meta.env.VITE_BASE_URL2, editStatus.id, editStatus)
    statusMan.value.updateStatus(editStatus.id, editStatus.name, editStatus.description)
    toaster.success(`The ${editStatus.name} status has been updated`);
    router.push('/status')
}

// const updateStatus = async (editStatus) => {
//     try {
//         const res = await fetch(`${import.meta.env.VITE_BASE_URL2}/${editStatus.id}`, {
//             method: 'PUT',
//             headers: {
//                 'content-type': 'application/json'
//             },
//             body: JSON.stringify(editStatus)
//         })

//         const editedStatus = await res.json()
//         router.back();
//         toaster.success(`The ${editedStatus.title} task has been updated`);
//         // return editedStatus
//     } catch (error) {
//         console.log(`error: ${error}`)
//         toaster.error(`The update was unsuccessful`);
//     }
// }

// update
// const updatingStatus = ref({ id: undefined, name: '', description: '' })
// const updateStatus = async () => {
//     try {
//         const res = await fetch(`${import.meta.env.VITE_BASE_URL2}/${id}` , {
//             method: 'PUT',
//             headers: {
//                 'Content-Type': 'application/json'
//             },
//             body: JSON.stringify(editingStatus.value)
//         });
//         if (!res.ok) {
//             throw new Error(`Failed to add status. Server responded with status ${res.status}`);
//         }
//         const addedItem = await res.json(); //respondจากbackend  ยังไม่ได้ใช้เพราะidที่ส่งมาผิด      
//         editingStatus.value = { id: undefined, name: '', description: '' }
//         // console.log(previousTask.value);

//         statusMan.value.addStatus(addedItem.id, addedItem.name, addedItem.description)
//         router.back();
//         toaster.success(`The ${addedItem.name} status has been added`)
//     }    // Navigate back
//     catch (error) {
//         console.error('Error adding status:', error);
//         // Handle error as needed
//         toaster.error(`An error has occurred, the status could not be added.`)
//     }
// }




</script>

<template>
    <div class="flex">
        <SideBar />
        <div class="flex flex-col w-screen h-screen items-center">
            <HeaderIT />
            <div class="flex justify-center">

                <div class="sm:px-20 w-full">
                    <div class="bg-white py-2 md:py-4 px-4 md:px-8 xl:px-10 ">
                        <div class="overflow-x-auto ">
                            <!-- button add -->
                            <div class="flex justify-end mb-9">
                                <router-link to="/status/add">
                                    <div class="rounded-lg ml-4 sm:ml-8">
                                        <buttonSlot size='sm' type="dark" class="itbkk-button-add">
                                            <template v-slot:title>
                                                Add Status
                                            </template>
                                        </buttonSlot>
                                    </div>
                                </router-link>
                            </div>

                            <div class="mt-7 overflow-x-auto rounded-2xl border border-gray-100">
                                <table class="w-full whitespace-nowrap ">
                                    <!-- head -->
                                    <thead class="bg-slate-200 text">
                                        <tr class="focus:outline-none h-16   text-base">
                                            <td>
                                                <div class="flex items-center pl-5">
                                                    <p class="font-medium leading-none text-gray-700 ml-6">
                                                        Name
                                                    </p>
                                                </div>
                                            </td>
                                            <td></td>
                                            <td>
                                                <div class="font-medium leading-none text-gray-700 mr-2">
                                                    Description
                                                </div>
                                            </td>
                                            <td></td>
                                            <td>
                                                <div class="px-10 font-medium leading-none text-gray-700 mr-2 ">
                                                    Action
                                                </div>
                                            </td>


                                        </tr>
                                    </thead>

                                    <!-- body content -->
                                    <tbody class="container">
                                        <tr v-for="(status, index) in statusList" :key="index"
                                            class="itbkk-item box h-16 border-t border-gray-100 rounded">
                                            <td class=" min-w-60 ">
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
                                            <td></td>
                                            <td class="itbkk-status-description ">
                                                <div v-if="status.description" class="text-base font-medium leading-none text-gray-700 mr-2">
                                                    {{ status.description }}
                                                </div>
                                                <div v-else class="text-base font-normal italic leading-none text-gray-400 mr-2">
                                                    No Description Provided
                                                </div>
                                            </td>

                                            <td></td>
                                            <td class="itbkk-status-action px-10 ">
                                                <div class="text-base font-medium leading-none text-gray-700 mr-2">
                                                    <router-link
                                                        :to="{ name: 'EditStatus', params: { id: status.id } }">
                                                        <button class="pr-2 itbkk-button-edit">
                                                            <Edit />
                                                        </button>
                                                    </router-link>

                                                    <button class="pr-1 itbkk-button-delete"
                                                        @click="(confirmDelete = true), (statusDelete = status), $emit('deleteC', status.id)">
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


    <router-view :status="editingStatus" @saveStatus="addStatus" @saveEdit="updateStatus" />

    <div v-if="confirmDelete">
        <div class="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-50">
            <div class="itbkk-message bg-white border-2 border-slate-200 shadow-lg rounded-2xl p-8 relative w-1/3">
                <div class="mb-4 text-base font-medium overflow-y-auto">
                    <p v-if="statusDelete.id === 1">You don't permit to delete </p>
                    <p v-else>Do you want to delete the status number {{ statusDelete.id }} ,</p>
                    <span class="text-red-600 text-lg italic text-wrap hover:text-balance">
                        {{ statusDelete.name }}
                    </span>
                    status?
                </div>

                <div class="flex justify-end">
                    <button @click="confirmDelete = false"
                        class="itbkk-button-cancel transition-all ease-in bg-gray-300 text-gray-800 px-4 py-2 rounded mr-2 hover:bg-gray-400">
                        Cancel
                    </button>

                    <button @click="deleteStatus" :disabled="statusDelete.id === 1"
                        class="itbkk-button-confirm transition-all ease-in bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 disabled:cursor-not-allowed disabled:bg-slate-600  disabled:text-slate-900">
                        Confirm
                    </button>

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

table {
    width: 100%;
    table-layout: fixed;
}

.box {
    transition: opacity 0.6s ease;
}

.container:hover> :not(:hover) {
    opacity: 0.2;
}
</style>