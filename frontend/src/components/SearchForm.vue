<script setup>

const emit = defineEmits(['search'])
const search = (e) => {
    // console.log(e.target.value);
    emit('search' , e.target.value)
}

</script>
 
<template>
    <form class="py-3 flex items-center" >
        <label class="sr-only">Search name</label>
        <div class="relative w-full">
            <input type="text" @input="search" placeholder="Search"
                class="bg-gray-50 border border-gray-300 
                text-gray-900 rounded-md p-2">
        </div>
    </form>
</template>
 
<style scoped>

</style>