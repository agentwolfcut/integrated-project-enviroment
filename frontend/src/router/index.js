import { createRouter, createWebHistory } from "vue-router";
import Task from "../views/ManageTask.vue";
import AddTask from "@/components/AddTask.vue";
import NotFoundId from "@/views/NotFoundId.vue";
import NotFound from "@/views/NotFound.vue";
import EditTask from "@/components/EditTask.vue";
import ManageStatus from "@/views/ManageStatus.vue";
import AddStatus from "@/components/AddStatus.vue";
import EditStatus from "@/components/EditStatus.vue";
import Login from "@/views/Login.vue";
import Board from "@/views/Board.vue";
import AddBoard from "@/components/AddBoard.vue";
import AccesDeny from "@/views/AccesDeny.vue";


// set history of stor path when visit
const history = createWebHistory(import.meta.env.BASE_URL);
// give roue paths
const routes = [
  // first page & wait t. give endpoint
  { path: "/", redirect: "/login" },
  { path: "/test", name: "AccessDeny", component: AccesDeny },
  { path: "/login", name: "Login", component: Login },
  {
    path: "/status",
    component: ManageStatus,
    children: [
      {
        path: "add",
        component: AddStatus,
        name: "AddStatus",
      },
      {
        path: ":id/edit",
        component: EditStatus,
        name: "EditStatus",
        props: true,
      },
    ],
  },
  { path: "/task/:id", name: "TaskDetail", component: NotFoundId },
  {
    path: "/:notfoundpath(.*)",
    name: "NotFound",
    component: NotFound,
    redirect: "/login",
  },

  // v3
  {
    path: "/board/:boardID",
    name: "Task",
    component: Task,
    props: true,
    children: [
      { path: "task/add", component: AddTask, name: "AddTask", props: true },
      {
        path: ":taskId/edit",
        component: EditTask,
        name: "EditTask",
        props: true,
      },
    ]
  },

  {
    path: "/board/:boardID/status",
    component: ManageStatus,
    name: "Status",
    props: true,
    children: [{ path: "add", component: AddStatus, name: "AddStatus" }],
  },
  {
    path: "/board",
    name: "Board",
    component: Board,
    children: [{ path: "add", component: AddBoard, name: "AddBoard" }],
  },
];

const router = createRouter({
  history,
  routes,
  linkActiveClass: "text-blue-300",
})
export default router;