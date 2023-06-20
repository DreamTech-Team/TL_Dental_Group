import { createRouter, createWebHistory } from 'vue-router';

import HomePage from '@/pages/Home/HomePage.vue';
const AboutPage = () => import('@/pages/About/AboutPage.vue');
const ProductPage = () => import('@/pages/Product/ProductPage.vue');
const NewsPage = () => import('@/pages/News/NewsPage.vue');
const NotFound = () => import('@/pages/NotFound/NotFound.vue');
const RecruimentPage = () => import('@/pages/Recruitment/RecruitmentPage.vue');
const SearchPage = () => import('@/pages/Search/SearchPage.vue');
const ContactPage = () => import('@/pages/Contact/ContactPage.vue');
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
    path: '/sanpham',
    name: 'sanpham',
    component: ProductPage
  },
  {
    path: '/tuyendung',
    name: 'tuyendung',
    component: RecruimentPage
  },
  {
    path: '/timkiem',
    name: 'timkiem',
    component: SearchPage
  },
  {
    path: '/tintuc',
    name: 'tintuc',
    component: NewsPage
  },
  {
    path: '/lienhe',
    name: 'lienhe',
    component: ContactPage
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
