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
import AccessDeny from '@/views/AccesDeny.vue'
import { checkBoardAccess } from "@/libs/authGuard.js"; // Import the reusable function

const routes = [
  { path: "/", redirect: "/login" },
  { path: "/test", name: "AccessDeny", component: AccessDeny },
  { path: "/login", name: "Login", component: Login },

  // ห้ามลบ
  {
    path: "/status",
    component: ManageStatus,
    children: [
      { path: "add", component: AddStatus, name: "AddStatus" },
      { path: ":id/edit", component: EditStatus, name: "EditStatus", props: true },
    ],
  },

  {
    path: "/board/:boardID",
    name: "Task",
    component: Task,
    props: true,
    children: [
      { path: "task/add", component: AddTask, name: "AddTask", props: true },
      { path: ":taskId/edit", component: EditTask, name: "EditTask", props: true },
    ],
    beforeEnter: checkBoardAccess, // Use the permission check function
  },

  {
    path: "/board/:boardID/status",
    component: ManageStatus,
    name: "Status",
    props: true,
    children: [{ path: "add", component: AddStatus, name: "AddStatus" }],
    beforeEnter: checkBoardAccess, // Use the permission check function
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
  linkActiveClass: "text-blue-300",
})


export default router;