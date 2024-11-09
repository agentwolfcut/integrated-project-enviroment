import { defineStore } from "pinia";
import { useToast } from "vue-toast-notification";
import router from "@/router";
import { AuthUserStore } from '@/stores/store'
import { patchItem } from "@/libs/fetchUtils";

export const useBoardPermissionStore = defineStore("boardPermission", {
  state: () => ({
    boardDetails: null,
    isOwner: false,
    hasAccess: false,
  }),

  actions: {
    async fetchBoardById(path, method) {
      const toast = useToast();
      const authStore = AuthUserStore()
      const token = authStore.token
      authStore.checkAccessToken()
      try {
        let headers = {};
        if (token) {
          headers = {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          };
        } else {
          headers = {
            "Content-Type": "application/json",
          };
        }
        const res = await fetch(`${import.meta.env.VITE_BASE_URL + path}`, {
          method: method,
          headers: headers,
        });

        if (res.ok) {
          const boardData = await res.json();
          this.boardDetails = boardData;
          const currentUser = localStorage.getItem("currentUser");
          const currentUserId = await authStore.findCurrentUser(currentUser);
          this.isOwner = boardData.ownerID === currentUserId;
          this.hasAccess = this.isOwner || boardData.visibility === "PUBLIC";
          this.visibility = boardData.visibility;
        } else if (res.status === 401) {
          router.push("/login");
          // real BE
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

    async fetchBoardByIdForPublic(path, method) {
      const toast = useToast();
      const authStore = AuthUserStore();

      try {
        const res = await fetch(`${import.meta.env.VITE_BASE_URL + path}`, {
          method: method,
        });

        if (res.ok) {
          const boardData = await res.json();
          this.boardDetails = boardData;
          const currentUser = localStorage.getItem("currentUser");
          const currentUserId = await authStore.findCurrentUser(currentUser);
          this.isOwner = boardData.ownerID === currentUserId;
          this.hasAccess = this.isOwner || boardData.visibility === "PUBLIC";
          this.visibility = boardData.visibility;
        } else if (res.status === 401) {
          router.push("/login");
          // real BE
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

    // async updateVisibility(boardID, newVisibility) {
    //   const token = localStorage.getItem("token")
    //   const toast = useToast()

    //   await patchItem(`${import.meta.env.VITE_BASE_URL}/boards` , boardID , newVisibility )
    //   try {
    //     const res = await fetch(
    //       `${import.meta.env.VITE_BASE_URL}/boards/${boardID}`,
    //       {
    //         method: "PATCH",
    //         headers: {
    //           "Content-Type": "application/json",
    //           Authorization: `Bearer ${token}`,
    //         },
    //         body: `"visibility" : "${newVisibility}"`,
    //       }
    //     )
    //     if (res.ok) {
    //       this.visibility = newVisibility
    //       toast.success(`Visibility updated to ${newVisibility}`)
    //     } else if (res.status === 401) {
    //       router.push("/login")
    //     } else if (res.status === 403) {
    //       toast.error("You do not have permission to change visibility.")
    //     } else {
    //       toast.error("There was a problem. Please try again later.")
    //     }
    //   } catch (error) {
    //     console.error("Error changing visibility:", error)
    //     toast.error("An error occurred while changing visibility.")
    //   }
    // },
    async updateVisibility(boardID, newVisibility) {
      const toast = useToast();
      const authStore = AuthUserStore()

      try {
        const patchData = { visibility: newVisibility }; // Set the data to be patched
        const patchedItem = await patchItem(
          `${import.meta.env.VITE_BASE_URL}/boards/${boardID}`,
          patchData
        );
        if (patchedItem) {
          this.visibility = newVisibility; // Update local visibility
        }
      } catch (error) {
        console.error("Error changing visibility:", error);
        if (error.message.includes("401")) {
          localStorage.removeItem("token");
          router.push("/login");
        } else if (error.message.includes("403")) {
          toast.error("You do not have permission to change visibility.");
        } else {
          toast.error("There was a problem. Please try again later.");
        }
      }
    },
  },
});
