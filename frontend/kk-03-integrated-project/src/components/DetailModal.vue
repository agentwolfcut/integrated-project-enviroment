<script setup>

</script>

<template>
    <div class="bg-slate-600  h-screen flex justify-center items-center">
        <div class="bg-white w-8/12 h-auto rounded-2xl shadow-xl">

            <!-- head -->
            <div class="itbkk-title font-semibold text-2xl text-black m-4 pb-2 border-b border-slate-600">
                titletitletitletitle
            </div>

            <!-- center -->
            <div class="flex flex-row gap-4 m-4">
                <div class="itbkk-description  w-8/12">
                    <p class="font-medium text-base mb-2">description</p>
                    <div class="text-base  rounded-md py-1 w-6/6 h-12/12">
                        Lorem ipsum, dolor sit amet consectetur Delectus ut in aliquam consequatur nihil necessitatibus
                        tempora perspiciatis deleniti earum voluptas quos amet, culpa corrupti quas saepe
                        repellendus ea, tenetur, libero magni.
                    </div>

                </div>
                <div class="flex flex-col w-5/12">

                    <div>
                        <div class="itbkk-assignees">
                            <p class="font-medium text-base">assignees</p>
                            <div class="text-base  rounded-md  w-6/6 border p-1 ">
                                whooo</div>
                        </div>
                        <div class="itbkk-status">
                            <p class="font-medium text-base">status</p>
                            <div class="text-base  rounded-md  w-6/6 border p-1">
                                donee</div>
                        </div>
                    </div>
                    <div class="flex flex-col mt-20 gap-1">
                        <div class="itbkk-timezone flex flex-row items-center gap-3">
                            <p class="font-medium text-base">timezone</p>
                            <div class="text-sm  rounded-md  w-6/6 border px-1">
                                donee</div>
                        </div>

                        <div class="itbkk-created-on flex flex-row items-center gap-3">
                            <p class="font-medium text-base">created-on</p>
                            <div class="text-sm  rounded-md  w-6/6 border px-1">
                                2013-05-555 </div>
                        </div>

                        <div class="itbkk-updated-on flex flex-row items-center gap-3">
                            <p class="font-medium text-base">updated-on</p>
                            <div class="text-sm  rounded-md  w-6/6 border px-1">
                                2013-05-555 </div>
                        </div>
                    </div>

                </div>
            </div>

            <!-- bottom -->
            <div class="m-3">
                <div class="buttons flex gap-2">
                    <button
                        class="itbkk-button font-medium text-base text-green-800 bg-green-300 rounded-md px-3 ">ok</button>
                    <button
                        class="itbkk-button font-medium text-base text-slate-800 bg-slate-300 rounded-md px-3">close</button>
                </div>
            </div>





        </div>
    </div>
</template>

<style scoped>
.itbkk-assignees {
    background-color: #edf2f7;
        padding: 10px; /* เพิ่มพื้นที่เว้นระหว่างข้อความและขอบ */
        margin-bottom: 10px; /* เพิ่มระยะห่างด้านล่าง */
    }

    /* เพิ่มสีพื้นหลังและสีตัวอักษรให้กับ status เพื่อให้ดูเด่น */
    .itbkk-status {
        background-color: #edf2f7;
        padding: 10px; /* เพิ่มพื้นที่เว้นระหว่างข้อความและขอบ */
        margin-top: 10px; /* เพิ่มระยะห่างด้านบน */
    }
.itbkk-description {
    border: 2px solid #4a5568; /* เพิ่มเส้นขอบสีเทาให้กับ description */
    padding: 10px; /* เพิ่มพื้นที่เว้นระหว่างข้อความและขอบ */
}

.itbkk-timezone,
.itbkk-created-on,
.itbkk-updated-on {
    border: 1px solid #4a5568; /* เพิ่มเส้นขอบสีเทาให้กับข้อมูลแต่ละช่อง */
    padding: 10px; /* เพิ่มพื้นที่เว้นระหว่างข้อความและขอบ */
}

.itbkk-button {
    padding: 10px 20px; /* ปรับขนาดของปุ่ม */
    border: 2px solid #4a5568; /* เพิ่มเส้นขอบสีเทาให้กับปุ่ม */
}
.buttons {
        display: flex;
        justify-content: space-between; /* จัดวางปุ่มไปทางด้านสองข้างของพื้นที่ */
        margin-top: 20px; /* เพิ่มระยะห่างด้านบนระหว่างปุ่มกับส่วนข้างบน */
    }

    .buttons > button {
        flex-grow: 0; /* ปรับให้ปุ่มไม่ขยายตัว */
        margin: 0 5px; /* เพิ่มระยะห่างด้านซ้ายและขวาของปุ่ม */
        width: calc(50% - 5px); /* กำหนดขนาดของปุ่มให้เท่ากับครึ่งของพื้นที่ของ container และลบระยะห่าง */
    }
</style>