import { createPinia, defineStore } from "pinia";
import { useToast } from "vue-toast-notification";
import router from "@/router";
import "vue-toast-notification/dist/theme-sugar.css";
// import { updateItem } from "@/libs/fetchUtils"

const toast = useToast();
const token = localStorage.getItem("token");

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
    // async updateVisibility(newVisibility) {
    //   try {
    //     // wait to change path and use utils
    //     const res = await updateItem(
    //       `${import.meta.env.VITE_BASE_URL}/boards/${this.boardId}/visibility`,
    //       { visibility: newVisibility },
    //       token
    //     )
    //     if (res.status === 200) {
    //       this.visibility = newVisibility
    //       toast.success(`Visibility updated to ${newVisibility}`)
    //     } else if (res.status === 401) {
    //       router.push("/login")
    //     } else if (res.status === 403) {
    //       toast.error(
    //         "You do not have permission to change board visibility mode."
    //       )
    //     } else {
    //       toast.error("There is a problem. Please try again later.")
    //     }
    //   } catch (error) {
    //     console.error("Error updating visibility:", error)
    //     toast.error("An error occurred while updating visibility.")
    //   }
    // },

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