import { defineStore } from "pinia";
import { useToast } from "vue-toast-notification";
import { getItems, addItem, editItem, deleteItemById } from "@/libs/fetchUtils";
import router from "@/router";

export const useCollaboratorStore = defineStore("collaboratorStore", {
  state: () => ({
    collaborators: [],
  }),
  actions: {
    async fetchCollaborators(boardId) {
      try {
        // wait api from backend
        const data = await getItems(`/boards/${boardId}/collaborators`);
        this.collaborators = data;
      } catch (error) {
        console.error("Error fetching collaborators:", error);
      }
    },

    async addCollaborator(boardId, email, access) {
      const toast = useToast();
      try {
        const newCollaborator = await addItem(
          `/boards/${boardId}/collaborators`,
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

    async updateCollaboratorAccess(boardId, collaboratorId, newAccess) {
      const toast = useToast();
      try {
        const updatedCollaborator = await editItem(`/boards/${boardId}/collaborators/${collaboratorId}`, { access: newAccess });
        if (updatedCollaborator) {
          toast.success("Collaborator access updated successfully.");
          const index = this.collaborators.findIndex((collab) => collab.id === collaboratorId);
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
        const success = await deleteItem(`/boards/${boardId}/collaborators/${collaboratorId}`);
        if (success) {
          toast.success("Collaborator removed successfully.");
          this.collaborators = this.collaborators.filter((collab) => collab.id !== collaboratorId);
        } else {
          toast.error("Failed to remove collaborator.");
        }
      } catch (error) {
        console.error("Error removing collaborator:", error);
        toast.error("An error occurred while removing the collaborator.");
      }
    },
  },
})