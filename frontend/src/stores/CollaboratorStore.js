import { defineStore } from "pinia";
import { useToast } from "vue-toast-notification";
import { getItems, addItem, editItem, deleteItemById } from "@/libs/fetchUtils";
import router from "@/router";
import { AuthUserStore } from "@/stores/store";

export const useCollaboratorStore = defineStore("collaboratorStore", {
  state: () => ({
    collaborators: [],
  }),
  actions: {
    async fetchCollaborators(boardId) {
      const authStore = AuthUserStore();
      authStore.checkAccessToken();

      try {
        const token = localStorage.getItem("token") || authStore.token;
        if (!token) {
          throw new Error("Authorization token is missing.");
        }

        const res = await fetch(
          `${import.meta.env.VITE_BASE_URL}/boards/${boardId}/collabs`,
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (!res.ok) {
          const errorText = await res.text(); // Inspect response text for debugging
          console.error("Error response:", errorText);
          throw new Error(`HTTP error! Status: ${res.status}`);
        }

        try {
          const data = await res.json();
          this.collaborators = data;
          // console.log(res.json());
        } catch (jsonError) {
          console.error("Failed to parse JSON:", jsonError);
          console.log("Response body might be:", await res.text());
        }
      } catch (error) {
        console.error("Error fetching collaborators:", error);
      }
    },

    async addCollaboratorOld(boardId, email, access) {
      const toast = useToast();
      try {
        const newCollaborator = await addItem(
          `${import.meta.env.VITE_BASE_URL}/boards/${boardId}/collabs`,
          { email, access }
        );
        if (newCollaborator) {
          toast.success("Collaborator added successfully.");
          this.collaborators.push(newCollaborator);
        } else {
          toast.error("Failed to add collaborator.");
        }
      } catch (error) {
        console.error("Error adding collaborator:", error);
        toast.error("An error occurred while adding the collaborator.");
      }
    },

    async addCollaborator(boardId, email, accessRight) {
      const toast = useToast();
      try {
        const token = localStorage.getItem("token");
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
        toast.error("An error occurred while adding the collaborator.");
      }
    },

    async updateCollaboratorAccess(boardId, collaboratorId, newAccess) {
      const toast = useToast();
      try {
        const updatedCollaborator = await editItem(
          `/boards/${boardId}/collaborators/${collaboratorId}`,
          { access: newAccess }
        );
        if (updatedCollaborator) {
          toast.success("Collaborator access updated successfully.");
          const index = this.collaborators.findIndex(
            (collab) => collab.id === collaboratorId
          );
          if (index !== -1) {
            this.collaborators[index].access = newAccess;
          }
        } else {
          toast.error("Failed to update collaborator access.");
        }
      } catch (error) {
        console.error("Error updating collaborator access:", error);
        toast.error("An error occurred while updating access rights.");
      }
    },

    async removeCollaborator(boardId, collaboratorId) {
      const toast = useToast();
      try {
        const success = await deleteItem(
          `${
            import.meta.env.VITE_BASE_URL
          }/boards/${boardId}/collabs/${collaboratorId}`
        );
        if (success) {
          toast.success("Collaborator removed successfully.");
          this.collaborators = this.collaborators.filter(
            (collab) => collab.id !== collaboratorId
          );
        } else {
          toast.error("Failed to remove collaborator.");
        }
      } catch (error) {
        console.error("Error removing collaborator:", error);
        toast.error("An error occurred while removing the collaborator.");
      }
    },
  },
});
