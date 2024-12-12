<script setup>
import { ref, computed } from "vue";
import { useCollaboratorStore } from "@/stores/CollaboratorStore";
import { useUserStore } from "@/stores/UserStore";
import { useBoardPermissionStore } from "@/stores/BoardPermissionStore";

const email = ref("");
const accessRight = ref("READ");
const currentUser = localStorage.getItem("currentUser");

const nameToEmail = () => {
  if (!currentUser) return "";
  const lowercaseName = currentUser.toLowerCase();
  const parts = lowercaseName.split(" ");
  if (parts.length < 2) return "";

  const firstName = parts[0];
  const lastName = parts[1];
  return `${firstName}.${lastName}@ad.sit.kmutt.ac.th`;
};

const formatEmail = nameToEmail(currentUser);

const isEmailValid = computed(() => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; 
  return emailRegex.test(email.value);
});

const emits = defineEmits(["saveAddCollab", "cancleAddCollab"]);

const saveAddCollab = () => {
  emits("saveAddCollab", email.value, accessRight.value)
  email.value = ''
  accessRight.value = "READ"
};

const isSaveDisabled = computed(() => {
  return (
    !isEmailValid.value || email.value === formatEmail || email.value === ""
  );
});
</script>

<template>
  <div
    class="absolute left-0 right-0 top-1/4 m-auto flex flex-wrap justify-center items-center"
  >
    <div
      class="px-3 lg:flex-none fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-70"
    >
      <div class="bg-white w-1/2 h-auto rounded-2xl shadow-xl p-6">
        <span
          class="text-3xl font-bold text-customBlue mb-12 disabled:cursor-not-allowed"
          >Add Collaborator</span
        >

        <div class="flex flex-row my-4 justify-between mt-10">
          <div class="flex flex-col">
            <span class="text-base font-medium text-customBlue "
              >Collaborator-Email</span
            >
            <input
              type="email"
              v-model="email"
              required
              maxlength="50"
              class="itbkk-collaborator-email bg-white text-slate-700 rounded-md border-slate-400 border h-10 w-56 pl-2"
            />
          </div>
          <div class="flex flex-col">
            <span class="text-base font-medium text-customBlue"
              >Access Right</span
            >
            <select
              v-model="accessRight"
              required
              name="access"
              class="itbkk-access-right bg-white rounded-md border-slate-400 border h-10 text-gray-600"
            >
              <option value="WRITE">write</option>
              <option value="READ">read</option>
            </select>
          </div>
        </div>

        <div class="flex justify-end">
          <button
            @click="
              $emit('cancleAddCollab', false); email = ''; accessRight = 'READ';
            "
            class="itbkk-button-cancel transition-all ease-in bg-gray-300 text-gray-800 px-4 py-2 rounded mr-2 hover:bg-gray-400"
          >
            Cancel
          </button>

          <button
            @click="saveAddCollab"
            :disabled="isSaveDisabled"
            class="disabled:opacity-50 disabled:cursor-not-allowed itbkk-button-confirm transition-all ease-in bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
          >
            Confirm
          </button>
        </div>
      </div>
    </div>
  </div>
</template>