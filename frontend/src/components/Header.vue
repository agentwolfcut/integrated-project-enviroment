<script setup></script>
<style>
@keyframes gradientChange {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

.gradient-animation {
  background: linear-gradient(
    to left,

    blue,
    indigo,
    violet
  );
  background-size: 300% 100%;
  animation: gradientChange 3s linear infinite;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}
</style>

<template>
  <div class="px-4 md:px-10 py-4 md:py-7">
    <div class="sm:px-20 w-screen justify-center">
      <h1
        class="mb-4 text-2xl font-extrabold text-gray-900 dark:text-white md:text-2xl lg:text-4xl"
      >
        <span
          class="text-transparent bg-clip-text bg-gradient-to-r to-emerald-600 from-sky-400 gradient-animation"
        >
          IT-Bangmod Kradon Kanban
        </span>
      </h1>
    </div>
  </div>
</template>

<style scoped></style>
