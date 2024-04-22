import { createRouter, createWebHistory } from 'vue-router'
import FirstPage from '../components/FirstPage.vue'

// set history of stor path when visit
const history = createWebHistory()

// give roue paths
const routes = [
  // first page & wait t. give endpoint
  {path:'/' , 
  name:'',
  component:FirstPage},
]

const router = createRouter({
  history,
  routes,
  linkActiveClass: 'text-blue-600',
})
export default router