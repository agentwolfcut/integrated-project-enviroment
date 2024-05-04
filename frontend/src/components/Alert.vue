<!-- <script setup></script>

<template>
  <div
    class="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-black bg-opacity-50"
  >
    <div
      class="bg-white border-2 border-slate-200 shadow-lg rounded-2xl p-8 relative w-1/3"
    >
      <p class="mb-4 text-base font-medium text-green-800">
        The task has been successfully added
      </p>
    </div>
  </div>
</template>

<style scoped></style> -->
<template>
  <div v-if="show" class="notification">
    {{ message }}
  </div>
</template>

<script>
export default {
  data() {
    return {
      show: false,
      message: '',
    }
  },
  methods: {
    showAlert(message) {
      this.message = message
      this.show = true
      setTimeout(() => {
        this.show = false
      }, 3000) // ปิดการแสดงผลแจ้งเตือนหลังจาก 3 วินาที
    },
  },
}
</script>

<style>
.notification {
  position: fixed;
  top: 20px;
  right: 20px;
  background-color: green;
  color: white;
  padding: 10px;
  border-radius: 5px;
}
</style>
