<script setup>
// useRoute use with parameters , useRouter  use with path
import { computed, } from 'vue';
import { useRoute, } from 'vue-router'
import moment from 'moment';

const { params } = useRoute();
//console.log(params.id);

// const router = useRouter()
// const goBack = () => {
//     router.go(-1)
// }

defineEmits(['saveModal', 'closeModal'])
const props = defineProps({
    task: {
        type: Object,
        default: {
            id: undefined,
            title: '',
            description: "",
            assignees: "",
            status: "",
            createdOn: "",
            updatedOn: ""
        }
    },

});

const taskSelect = computed(() => props.task)

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

// const formatLocalDate = (dateString) => {
// if (!dateString) return ''
//   const date = new Date(dateString)

//   return date.toLocaleString('en-GB')
// }





</script>

<template>
    <div class="bg-slate-600  h-screen flex justify-center items-center">
        <div class="bg-white w-7/12 h-auto rounded-2xl shadow-xl">
            <!-- head -->

            <div class="itbkk-title font-semibold text-2xl text-black m-4 pb-2 border-b border-slate-600">
                {{ taskSelect.title }} </div>
            <!-- center -->
            <div class="flex flex-row gap-4 m-4">
                <div class="itbkk-description  w-8/12">
                    <p class="font-medium text-base mb-2">description</p>
                    <div v-if="taskSelect.description" class="text-base  rounded-md py-1 w-6/6 h-12/12" type="text">
                        {{ taskSelect.description }}
                    </div>
                    <div v-else class="italic">
                        No Description Provided
                    </div>


                </div>
                <div class="flex flex-col w-5/12">

                    <div>
                        <div class="itbkk-assignees">
                            <p class="font-medium text-base">assignees</p>
                            <div v-if="taskSelect.assignees" class="text-base  rounded-md  w-6/6 border p-1 ">
                                {{ taskSelect.assignees }}
                            </div>
                            <div v-else class="italic">
                                Unassigned
                            </div>
                        </div>
                        <div class="itbkk-status">
                            <p class="font-medium text-base">status</p>
                            <div class="text-base  rounded-md  w-6/6 border p-1">
                                {{ taskSelect.status }}</div>
                        </div>
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
                                {{ formatLocalDate(taskSelect.createdOn) }} </div>
                        </div>

                        <div class="itbkk-updated-on flex flex-row items-center gap-3">
                            <p class="font-medium text-base">updated-on</p>
                            <div class="text-sm  rounded-md  w-6/6 border px-1">
                                {{ formatLocalDate(taskSelect.updatedOn) }} </div>
                        </div>
                    </div>

                </div>
            </div>

            <!-- bottom -->
            <div class="m-3">
                <div class="buttons flex gap-2">
                    <!-- <button @click="$emit('saveModal' , taskSelect)" -->
                    <button @click="$emit('saveModal', false)"
                        class="itbkk-button font-medium text-base text-green-800 bg-green-300 rounded-md px-3 ">
                        ok
                    </button>
                    <button @click="$emit('closeModal', false)"
                        class="itbkk-button font-medium text-base text-slate-800 bg-slate-300 rounded-md px-3">
                        close
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped></style>