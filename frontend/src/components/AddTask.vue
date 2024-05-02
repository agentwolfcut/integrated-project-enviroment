<script setup>
import { ref } from 'vue'


const props = defineProps({
    task: {
        type: Object,
        default: {
            id: undefined,
            title: '',
            description: "",
            assignees: "",
            status: "No Status",
        },
        require: true
    },
})

const tasks = ref({ ...props.task })


</script>

<template>

    <div class="absolute left-0 right-0 top-1/4 m-auto flex flex-wrap justify-center items-center">
        <div
            class="px-3 lg:flex-none fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-70">

            <div class="bg-white w-7/12 h-auto rounded-2xl shadow-xl">
                <!-- head -->

                <div class="itbkk-title font-semibold text-2xl text-black m-4 pb-2 border-b border-slate-600">
                </div>
                <!-- center -->
                <div class="flex flex-row gap-4 m-4">
                    <div class="itbkk-description  w-8/12">
                        <p class="font-medium text-base mb-2">description</p>
                        <div class="text-base  rounded-md py-1 w-6/6 h-12/12" type="text">

                        </div>
                      
                    </div>
                    <div class="flex flex-col w-5/12">

                        <div>
                            <div class="itbkk-assignees">
                                <p class="font-medium text-base">assignees</p>
                                <div  class="text-base  rounded-md  w-6/6 border p-1 ">

                                </div>
                               
                            </div>
                            <div class="itbkk-status">
                                <p class="font-medium text-base">status</p>
                                <div class="text-base  rounded-md  w-6/6 border p-1">
                                </div>
                            </div>
                        </div>
                        <div class="flex flex-col mt-20 gap-1">
                            <div class="itbkk-timezone flex flex-row items-center gap-3">
                                <p class="font-medium text-base">timezone</p>
                                <div class="text-sm  rounded-md  w-6/6 border px-1">

                                </div>
                            </div>

                            <div class="itbkk-created-on flex flex-row items-center gap-3">
                                <p class="font-medium text-base">created-on</p>
                                <div class="text-sm  rounded-md  w-6/6 border px-1">
                                </div>
                            </div>

                            <div class="itbkk-updated-on flex flex-row items-center gap-3">
                                <p class="font-medium text-base">updated-on</p>
                                <div class="text-sm  rounded-md  w-6/6 border px-1">
                                </div>
                            </div>
                        </div>

                    </div>
                </div>

                <!-- bottom -->
                <div class="m-3">
                    <div class="buttons flex gap-2">
                        
                        <button class="itbkk-button font-medium text-base text-green-800 bg-green-300 rounded-md px-3 ">
                            ok
                        </button>
                        <button @click="$router.go(-1)" class="itbkk-button font-medium text-base text-slate-800 bg-slate-300 rounded-md px-3">
                            close
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