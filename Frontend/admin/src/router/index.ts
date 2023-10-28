/* eslint-disable @typescript-eslint/no-explicit-any */
import { createRouter, createWebHistory, useRouter } from 'vue-router';

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
    component: HomePage,
    beforeEnter: (to: any, from: any, next: () => void) => {
      const useRouterTo = useRouter();

      const getInforAdmin = localStorage.getItem('infor_admin');
      if (!getInforAdmin) {
        useRouterTo.push('/login');
      }
      next();
    }
  },
  {
    path: '/dashboard',
    redirect: { path: '/' }
  },
  {
    path: '/mnabout',
    name: 'mnabout',
    component: AboutPage,
    beforeEnter: (to: any, from: any, next: () => void) => {
      const useRouterTo = useRouter();

      const getInforAdmin = localStorage.getItem('infor_admin');
      if (!getInforAdmin) {
        useRouterTo.push('/login');
      }
      next();
    }
  },
  {
    path: '/mnuirecruitment',
    name: 'mnuirecruitment',
    component: RecruitmentPage,
    beforeEnter: (to: any, from: any, next: () => void) => {
      const useRouterTo = useRouter();

      const getInforAdmin = localStorage.getItem('infor_admin');
      if (!getInforAdmin) {
        useRouterTo.push('/login');
      }
      next();
    }
  },
  {
    path: '/mnproduct',
    name: 'mnproduct',
    component: ProductMN,
    beforeEnter: (to: any, from: any, next: () => void) => {
      const useRouterTo = useRouter();

      const getInforAdmin = localStorage.getItem('infor_admin');
      if (!getInforAdmin) {
        useRouterTo.push('/login');
      }
      next();
    }
  },
  {
    path: '/mnactivity',
    name: 'mnactivity',
    component: ActivityVue,
    beforeEnter: (to: any, from: any, next: () => void) => {
      const useRouterTo = useRouter();

      const getInforAdmin = localStorage.getItem('infor_admin');
      if (!getInforAdmin) {
        useRouterTo.push('/login');
      }
      next();
    }
  },
  {
    path: '/mnstaff',
    name: 'mnstaff',
    component: Staff,
    beforeEnter: (to: any, from: any, next: () => void) => {
      const useRouterTo = useRouter();

      const getInforAdmin = localStorage.getItem('infor_admin');
      if (!getInforAdmin) {
        useRouterTo.push('/login');
      }
      next();
    }
  },
  {
    path: '/mncategory',
    name: 'mncategory',
    component: CategoryMN,
    beforeEnter: (to: any, from: any, next: () => void) => {
      const useRouterTo = useRouter();

      const getInforAdmin = localStorage.getItem('infor_admin');
      if (!getInforAdmin) {
        useRouterTo.push('/login');
      }
      next();
    }
  },
  {
    path: '/',
    name: 'dashboard',
    component: Dashboard,
    beforeEnter: (to: any, from: any, next: () => void) => {
      const useRouterTo = useRouter();

      const getInforAdmin = localStorage.getItem('infor_admin');
      if (!getInforAdmin) {
        useRouterTo.push('/login');
      }
      next();
    }
  },
  {
    path: '/:catchAll(.*)*',
    component: NotFound
  },
  {
    path: '/mncompany',
    name: 'mncompany',
    component: ManageCompany,
    beforeEnter: (to: any, from: any, next: () => void) => {
      const useRouterTo = useRouter();

      const getInforAdmin = localStorage.getItem('infor_admin');
      if (!getInforAdmin) {
        useRouterTo.push('/login');
      }
      next();
    }
  },
  {
    path: '/mnrecruitment',
    name: 'mnrecruitment',
    component: MNRecruitmentVue,
    beforeEnter: (to: any, from: any, next: () => void) => {
      const useRouterTo = useRouter();

      const getInforAdmin = localStorage.getItem('infor_admin');
      if (!getInforAdmin) {
        useRouterTo.push('/login');
      }
      next();
    }
  },
  {
    path: '/login',
    name: 'login',
    component: Login,
    beforeEnter: (to: any, from: any, next: () => void) => {
      const useRouterTo = useRouter();

      const getInforAdmin = localStorage.getItem('infor_admin');
      if (getInforAdmin) {
        useRouterTo.push(from.fullPath);
      }
      next();
    }
  },
  {
    path: '/forgot',
    name: 'forgot',
    component: Forgot
  },
  {
    path: '/mnpolicy',
    name: 'mnpolicy',
    component: ManagePolicy,
    beforeEnter: (to: any, from: any, next: () => void) => {
      const useRouterTo = useRouter();

      const getInforAdmin = localStorage.getItem('infor_admin');
      if (!getInforAdmin) {
        useRouterTo.push('/login');
      }
      next();
    }
  },
  {
    path: '/mncustomer',
    name: 'mncustomer',
    component: ManageCustomer,
    beforeEnter: (to: any, from: any, next: () => void) => {
      const useRouterTo = useRouter();

      const getInforAdmin = localStorage.getItem('infor_admin');
      if (!getInforAdmin) {
        useRouterTo.push('/login');
      }
      next();
    }
  },
  {
    path: '/mnui',
    name: 'mnui',
    component: ManageUI,
    beforeEnter: (to: any, from: any, next: () => void) => {
      const useRouterTo = useRouter();

      const getInforAdmin = localStorage.getItem('infor_admin');
      if (!getInforAdmin) {
        useRouterTo.push('/login');
      }
      next();
    }
  },
  {
    path: '/mnbanner',
    name: 'mnbanner',
    component: UpdateBanner,
    beforeEnter: (to: any, from: any, next: () => void) => {
      const useRouterTo = useRouter();

      const getInforAdmin = localStorage.getItem('infor_admin');
      if (!getInforAdmin) {
        useRouterTo.push('/login');
      }
      next();
    }
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

export default router;
