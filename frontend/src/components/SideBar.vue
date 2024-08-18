<script setup></script>

<template>
  <aside
    class="w-64 px-6 flex h-screen flex-col items-center border-r bg-slate-900"
  >
    <!-- <div
      class="text-white flex h-[4.5rem] items-center justify-center border-b border-gray-200 p-15 w-20"
    >
      <img src="./logo.gif" alt="Logo" />
    </div> -->

    <div class="flex items-center w-full py-4 px-4 border-b border-gray-200">
      <img
        class="w-12 h-12 rounded-full"
        src="https://imgur.com/UhqEppI.png"
        alt="User Avatar"
      />
      <div class="ml-5">
        <p class="text-white font-semibold">John Y.</p>
        <p class="text-gray-400 text-sm">User</p>
      </div>
    </div>

    <nav class="flex flex-1 flex-col items-center gap-y-4 pt-10">
      <div class="group relative rounded-xl p-2 text-white">
        <router-link to="/task">
          <div class="btn btn-ghost">Task view</div>
        </router-link>

        <!-- <div
          class="absolute inset-y-0 left-16 hidden items-center group-hover:flex"
        >
          <div
            class="relative whitespace-nowrap rounded-md bg-white px-4 py-2 text-sm font-semibold text-gray-900 drop-shadow-lg"
          >
            <div class="absolute inset-0 -left-1 flex items-center">
              <div class="h-2 w-2 rotate-45 bg-white"></div>
            </div>
            Show Task <span class="text-gray-400"> </span>
          </div>
        </div> -->
      </div>

      <div class="group relative rounded-xl p-2 text-white itbkk-manage-status">
        <router-link to="/status">
          <div class="btn btn-ghost">Status view</div>
        </router-link>

        <!-- <div
          class="absolute inset-y-0 left-16 hidden items-center group-hover:flex"
        >
          <div
            class="relative whitespace-nowrap rounded-md bg-white px-4 py-2 text-sm font-semibold text-gray-900 drop-shadow-lg"
          >
            <div class="absolute inset-0 -left-0 flex items-center">
              <div class="h-2 w-2 rotate-45 bg-white"></div>
            </div>
            Manage status <span class="text-gray-400"> </span>
          </div>
        </div> -->
      </div>
    </nav>
  </aside>
</template>

<style scoped></style>
