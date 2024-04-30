<script setup>
import { ref } from 'vue'

defineEmits(['saveModal' , 'addTask'])

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
        require : true
    },
})

const tasks = ref({ ...props.task })
const save = () => {
    const tmp = {...tasks.value}
    tasks.value = {...props.task}
    emit('addTask' , tmp)
}

</script>

<template>

    <div class="absolute left-0 right-0 top-1/4 m-auto flex flex-wrap justify-center items-center">
        <div
            class="px-3 lg:flex-none fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-70">
            <div class="bg-white w-7/12 h-auto rounded-2xl shadow-xl">

                <div class="flex justify-center text-2xl font-semibold">Add New Task</div>

                <div class="content">
                    <div class="itbkk-title">
                        Title
                    </div>
                    <div>
                        <div class="itbkk-description"> description </div>
                        <div>
                            <div class="itbkk-assigness"> assigness </div>
                            <div class="itbkk-status"> status</div>
                        </div>
                    </div>
                </div>

                <div class="m-3">
                    <div class="buttons flex gap-2">
                        <button @click="save"
                            class="itbkk-button-confirm font-medium text-base text-green-800 bg-green-300 rounded-md px-3 ">
                            ok
                        </button>

                        <button @click="$router.go(-1)"
                            class="itbkk-button-cancel font-medium text-base text-slate-800 bg-slate-300 rounded-md px-3">
                            close
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>

</template>

<style scoped>
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