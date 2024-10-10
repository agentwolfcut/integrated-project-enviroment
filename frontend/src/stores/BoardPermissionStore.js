import { defineStore } from "pinia";
import { useToast } from "vue-toast-notification";
import router from "@/router";
import { BoardStore, AuthUserStore } from "./Store.js";

const token = localStorage.getItem("token");
const toast = useToast();
const userStore = AuthUserStore();
export const useBoardPermissionStore = defineStore("boardPermission", {
  state: () => ({
    boardDetails: null, // Stores the board details fetched from the backend
    isOwner: false, // Check if the current user is the owner
    hasAccess: false, // Check if the user has access based on visibility
  }),

  actions: {
    // Fetch board details and check permission
    async fetchBoardById(boardID) {
      try {
        const data = await fetch(
          `${import.meta.env.VITE_BASE_URL}/boards/${boardID}`,
          {
            method: "GET",
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        if (data.ok) {
          const boardData = await res.json();
          console.log(boardData);
          this.boardDetails = boardData;
          const currentUserId = userStore.currentUser;
          // Check if the user is the owner
          this.isOwner = boardData.ownerID === currentUserId;
          // Check if the user has access based on the visibility
          this.hasAccess = this.isOwner || boardData.visibility === "PUBLIC";
        } else if (res.status === 401) {
          router.push("/login"); // Redirect to login if unauthorized
        } else if (res.status === 403) {
          toast.error("You do not have permission to access this board.");
          router.push("/test"); // Access Denied page
        } else {
          toast.error("Failed to fetch board details.");
        }
      } catch (error) {
        console.error("Error fetching board details:", error);
        toast.error("An error occurred while fetching board details.");
      }
    },

    async updateVisibility(boardID, newVisibility) {
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
          useToast().success(`Visibility updated to ${newVisibility}`);
        } else if (res.status === 401) {
          router.push("/login"); // Redirect to login if unauthorized
        } else if (res.status === 403) {
          useToast().error("You do not have permission to change visibility.");
        } else {
          useToast().error("There was a problem. Please try again later.");
        }
      } catch (error) {
        console.error("Error changing visibility:", error);
        useToast().error("An error occurred while changing visibility.");
      }
    },
  },
});
