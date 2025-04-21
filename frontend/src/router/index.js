import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '../components/LoginPage.vue';
import HomePage from '../components/HomePage.vue';
import CrewMemberProfile from '../components/CrewMemberProfile.vue';
import GameSchedule from '../components/GameSchedule.vue';
import CrewList from '../components/CrewList.vue';
import AdminDashboard from '../components/AdminDashboard.vue';
import GameScheduleManager from '../components/GameScheduleManager.vue';
import CrewScheduler from '../components/CrewScheduler.vue';
import CrewMemberRegistration from '../components/CrewMemberRegistration.vue';
import crewData from '../../db.json';

// Navigation guard
const requireAuth = (to, from, next) => {
  if (!crewData.currentUser) {
    next('/login');
  } else {
    next();
  }
};

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: LoginPage
    },
    {
      path: '/',
      name: 'Home',
      component: HomePage,
      beforeEnter: requireAuth
    },
    {
      path: '/crew-member/:id',
      name: 'CrewMemberProfile',
      component: CrewMemberProfile,
      beforeEnter: requireAuth
    },
    {
      path: '/schedule',
      name: 'GameSchedule',
      component: GameSchedule,
      beforeEnter: requireAuth
    },
    {
      path: '/crew-list/:id',
      name: 'CrewList',
      component: CrewList,
      beforeEnter: requireAuth
    },
    {
      path: '/admin',
      name: 'AdminDashboard',
      component: AdminDashboard,
      beforeEnter: (to, from, next) => {
        if (!crewData.currentUser) {
          next('/login');
        } else if (crewData.currentUser.role === 'ADMIN') {
          next();
        } else {
          next('/');
        }
      }
    },
    {
      path: '/admin/schedules',
      name: 'GameScheduleManager',
      component: GameScheduleManager,
      beforeEnter: (to, from, next) => {
        if (!crewData.currentUser) {
          next('/login');
        } else if (crewData.currentUser.role === 'ADMIN') {
          next();
        } else {
          next('/');
        }
      }
    },
    {
      path: '/admin/schedule-crew/:id',
      name: 'CrewScheduler',
      component: CrewScheduler,
      beforeEnter: (to, from, next) => {
        if (!crewData.currentUser) {
          next('/login');
        } else if (crewData.currentUser.role === 'ADMIN') {
          next();
        } else {
          next('/');
        }
      }
    },
    {
      path: '/crewmember/register',
      name: 'CrewMemberRegistration',
      component: CrewMemberRegistration,
      beforeEnter: (to, from, next) => {
        const token = to.query.token;
        if (!token) {
          next('/');
        } else {
          const invitation = crewData.invitations?.find(inv => inv.token === token);
          if (!invitation) {
            next('/');
          } else {
            next();
          }
        }
      }
    }
  ]
});

// Global navigation guard
router.beforeEach((to, from, next) => {
  // Allow access to login and registration pages without authentication
  if (to.path === '/login' || to.path === '/crewmember/register') {
    if (crewData.currentUser && to.path === '/login') {
      next('/');
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;