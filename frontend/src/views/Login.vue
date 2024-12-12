<script setup>
import router from "@/router";
import { computed, ref } from "vue";
import VueJwtDecode from "vue-jwt-decode";
import { AuthUserStore, BoardStore } from "@/stores/store";

const usrpw = ref({ username: "", password: "" });
const error = ref(false);
const complete = ref(false);
const classNotify = ref("");
const textNotify = ref("");
const authUserStore = AuthUserStore();
const boardStore = BoardStore();
const current_user = ref(null);
const showPassword = ref(false);

localStorage.clear();

const inputUsrpw = async () => {
  try {
    authUserStore.clearTokens();
    boardStore.board = [];
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(usrpw.value),
    });

    if (res.ok) {
      const data = await res.json();
      authUserStore.setTokens(data.access_token, data.refresh_token);
      decode();
      authUserStore.scheduleTokenRefresh();
      router.push("/board");
    } else {
      if (res.status === 400 || res.status === 401) {
        errorNotify("username or Password is incorrect.");
        // console.log(res.status + res.statusText);
      } else {
        errorNotify("There is a problem. Please try again later.");
        console.log(res.status + res.statusText);
      }
    }
  } catch (error) {
    errorNotify("There is a problem. Please try again later.");
    console.log(error);
  }
};

const decode = () => {
  const token = authUserStore.token;
  try {
    let decoded = VueJwtDecode.decode(token);
    current_user.value = decoded.name; // Store the decoded token
    authUserStore.setUser(current_user.value);
    console.log(decoded.exp);
  } catch (err) {
    console.log("token is null: ", err);
  }
};

const errorNotify = (text) => {
  error.value = true;
  classNotify.value = "bg-red-500";
  textNotify.value = text;
  // textNotify.value = `username or Password is incorrect.`;
  setTimeout(() => {
    error.value = false;
  }, 2000);
};

const canLogin = computed(() => {
  return (
    usrpw.value.username.length > 0 &&
    usrpw.value.password.length > 0 &&
    usrpw.value.username.length <= 50 &&
    usrpw.value.password.length <= 14
  );
});

const togglePasswordVisibility = () => {
  showPassword.value = !showPassword.value
};
</script>

