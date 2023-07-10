import { createRouter, createWebHistory } from 'vue-router';

import HomePage from '@/pages/Home/HomePage.vue';
const AboutPage = () => import('@/pages/About/AboutPage.vue');
const NotFound = () => import('@/pages/NotFound/NotFound.vue');
const ProductMN = () => import('@/pages/ProductMN/ProductMN.vue');
const RecruitmentPage = () => import('@/pages/Recruitment/RecruitmentPage.vue');
const CategoryMN = () => import('@/pages/CategoryMN/CategoryMN.vue');

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
    path: '/tuyendung',
    name: 'tuyendung',
    component: RecruitmentPage
  },
  {
    path: '/mnproduct',
    name: 'mnproduct',
    component: ProductMN
  },
  {
    path: '/mncategory',
    name: 'mncategory',
    component: CategoryMN
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
