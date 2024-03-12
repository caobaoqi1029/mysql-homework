import Vue from 'vue'
import VueRouter from 'vue-router'
Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        redirect: '/login',
    },
    {
        path: '/login',
        component: () => import('../views/Login.vue')
    },
    {
        path: '/addAdmin',
        // component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
        component: () => import('../views/AddAdmin.vue')
    },
    {
        path: '/admin',
        redirect: '/admin/index',
        component: () => import('../views/Admin.vue'),
        children: [
            {
                path: 'index',
                component: () => import('../views/admin/Index.vue'),
                // meta:{requireAuth:true}//加了这个，路由跳转前会先去permission.js中验证token
            },
            {
                path: 'stuManage',
                component: () => import('../views/admin/StuManage.vue'),
                // meta:{requireAuth:true},
            },
            {
                path: 'teaManage',
                component: () => import('../views/admin/TeaManage.vue'),
                // meta:{requireAuth:true},

            },
            {
                path: 'majorManage',
                component: () => import('../views/admin/MajorManage.vue'),
                // meta:{requireAuth:true},

            },
            {
                path: 'classManage',
                component: () => import('../views/admin/ClassManage.vue'),
                // meta:{requireAuth:true},

            },
            {
                path: 'courseManage',
                component: () => import('../views/admin/CourseManage.vue'),
            },
            {
                path: 'oneClick',
                component: () => import('../views/admin/OneClick.vue'),
            },
            {
                path: 'delAdmin',
                component: () => import('../views/admin/DelAdmin.vue'),
            },
            {
                path: 'allScore',
                component: () => import('../views/admin/AllScore.vue'),
            },
        ]
    },
    {
        path: '/student',
        redirect: '/student/index',
        component: () => import('../views/Student'),
        children: [
            {
                path: 'index',
                component: () => import('../views/student/Index.vue'),
                // meta:{requireAuth:true}
            },
            {
                path: 'majorScore',
                component: () => import('../views/student/MajorScore.vue'),
                // meta:{requireAuth:true}
            },
            {
                path: 'chooseCourse',
                component: () => import('../views/student/ChooseCourse.vue'),
                // meta:{requireAuth:true}
            },
            {
                path: 'stuMessage',
                component: () => import('../views/student/StuMessage.vue'),
                // meta:{requireAuth:true}
            },
        ]
    },
    {
        path: '/teacher',
        redirect: '/teacher/index',
        component: () => import('../views/Teacher'),
        children: [
            {
                path: 'index',
                component: () => import('../views/teacher/Index.vue'),
            },
            {
                path: 'majorScore',
                component: () => import('../views/teacher/MajorScore.vue')
            },
            {
                path: 'classScore',
                component: () => import('../views/teacher/ClassScore.vue')
            },
            {
                path: 'hasSendMsg',
                component: () => import('../views/teacher/HasSendMsg.vue')
            },
        ]
    },
]

const router = new VueRouter({
    routes
})

export default router
