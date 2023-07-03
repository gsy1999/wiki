import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import HomeView from '../views/homeView.vue'
import AboutView from "@/views/aboutView.vue";
import AdminEbook from "@/views/admin/admin-ebook.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    component: AboutView
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    // component: () => import(/* webpackChunkName: "about" */ '../views/aboutView.vue') 因为页面比较简单就去掉了懒加载
  },
  {
    path: '/admin/ebook',
    name: 'AdminEbook',
    component: AdminEbook
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