<template>
  <div class="area">
    <ul class="circles">
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
    </ul>

    <div class="bg-customBg font-[sans-serif]">
      <div
        class="min-h-screen flex flex-col items-center justify-center py-6 px-4"
      >
        <div class="max-w-md w-full">
          <div class="p-8 mockup-window bg-customBeige shadow-xl">
            <h2 class="text-customBlue text-center text-2xl font-bold">
              Log in
            </h2>
            <form class="mt-8 space-y-4">
              <div>
                <label class="text-customBlue text-sm mb-2 block"
                  >User name</label
                >
                <div class="relative flex items-center">
                  <input
                    v-model="usrpw.username"
                    name="username"
                    type="text"
                    required
                    maxlength="50"
                    class="itbkk-username w-full bg-white text-gray-600 text-sm border border-gray-300 px-4 py-3 rounded-md outline-blue-600"
                    placeholder="Enter user name"
                  />

                  <!-- icon -->

                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    fill="#bbb"
                    stroke="#bbb"
                    class="w-4 h-4 absolute right-4"
                    viewBox="0 0 24 24"
                  >
                    <circle
                      cx="10"
                      cy="7"
                      r="6"
                      data-original="#000000"
                    ></circle>
                    <path
                      d="M14 15H6a5 5 0 0 0-5 5 3 3 0 0 0 3 3h12a3 3 0 0 0 3-3 5 5 0 0 0-5-5zm8-4h-2.59l.3-.29a1 1 0 0 0-1.42-1.42l-2 2a1 1 0 0 0 0 1.42l2 2a1 1 0 0 0 1.42 0 1 1 0 0 0 0-1.42l-.3-.29H22a1 1 0 0 0 0-2z"
                      data-original="#000000"
                    ></path>
                  </svg>
                </div>
              </div>

              <div>
                <label class="text-customBlue text-sm mb-2 block"
                  >Password</label
                >
                <div class="relative flex items-center">
                  <input
                    v-model="usrpw.password"
                    name="password"
                    :type="showPassword ? 'text' : 'password'"
                    required
                    maxlength="14"
                    class="itbkk-password bg-white w-full text-gray-800 text-sm border border-gray-300 px-4 py-3 rounded-md outline-blue-600"
                    placeholder="Enter password"
                  />

                  <!-- icon -->

                  <svg
                    v-if="showPassword"
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke-width="1.5"
                    stroke="currentColor"
                    class="absolute right-4 w-5 h-5 cursor-pointer text-gray-400"
                    @click="togglePasswordVisibility"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M2.036 12.322a1.012 1.012 0 0 1 0-.639C3.423 7.51 7.36 4.5 12 4.5c4.638 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.638 0-8.573-3.007-9.963-7.178Z"
                    />
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z"
                    />
                  </svg>

                  <svg
                    v-else
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke-width="1.5"
                    stroke="currentColor"
                    class="absolute right-4 w-5 h-5 cursor-pointer text-gray-400"
                    @click="togglePasswordVisibility"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M3.98 8.223A10.477 10.477 0 0 0 1.934 12C3.226 16.338 7.244 19.5 12 19.5c.993 0 1.953-.138 2.863-.395M6.228 6.228A10.451 10.451 0 0 1 12 4.5c4.756 0 8.773 3.162 10.065 7.498a10.522 10.522 0 0 1-4.293 5.774M6.228 6.228 3 3m3.228 3.228 3.65 3.65m7.894 7.894L21 21m-3.228-3.228-3.65-3.65m0 0a3 3 0 1 0-4.243-4.243m4.242 4.242L9.88 9.88"
                    />
                  </svg>
                </div>
              </div>

              <!-- for future feature -->
              <!-- <div class="flex flex-wrap items-center justify-between gap-4">
                <div class="flex items-center">
                  <input
                    id="remember-me"
                    name="remember-me"
                    type="checkbox"
                    class="h-4 w-4 shrink-0 bg-white text-blue-600 focus:ring-blue-500 border-gray-300 rounded "
                  />
                  <label
                    for="remember-me"
                    class="ml-3 block text-sm text-slate-600"
                  >
                    Remember me
                  </label>
                </div>
                <div class="text-sm">
                  <a
                    href="jajvascript:void(0);"
                    class="btn btn-active btn-link text-blue-500"
                  >
                    Forgot your password?
                  </a>
                </div>
              </div> -->

              <div class="!mt-8">
                <button
                  @click="inputUsrpw"
                  type="button"
                  :disabled="!canLogin"
                  :class="!canLogin ? 'disabled' : ''"
                  class="itbkk-button-signin disabled:cursor-not-allowed disabled:bg-slate-300 disabled:text-slate-900 w-full py-3 px-4 text-sm tracking-wide rounded-lg text-white bg-customBlue hover:bg-blue-700 focus:outline-none"
                >
                  Sign in
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
      <div
        v-show="error || complete"
        :class="[
          'itbkk-message absolute bottom-0 right-0 text-white font-medium py-3 px-6 rounded-lg shadow-xl m-12',
          classNotify,
        ]"
        v-text="textNotify"
      ></div>
    </div>
  </div>
</template>

<style scoped>
@tailwind base;
@tailwind components;
@tailwind utilities;

/*Start Animations*/
@-webkit-keyframes animatetop {
  from {
    top: -300px;
    opacity: 0;
  }

  to {
    top: 0;
    opacity: 1;
  }
}

@keyframes animatetop {
  from {
    top: -300px;
    opacity: 0;
  }

  to {
    top: 0;
    opacity: 1;
  }
}

@-webkit-keyframes zoomIn {
  0% {
    opacity: 0;
    -webkit-transform: scale3d(0.3, 0.3, 0.3);
    transform: scale3d(0.3, 0.3, 0.3);
  }

  50% {
    opacity: 1;
  }
}

@keyframes zoomIn {
  0% {
    opacity: 0;
    -webkit-transform: scale3d(0.3, 0.3, 0.3);
    transform: scale3d(0.3, 0.3, 0.3);
  }

  50% {
    opacity: 1;
  }
}

/*End Animations*/
/*
-- Start BackGround Animation 
*/
.area {
  background: #4e54c8;
  background: -webkit-linear-gradient(to left, #8f94fb, #4e54c8);
  width: 100%;
  height: 100vh;
  position: absolute;
  z-index: -1;
}

.circles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 96%;
  overflow: hidden;
}

.circles li {
  position: absolute;
  display: block;
  list-style: none;
  width: 20px;
  height: 20px;
  background: rgba(255, 255, 255, 0.2);
  animation: animate 25s linear infinite;
  bottom: -150px;
}

.circles li:nth-child(1) {
  left: 25%;
  width: 80px;
  height: 80px;
  animation-delay: 0s;
}

.circles li:nth-child(2) {
  left: 10%;
  width: 20px;
  height: 20px;
  animation-delay: 2s;
  animation-duration: 12s;
}

