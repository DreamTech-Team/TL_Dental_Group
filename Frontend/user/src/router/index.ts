import { createRouter, createWebHistory } from 'vue-router';

import HomePage from '@/pages/Home/HomePage.vue';
const AboutPage = () => import('@/pages/About/AboutPage.vue');
const NewsPage = () => import('@/pages/News/NewsPage.vue');
const NotFound = () => import('@/pages/NotFound/NotFound.vue');
const RecruimentPage = () => import('@/pages/Recruitment/RecruitmentPage.vue');

const routes = [
  {
    path: '/',
    name: 'trangchu',
    component: HomePage
  },
  {
    path: '/trangchu',
    redirect: { path: '/' }
  },
  {
    path: '/gioithieu',
    name: 'gioithieu',
    component: AboutPage
  },
  {
    path: '/tuyendung',
    name: 'tuyendung',
    component: RecruimentPage
  },
  {
    path: '/tintuc',
    name: 'tintuc',
    component: NewsPage
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
