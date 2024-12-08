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
  // case 1 problem
  // authUserStore.checkAccessToken()
  const token = localStorage.getItem('token') 
  if (token) {    
    await boardPermStore.fetchBoardById(`/boards/${boardID}`, "GET");
  } else {
    // no token
    await boardPermStore.fetchBoardByIdForPublic(`/boards/${boardID}`, "GET");
  }
  const { hasAccess, isOwner , isCollab} = boardPermStore
  if (hasAccess || isOwner || isCollab) {
    next(); // Proceed if the user has access or is the owner
  } else {
    toast.error("Access denied, you do not have permission to view this page.");
    next("/test"); // Redirect to Access Denied page if access is denied
  }
}