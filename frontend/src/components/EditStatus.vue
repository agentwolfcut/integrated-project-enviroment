<script setup>
import router from '@/router';
import { useRoute } from 'vue-router'
import { computed, ref, onMounted, watch } from 'vue';

import { createToaster } from '../../node_modules/@meforma/vue-toaster'

const toaster = createToaster({ /* options */ })
const route = useRoute()
const statusId = ref(route.params.id)


const emits = defineEmits(['saveEdit', 'cancelEdit','failEdit'])
const props = defineProps({
    status: {
        type: Object,
        default: {
            id: undefined,
            name: '',
            description: null
        }
    }
})

const previousStatus = ref({...props.status})

if (previousStatus.value.id === undefined || previousStatus.value.name === null || previousStatus.value.name === undefined || previousStatus.value.title == '') {
    // router.back()
    emits('failEdit');
    setTimeout(()=>{
        router.back();
    } , 800)
    // toaster.error(`An error has occurred, the status does not exist.`);
}

// Track the initial state of the status to detect changes
const initialStatus = JSON.parse(JSON.stringify(props.status))
console.log(initialStatus);

const isSaveDisabled = computed(() => {
  return (
    JSON.stringify(previousStatus.value) === JSON.stringify(initialStatus) ||
    previousStatus.value.name.length < 1 
  )
})

const textColorClass = computed(() => {
    if (previousStatus.value.description !== null) {
          return previousStatus.value.name.length > 49 || previousStatus.value.description.length > 199 ? 'text-red-600' : 'text-blue-600';
    }
});

const limitInputLength = () => {
    if (previousStatus.value.name.length > 50) {
        previousStatus.value.name = previousStatus.value.name.slice(0, 50);
    }
    if (previousStatus.value.description !== null && previousStatus.value.description.length > 200) {
        previousStatus.value.description = previousStatus.value.description.slice(0, 200);
    }
};

const saveStatus = () => {    
    // Trim the description and set to null if it contains only whitespace
    // if (previousStatus.value.description.length === 0) {
    //     previousStatus.value.description = null;
    // }
    emits('saveEdit', previousStatus.value);
};

</script>

<template>
    <div class="absolute left-0 right-0 top-1/4 m-auto flex flex-wrap justify-center items-center">
        <div
            class="px-3 lg:flex-none fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-85">
            <div class="bg-white w-1/2 h-auto p-2 rounded-2xl shadow-xl">

                <p class="font-bold text-3xl text-black flex justify-center m-5">
                    Edit Status
                </p>

                <div v-if="previousStatus.id === 1 || previousStatus.name==='Done'" class="flex justify-center font-semibold text-xl text-red-600">
                    You can't edit default status
                </div>

                <div v-else>
                    <!-- head -->
                    <div class="m-4">
                        <label for="title" class="font-medium text-base">Name of Status</label>
                        <input v-model="previousStatus.name"
                            class="itbkk-title p-2 w-full bg-slate-100 flex font-semibold text-xl text-black rounded-md border-slate-600"
                            type="text" maxlength="100" @input="limitInputLength" placeholder="Enter status name">
                        </input>
                        <p :class="textColorClass" class="text-end text-sm font-semibold text-blue-600">{{ previousStatus.name.length}}/50</p>

                    </div>

                    <!-- center -->
                    <div class="flex flex-row gap-4 m-4">
                        <div class="itbkk-description w-full ">
                            <p class="font-medium text-base mb-2">description</p>

                            <input v-model="previousStatus.description"
                                class="text-sm bg-slate-100   rounded-md py-1 h-16 w-full " style='padding: 15px;'
                                type="text"
                                @input="limitInputLength">
                            </input>
                            <p v-if="previousStatus.description" :class="textColorClass" class="text-end text-sm font-semibold text-blue-600">{{ previousStatus.description.length}}/200</p>


                        </div>

                    </div>
                </div>
                <!-- bottom -->

                <div class="m-3">
                    <div class="buttons flex justify-center gap-2">
                        <button
                        v-show="previousStatus.id !== 1 || previousStatus.name !=='Done'"
                            @click="saveStatus(previousStatus)"  
                            :disabled="isSaveDisabled"                         
                            class="disabled border border-slate-800 hover:bg-green-500 hover:text-white transition-all ease-out itbkk-button-confirm p-3 font-medium text-base text-green-800 bg-green-300 rounded-md px-3 disabled:opacity-50 
                            disabled:cursor-not-allowed disabled:bg-slate-600  disabled:text-slate-900"
                        >
                            save
                        </button>
                        <button @click="router.back(), $emit('cancelEdit')"
                            class="itbkk-button-cancel border border-slate-800 hover:bg-slate-400 hover:text-white transition-all ease-out p-3 font-medium text-base text-slate-800 bg-slate-300 rounded-md px-3">
                            cancel
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<style scoped></style>