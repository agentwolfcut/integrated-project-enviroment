import { createRouter, createWebHistory } from "vue-router";
import Task from "../views/ManageTask.vue";
import AddTask from "@/components/AddTask.vue";
import NotFound from "@/views/NotFound.vue";
import EditTask from "@/components/EditTask.vue";
import ManageStatus from "@/views/ManageStatus.vue";
import AddStatus from "@/components/AddStatus.vue";
import EditStatus from "@/components/EditStatus.vue";
import Login from "@/views/Login.vue";
import Board from "@/views/Board.vue";
import AddBoard from "@/components/AddBoard.vue";
import AccessDeny from "@/views/AccesDeny.vue";
import { checkBoardAccess } from "@/libs/authGuard.js"; // Import the reusable function
import { useBoardPermissionStore } from "@/stores/BoardPermissionStore";
import BoardCollab from "@/views/BoardCollab.vue";

const routes = [
  { path: "/", redirect: "/login" },
  { path: "/test", name: "AccessDeny", component: AccessDeny },
  { path: "/login", name: "Login", component: Login },

  // ห้ามลบ
  {
    path: "/status",
    component: ManageStatus,
  },

  {
    path: "/board/:boardID",
    name: "Task",
    component: Task,
    props: true,
    children: [
      {
        path: "collab",
        component: BoardCollab,
        name: "CollabBoard",
        props: true,
        // meta: { requiresOwner: true },
      },
      {
        path: "task/add",
        component: AddTask,
        name: "AddTask",
        props: true,
        meta: { requiresOwner: true },
      },
      {
        path: ":taskId/edit",
        component: EditTask,
        name: "EditTask",
        props: true,
        meta: { requiresOwner: true },
      },
    ],
    beforeEnter: checkBoardAccess, // Use the permission check function
  },

  {
    path: "/board/:boardID/status",
    component: ManageStatus,
    name: "Status",
    props: true,
    children: [
      {
        path: "add",
        component: AddStatus,
        name: "AddStatus",
        meta: { requiresOwner: true },
      },
      {
        path: ":id/edit",
        component: EditStatus,
        name: "EditStatus",
        props: true,
        meta: { requiresOwner: true },
      },
    ],
    beforeEnter: checkBoardAccess, // Use the permission check function
  },
  {
    path: "/board/:boardID/collab",
    component: BoardCollab,
    name: "CollabBoard",
    props: true,
    // meta: { requiresOwner: true },
  },

  {
    path: "/board",
    name: "Board",
    component: Board,
    children: [{ path: "add", component: AddBoard, name: "AddBoard" }],
  },

  {
    path: "/:notfoundpath(.*)",
    name: "NotFound",
    component: NotFound,
    redirect: "/test", // Redirect to Access Denied on not found
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

router.beforeEach(async (to, from, next) => {
  const boardPermissionStore = useBoardPermissionStore();
  const boardID = to.params.boardID;

  if (to.meta.requiresOwner) {
    if (!boardID) {
      console.log("Missing board ID, redirecting to Access Denied");
      return next("/test");
    }
    try {
      // Use token-based or public fetch to get board permissions
      const token = localStorage.getItem("token");
      if (token) {
        await boardPermissionStore.fetchBoardById(`/boards/${boardID}`, "GET");
      } else {
        await boardPermissionStore.fetchBoardByIdForPublic(
          `/boards/${boardID}`,
          "GET"
        );
      }
      // Validate access permissions
      const { isOwner, isCollab } = boardPermissionStore;
      if (isOwner || isCollab) {
        console.log("Access granted to board");
        return next(); // Proceed to the route
      } else {
        console.log("Access denied: Not owner or collaborator");
        return next("/test"); // Redirect to Access Denied
      }
    } catch (error) {
      console.error("Error fetching board permissions:", error);
      return next("/test"); // Redirect to Access Denied
    }
  }
  next();
});

export default router;
