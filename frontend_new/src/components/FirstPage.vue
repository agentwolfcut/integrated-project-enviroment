<script setup>
import { onMounted, ref, toRaw } from 'vue';
import { addItem, editItem, getItemById, getItems } from '../libs/fetchUtils';
import { TaskManagement } from '@/libs/TaskManagement';
import TaskList from './TaskList.vue';
import TaskDetail from './TaskDetail.vue';
import { RouterLink, useRouter } from 'vue-router';
import AddTask from './AddTask.vue';



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
    status: "",
    createdOn: "",
    updatedOn: ""
})

// const router = useRouter();

const openDetails = async (id) => {
    //console.log(id);
    const item = await getItemById(
        import.meta.env.VITE_BASE_URL, id
    )
    selectTask.value = item
    showModal.value = true
    // router.push(`/task/${id}`)
}

// UPDATE
const addTasks = async (newTask) => {
    // back
    if (newTask.id === undefined) {
        const addedTask = await addItem(import.meta.env.VITE_BASE_URL, {
            status: newTask.status
            // other that will add
        })
        console.log(addedTask);
        // front
        taskMan.value.addtask(newTask.id, newTask.title, newTask.description,
            newTask.assignees, newTask.status)

    } else {
        // backend put (replace with new)
        const editTask = await editItem(import.meta.env.VITE_BASE_URL, newTask.id, newTask)
        // front
        taskMan.value.updatetask(newTask.id, newTask.title, newTask.description,
            newTask.assignees, newTask.status, newTask.createdOn, newTask.updatedOn)
    }
    // finish operation then clear value to old
    showModal.value = false // close after click
    selectTask.value = {
        id: undefined,
        title: '',
        description: "",
        assignees: "",
        status: "",
        createdOn: "",
        updatedOn: ""
    }
}

const complete = (flag) => {
    showModal.value = flag
}

const cancel = (flag) => {
    showModal.value = flag
    openModalAdd.value = flag
}

const openModalAdd = ref(false)



</script>

<template>
    <header>
        <div class="px-4 md:px-10 py-4 md:py-7">
            <div class="flex items-center justify-center italic font-bold text-2xl text-slate-900">
                IT-Bangmod Kradan Kanban
            </div>
        </div>
    </header>

    <TaskList :tasks="taskMan.gettasks()" @showDetail="openDetails" @showAdd="openModalAdd = true" />

    <!-- add task -->
    <Teleport to="#ViewTask">
        <div v-show="showModal">
            <!-- <AddTask v-if="showAddModal" /> -->
            <TaskDetail :task='selectTask' @saveModal='complete' @close-modal="cancel" />
        </div>
    </Teleport>


    <RouterLink to="/task/add">
        <Teleport to="#addTask">
            <div v-show="openModalAdd">
                <AddTask @addTask=addTasks />
            </div>
        </Teleport>
    </RouterLink>

</template>

<style scoped></style>