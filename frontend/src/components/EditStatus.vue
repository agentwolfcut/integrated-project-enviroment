<script setup>
import router from '@/router';
import { useRoute } from 'vue-router'
import { computed, ref , onMounted } from 'vue';
import { getItemById } from '../libs/fetchUtils';
import { createToaster } from '../../node_modules/@meforma/vue-toaster'

const toaster = createToaster({ /* options */ })

let previousStatus = ref('')
let originalTask = ref('')

const route = useRoute()
const statusId = ref(route.params.id)


onMounted(async () => {
    const statusRes = await getItemById(import.meta.env.VITE_BASE_URL2 , statusId.value)
    previousStatus.value = {...statusRes}
    originalTask.value = {...statusRes}
})



</script>

<template>
    <div class="absolute left-0 right-0 top-1/4 m-auto flex flex-wrap justify-center items-center">
        <div
            class="px-3 lg:flex-none fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-70">

            <div class="bg-white w-1/2 h-auto p-2 rounded-2xl shadow-xl">

                <p class="font-bold text-3xl text-black flex justify-center m-5">
                    Edit Status
                </p>

                <!-- head -->
                <div class="m-4">
                    <label for="title" class="font-medium text-base">Name of Status</label>
                    <input v-model="previousStatus.name"
                        class="itbkk-title p-2 w-full bg-slate-100 flex font-semibold text-xl text-black rounded-md border-slate-600"
                        type="text" maxlength="100" placeholder="Enter status name">
                    </input>
                </div>

                <!-- center -->
                <div class="flex flex-row gap-4 m-4">
                    <div class="itbkk-description w-full ">
                        <p class="font-medium text-base mb-2">description</p>
                        <input v-model="previousStatus.description"
                            class="text-sm bg-slate-100   rounded-md py-1 h-16 w-full " style='padding: 15px;'
                            type="text">

                        </input>

                    </div>

                </div>

                <!-- bottom -->
                <div class="m-3">
                    <div class="buttons flex gap-2">

                        <button @click="$emit('saveEdit', previousStatus)" class="disabled border border-slate-800 hover:bg-green-500 hover:text-white transition-all ease-out itbkk-button-confirm p-3 font-medium text-base text-green-800 bg-green-300 rounded-md px-3 disabled:opacity-50 
                            disabled:cursor-not-allowed disabled:bg-slate-600  disabled:text-slate-900
                            ">
                            save
                        </button>
                        <button @click="router.back()"
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