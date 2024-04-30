import { createRouter, createWebHistory } from 'vue-router'
import FirstPage from '@/components/FirstPage.vue'
import TaskDetail from '@/components/TaskDetail.vue'
import AddTask from '@/components/AddTask.vue'
import NotFoundId from '@/views/NotFoundId.vue'
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
    component: FirstPage
  },

  {
    path: '/task/:id',
    name: 'TaskDetail',
    component: NotFoundId,
    
  },

  { path: '/task/add', component: AddTask }
]
const router = createRouter({
  history,
  routes,
  linkActiveClass: 'text-blue-600',
})
export default router