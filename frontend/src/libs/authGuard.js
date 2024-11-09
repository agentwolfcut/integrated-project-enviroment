import { useBoardPermissionStore } from "@/stores/BoardPermissionStore";
import { useToast } from "vue-toast-notification";
import router from "@/router";
import { getItemById } from "./fetchUtils";
import { AuthUserStore } from '@/stores/store';

export async function checkBoardAccess(to, from, next) {
  const boardPermStore = useBoardPermissionStore();
  const toast = useToast();
  const boardID = to.params.boardID;

  const authUserStore = AuthUserStore()
  const token = authUserStore.token  
  if (token) {    
    await boardPermStore.fetchBoardById(`/boards/${boardID}`, "GET");
  } else {
    // no token
    await boardPermStore.fetchBoardByIdForPublic(`/boards/${boardID}`, "GET");
  }
  const { hasAccess, isOwner } = boardPermStore
  if (hasAccess || isOwner) {
    next(); // Proceed if the user has access or is the owner
  } else {
    toast.error("Access denied, you do not have permission to view this page.");
    next("/test"); // Redirect to Access Denied page if access is denied
  }
}