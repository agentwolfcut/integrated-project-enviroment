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

// set history of stor path when visit
const history = createWebHistory(import.meta.env.BASE_URL);
// give roue paths
const routes = [
  // first page & wait t. give endpoint
  { path: "/", redirect: "/login" },
  { path: "/login", name: "Login", component: Login },
  // {
  //   path: "/task",
  //   name: "Task",
  //   component: FirstPage,
  //   children: [
  //     { path: "add", component: AddTask, name: "AddTask" },
  //     { path: ":taskId/edit", component: EditTask, name: "EditTask" },
  //   ],
  // },
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
  { path: "/board/:boardID", name: "Task", component: Task , props: true,
    children: [
      { path: "add", component: AddTask, name: "AddTask" , props:true },
      { path: ":taskId/edit", component: EditTask, name: "EditTask" },
    ],
  },
  {
    path: "/board",
    name: "Board",
    component: Board,
    children: [
      { path: "add", component: AddBoard, name: "AddBoard" },
    ],
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
