import { defineStore } from "pinia";
import { useToast } from "vue-toast-notification";
import router from "@/router";
import { AuthUserStore } from "@/stores/store";
import { patchItem } from "@/libs/fetchUtils";
import { useUserStore } from "@/stores/UserStore";
import { ref } from "vue";
import { useCollaboratorStore } from "./CollaboratorStore";

export const useBoardPermissionStore = defineStore("boardPermission", {
  state: () => ({
    boardDetails: null,
    isOwner: false,
    hasAccess: false,
    user: null,
    collaborators: [],
    isCollab: false,
    isEditor: false,
    realCollab : []
  }),

  actions: {
    transformUserFormat(user) {
      // Transform "ITBKK SANIT" to "itbkk.sanit"
      return user.toLowerCase().split(" ").join(".");
    },
    isCollaboratorWrite(username) {
      // Check if the user is a collaborator with 'WRITE' access
      return this.collaborators.some(
        (collab) =>
          collab.username === username && collab.accessRight === "WRITE"
      );
    },

    async fetchBoardById(path, method) {
      const toast = useToast();
      const authStore = AuthUserStore();
      const collabStore = useCollaboratorStore();
      authStore.checkAccessToken();
      // const token = authStore.token;
      const token = localStorage.getItem("token"); // fix problem 1
      console.log(token);

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
          console.log(`current user is : ${currentUser}`);
          const currentUserId = await authStore.findCurrentUser(currentUser);
          this.isOwner = boardData.ownerID === currentUserId;
          console.log(`isOwner : ${this.isOwner}`);
          // collab
          await collabStore.fetchCollaborators(boardData.id);
          this.collaborators = collabStore.collaborators.map(
            (collab) => collab.name // Extract only the name
          );
          console.log("Collaborators:", this.collaborators);
          this.realCollab = collabStore.collaborators

          // Check if currentUser exists in collaborators
          this.isCollab = this.collaborators.includes(currentUser);
          console.log(`Is Collaborator: ${this.isCollab}`);

          // Check if currentUser is an editor
          this.isEditor = this.realCollab.some(
            (collab) =>
              collab.name === currentUser && collab.accessRight === "WRITE"
          );
          console.log(`Is Editor: ${this.isEditor}`);
          

          this.hasAccess =
            this.isOwner ||
            boardData.visibility === "PUBLIC" ||
            this.isCollab ||
            this.isEditor

          this.visibility = boardData.visibility;
        } else if (res.status === 401) {
          router.push("/login");
          // real BE
        } else if (res.status === 403) {
          console.log("You do not have permission to access this board.");
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
          if (currentUser) {
            const formattedUser = this.transformUserFormat(currentUser);
            this.user = formattedUser; // Store transformed user
          }
          const currentUserId = await authStore.findCurrentUser(currentUser);
          this.isOwner = boardData.ownerID === currentUserId;          
          this.hasAccess = this.isOwner || boardData.visibility === "PUBLIC" || this.isCollab;
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

    async updateVisibility(boardID, newVisibility) {
      const toast = useToast();
      const authStore = AuthUserStore();

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
