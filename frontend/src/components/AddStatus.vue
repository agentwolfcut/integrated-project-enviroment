<script setup>
import router from '@/router';
import { computed, ref } from 'vue';


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

const emit = defineEmits(['saveStatus','cancelAdd'])

const previousStatus = computed(() => props.status)

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

// Track the initial state of the status to detect changes
const initialStatus = ref({ ...props.status });

const isFormModified = computed(() => {
    return (
        initialStatus.value.name !== previousStatus.value.name ||
        initialStatus.value.description !== previousStatus.value.description
    );
});

const canSave = computed(() => {
    return previousStatus.value.name.trim().length > 0 && isFormModified.value;
});

const saveStatus = () => {
    // Trim the description and set to null if it contains only whitespace
    if (previousStatus.value.description && previousStatus.value.description.length === 0) {
        previousStatus.value.description = null;
    }
    emit('saveStatus', previousStatus.value);
};

</script>

<template>

    <div class="itbkk-modal-status absolute left-0 right-0 top-1/4 m-auto flex flex-wrap justify-center items-center">
        <div
            class="px-3 lg:flex-none fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-70">

            <div class="bg-white w-1/2 h-auto p-2 rounded-2xl shadow-xl">

                <p class="font-bold text-3xl text-black flex justify-center m-5">
                    Add Status
                </p>

                <!-- head -->
                <div class="m-4">
                    <label for="title" class="font-medium text-base">Name of Status</label>
                    <input v-model.trim()="previousStatus.name"
                        class="itbkk-status-name p-2 w-full bg-slate-100 flex font-semibold text-xl text-black rounded-md border-slate-600"
                        type="text" maxlength="50" placeholder="Enter status name" @input="limitInputLength">
                    </input>
                    <p :class="textColorClass" class="text-end text-sm font-semibold text-blue-600">{{ previousStatus.name.length}}/50</p>
                    <!-- <p v-if="previousStatus.name.length === 50" class="text-red-500 text-end text-sm font-semibold ">! you already have limit Maximum field size for status name </p> -->
                </div>

                <!-- center -->
                <div class="flex flex-row gap-4 m-4">
                    <div class="itbkk-status-description w-full ">
                        <p class="font-medium text-base mb-2">description</p>
                        <input v-model.trim()="previousStatus.description"
                            class="text-sm bg-slate-100   rounded-md py-1 h-16 w-full " style='padding: 15px;'
                            type="text"
                            placeholder="Enter status description"
                            @input="limitInputLength">
                        </input>
                        <p v-if="previousStatus.description" :class="textColorClass" class="text-end text-sm font-semibold text-blue-600">{{ previousStatus.description.length}}/200</p>
                        <!-- <p v-if="previousStatus.description.length === 200 " class="text-red-500 text-end text-sm font-semibold ">! you already have limit Maximum field size for status description </p> -->
                    </div>

                </div>

                <!-- bottom -->
                <div class="m-3">
                    <div class="buttons flex gap-2">
                        <button @click="saveStatus" :disabled="!previousStatus.name" class="itbkk-button-confirm disabled border border-slate-800 hover:bg-green-500 hover:text-white transition-all ease-out  p-3 font-medium text-base text-green-800 bg-green-300 rounded-md px-3 disabled:opacity-50 
                            disabled:cursor-not-allowed disabled:bg-slate-600  disabled:text-slate-900
                            ">
                            save
                        </button>
                        <button @click="router.back() , $emit('cancelAdd')"
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