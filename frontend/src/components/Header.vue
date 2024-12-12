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

.header-container {
  display: flex;
  justify-content: flex-end;
  width: 100%;
}
</style>

<template>
  <div class="navbar bg-white">
    <a class="p-2 px-4 text-3xl text-black"
      >𝙆𝙧𝙖𝙙𝙖𝙣𝙆𝙖𝙣𝙗𝙖𝙣 </a>

    <div class="header-container px-2">
      <img src="../../public/logoSIT.png" class="h-7 w-29" />
    </div>
  </div>
</template>

<style scoped></style>