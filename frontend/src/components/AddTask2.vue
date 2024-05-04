<script setup>
import { computed } from 'vue'


defineEmits(['closeModal' , 'saveTask'])

const props = defineProps({
    task: {
        type: Object,
        default: {
            id: undefined,
            title: '',
            description: "",
            assignees: "",
            status: '',
        }
    }
})

const previousTask = computed(() => props.task)

</script>

<template>

    <div class="absolute left-0 right-0 top-1/4 m-auto flex flex-wrap justify-center items-center">
        <div
            class="px-3 lg:flex-none fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-70">

            <div class="bg-white w-7/12 h-auto rounded-2xl shadow-xl">
                <!-- head -->
                <p class="font-bold text-3xl text-black flex justify-center m-3">Add Task</p>

                <input v-model.trim="previousTask.title"
                    class="itbkk-title bg-slate-100 w-10/12 flex font-semibold text-xl text-black m-4 p-2 rounded-md border-slate-600"
                    type="text">

                </input>
                <!-- center -->
                <div class="flex flex-row gap-4 m-4">
                    <div class="itbkk-description  w-8/12">
                        <p class="font-medium text-base mb-2">description</p>
                        <input v-model.trim="previousTask.description" class="text-base  rounded-md py-1 h-16 w-10/12 " type="text">

                        </input>

                    </div>
                    <div class="flex flex-col w-5/12">
                        <div>
                            <div class="itbkk-assignees">
                                <p class="font-medium text-base">assignees</p>
                                <input v-model.trim="previousTask.assignees" class="text-base  rounded-md border p-1 ">

                                </input>

                            </div>
                            <div class="itbkk-status">
                                <form class="max-w-sm mx-auto">
                                    <label for="status" class="block mb-2 text-base font-medium text-gray-900">
                                        Status</label>
                                    <select v-model="previousTask.status" id="status" 
                                        class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 ">
                                        <option selected>No Status</option>                                        
                                        <option value="td">To Do</option>
                                        <option value="do">Doing</option>
                                        <option value="dn">Done</option>
                                    </select>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- bottom -->
                <div class="m-3">
                    <div class="buttons flex gap-2">
                        <button @click="$emit('saveTask' , previousTask)" class="itbkk-button font-medium text-base text-green-800 bg-green-300 rounded-md px-3 ">
                            save
                        </button>
                        <button @click="$emit('closeModal')"  
                            class="itbkk-button font-medium text-base text-slate-800 bg-slate-300 rounded-md px-3">
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

.itbkk-button {
    padding: 10px 20px;
    /* ปรับขนาดของปุ่ม */
    border: 1px solid #4a5568;
    /* เพิ่มเส้นขอบสีเทาให้กับปุ่ม */
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