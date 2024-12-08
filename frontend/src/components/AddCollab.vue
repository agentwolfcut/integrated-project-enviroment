<script setup>
import { ref , computed } from "vue";
import { useCollaboratorStore } from "@/stores/CollaboratorStore";

const email = ref("");
const accessRight = ref("READ");

const emits = defineEmits(["saveAddCollab", "cancleAddCollab"]);

const saveAddCollab = (email, accessRight) => {
  emits("saveAddCollab", email, accessRight);
  email.value = ""; // Reset form fields
  accessRight.value = "READ";
};

const isEmailValid = computed(() =>
  /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value) &&
  email.value.length <= 50
);
</script>

<template>
  <div
    class="absolute left-0 right-0 top-1/4 m-auto flex flex-wrap justify-center items-center"
  >
    <div
      class="px-3 lg:flex-none fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-70"
    >
      <div class="bg-white w-1/2 h-auto rounded-2xl shadow-xl p-6">
        <span class="text-2xl font-semibold text-red-600 italic mb-12"
          >Add Collaborator</span
        >

        <div class="flex flex-row my-4 justify-between">
          <div class="flex flex-col">
            <span class="text-base font-medium text-slate-800"
              >Collaborator-Email</span
            >
            <input
              type="email"
              v-model="email"
              required
              class="itbkk-collaborator-email bg-white rounded-md border-slate-400 border h-10 w-56"
            />
          </div>
          <div class="flex flex-col">
            <span class="text-base font-medium text-slate-800"
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
            @click="$emit('cancleAddCollab', false)"
            class="itbkk-button-cancel transition-all ease-in bg-gray-300 text-gray-800 px-4 py-2 rounded mr-2 hover:bg-gray-400"
          >
            Cancel
          </button>

          <button
            @click="saveAddCollab(email, accessRight)"
            class="itbkk-button-confirm transition-all ease-in bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
          >
            Confirm
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
