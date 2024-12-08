import { createPinia, defineStore } from "pinia";
import { useToast } from "vue-toast-notification";
import router from "@/router";
import "vue-toast-notification/dist/theme-sugar.css";
// import { updateItem } from "@/libs/fetchUtils"

export const pinia = createPinia();
export const useVisibilityStore = defineStore("visibility", {
  state: () => ({
    visibility: "PRIVATE", // (private หรือ public)
    boardId: null,
  }),

  actions: {
    setVisibility(newVisibility) {
      this.visibility = newVisibility;
    },

    async updateVisibility(newVisibility , boardId) {
      try {
        const res = await fetch(
          `${import.meta.env.VITE_BASE_URL}/boards/${boardId}`,
          {
            method: "PATCH",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
            body: JSON.stringify({ visibility: newVisibility }),
          }
        );
        if (res.status === 200) {
          this.visibility = newVisibility;
          useToast().success(`Visibility updated to ${newVisibility}`);
        } else if (res.status === 401) {
          router.push("/login");
        } else if (res.status === 403) {
          useToast().error("You do not have permission to change visibility.");
        } else {
          useToast().error("There is a problem. Please try again later.");
        }
      } catch (error) {
        console.error("Error updating visibility:", error);
        useToast().error("An error occurred while updating visibility.");
      }
    }
  },
});