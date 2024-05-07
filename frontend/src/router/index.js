import { createRouter, createWebHistory } from 'vue-router'
import FirstPage from '@/components/FirstPage.vue'
import AddEditTask from '@/components/AddEditTask.vue'
import NotFoundId from '@/views/NotFoundId.vue'
import NotFound from '@/views/NotFound.vue'
import EditTask from '@/components/EditTask.vue'

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

  { path: '/task/add', component: AddEditTask , name:'AddTask' } ,
  { path: '/task/:taskId/edit', component: EditTask , name:'EditTask'  } ,
  { path: '/:notfoundpath(.*)', name: 'NotFound', component: NotFound , redirect: '/task'}
]
const router = createRouter({
  history,
  routes,
  linkActiveClass: 'text-blue-600',
})
export default router