.circles li:nth-child(3) {
  left: 70%;
  width: 20px;
  height: 20px;
  animation-delay: 4s;
}

.circles li:nth-child(4) {
  left: 40%;
  width: 60px;
  height: 60px;
  animation-delay: 0s;
  animation-duration: 18s;
}

.circles li:nth-child(5) {
  left: 65%;
  width: 20px;
  height: 20px;
  animation-delay: 0s;
}

.circles li:nth-child(6) {
  left: 75%;
  width: 110px;
  height: 110px;
  animation-delay: 3s;
}

.circles li:nth-child(7) {
  left: 35%;
  width: 150px;
  height: 150px;
  animation-delay: 7s;
}

.circles li:nth-child(8) {
  left: 50%;
  width: 25px;
  height: 25px;
  animation-delay: 15s;
  animation-duration: 45s;
}

.circles li:nth-child(9) {
  left: 20%;
  width: 15px;
  height: 15px;
  animation-delay: 2s;
  animation-duration: 35s;
}

.circles li:nth-child(10) {
  left: 85%;
  width: 150px;
  height: 150px;
  animation-delay: 0s;
  animation-duration: 11s;
}

@keyframes animate {
  0% {
    transform: translateY(0) rotate(0deg);
    opacity: 1;
    border-radius: 0;
  }

  100% {
    transform: translateY(-1000px) rotate(720deg);
    opacity: 0;
    border-radius: 50%;
  }
}

@tailwind base;
@tailwind components;
@tailwind utilities;

/*Start Animations*/
@-webkit-keyframes animatetop {
  from {
    top: -300px;
    opacity: 0;
  }

  to {
    top: 0;
    opacity: 1;
  }
}

@keyframes animatetop {
  from {
    top: -300px;
    opacity: 0;
  }

  to {
    top: 0;
    opacity: 1;
  }
}

@-webkit-keyframes zoomIn {
  0% {
    opacity: 0;
    -webkit-transform: scale3d(0.3, 0.3, 0.3);
    transform: scale3d(0.3, 0.3, 0.3);
  }

  50% {
    opacity: 1;
  }
}

@keyframes zoomIn {
  0% {
    opacity: 0;
    -webkit-transform: scale3d(0.3, 0.3, 0.3);
    transform: scale3d(0.3, 0.3, 0.3);
  }

  50% {
    opacity: 1;
  }
}

/*End Animations*/
/*
-- Start BackGround Animation 
*/
.area {
  background: #4e54c8;
  background: -webkit-linear-gradient(to left, #8f94fb, #4e54c8);
  width: 100%;
  height: 100vh;
  position: absolute;
  z-index: -1;
}

.circles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 96%;
  overflow: hidden;
}

.circles li {
  position: absolute;
  display: block;
  list-style: none;
  width: 20px;
  height: 20px;
  background: rgba(255, 255, 255, 0.2);
  animation: animate 25s linear infinite;
  bottom: -150px;
}

.circles li:nth-child(1) {
  left: 25%;
  width: 80px;
  height: 80px;
  animation-delay: 0s;
}

.circles li:nth-child(2) {
  left: 10%;
  width: 20px;
  height: 20px;
  animation-delay: 2s;
  animation-duration: 12s;
}

.circles li:nth-child(3) {
  left: 70%;
  width: 20px;
  height: 20px;
  animation-delay: 4s;
}

.circles li:nth-child(4) {
  left: 40%;
  width: 60px;
  height: 60px;
  animation-delay: 0s;
  animation-duration: 18s;
}

.circles li:nth-child(5) {
  left: 65%;
  width: 20px;
  height: 20px;
  animation-delay: 0s;
}

.circles li:nth-child(6) {
  left: 75%;
  width: 110px;
  height: 110px;
  animation-delay: 3s;
}

.circles li:nth-child(7) {
  left: 35%;
  width: 150px;
  height: 150px;
  animation-delay: 7s;
}

.circles li:nth-child(8) {
  left: 50%;
  width: 25px;
  height: 25px;
  animation-delay: 15s;
  animation-duration: 45s;
}

.circles li:nth-child(9) {
  left: 20%;
  width: 15px;
  height: 15px;
  animation-delay: 2s;
  animation-duration: 35s;
}

.circles li:nth-child(10) {
  left: 85%;
  width: 150px;
  height: 150px;
  animation-delay: 0s;
  animation-duration: 11s;
}

@keyframes animate {
  0% {
    transform: translateY(0) rotate(0deg);
    opacity: 1;
    border-radius: 0;
  }

  100% {
    transform: translateY(-1000px) rotate(720deg);
    opacity: 0;
    border-radius: 50%;
  }
}
</style>
