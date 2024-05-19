<script setup>
import { useRoute } from 'vue-router';
import router from '@/router';
import { ref, onMounted , computed } from 'vue'
import { getItemById, getItems } from '../libs/fetchUtils';
import { createToaster } from '../../node_modules/@meforma/vue-toaster'

const props = defineProps({
    task: {
        type: Object,
        default: () => ({
            id: undefined,
            name: '',
            description: null,
            status: '' ,
            createdOn: "",
            updatedOn: "",
        })
    },
    statusOptions: {
        type: Array,
        default: () => []
    }
});

const emit = defineEmits(['saveEdit','cancelOpe','failEdit']); // Define the custom event

const statusOptions = ref('')

onMounted(async () => {
  const statusRes = await getItems(import.meta.env.VITE_BASE_URL2)
  statusOptions.value = statusRes;
})

// const previousTask = ref({ ...props.task });

// Computed property to find the status id from status name
const statusId = computed(() => {
    const selectedStatus = props.statusOptions.find(
        (status) => status.name === previousTask.value.status
    );
    return selectedStatus ? selectedStatus.id : null;
});

const saveTask = () => {
    try {
        const taskToSave = {
            ...previousTask.value,
            status: statusId.value // Send status id to the backend
        };
        emit('saveEdit', taskToSave);
    } catch (error) {
        console.error('Error saving task:', error);
        // Handle error as needed
    }
}


const previousTask = computed(() => props.task)
console.log(previousTask.value);

if (previousTask.value.title === null || previousTask.value.title === undefined || previousTask.value.title == '') {
    // router.back()
    emit('failEdit');
    setTimeout(()=>{
        router.back();
    } , 800)
    // toaster.error(`An error has occurred, the status does not exist.`);
}

const formatLocalDate = (dateString) => {
    if (!dateString) return ''

    const date = new Date(dateString)

    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    const hours = String(date.getHours()).padStart(2, '0')
    const minutes = String(date.getMinutes()).padStart(2, '0')
    const seconds = String(date.getSeconds()).padStart(2, '0')

    //   return `${year}-${day}-${day} ${hours}:${minutes}:${seconds}`
    return `${day}/${month}/${year} ${hours}:${minutes}:${seconds} `
}

const textColorClass = computed(() => {
    if (previousTask.value.description !== null) {  
        return previousTask.value.title.length > 99 ? 'text-red-600' : 'text-blue-600';  
    }
});

const descriptionClass = computed(()=>{
    return previousTask.value.description.length > 499 ? 'text-red-600' : 'text-blue-600';
})

const assigneesClass = computed(() => {
    return previousTask.value.assignees.length > 29 ? 'text-red-600' : 'text-blue-600';
})

const limitInputLength = () => {
    if (previousTask.value.title.length > 100) {
        previousTask.value.title = previousTask.value.title.slice(0, 100);
    }
    if (previousTask.value.description !== null && previousTask.value.description.length > 500) {
        previousTask.value.description = previousTask.value.description.slice(0, 500);
    }
    if(previousTask.value.assignees !== null && previousTask.value.assignees.length > 30){
        previousTask.value.assignees = previousTask.value.assignees.slice(0, 30);
    }
};



// Track the initial state of the status to detect changes
const initialTask = JSON.parse(JSON.stringify(props.task))

const isSaveDisabled = computed(() => {
  return (
    JSON.stringify(previousTask.value) === JSON.stringify(initialTask) ||
    previousTask.value.title.length < 1 
  )
})

</script>

