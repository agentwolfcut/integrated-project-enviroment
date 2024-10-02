import { createPinia, defineStore } from "pinia";
import { useToast } from "vue-toast-notification";
import "vue-toast-notification/dist/theme-sugar.css";
import router from "@/router";
import { getItems } from "@/libs/fetchUtils";

const toast = useToast();
const token = localStorage.getItem("token");

export const pinia = createPinia();
export const useTaskStore = defineStore("tasks", {
  state: () => ({
    tasks: [],
  }),
  actions: {
    async fetchTask(boardId) {
      try {
        // tches the data from the specified URL
        const tasks = await getItems(
          `${import.meta.env.VITE_BASE_URL}/boards/${boardId}/tasks`
        );
        // returns the parsed JSON data
        this.tasks = tasks;
      } catch (error) {
        console.error('Error fetching tasks:', error)
        toast.error('An error occurred while fetching tasks')
      }
    },
  },
});
