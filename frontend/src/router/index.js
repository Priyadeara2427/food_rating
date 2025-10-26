import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import CanteenView from '../views/CanteenView.vue'
import AddDishView from '../views/AddDishView.vue'
import EditDishView from '../views/EditDishView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/canteen/:id',
    name: 'canteen',
    component: CanteenView,
    props: true
  },
  {
    path: '/add-dish',
    name: 'add-dish',
    component: AddDishView
  },
  {
    path: '/edit-dish/:id',
    name: 'edit-dish',
    component: EditDishView,
    props: true
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router