<template>
    <div class="absolute left-0 right-0 top-1/4 m-auto flex flex-wrap justify-center items-center">
        <div
            class="px-3 lg:flex-none fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-70">

            <div class="bg-white w-1/2 h-auto rounded-2xl shadow-xl">

                <p class="font-bold text-3xl text-black flex justify-center m-3">
                    Edit Task
                </p>

                <!-- head -->
                <div class="m-4">
                    <label for="title" class="font-medium text-base">Title</label>
                    <input v-model="previousTask.title" required
                        class="itbkk-title p-2 w-full bg-slate-100 flex font-semibold text-xl text-black rounded-md border-slate-600"
                        type="text"
                        @input="limitInputLength">
                    </input>
                    <p :class="textColorClass" class="text-end text-sm font-semibold text-blue-600">{{ previousTask.title.length}}/100</p>

                </div>

                <!-- center -->
                <div class="flex flex-row gap-4 m-4">
                    <div class="itbkk-description  w-8/12">
                        <p class="font-medium text-base mb-2">description</p>
                        <input v-model="previousTask.description" class="text-base  rounded-md py-1 h-16 w-10/12 "
                            style='padding: 15px;' type="text" @input="limitInputLength"/>
                            <p v-if="previousTask.description" :class="descriptionClass" class="text-end text-sm font-semibold text-blue-600">{{ previousTask.description.length }}/500</p>

                    </div>
                    <div class="flex flex-col w-5/12">
                        <div>
                            <div class="itbkk-assignees">
                                <p class="font-medium text-base">assignees</p>
                                <input v-model="previousTask.assignees" class=" w-full text-base rounded-md border p-1"
                                    :class="{ 'italic text-slate-500': previousTask.assignees === '' }"
                                    @input="limitInputLength"/>
                                    <p v-if="previousTask.assignees" :class="assigneesClass" class="text-end text-blue-600 text-sm font-semibold ">{{ previousTask.assignees.length}}/30</p>

                            </div>
                            <div>
                                <form class="max-w-sm mx-auto">
                                    <label for="status" class="block mb-2 text-base font-medium text-gray-900">
                                        Status</label>
                                    <select id="status" v-model="previousTask.status"
                                        class="itbkk-status bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 ">
                                        <option v-for="status in statusOptions" 
                                            :key="status.id" 
                                            :value="status.name"
                                        >
                                            {{ status.name }}
                                        </option>
                                    </select>
                                </form>
                            </div>

                            <div class="flex flex-col mt-20 gap-1">
                                <div class="itbkk-timezone flex flex-row items-center gap-3">
                                    <p class="font-medium text-base">timezone</p>
                                    <div class="text-sm  rounded-md  w-6/6 border px-1">
                                        {{ Intl.DateTimeFormat().resolvedOptions().timeZone }}
                                    </div>
                                </div>

                                <div class="itbkk-created-on flex flex-row items-center gap-3">
                                    <p class="font-medium text-base">created-on</p>
                                    <div class="text-sm  rounded-md  w-6/6 border px-1">
                                        {{ formatLocalDate(previousTask.createdOn) }} </div>
                                </div>

                                <div class="itbkk-updated-on flex flex-row items-center gap-3">
                                    <p class="font-medium text-base">updated-on</p>
                                    <div class="text-sm  rounded-md  w-6/6 border px-1">
                                        {{ formatLocalDate(previousTask.updatedOn) }} </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- bottom -->
                <div class="m-3">
                    <div class="buttons flex gap-2">

                        <button @click="saveTask(previousTask)" :disabled="isSaveDisabled" class="disabled border border-slate-800 hover:bg-green-500 hover:text-white transition-all ease-out itbkk-button-confirm p-3 font-medium text-base text-green-800 bg-green-300 rounded-md px-3 disabled:opacity-50 
                            disabled:cursor-not-allowed disabled:bg-slate-600  disabled:text-slate-900
                            ">
                            save
                        </button>
                        <button @click="$router.go(-1) , $emit('cancelOpe')"
                            class="itbkk-button-cancel border border-slate-800 hover:bg-slate-400 hover:text-white transition-all ease-out p-3 font-medium text-base text-slate-800 bg-slate-300 rounded-md px-3">
                            cancel
                        </button>
                    </div>
                </div>
            </div>

        </div>
    </div>
</template>

<style scoped>
.itbkk-title {
    overflow-wrap: break-word;
    word-wrap: break-word;
    word-break: break-word;
}

.itbkk-assignees {
    background-color: #edf2f7;
    padding: 10px;
    /* เพิ่มพื้นที่เว้นระหว่างข้อความและขอบ */
    margin-bottom: 10px;
    /* เพิ่มระยะห่างด้านล่าง */
    overflow-wrap: break-word;
    word-wrap: break-word;
    word-break: break-word;
}

/* เพิ่มสีพื้นหลังและสีตัวอักษรให้กับ status เพื่อให้ดูเด่น */
.itbkk-status {
    background-color: #edf2f7;
    padding: 10px;
    /* เพิ่มพื้นที่เว้นระหว่างข้อความและขอบ */
    margin-top: 10px;
    /* เพิ่มระยะห่างด้านบน */
}

.itbkk-description {
    border: 2px solid #4a5568;
    /* เพิ่มเส้นขอบสีเทาให้กับ description */
    padding: 10px;
    /* เพิ่มพื้นที่เว้นระหว่างข้อความและขอบ */
    overflow-wrap: break-word;
    word-wrap: break-word;
    word-break: break-word;
}

.itbkk-timezone,
.itbkk-created-on,
.itbkk-updated-on {
    background-color: #edf2f7;
    padding: 10px;
    /* เพิ่มพื้นที่เว้นระหว่างข้อความและขอบ */
    overflow-wrap: break-word;
    word-wrap: break-word;
    word-break: break-word;
}

.buttons {
    display: flex;
    justify-content: space-between;
    /* จัดวางปุ่มไปทางด้านสองข้างของพื้นที่ */
    margin-top: 20px;
    /* เพิ่มระยะห่างด้านบนระหว่างปุ่มกับส่วนข้างบน */
}

.buttons>button {
    flex-grow: 0;
    /* ปรับให้ปุ่มไม่ขยายตัว */
    margin: 0 5px;
    /* เพิ่มระยะห่างด้านซ้ายและขวาของปุ่ม */
    width: calc(50% - 5px);
    /* กำหนดขนาดของปุ่มให้เท่ากับครึ่งของพื้นที่ของ container และลบระยะห่าง */
}
</style>