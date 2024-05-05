<script setup>
import { onMounted, ref, toRaw } from 'vue';
import { addItem, editItem, getItemById, getItems, deleteItemById } from '../libs/fetchUtils';
import { TaskManagement } from '@/libs/TaskManagement';
import TaskList from './TaskList.vue';
import TaskDetail from './TaskDetail.vue';
import { createToaster } from '../../node_modules/@meforma/vue-toaster'

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
// DELETE
const deleteTask = async (removeId) => {
    removeId = removeIdC.value
    const status = await deleteItemById(
        import.meta.env.VITE_BASE_URL, removeId
    )
    taskMan.value.removetask(removeId)
    toaster.success(`The task has  been deleted`);
    //console.log(taskMan.value);
}



</script>

<template>
    <header>
        <div class="px-4 md:px-10 py-4 md:py-7">
            <div class="flex items-center justify-center italic font-bold text-2xl text-slate-900">
                IT-Bangmod Kradan Kanban
            </div>
        </div>
    </header>

    <TaskList :tasks="taskMan.gettasks()" @showDetail="openDetails" @deleteC="deleteIdConfirm"
        @deleteConfirm="deleteTask" />

    <!-- add task -->
    <Teleport to="#ViewTask">
        <div v-show="showModal">
            <TaskDetail :task='selectTask' @saveModal='complete' @close-modal="cancel" />
        </div>
    </Teleport>


</template>

<style scoped></style>