import { createRouter, createWebHistory } from 'vue-router';

import HomePage from '@/pages/Home/HomePage.vue';
import AboutPage from '@/pages/About/AboutPage.vue';
import NewsPage from '@/pages/News/NewsPage.vue';
const ProductPage = () => import('@/pages/Product/ProductPage.vue');
const DetailPage = () => import('@/pages/Detail/DetailPage.vue');
const NotFound = () => import('@/pages/NotFound/NotFound.vue');
const RecruimentPage = () => import('@/pages/Recruitment/RecruitmentPage.vue');
const RecruitmentDetailsPage = () => import('@/pages/RecruitmentDetails/RecruitmentDetails.vue');
const SearchPage = () => import('@/pages/Search/SearchPage.vue');
const ContactPage = () => import('@/pages/Contact/ContactPage.vue');
const PolicyPage = () => import('@/pages/Policy/PolicyPage.vue');
const NewsDetailPage = () => import('@/pages/NewsDetail/NewsDetail.vue');
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
    path: '/sanpham/:catchAll(.*)*',
    name: 'sanpham',
    component: ProductPage
  },
  {
    path: '/chitiet/:catchAll(.*)*',
    name: 'chitiet',
    component: DetailPage
  },
  {
    path: '/tuyendung',
    name: 'tuyendung',
    component: RecruimentPage
  },
  {
    path: '/tuyendung/:catchAll(.*)*',
    name: 'chitiettuyendung',
    component: RecruitmentDetailsPage
  },
  {
    path: '/chinhsach',
    name: 'chinhsach',
    component: PolicyPage
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
    path: '/tintuc/:catchAll(.*)*',
    name: 'chitiettintuc',
    component: NewsDetailPage
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

router.beforeEach((to, from, next) => {
  if (to.path !== from.path) {
    window.scrollTo(0, 0);
  }
  next();
});

export default router;
