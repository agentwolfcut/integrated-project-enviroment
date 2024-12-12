import { defineStore } from "pinia";
import { useToast } from "vue-toast-notification";
import { getItems, addItem, editItem, deleteItemById } from "@/libs/fetchUtils";
import router from "@/router";
import { AuthUserStore } from "@/stores/store";

export const useCollaboratorStore = defineStore("collaboratorStore", {
  state: () => ({
    collaborators: [],
    boardId: "",
  }),
  
  actions: {
    async fetchCollaborators(boardId) {
      const authStore = AuthUserStore();
      authStore.checkAccessToken();
      let headers = {};

      try {
        const token = localStorage.getItem("token") || authStore.token;
        if (!token) {
          headers = {
            "Content-Type": "application/json",
          };
          // throw new Error("Authorization token is missing.");
        }
        headers = {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        };
        const res = await fetch(
          `${import.meta.env.VITE_BASE_URL}/boards/${boardId}/collabs`,
          {
            headers: headers,
          }
        );

        if (res.ok) {
          const data = await res.json();
          this.collaborators = data.collaborators
            .map((entry) => entry.collaborator) 
            .sort((a, b) => new Date(a.addedOn) - new Date(b.addedOn))
          this.boardId = data.boardId;
          console.log(this.boardId);
        } else {
          console.error(`Failed to fetch collaborators. Status: ${res.status}`);
        }
      } catch (error) {
        console.error("Error fetching collaborators:", error);
      }
    },

    async addCollaborator(boardId, email, accessRight) {
      const authStore = AuthUserStore();
      authStore.checkAccessToken()
      const token = localStorage.getItem('token') || authStore.token
      const toast = useToast();
      try {
        if (!token) {
          throw new Error("Authorization token is missing.");
        }

        const res = await fetch(
          `${import.meta.env.VITE_BASE_URL}/boards/${boardId}/collabs`,
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify({
              email,
              accessRight,
            }),
          }
        );

        if (res.status === 201) {
          const newCollaborator = await res.json();
          this.collaborators.push(newCollaborator);
          toast.success("Collaborator added successfully.");
          return true; // Notify success
        } else if (res.status === 401) {
          toast.error("Unauthorized. Please login again.");
        } else if (res.status === 403) {
          toast.error("You do not have permission to add this collaborator.");
        } else if (res.status === 404) {
          toast.error("The user does not exist.");
        } else if (res.status === 409) {
          toast.error("The user is already a collaborator of this board.");
        } else {
          toast.error("There is a problem. Please try again later.");
        }
        return false;
      } catch (error) {
        console.error("Error adding collaborator:", error);
      }
    },

    async updateCollaboratorAccess(boardId, collaboratorOid, newAccessRight) {
      const toast = useToast();
      const authStore = AuthUserStore();
      authStore.checkAccessToken()
      const token = localStorage.getItem('token') || authStore.token
      try {
        const res = await fetch(
          `${
            import.meta.env.VITE_BASE_URL
          }/boards/${boardId}/collabs/${collaboratorOid}`,
          {
            method: "PATCH",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify({ accessRight: newAccessRight }),
          }
        );

        if (res.status === 200) {
          toast.success("updateed access right successfully.")
          const updatedCollaborator = await res.json()
          this.collaborators = this.collaborators.map((collab) =>
            collab.oid === collaboratorOid ? updatedCollaborator : collab
          )
        } else {
          throw new Error("Failed to update collaborator access.");
        }
      } catch (error) {
        console.error("Error updating access rights:", error);
      }
    },

    async removeCollaborator(boardId, userOid) {
      const authStore = AuthUserStore();
      authStore.checkAccessToken()
      const token = localStorage.getItem('token') || authStore.token
      const toast = useToast();
      try {
        if (!token) {
          throw new Error("Authorization token is missing.");
        }
    
        const res = await fetch(
          `${import.meta.env.VITE_BASE_URL}/boards/${boardId}/collabs/${userOid}`,
          {
            method: "DELETE",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );
    
        if (res.status === 200) {
          toast.success("Collaborator removed successfully.");
          this.collaborators = this.collaborators.filter(
            (collab) => collab.oid !== userOid
          );
          return true;
        } else if (res.status === 401) {
          toast.error("Unauthorized. Please login again.");
        } else if (res.status === 403) {
          toast.error("You do not have permission to remove this collaborator.");
        } else if (res.status === 404) {
          toast.error(`${userOid} is not a collaborator.`);
        } else {
          toast.error("There is a problem. Please try again later.");
        }
        return false;
      } catch (error) {
        console.error("Error removing collaborator:", error);
        toast.error("An error occurred while removing the collaborator.");
        return false;
      }
    }
  },
});
