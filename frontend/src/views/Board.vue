<script setup>
import SideBar from '@/components/SideBar.vue'
import HeaderIT from '@/components/Header.vue'
import { useRoute } from 'vue-router'
import { ref, onMounted, provide } from 'vue'
import VueJwtDecode from 'vue-jwt-decode'
import buttonSlot from '@/components/Button.vue'
const route = useRoute()
const currentUser = ref(null)
onMounted(async () => {
  if (route.state && route.state.currentUser) {
    currentUser.value = route.state.currentUser
  } else {
    const token = localStorage.getItem('token')
    if (token) {
      const decoded = VueJwtDecode.decode(token)
      currentUser.value = decoded.name
      console.log(currentUser.value)
    }
  }
})
provide('currentUser', currentUser)
</script>
<template>
  <div class="flex">
    <SideBar :user="currentUser" />
    <div class="flex flex-col w-screen h-screen items-center bg-gray-200">
      <HeaderIT />
      <div class="flex justify-center overflow-y-scroll">
        <div class="sm:px-20 w-full">
          <div class="py-2 md:py-4 px-4 md:px-8 xl:px-10 bg-gray-200">
            <div>
              <!-- button add -->

              <div class="flex justify-end mb-9">
                <input
                  type="text"
                  placeholder="Filter by status"
                  class="input input-bordered w-full max-w-xs"
                />
                <router-link to="/board/add">
                  <div class="rounded-lg ml-4 sm:ml-8">
                    <buttonSlot size="sm" type="dark" class="itbkk-button-add">
                      <template v-slot:title> Create personal board </template>
                    </buttonSlot>
                  </div>
                </router-link>
              </div>

              <div class="mt-7 overflow-x-auto rounded-2xl">
                <table class="w-full whitespace-nowrap">
                  <!-- head -->
                  <thead
                    class="text-slate-50 text"
                    style="background-color: #15161a"
                  >
                    <tr class="focus:outline-none h-16 text-base">
                      <th
                        class="w-5/12 p-3 pl-12 text-base font-medium tracking-wide text-left"
                      >
                        No
                      </th>
                      <th
                        class="p-3 text-base font-medium tracking-wide text-left"
                      >
                        Name
                      </th>

                      <th
                        class="p-3 w-2/12 text-base font-medium tracking-wide text-left"
                      >
                        Action
                      </th>
                    </tr>
                  </thead>

                  <!-- body content -->
                  <tbody class="container">
                    <tr
                      v-for="(status, index) in statusList"
                      :key="index"
                      :class="{ 'bg-slate-100': index % 2 === 0 }"
                      class="itbkk-item h-16 box ease-in transition-colors"
                    >
                      <td class="min-w-60">
                        <div class="flex items-center pl-5">
                          <div class="flex flex-row justify-start">
                            <p
                              class="text-base font-medium leading-none text-gray-700 mr-4"
                            >
                              {{ index + 1 }}
                            </p>
                          </div>

                          <div
                            class="itbkk-status-name truncate text-base font-medium leading-none text-gray-700 mr-4"
                          >
                            {{ status.name }}
                          </div>
                        </div>
                      </td>

                      <td class="">
                        <div
                          v-if="status.description"
                          class="itbkk-status-description text-base truncate font-medium leading-none text-gray-700 mr-2"
                        >
                          {{ status.description }}
                        </div>
                        <div
                          v-else
                          class="itbkk-status-description text-base font-normal italic leading-none text-gray-400 mr-2"
                        >
                          No description is provided
                        </div>
                      </td>

                      <td class="p-3">
                        <div
                          class="text-base font-medium leading-none text-gray-700 mr-2"
                        >
                          <router-link
                            class="itbkk-button-edit"
                            :to="{
                              name: 'EditStatus',
                              params: { id: status.id },
                            }"
                          >
                            <button class="pr-2" @click="openToEdit(status)">
                              <Edit />
                            </button>
                          </router-link>

                          <button
                            class="pr-1 itbkk-button-delete"
                            @click="
                              ;(statusDelete = status),
                                checkTasksBeforeDelete(status)
                            "
                          >
                            <Trash />
                          </button>
                        </div>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
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
  </div>

  <router-view
    :status="editingStatus"
    @saveStatus="addStatus"
    @saveEdit="updateStatus"
    @cancelEdit="clearEdit"
    @cancelAdd="clearEdit"
    @failEdit="handelFail"
  />

  <div v-if="deleteDefault">
    <div
      class="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-50"
    >
      <div
        class="bg-white border-2 border-slate-200 shadow-lg rounded-2xl p-3 relative w-1/3"
      >
        <div class="m-1 flex justify-end">
          <button @click="deleteDefault = false" class="mb-2">
            <LineMdCloseSmall
              class="bg-black text-white rounded-full w-5 h-5 hover:bg-gray-700"
            />
          </button>
        </div>
        <div
          class="mb-5 text-xl font-medium overflow-y-auto justify-center items-center flex"
        >
          <p>
            <span class="text-red-500">
              {{ statusDelete.name }}
            </span>
            status is the default status and cannot be modified
          </p>
        </div>
      </div>
    </div>
  </div>

  <div v-if="confirmDelete">
    <div
      class="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-50"
    >
      <div
        class="itbkk-message bg-white border-2 border-slate-200 shadow-lg rounded-2xl p-8 relative w-1/3"
      >
        <div class="mb-4 text-base font-medium overflow-y-auto">
          <p>Do you want to delete the status "{{ statusDelete.name }}"?</p>
        </div>

        <div class="flex justify-end mt-4">
          <button
            @click="confirmDelete = false"
            class="itbkk-button-cancel transition-all ease-in bg-gray-300 text-gray-800 px-4 py-2 rounded mr-2 hover:bg-gray-400"
          >
            Cancel
          </button>
          <button
            @click="deleteStatus"
            class="itbkk-button-confirm transition-all ease-in bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 disabled:cursor-not-allowed disabled:bg-slate-600 disabled:text-slate-900"
          >
            Confirm
          </button>
        </div>
      </div>
    </div>
  </div>

  <div v-if="confirmTransfer">
    <div
      class="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-50"
    >
      <div
        class="itbkk-message bg-white border-2 border-slate-200 shadow-lg rounded-2xl p-8 relative w-1/3"
      >
        <div class="mb-4 text-base font-medium overflow-y-auto">
          <p>
            There are
            <span class="text-red-600 italic">{{ taskCount }}</span> tasks in
            <span class="text-red-600 italic">{{ statusDelete.name }}</span>
            status. To delete this status, please transfer tasks to an other
            status.
          </p>
        </div>
        <div>
          <select
            v-model="destinationStatusId"
            class="w-full p-2 border rounded"
          >
            <option value="" selected disabled>
              Select destination status
            </option>
            <option
              :disabled="statusDelete.name === status.name"
              v-for="status in statusList"
              :key="status.id"
              :value="status.id"
            >
              {{ status.name }}
            </option>
          </select>
        </div>
        <div class="flex justify-end mt-4">
          <button
            @click="confirmTransfer = false"
            class="itbkk-button-cancel transition-all ease-in bg-gray-300 text-gray-800 px-4 py-2 rounded mr-2 hover:bg-gray-400"
          >
            Cancel
          </button>
          <button
            @click="transferAndDeleteStatus(destinationStatusId)"
            :disabled="hasTasksToTransfer && !destinationStatusId"
            class="itbkk-button-confirm transition-all ease-in bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 disabled:cursor-not-allowed disabled:bg-slate-600 disabled:text-slate-900"
          >
            Transfer and Delete
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.itbkk-button-add {
  background-color: #260b8a;
}

.itbkk-button-add:hover {
  background-color: #c7b8ea;
  /* light purple color */
}

table {
  width: 100%;
  table-layout: fixed;
}

.box {
  transition: opacity 0.6s ease;
}

.container:hover > :not(:hover) {
  opacity: 0.2;
}
</style>
