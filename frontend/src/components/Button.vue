<script setup>

const props = defineProps({
    size: {
        type: String,
        default: 'sm'
    },
    type: {
        type: String,
        require: 'dark'
    }
})

</script>

<template>
    <button class="mt-4 rounded-lg
                sm:mt-0 inline-flex px-6 py-3 relative items-center justify-center
              overflow-hidden  font-medium shadow-md  transition-all duration-300 before:absolute 
              before:inset-0 before:border-0  before:duration-100 before:ease-linear
               hover:text-black hover:shadow-slate-400
               " :class="[type === 'light' ? 'text-black hover:text-white'
                : type === 'dark' ? 'text-white hover:text-black' : 'text-transparent']">

        <span class="relative font-medium"
            :class="[size === 'sm' ? 'text-sm' : size === 'base' ? 'text-base' : 'text-lg']">
            <slot name="title">
                Title
            </slot>
        </span>

    </button>

</template>


<style scoped></style>