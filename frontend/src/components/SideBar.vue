<script setup>

</script>
 
<template>
<aside class="flex h-screen px-6  flex-col items-center border-r border-gray-200
         bg-slate-900">
            <div class= "text-white flex h-[4.5rem] w-full items-center justify-center
             border-b border-gray-200 p-2">
                logoooo
            </div>

            <nav class="flex flex-1 flex-col items-center gap-y-4 pt-10">
                <!-- link -->

                <div class="group relative rounded-xl p-2  text-white ">
                    <router-link to="/task">
                        <div class="itbkk-manage-status rounded-lg font-medium">
                            Task
                        </div>
                    </router-link>

                    <div class="absolute inset-y-0 left-16 hidden items-center group-hover:flex">
                        <div
                            class="relative whitespace-nowrap rounded-md bg-white px-4 py-2 text-sm font-semibold text-gray-900 drop-shadow-lg">
                            <div class="absolute inset-0 -left-1 flex items-center">
                                <div class="h-2 w-2 rotate-45 bg-white"></div>
                            </div>
                            Show Task <span class="text-gray-400">
                            </span>
                        </div>
                    </div>
                </div>

                <div class="group relative rounded-xl p-2  text-white ">
                    <router-link to="/status">
                        <div class="itbkk-manage-status rounded-lg font-medium">
                            Status
                        </div>
                    </router-link>

                    <div class="absolute inset-y-0 left-16 hidden items-center group-hover:flex">
                        <div
                            class="relative whitespace-nowrap rounded-md bg-white px-4 py-2 text-sm font-semibold text-gray-900 drop-shadow-lg">
                            <div class="absolute inset-0 -left-1 flex items-center">
                                <div class="h-2 w-2 rotate-45 bg-white"></div>
                            </div>
                            Manage status <span class="text-gray-400">
                            </span>
                        </div>
                    </div>
                </div>
            </nav>
        </aside>
</template>
 
<style scoped>

</style>