import { createRouter, createWebHistory } from "vue-router";
import Task from "@/components/FirstPage.vue";
import AddTask from "@/components/AddEditTask.vue";
import NotFoundId from "@/views/NotFoundId.vue";
import NotFound from "@/views/NotFound.vue";
import EditTask from "@/components/EditTask.vue";
import ManageStatus from "@/components/ManageStatus.vue";
import AddStatus from "@/components/AddStatus.vue";
import EditStatus from "@/components/EditStatus.vue";
import Login from "@/views/Login.vue";
import Board from "@/views/Board.vue";
import AddBoard from "@/components/AddBoard.vue";
import { BoardStore } from "@/stores/store.js";
import { useToast } from "vue-toast-notification";

const toast = useToast();
// set history of stor path when visit
const history = createWebHistory(import.meta.env.BASE_URL);
// give roue paths
const routes = [
  // first page & wait t. give endpoint
  { path: "/", redirect: "/login" },
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
      { path: "task/add", component: AddTask, name: "AddTask" , props :true },
      {
        path: ":taskId/edit",
        component: EditTask,
        name: "EditTask",
        props: true,
      },
    ],
    beforeEnter: async (to, from, next) => {
      // Check if the boardID exists
      // You can replace this with your own logic to check if the boardID exists
      const boardID = to.params.boardID;
      const boardStore = BoardStore();
      const board = await boardStore.getBoardById(boardID)
      if (!board) {
        toast.error(`Board  with ID ${boardID} does not exist`);
        next({ path: '/board' });
      } else {
        next();
      }
    },
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
});

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("token");
  if (!token && to.path !== "/login") {
    next("/login");
  } else {
    next();
  }
});

export default router;
