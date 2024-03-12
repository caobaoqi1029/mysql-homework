import router from "@/router";

router.beforeEach((to, from, next) => {
    let tokenStr = localStorage.getItem("token");
    let userInfo = JSON.parse(localStorage.getItem("userInfo"))

    function noToken() {
        if (to.path == '/login' || to.path == '/addAdmin' || to.path == '/') {
            next();
        } else {
            next({path: '/login'})
        }
    }

    function hasToken() {
        //去登录注册
        if (to.path == '/login' || to.path == '/' || to.path == '/addAdmin') {
            if (userInfo.adminId != null) {
                next({path: '/admin'})
            } else if (userInfo.teaId != null) {
                next({path: '/teacher'})
            } else if (userInfo.stuId != null) {
                next({path: '/student'})
            }
        }
        //去里层
        else {
            if ((userInfo.adminId != null && to.path.substring(1, 2) == 'a') ||
                (userInfo.teaId != null && to.path.substring(1, 2) == 't') ||
                (userInfo.stuId != null && to.path.substring(1, 2) == 's')) {
                next()
            } else if ((userInfo.adminId != null && to.path.substring(1, 2) != 'a') ||
                (userInfo.teaId != null && to.path.substring(1, 2) !== 't') ||
                (userInfo.stuId != null && to.path.substring(1, 2) !== 's')) {
                next({path: from.path})
            }
        }


    }

    if (tokenStr == null || tokenStr == '') {
        noToken()
    } else if (tokenStr != null && tokenStr != '') {
        hasToken()
    }
})