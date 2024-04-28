import { createRouter, createWebHistory } from 'vue-router'
import FirstPage from '@/components/FirstPage.vue'
import TaskDetail from '@/components/TaskDetail.vue'
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
    component: TaskDetail,
    redirect: '/task',
    // beforeEnter: (to, from, next) => {
    //   const id = to.params.id;
    //   if (id === '100') {
    //     next('/task');
    //   } else {
    //     next();
    //   }
    // },
  },

]
const router = createRouter({
  history,
  routes,
  linkActiveClass: 'text-blue-600',
})
export default router