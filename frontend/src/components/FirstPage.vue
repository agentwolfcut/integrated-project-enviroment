<script setup>
import { onMounted, ref, toRaw } from 'vue';
import { addItem, editItem, getItemById, getItems, deleteItemById } from '../libs/fetchUtils';
import { TaskManagement } from '@/libs/TaskManagement';
import TaskList from './TaskList.vue';
import TaskDetail from './TaskDetail.vue';
import { createToaster } from '../../node_modules/@meforma/vue-toaster'
import HeaderIT from './Header.vue'
import SideBar from './SideBar.vue'

const toaster = createToaster({ /* options */ })

const taskMan = ref(new TaskManagement())

const showModal = ref(false)

// GET items
onMounted(async () => {
    const taskRes = await getItems(
        import.meta.env.VITE_BASE_URL
    )
    taskMan.value.addtasks(taskRes)
    //tasks.value = taskRes // reverse and slice to show the most
})

// for modal
const selectTask = ref({
    id: undefined,
    title: '',
    description: "",
    assignees: "",
    status: "No Status",
    createdOn: "",
    updatedOn: ""
})


const openDetails = async (id) => {
    //console.log(id);
    const item = await getItemById(
        import.meta.env.VITE_BASE_URL, id
    )
    selectTask.value = item
    selectTask.value.status = selectTask.value.status.split('_').map(words => words.charAt(0).toUpperCase() + words.slice(1).toLowerCase()).join(' ')
    showModal.value = true
    // router.push(`/task/${id}`)
}

const complete = (flag) => {
    showModal.value = flag
}
const cancel = (flag) => {
    showModal.value = flag
}
const removeIdC = ref('')
const deleteIdConfirm = (removeId) => {
    removeIdC.value = removeId
}

const deleteTask = async (removeId) => {
    try {
        removeId = removeIdC.value;
        const status = await deleteItemById(import.meta.env.VITE_BASE_URL, removeId);
        if (status === 200) {
            taskMan.value.removetask(removeId)
            toaster.success(`The task has been deleted`);
        } else {
            toaster.error(`The task does not exist
            
            , please refresh page`);
        }
    } catch (error) {
        console.error('Error deleting task:', error);
        // Handle error as needed
        toaster.error(`Failed to delete task. An error occurred.`);
    }
}

</script>

<template>

    <!-- component -->
    <!-- img/beams.jpg -->

    <div class="flex ">
        
        <SideBar/>

        <div class="flex flex-col w-screen h-screen">
            <HeaderIT />

            <TaskList :tasks="taskMan.gettasks()" @showDetail="openDetails" @deleteC="deleteIdConfirm"
                @deleteConfirm="deleteTask" />
        </div>

    </div>
    <!-- add task -->
    <Teleport to="#ViewTask">
        <div v-show="showModal">
            <TaskDetail :task='selectTask' @saveModal='complete' @close-modal="cancel" />
        </div>
    </Teleport>


</template>

<style scoped></style>