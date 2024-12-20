import { useBoardPermissionStore } from "@/stores/BoardPermissionStore";
import { useToast } from "vue-toast-notification";
import router from "@/router";
import { getItemById } from "./fetchUtils";
import { AuthUserStore } from "@/stores/store";

export async function checkBoardAccess(to, from, next) {
  const boardPermissionStore = useBoardPermissionStore();
  const boardID = to.params.boardID;
  if (!boardID) {
    next('/access-deny');
    return;
  }

  try {
    const token = localStorage.getItem("token");
    if (token) {
      console.log(token);
      
      await boardPermissionStore.fetchBoardById(`/boards/${boardID}`, 'GET');
    } else {
      await boardPermissionStore.fetchBoardByIdForPublic(`/boards/${boardID}`, 'GET');
    }
    const { hasAccess, isOwner , isCollab , isEditor } = boardPermissionStore;

    if (hasAccess || isOwner || isCollab || isEditor) {
      next(); // Proceed to the route
    } else {
      const toast = useToast();
      toast.error("Access denied, you do not have permission to view this page.");
      next('/access-deny');
    }
  } catch (error) {
    console.error("Error in checkBoardAccess:", error);
    const toast = useToast();
    toast.error("An error occurred while checking permissions.");
    next('/access-deny');
  }
}
