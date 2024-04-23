<script setup>function dropdownFunction(element) {
    var dropdowns = document.getElementsByClassName("dropdown-content");
    var i;
    let list = element.parentElement.parentElement.getElementsByClassName("dropdown-content")[0];
    list.classList.add("target");
    for (i = 0; i < dropdowns.length; i++) {
        if (!dropdowns[i].classList.contains("target")) {
            dropdowns[i].classList.add("hidden");
        }
    }
    list.classList.toggle("hidden");

    const modalDetail = () => {
        
    }
}</script>

<template>
    <!-- space for html + tailwind -->
    <header>
        <div class="flex justify-center m-6">
            <div class="italic font-bold text-2xl text-slate-900">
                IT-Bangmod Kradan Kanban
            </div>
        </div>
    </header>

    <main>
        <!-- component -->
        <div class="sm:px-6 w-full ">
            <div class="px-4 md:px-10 py-4 md:py-7">
                <div class="flex items-center justify-between">

                </div>
            </div>

            <div class="bg-white py-4 md:py-7 px-4 md:px-8 xl:px-10">
                <!-- head -->
                <div class="sm:flex items-center justify-between">
                    <div class="flex items-center">
                        <a
                            class="rounded-full focus:outline-none focus:ring-2  focus:bg-indigo-50 focus:ring-indigo-800">
                            <div class="py-2 px-8 bg-indigo-100 text-indigo-700 rounded-full">
                                <p>All</p>
                            </div>
                        </a>
                        <a class="rounded-full focus:outline-none focus:ring-2 focus:bg-indigo-50 focus:ring-indigo-800 ml-4 sm:ml-8"
                            href="javascript:void(0)">
                            <div
                                class="py-2 px-8 text-gray-600 hover:text-indigo-700 hover:bg-indigo-100 rounded-full ">
                                <p>Doing</p>
                            </div>
                        </a>
                        <a class="rounded-full focus:outline-none focus:ring-2 focus:bg-indigo-50 focus:ring-indigo-800 ml-4 sm:ml-8"
                            href="javascript:void(0)">
                            <div
                                class="py-2 px-8 text-gray-600 hover:text-indigo-700 hover:bg-indigo-100 rounded-full ">
                                <p>Done</p>
                            </div>
                        </a>
                    </div>

                    <!-- button -->
                    <button onclick="popuphandler(true)"
                        class="focus:ring-2 focus:ring-offset-2 focus:ring-indigo-600 mt-4 sm:mt-0 inline-flex items-start justify-start px-6 py-3 bg-indigo-700 hover:bg-indigo-600 focus:outline-none rounded">
                        <p class="text-sm font-medium leading-none text-white">Add Task</p>
                    </button>

                </div>

                <!-- contents -->
                <div class="mt-7 overflow-x-auto">
                    <table class="w-full whitespace-nowrap">
                        <tbody>
                            <tr class="itbkk-item focus:outline-none h-16 border border-gray-100 rounded ">
                                                              
                                <td class="itbkk-title" @click="modalDetail">
                                    <div class="flex items-center pl-5">
                                        <p class="text-base font-medium leading-none text-gray-700 mr-2">Marketing
                                            Keynote Presentation
                                        </p>
                                    </div>
                                </td>

                                <td>
                                    <div class="text-base font-medium leading-none text-gray-700 mr-2">
                                        Poy Talay
                                    </div>
                                </td>
                                <td class="itbkk-status ">
                                    <button
                                        class="py-3 px-3 text-sm focus:outline-none leading-none text-green-700 bg-green-100 rounded">Due
                                        Done
                                    </button>
                                </td>
                                
                                
                            </tr>


                            <tr class="h-3"></tr>




                        </tbody>
                    </table>
                </div>
            </div>
        </div>


    </main>

</template>

<style>
.checkbox:checked+.check-icon {
    display: flex;
}
</style>