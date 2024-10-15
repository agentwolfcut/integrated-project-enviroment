import { useBoardPermissionStore } from "@/stores/BoardPermissionStore";
import { useToast } from "vue-toast-notification";
import router from "@/router";

export async function checkBoardAccess(to, from, next) {
  const boardPermStore = useBoardPermissionStore();
  const token = localStorage.getItem("token");
  const toast = useToast();

  if (!token) {
    toast.warning("You are guest user");
    next("/test"); // Redirect to login if user is not authenticated
  } else {
    const boardID = to.params.boardID;
    await boardPermStore.fetchBoardById(`/boards/${boardID}`);

    const { hasAccess, isOwner } = boardPermStore;
    if (hasAccess || isOwner) {
      next(); // Proceed if the user has access or is the owner
    } else {
      toast.error("Access denied, you do not have permission to view this page.");
      next("/test"); // Redirect to Access Denied page if access is denied
    }
  }
}