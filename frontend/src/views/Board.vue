<script setup>
import SideBar from "@/components/SideBar.vue";
import HeaderIT from "@/components/Header.vue";
import { useRoute } from "vue-router";
import { ref, onMounted, provide } from "vue";
import VueJwtDecode from "vue-jwt-decode";
import buttonSlot from "@/components/Button.vue";
const route = useRoute();
const currentUser = ref(null);
onMounted(async () => {
  if (route.state && route.state.currentUser) {
    currentUser.value = route.state.currentUser;
  } else {
    const token = localStorage.getItem("token");
    if (token) {
      const decoded = VueJwtDecode.decode(token);
      currentUser.value = decoded.name;
      console.log(currentUser.value);
    }
  }
});
provide("currentUser", currentUser);
</script>

<template>
  <div>
    <div class="flex">
      <SideBar :user="currentUser" />
      <!-- <div class="flex content flex-col items-center h-screen"> -->
      <div class="flex flex-col w-screen h-screen items-center bg-gray-200">
        <HeaderIT />
        <div class="flex justify-center overflow-y-scroll">
          <div class="sm:px-20 w-full">
            <div class="py-2 md:py-4 px-4 md:px-8 xl:px-10 bg-gray-200">
              <div>
                <!-- button add -->
                <div class="flex justify-end mb-9">
                  <router-link to="/board/add">
                    <div class="rounded-lg ml-4 sm:ml-8">
                      <buttonSlot
                        size="sm"
                        type="dark"
                        class="itbkk-button-add"
                      >
                        <template v-slot:title> Create personal board </template>
                      </buttonSlot>
                    </div>
                  </router-link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <router-view />
</template>

<style scoped>
.itbkk-button-add {
  background-color: #260b8a;
}

.itbkk-button-add:hover {
  background-color: #c7b8ea;
  /* light purple color */
}
</style>