import { createRouter, createWebHistory } from 'vue-router';

import HomePage from '@/pages/Home/HomePage.vue';
import ActivityVue from '@/pages/Activity/Activity.vue';
import MNRecruitmentVue from '@/pages/ManageRecruitment/MNRecruitment.vue';
const AboutPage = () => import('@/pages/About/AboutPage.vue');
const NotFound = () => import('@/pages/NotFound/NotFound.vue');
const ProductMN = () => import('@/pages/ProductMN/ProductMN.vue');
const ManageCompany = () => import('@/pages/ManageCompany/ManageCompany.vue');
const RecruitmentPage = () => import('@/pages/Recruitment/RecruitmentPage.vue');
const Staff = () => import('@/pages/Staff/ManageStaff.vue');
const CategoryMN = () => import('@/pages/CategoryMN/CategoryMN.vue');
const Dashboard = () => import('@/pages/Dashboard/DashboardComp.vue');
const Login = () => import('@/pages/Authen/LoginPage.vue');
const Forgot = () => import('@/pages/Authen/ForgotPage.vue');
const ManageCustomer = () => import('@/pages/ManageCustomer/ManageCustomer.vue');
const ManageUI = () => import('@/pages/ManageUI/ManageUI.vue');
const ManagePolicy = () => import('@/pages/ManagePolicy/ManagePolicy.vue');
const UpdateBanner = () => import('@/pages/UpdateBanner/UpdateBanner.vue');

const routes = [
  {
    path: '/mnhome',
    name: 'mnhome',
    component: HomePage
  },
  {
    path: '/dashboard',
    redirect: { path: '/' }
  },
  {
    path: '/mnabout',
    name: 'mnabout',
    component: AboutPage
  },
  {
    path: '/mnuirecruitment',
    name: 'mnuirecruitment',
    component: RecruitmentPage
  },
  {
    path: '/mnproduct',
    name: 'mnproduct',
    component: ProductMN
  },
  {
    path: '/mnactivity',
    name: 'mnactivity',
    component: ActivityVue
  },
  {
    path: '/mnstaff',
    name: 'mnstaff',
    component: Staff
  },
  {
    path: '/mncategory',
    name: 'mncategory',
    component: CategoryMN
  },
  {
    path: '/',
    name: 'dashboard',
    component: Dashboard
  },
  {
    path: '/:catchAll(.*)*',
    component: NotFound
  },
  {
    path: '/mncompany',
    name: 'mncompany',
    component: ManageCompany
  },
  {
    path: '/mnrecruitment',
    name: 'mnrecruitment',
    component: MNRecruitmentVue
  },
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  {
    path: '/forgot',
    name: 'forgot',
    component: Forgot
  },
  {
    path: '/mnpolicy',
    name: 'mnpolicy',
    component: ManagePolicy
  },
  // {
  //   path: '/mnpolicy/:catchAll(.*)*',
  //   name: 'mnpolicyedit',
  //   component: ManagePolicyEdit
  // },
  {
    path: '/mncustomer',
    name: 'mncustomer',
    component: ManageCustomer
  },
  {
    path: '/mnui',
    name: 'mnui',
    component: ManageUI
  },
  {
    path: '/mnbanner',
    name: 'mnbanner',
    component: UpdateBanner
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

export default router;
