<script setup>

</script>

<template>
    <div class="flex justify-center items-center h-screen flex-col">
        <div class="text-red-600 font-bold text-5xl">Not Found Page</div>
        <div class="text-slate-800 font-semibold text-lg "> Maybe you typed in the URL wrong.</div>
        <router-link to="/task" class="bg-blue-200 text-blue-700 font-medium m-3 px-2 py-1 rounded-md"> go-back
        </router-link>
    </div>
</template>

<style scoped></style>