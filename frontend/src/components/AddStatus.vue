<script setup>

</script>
 
<template>
<div class="absolute left-0 right-0 top-1/4 m-auto flex flex-wrap justify-center items-center">
        <div
            class="px-3 lg:flex-none fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-70">

            <div class="bg-white w-1/2 h-auto rounded-2xl shadow-xl">

                <p class="font-bold text-3xl text-black flex justify-center m-3">
                    
                </p>

                <!-- head -->
                <div class="m-4">
                    <label for="title" class="font-medium text-base">Title</label>
                    <input 
                        class="itbkk-title p-2 w-full bg-slate-100 flex font-semibold text-xl text-black rounded-md border-slate-600"
                        type="text">
                    </input>
                </div>

                <!-- center -->
                <div class="flex flex-row gap-4 m-4">
                    <div class="itbkk-description  w-8/12">
                        <p class="font-medium text-base mb-2">description</p>
                        <input class="text-base  rounded-md py-1 h-16 w-10/12 "
                            style='padding: 15px;' type="text">

                        </input>

                    </div>
                    <div class="flex flex-col w-5/12">
                        <div>
                            <div class="itbkk-assignees">
                                <p class="font-medium text-base">assignees</p>
                                <input  class=" w-full text-base rounded-md border p-1">

                                </input>

                            </div>
                            <div>
                                <form class="max-w-sm mx-auto">
                                    <label for="status" class="block mb-2 text-base font-medium text-gray-900">
                                        Status</label>
                                    <select id="status" 
                                        class="itbkk-status bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 ">
                                        <option value="No Status" selected>No Status</option>
                                        <option value="To Do">To Do</option>
                                        <option value="Doing">Doing</option>
                                        <option value="Done">Done</option>
                                    </select>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- bottom -->
                <div class="m-3">
                    <div class="buttons flex gap-2">

                        <button  class="disabled border border-slate-800 hover:bg-green-500 hover:text-white transition-all ease-out itbkk-button-confirm p-3 font-medium text-base text-green-800 bg-green-300 rounded-md px-3 disabled:opacity-50 
                            disabled:cursor-not-allowed disabled:bg-slate-600  disabled:text-slate-900
                            ">
                            save
                        </button>
                        <button 
                            class="itbkk-button-cancel border border-slate-800 hover:bg-slate-400 hover:text-white transition-all ease-out p-3 font-medium text-base text-slate-800 bg-slate-300 rounded-md px-3">
                            cancel
                        </button>
                    </div>
                </div>
            </div>

        </div>
    </div>
</template>
 
<style scoped>

</style>