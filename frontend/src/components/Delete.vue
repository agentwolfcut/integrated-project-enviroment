<script setup>
const props = defineProps({
    task: {
        type: Object,
        require: true
    }
})
defineEmits(['cancelDelete', 'confirmDelete'])

</script>

<template>
    <div class="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-50">
        <div class="bg-white border-2 border-slate-200 shadow-lg rounded-2xl p-8 relative w-1/3">
            <p class="mb-4 text-base font-medium">
                Are you sure , you want to <span class="text-base font-semibold italic text-red-600 px-3">delete</span>
                this task?
            </p>

            <div class="flex justify-end">
                <button @click="$emit('cancelDelete')"
                    class="transition-all ease-in bg-gray-300 text-gray-800 px-4 py-2 rounded mr-2 hover:bg-gray-400">
                    Cancel
                </button>

                <button @click="$emit('confirmDelete')"
                    class="transition-all ease-in bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600">
                    Delete
                </button>
            </div>
        </div>
    </div>
</template>

<style scoped></style>