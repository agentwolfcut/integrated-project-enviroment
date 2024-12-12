<script setup>
import { useRoute, useRouter } from "vue-router";

const route = useRouter();
const takeMe = () => {
  const token = localStorage.getItem('token')
  if (token) route.back();
  else route.push("/login");
};
</script>

<template>
  <section class="bg-customBeige dark:bg-gray-900">
    <div class="container flex items-center min-h-screen px-6 py-12 mx-auto">
      <div>
        <p class="text-sm font-medium text-blue-500 dark:text-blue-400">
          error
        </p>
        <h1
          class="mt-3 text-2xl font-semibold text-gray-800 dark:text-white md:text-3xl"
        >
          Access denied
        </h1>
        <p class="mt-4 text-gray-500 dark:text-gray-400">
          you do not have permission to view this page.
        </p>
        <div class="flex items-center mt-6 gap-x-3">
          <button
            @click="takeMe"
            class="itbkk-button-back text-white w-1/2 px-5 py-2 text-sm tracking-wide rounded-md transition-colors duration-200rounded-lg shrink-0 sm:w-auto hover:bg-customBlue dark:hover:bg-customBeige dark:bg-customBeige bg-customPink dark:text-black"
          >
            Take me home
          </button>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.itbkk-button-back:hover {
  background-color: #260b8a;
  color: wheat;
}
</style>
