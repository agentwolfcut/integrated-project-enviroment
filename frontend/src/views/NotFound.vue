<script setup>

import { RouterView } from 'vue-router'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const timeout = setTimeout(() => {
  router.back()
}, 1300)

</script>

<template>
    <div class="flex justify-center items-center h-screen ">
        <div class="text-red-600 font-bold text-2xl">
            Bad request , please try again 
        </div>
    

    </div>
</template>

<style scoped></style>