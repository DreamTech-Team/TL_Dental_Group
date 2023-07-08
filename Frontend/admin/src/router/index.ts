import { createRouter, createWebHistory } from 'vue-router';

import HomePage from '@/pages/Home/HomePage.vue';
import ActivityVue from '@/pages/Activity/Activity.vue';
const AboutPage = () => import('@/pages/About/AboutPage.vue');
const NotFound = () => import('@/pages/NotFound/NotFound.vue');
const ProductMN = () => import('@/pages/ProductMN/ProductMN.vue');

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomePage
  },
  {
    path: '/home',
    redirect: { path: '/' }
  },
  {
    path: '/about',
    name: 'about',
    component: AboutPage
  },
  {
    path: '/mnproduct',
    name: 'mnproduct',
    component: ProductMN
  },
  {
    path: '/activity',
    name: 'activity',
    component: ActivityVue
  },
  {
    path: '/:catchAll(.*)*',
    component: NotFound
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

export default router;
