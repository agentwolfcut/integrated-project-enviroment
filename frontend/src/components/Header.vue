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
  <!-- 


<div class="text-center text-4xl ">
  <p class="font-sans md:font-serif font-bold">IT-Bangmod Kradan Kanban </p>
</div> -->

  <div
    class="navbar bg-slate-800"
    style="background-color: rgb(15, 26, 44, 255)"
  >
    <a
      class="p-2 px-4 text-xl text-white"
      style="background-color: rgb(14, 21, 39)"
      >𝙆𝙧𝙖𝙙𝙖𝙣𝙆𝙖𝙣𝙗𝙖𝙣 <img src="./logo.gif" class="p-1 h-10 w-10"
    /></a>

    <div class="header-container px-2">
      <img src="./SIT-Logo-for-web-120px.png" class="h-7 w-29" />
    </div>
  </div>
</template>

<style scoped></style>
