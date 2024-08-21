<script setup>
import router from '@/router';
import { ref } from 'vue';

// input username password
const usrpw = ref({userName : undefined , password : undefined})
const error = ref(false);
const complete = ref(false);
const classNotify = ref('');
const textNotify = ref('');
const inputUsrpw = async () => {
  try {
    // wait for agent api
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(usrpw.value),
    });
    if (!res.ok) {
      errorNotify()
      router.back
      throw new Error(
        // text for error
        ` somehting ${res.status}`
      )
    }
    // success
    completeNotify(usrpw.value.userName, "logged in")
    router.push('/task');
  } catch (error) {
    // error
    errorNotify()
  }
}
const errorNotify = () => {
  error.value = true;
  classNotify.value = "bg-red-500";
  textNotify.value = `Username or Password is incorrect.`;
  setTimeout(() => {
    error.value = false;
  }, 1500);
};

const completeNotify = (status, action) => {
  complete.value = true;
  classNotify.value = "bg-green-600";
  textNotify.value = `The status ${status} has been successfully ${action}.`;
  setTimeout(() => {
    complete.value = false;
  }, 1500);
};

const canLogin = () => {
  return usrpw.value.userName.trim().length > 0 && usrpw.value.password.trim().length > 0;

}

</script>
 
<template>
<div class="bg-gray-50 font-[sans-serif]">
      <div class="min-h-screen flex flex-col items-center justify-center py-6 px-4">
        <div class="max-w-md w-full">
          

          <div class="p-8 rounded-2xl bg-white shadow">
            <h2 class="text-gray-800 text-center text-2xl font-bold">Log in</h2>
            <form class="mt-8 space-y-4">
              <div>
                <label class="text-gray-800 text-sm mb-2 block">User name</label>
                <div class="relative flex items-center">

                  <input v-model="usrpw.userName" name="username" type="text" required class="w-full text-gray-800 text-sm border border-gray-300 px-4 py-3 rounded-md outline-blue-600" placeholder="Enter user name" />
                  
                  <!-- icon -->
                  
                  <!-- <svg xmlns="http://www.w3.org/2000/svg" fill="#bbb" stroke="#bbb" class="w-4 h-4 absolute right-4" viewBox="0 0 24 24">
                    <circle cx="10" cy="7" r="6" data-original="#000000"></circle>
                    <path d="M14 15H6a5 5 0 0 0-5 5 3 3 0 0 0 3 3h12a3 3 0 0 0 3-3 5 5 0 0 0-5-5zm8-4h-2.59l.3-.29a1 1 0 0 0-1.42-1.42l-2 2a1 1 0 0 0 0 1.42l2 2a1 1 0 0 0 1.42 0 1 1 0 0 0 0-1.42l-.3-.29H22a1 1 0 0 0 0-2z" data-original="#000000"></path>
                  </svg> -->
                </div>
              </div>

              <div>
                <label class="text-gray-800 text-sm mb-2 block">Password</label>
                <div class="relative flex items-center">
                  <input v-model="usrpw.password" name="password" type="password" required class="w-full text-gray-800 text-sm border border-gray-300 px-4 py-3 rounded-md outline-blue-600" placeholder="Enter password" />
                  
                  <!-- icon -->

                  <!-- <svg xmlns="http://www.w3.org/2000/svg" fill="#bbb" stroke="#bbb" class="w-4 h-4 absolute right-4 cursor-pointer" viewBox="0 0 128 128">
                    <path d="M64 104C22.127 104 1.367 67.496.504 65.943a4 4 0 0 1 0-3.887C1.367 60.504 22.127 24 64 24s62.633 36.504 63.496 38.057a4 4 0 0 1 0 3.887C126.633 67.496 105.873 104 64 104zM8.707 63.994C13.465 71.205 32.146 96 64 96c31.955 0 50.553-24.775 55.293-31.994C114.535 56.795 95.854 32 64 32 32.045 32 13.447 56.775 8.707 63.994zM64 88c-13.234 0-24-10.766-24-24s10.766-24 24-24 24 10.766 24 24-10.766 24-24 24zm0-40c-8.822 0-16 7.178-16 16s7.178 16 16 16 16-7.178 16-16-7.178-16-16-16z" data-original="#000000"></path>
                  </svg> -->
                </div>
              </div>


              <!-- for future feature -->
              <div class="flex flex-wrap items-center justify-between gap-4">
                <div class="flex items-center">
                  <input id="remember-me" name="remember-me" type="checkbox" class="h-4 w-4 shrink-0 text-blue-600 focus:ring-blue-500 border-gray-300 rounded" />
                  <label for="remember-me" class="ml-3 block text-sm text-gray-800">
                    Remember me
                  </label>
                </div>
                <div class="text-sm">
                  <a href="jajvascript:void(0);" class="text-blue-600 hover:underline font-semibold">
                    Forgot your password?
                  </a>
                </div>
              </div>

              <div class="!mt-8">
                <button @click="inputUsrpw"  type="button" class="w-full py-3 px-4 text-sm tracking-wide rounded-lg text-white bg-blue-600 hover:bg-blue-700 focus:outline-none">
                  Log in
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
      <div
        v-show="error || complete"
        :class="[
          'itbkk-message absolute bottom-0 right-0 text-white font-semibold py-3 px-6 rounded-lg shadow-xl m-12',
          classNotify,
        ]"
        v-text="textNotify"
      ></div>
    </div>
</template>
 
<style scoped>

</style>