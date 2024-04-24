import { createRouter, createWebHistory } from 'vue-router'
import TaskList from '../components/TaskList.vue'
import NotFound from '../components/NotFound.vue'

// set history of stor path when visit
const history = createWebHistory()

// give roue paths
const routes = [
  // first page & wait t. give endpoint
  {
    path: '/',
    redirect: '/task'
  },
  {
    path: '/task',
    name: 'taskList',
    component: TaskList
  },

  {
    path: '/:notfoundpath(.*)',
    name: 'NotFound',
    component: NotFound
  }
]

const router = createRouter({
  history,
  routes,
  linkActiveClass: 'text-blue-600',
})
export default router