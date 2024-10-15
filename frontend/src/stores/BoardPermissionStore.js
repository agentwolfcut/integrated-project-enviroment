import { defineStore } from "pinia";
import { useToast } from "vue-toast-notification";
import router from "@/router";
import { AuthUserStore } from "@/stores/Store.js";

const token = localStorage.getItem("token");
const toast = useToast();

export const useBoardPermissionStore = defineStore("boardPermission", {
  state: () => ({
    boardDetails: null,
    isOwner: false,
    hasAccess: false,
  }),

  actions: {
    async fetchBoardById(boardID) {
      const token = localStorage.getItem("token");
      const toast = useToast();
      const authStore = AuthUserStore(); // Initialize authStore correctly before using it

      const currentUser = authStore.currentUser; // Now we can access currentUser

      console.log(`currentUser is ${currentUser}`);

      if (!currentUser) {
        useToast().error("User is not authenticated");
        router.push("/login"); // Redirect to login if user is not authenticated
        return;
      }

      try {
        const res = await fetch(
          `${import.meta.env.VITE_BASE_URL}/boards/${boardID}`,
          {
            method: "GET",
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (res.ok) {
          const boardData = await res.json();
          this.boardDetails = boardData;
          // ตอนนี้ยังเป็นชื่อ รอเขียนฟังก์ชัน
          const currentUserId = authStore.currentUser;

          this.isOwner = boardData.ownerID === currentUserId;
          console.log(`user id =  ${currentUserId}`); // edit
          console.log(`boardData = ${boardData.ownerID}`);

          this.hasAccess = this.isOwner || boardData.visibility === "PUBLIC";
          console.log(boardData.visibility);
        } else if (res.status === 401) {
          router.push("/login");
        } else if (res.status === 403) {
          toast.error("You do not have permission to access this board.");
          router.push("/test");
        } else {
          toast.error("Failed to fetch board details.");
        }
      } catch (error) {
        console.error("Error fetching board details:", error);
        toast.error("An error occurred while fetching board details.");
      }
    },

    async updateVisibility(boardID, newVisibility) {
      const token = localStorage.getItem("token");
      const toast = useToast();

      try {
        const res = await fetch(
          `${import.meta.env.VITE_BASE_URL}/boards/${boardID}`,
          {
            method: "PATCH",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify({ visibility: newVisibility }),
          }
        );
        if (res.ok) {
          this.visibility = newVisibility;
          toast.success(`Visibility updated to ${newVisibility}`);
        } else if (res.status === 401) {
          router.push("/login");
        } else if (res.status === 403) {
          toast.error("You do not have permission to change visibility.");
        } else {
          toast.error("There was a problem. Please try again later.");
        }
      } catch (error) {
        console.error("Error changing visibility:", error);
        toast.error("An error occurred while changing visibility.");
      }
    },
  },
});
