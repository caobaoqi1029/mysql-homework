---
title: 学生管理系统-前端
order: 4
icon: /project-03.svg
category:
    - PROJECT
    - MD
---

## 配置

### vue.config.js

```js
const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  devServer:{
    host: 'localhost',
    port:'8090',
    open: true
  },
  transpileDependencies: true,
  publicPath:process.env.NODE_ENV=='production'?'':'',
  outputDir:'dist',
  assetsDir:'static',
  // indexPath:'',
  // parallel:''
})
```

### main.js

```js
import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from "axios";
import "./permission"


Vue.use(ElementUI);
Vue.config.productionTip = false
Vue.prototype.$axios = axios

new Vue({
    router,
    store,
    axios,
    render: h => h(App)
}).$mount('#app')

```

### jsconfig.json

```json
{
  "compilerOptions": {
    "target": "es5",
    "module": "esnext",
    "baseUrl": "./",
    "moduleResolution": "node",
    "paths": {
      "@/*": [
        "src/*"
      ]
    },
    "lib": [
      "esnext",
      "dom",
      "dom.iterable",
      "scripthost"
    ]
  }
}

```

### babel.config.js

```js
module.exports = {
  presets: [
    '@vue/cli-plugin-babel/preset'
  ]
}
```

## 工具类

### Request

```js

const service={
    assertsSubDirectory:'static',
    assertsPublicPath:'/',
    baseURL: 'http://'+"localhost"+':'+'8091',
    timeout: 20000,
    changeOrigin:true
}
export default service
```

### Permission.js

```js
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
```

### store 

```js
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {//设置状态
        token: localStorage.getItem("token"),
        userInfo: JSON.parse(localStorage.getItem("userInfo"))
    },
    getters: {
        getUser: state => {//得到用户信息
            return state.userInfo
        },
        getToken: state => {
            return state.token
        }
    },
    mutations: {
        /**
         * 将token存储到浏览器
         * @param state
         * @param token
         * @constructor
         */
        SET_TOKEN: (state, token) => {
            localStorage.setItem("token", token)
            state.token = token;
        },
        /**
         * 存储用户信息
         * @param state
         * @param userinfo
         * @constructor
         */
        SET_USERINFO: (state, userInfo) => {
            localStorage.setItem("userInfo", JSON.stringify(userInfo))
            state.userInfo = userInfo;
        },
        /**
         * 清除浏览器中的token信息
         * @param state
         * @constructor
         */
        REMOVE_INFO: (state) => {
            state.token = "";
            state.userInfo = {};
            localStorage.setItem("token", "")
            localStorage.setItem("userInfo", JSON.stringify(""));
        }
    },
    actions: {},
    modules: {}
})

```

## App.vue

```vue
<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<style>
#app {
  /*font-family: Avenir, Helvetica, Arial, sans-serif;*/
  /*-webkit-font-smoothing: antialiased;*/
  /*-moz-osx-font-smoothing: grayscale;*/
  /*text-align: center;*/
  /*color: #2c3e50;*/
}
body {
  margin: 0;
  padding: 0;
}
</style>

```

## Router 路由

```js
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

```

## 公用组件

### Header 头部

```vue
<template>
  <div class="header">
    <a href="https://gitee.com/cola777jz/mysql-homework"><img src="https://readme-typing-svg.herokuapp.com?font=Agbalumo&amp;color=F7184F&amp;center=true&amp;vCenter=true&amp;width=600&amp;lines=MySQL-Homework;" referrerpolicy="no-referrer" alt="Typing SVG"></a>    <el-button type="success" style="margin-top: 8px" @click="logout" round icon="el-icon-switch-button">退出登录</el-button>
  </div>
</template>

<script>
import axios from "axios";
import service from "@/request";
export default {
  name: "Header",
  methods: {
    logout() {
      axios.get(service.baseURL+"/logout", {
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp) => {
        if (resp.data.code = '20000') {
          this.$store.commit('REMOVE_INFO')
          this.$router.push('/login')
          this.$message.success(resp.data.msg)
        } else {
          this.$message.error("注销失败")
        }
      })
    }
  }
}
</script>

<style scoped>

.header {
  height: 60px;
  line-height: 60px;
  text-align: center;
}

.el-button {
  float: right;
}

h1 {
  display: inline;
  color: #ff8000;
  font-size: 40px;
}
</style>
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209201615374.png" alt="image-20231209201615374" style="zoom:67%;" />

### Admin 页面菜单

```vue
<template>
  <el-menu
      default-active="2"
      class="el-menu-vertical-demo"
      @open="handleOpen"
      @close="handleClose"
      background-color="#545c64"
      text-color="#fff"
      :router="true"
      active-text-color="#ffd04b">
    <el-menu-item index="/admin/index">
      <i class="el-icon-user"></i>
      <span slot="title">个人信息</span>
    </el-menu-item>

    <el-menu-item index="/admin/stuManage">
      <i class="el-icon-setting"></i>
      <span slot="title">学生维护</span>
    </el-menu-item>
    <el-menu-item index="/admin/teaManage">
      <i class="el-icon-setting"></i>
      <span slot="title">班主任维护</span>
    </el-menu-item>
    <el-menu-item index="/admin/classManage">
      <i class="el-icon-setting"></i>
      <span slot="title">班级维护</span>
    </el-menu-item>
    <el-menu-item index="/admin/courseManage">
      <i class="el-icon-setting"></i>
      <span slot="title">课程维护</span>
    </el-menu-item>
    <el-menu-item index="/admin/majorManage">
      <i class="el-icon-setting"></i>
      <span slot="title">专业维护</span>
    </el-menu-item>
    <el-menu-item index="/admin/allScore">
      <i class="el-icon-s-grid"></i>
      <span slot="title">全校成绩表</span>
    </el-menu-item>
    <el-menu-item index="/admin/oneClick">
      <i class="el-icon-thumb"></i>
      <span slot="title">一键功能</span>
    </el-menu-item>
    <el-menu-item index="/admin/delAdmin">
      <i class="el-icon-delete"></i>
      <span slot="title">注销账号</span>
    </el-menu-item>
  </el-menu>
</template>

<script>
export default {
  name: "AdminLeftSide",
  methods: {
    handleOpen(key, keyPath) {
    },
    handleClose(key, keyPath) {
    },
  }
}
</script>

<style scoped>
.el-menu {
  /*width: 300px;*/
  min-height: 100%;
  position: absolute;
  position: fixed;
  width: 200px;
}

</style>
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209201659182.png" alt="image-20231209201659182" style="zoom:67%;" />

### Teacher 页面菜单

```vue
<template>
  <el-menu
      default-active="2"
      class="el-menu-vertical-demo"
      @open="handleOpen"
      @close="handleClose"
      background-color="#545c64"
      text-color="#fff"
      :router="true"
      active-text-color="#ffd04b">
    <el-menu-item index="/teacher/index">
      <i class="el-icon-user"></i>
      <span slot="title">个人信息</span>
    </el-menu-item>
    <el-menu-item index="/teacher/classScore">
      <i class="el-icon-menu"></i>
      <span slot="title">本班成绩表</span>
    </el-menu-item>
    <el-menu-item index="/teacher/majorScore">
      <i class="el-icon-s-grid"></i>
      <span slot="title">本专业成绩表</span>
    </el-menu-item>
    <el-menu-item index="/teacher/hasSendMsg">
      <i class="el-icon-chat-line-square"></i>
      <span slot="title">已发消息</span>
    </el-menu-item>
  </el-menu>
</template>
<script>
export default {
  name: "TeaLeftSide",
  methods:{
    handleOpen(key, keyPath) {
    },
    handleClose(key, keyPath) {
    },
  }
}
</script>

<style scoped>
.el-menu {
  /*width: 300px;*/
  min-height: 100%;
  position: absolute;
  position: fixed;
  width: 200px;
}
</style>
```

### Student 页面菜单
```vue
<template>
  <el-menu
      default-active="2"
      class="el-menu-vertical-demo"
      @open="handleOpen"
      @close="handleClose"
      background-color="#545c64"
      text-color="#fff"
      :router="true"
      active-text-color="#ffd04b">
    <el-menu-item index="/student/index">
      <i class="el-icon-user"></i>
      <span slot="title">个人信息</span>
    </el-menu-item>
    <el-menu-item index="/student/majorScore">
      <i class="el-icon-menu"></i>
      <span slot="title">本专业成绩表</span>
    </el-menu-item>
    <el-menu-item index="/student/chooseCourse">
      <i class="el-icon-school"></i>
      <span slot="title">学生选课</span>
    </el-menu-item>

    <el-menu-item index="/student/stuMessage" @click="noMsgTip">
      <i class="el-icon-chat-dot-square"></i>
      <span slot="title">
        <span>班主任消息</span>
        <el-badge :value="stuUnreadMsgNum" style="margin-left: 15px"></el-badge>
      </span>
    </el-menu-item>
  </el-menu>
</template>

<script>
import axios from "axios";
import service from "@/request";
export default {
  name: "StuLeftSide",
  data() {
    return {
      stuUnreadMsgNum:''
    }
  },
  created() {

  },
  mounted() {
    // 页面加载后
    // 1.在执行定时器前先执行一次获取接口数据的操作函数, 否则接口会1秒钟后才调用
    // 2.为了避免退出当前页面后,在其他页面也继续调用接口,退出前需要清除定时器.
    this.getStuUnreadNum()
    this.timer = setInterval(() => {
      setTimeout(this.getStuUnreadNum, 0)
    }, 1000*5)//50s检测一次未读数量
  },
  methods: {

    noMsgTip(){
      axios.put(service.baseURL+"/stuMessage/hasRead",{
        stuId:this.$store.state.userInfo.stuId
      },{
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp) => {
        if (resp.data.code == '20000') {
            this.stuUnreadMsgNum=0+""//点击后置0
        }
      })
    },

    getStuUnreadNum(){
      axios.get(service.baseURL+"/stuMessage/getStuUnreadNum?stuId="+this.$store.state.userInfo.stuId,{
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp)=>{
        if (resp.data.code=='20000'){
            this.stuUnreadMsgNum=resp.data.data+'';//5分钟动态获取未读数
        }
      })
    },

    handleOpen(key, keyPath) {
    },
    handleClose(key, keyPath) {
    },
  },
  beforeDestroy() {
    clearInterval(this.timer);
    this.timer = null;
  }
}
</script>

<style scoped>
.el-menu {
  /*width: 300px;*/
  min-height: 100%;
  position: absolute;
  position: fixed;
  width: 200px;
}
</style>
```

## 登录注册页面

### 登录

```vue
<template>
  <div style="width: 100vw;height: 100vh;overflow: hidden;display: flex">
    <div class="background-container">
      <el-image class="logo" :src="Bsrc"/>
      <el-image class="background" :src="Msrc"/>
    </div>

    <div class="welcome-info">
      <div style="font-size: 30px">欢迎来到晋中学院</div>
      <div style="margin-top: 5px">士不可以不弘毅，任重而道远。</div>
    </div>

    <div style="width: 400px;background-color: white;z-index: 1">
      <div style="text-align: center;margin-top: 150px">
        <div style="font-size: 25px;font-weight: bold">登录</div>
        <div style="font-size: 14px; color: grey;margin-top: 2px">进入系统前请先进行登录</div>
      </div>
      <div style="margin-left: 55px;margin-top: 5px">
        <el-form :model="loginForm" status-icon :rules="rules" ref="loginForm"
                 class="demo-ruleForm" style="text-align: center">
          <el-form-item prop="id">
            <el-input prefix-icon="el-icon-user" v-model="loginForm.id" autocomplete="off" placeholder="请输入学号 | 班主任编号 | 管理员编号"
                      clearable></el-input>
          </el-form-item>

          <el-form-item prop="password">
            <el-input prefix-icon="el-icon-lock" show-password v-model="loginForm.password" autocomplete="off"
                      placeholder="请输入密码(默认为身份证后 6 位)"
                      clearable></el-input>
          </el-form-item>

          <el-form-item prop="state">
            <el-select clearable placeholder="请选择身份" v-model="loginForm.state">
              <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button style="width:145px" type="success" @click="submitForm('loginForm')" round :disabled="ifDisabled">
              登录
            </el-button>
            <el-button style="width:145px" type="warning" @click="resetForm('loginForm')" round>重置表单</el-button>
          </el-form-item>
          <el-divider>
            <span style="color: grey;font-size: 13px">没有账号？</span>
          </el-divider>
          <el-form-item>
            <el-button style="width:300px" type="danger" round @click="toRegister">立即注册</el-button>
          </el-form-item>
        </el-form>
      </div>

    </div>
  </div>

</template>

<script>
import axios from "axios";
import service from "@/request";

export default {
  name: "Login",
  data() {
    return {
      Msrc: "https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/login-back.jpg",
      Bsrc: "https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/jz-logo.png",
      loginForm: {
        id: "",
        password: "",
        state: ""
      },
      options: [{
        value: '1',
        label: '学生'
      }, {
        value: '2',
        label: '班主任'
      }, {
        value: '3',
        label: '管理员'
      }],
      rules: {
        id: [
          {required: true, message: '编号不能为空', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
          {min: 6, max: 16, message: '密码长度为 6 到 16 个字符', trigger: 'blur'}
        ],
        state: [
          {required: true, message: '请选择身份', trigger: 'blur'}
        ]
      },
      ifDisabled: null
    }

  },
  methods: {
    toRegister() {
      this.$router.push('/addAdmin');
    },
    submitForm(loginForm) {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          axios.post(service.baseURL + "/login?id=" + this.loginForm.id + "&password=" +
              this.loginForm.password + "&state=" + this.loginForm.state,)
              .then((resp) => {
                if (resp.data.code == '20000') {
                  this.$message.success(resp.data.msg);
                  this.$store.commit('SET_TOKEN', resp.data.data.token)
                  this.$store.commit('SET_USERINFO', resp.data.data.user)
                  if (resp.data.data.state == '管理员') {
                    this.ifDisabled = true
                    this.$router.push('/admin')
                  } else if (resp.data.data.state == '学生') {
                    this.ifDisabled = true
                    this.$router.push('/student')
                  } else {
                    this.ifDisabled = true
                    this.$router.push('/teacher')
                  }
                  this.$refs['loginForm'].resetFields();
                } else {
                  this.ifDisabled = false
                  this.$message.error(resp.data.msg)
                }
              }).catch(() => {
          })
        } else {
          return false;
        }
      })
    },
    resetForm(loginForm) {
      this.$refs.loginForm.resetFields();
    }
  }
}
</script>

<style scoped>
.background-container {
  flex: 1;
  position: relative;
}

.logo {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 1; /* 设置z-index属性将logo放在背景图上方 */
}

.background {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.welcome-info {
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: white;
  text-shadow: 0 0 10px black;
}

.login {
  background-image: url("../assets/img/login-back.jpg");
  background-size: 100%;
  height: 100%;
  width: 100%;
  position: absolute;
  display: flex;
  justify-content: center;
  align-items: center;
}

.content {
  text-align: center;
}

h1 {
  color: #a52a47;
}

.el-form {
  width: 300px;
}

.el-form-item .el-select {
  width: 300px;
}

</style>
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231214114606740.png" alt="image-20231214114606740" style="zoom:67%;" />

### 注册

```vue
<template>

  <div class="register">

    <div class="content">
      <h1>MySQL-Homework</h1>
      <el-form :model="registerForm" status-icon :rules="rules" ref="registerForm" class="demo-ruleForm">
        <el-form-item prop="name">
          <el-input prefix-icon="el-icon-user" v-model="registerForm.name" autocomplete="off" placeholder="请输入姓名" clearable></el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" type="password" v-model="registerForm.password" autocomplete="off" placeholder="请输入密码"
                    clearable></el-input>
        </el-form-item>

        <el-form-item prop="checkPwd">
          <el-input prefix-icon="el-icon-lock" type="password" v-model="registerForm.checkPwd" autocomplete="off"
                    placeholder="请再次输入相同密码"
                    clearable></el-input>
        </el-form-item>

        <el-form-item prop="adminSecret">
          <el-input prefix-icon="el-icon-lock" v-model="registerForm.adminSecret" placeholder="请输入管理员密钥" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button style="width:145px"type="success" @click="submitForm('registerForm')" round>注册</el-button>
          <el-button style="width:145px" type="warning" @click="resetForm('registerForm')" round>重置</el-button>
        </el-form-item>
        <el-form-item>
          <el-button style="width:300px" round type="danger" @click="toLogin">返回登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import service from "@/request";

export default {
  name: "Register",
  data() {
    let validateCheckPwd = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入相同密码'));
      } else if (value !== this.registerForm.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      registerForm: {
        name: '',
        password: '',
        checkPwd: '',
        adminSecret: '',
      },
      rules: {
        name: [
          {required: true, message: '姓名不能为空', trigger: 'blur'},
          {min: 2, max: 20, message: '姓名长度为2到20个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
          {min: 6, max: 16, message: '密码长度为6到16个字符', trigger: 'blur'}
        ],
        checkPwd: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
          {validator: validateCheckPwd, trigger: 'blur'}
        ],
        adminSecret: [
          {required: true, message: '管理员密钥不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    toLogin() {
      this.$router.push('/login')
    },
    submitForm(registerForm) {
      this.$refs.registerForm.validate((valid) => {
        if (valid) {
          axios.post(service.baseURL+"/admin/add?adminSecret=" + this.registerForm.adminSecret, {
                'adminName': this.registerForm.name,
                'adminPwd': this.registerForm.password,
              },
          ).then((resp) => {
            if (resp.data.code == '20000') {
              this.$refs['registerForm'].resetFields();
              this.$message.success(resp.data.msg);
            } else {
              this.$message.error(resp.data.msg)
            }

          })
        } else {
          return false;
        }
      });
    },
    resetForm(registerForm) {
      this.$refs.registerForm.resetFields();
    }
  }
}
</script>

<style scoped>
.register {
  background-image: url("https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/jinzhong-02.jpg");
  background-size: 100%;
  height: 100%;
  width: 100%;
  position: absolute;
  display: flex;
  justify-content: center;
  align-items: center;
}

.content {
  text-align: center;
}

h1 {
  color: #a52a47;
}

.el-form {
  width: 300px;
}

.el-form-item .el-select {
  width: 300px;
}
</style>
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231214114627073.png" alt="image-20231214114627073" style="zoom:67%;" />

## 管理员页面

```vue
<template>
  <el-container>
    <el-aside width="200px">
      <AdminLeftSide></AdminLeftSide>
    </el-aside>
    <el-container>
      <el-header style="padding: 0 10px">
        <Header></Header>
      </el-header>
      <el-main style="padding: 0 10px">
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import AdminLeftSide from "@/components/AdminLeftSide";
import Header from "@/components/Header";
export default {
  name: "Admin",
  components: {AdminLeftSide,Header},
  data(){
    return{

    }
  }
}
</script>

<style scoped>

.el-main {
  background-color: #E9EEF3;
  /*text-align: center;*/
  /*line-height: 160px;*/
  width: 100%;
  /*min-height: 100%;*/
  /*position: absolute;*/
}
.el-header{
  /*display: flex;*/
  /*justify-content: center;*/
  /*align-items: center;*/
}

</style>
```

### 管理员信息

```vue
<template>
  <div class="myInfo">
    <el-card class="admin-card">
      <div slot="header" align="center" class="header">
        <span>管理员个人资料</span>
      </div>
      <div class="content">
        <div class="edit">
          <el-button type="primary" round style="float: right;margin-bottom: 15px" @click="openEditAdmin">编辑
          </el-button>
        </div>
        <el-descriptions title="" border :column="column">
          <el-descriptions-item label="身份">管理员</el-descriptions-item>
          <el-descriptions-item label="账号">{{ userInfo.adminId }}</el-descriptions-item>
          <el-descriptions-item label="管理员姓名">{{ userInfo.adminName }}</el-descriptions-item>
          <el-descriptions-item label="密码">{{ userInfo.adminPwd }}</el-descriptions-item>
          <el-descriptions-item label="注册时间">{{ userInfo.createTime }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
    <!--    编辑管理员表单-->
    <div class="editDialog">
      <el-dialog
          @close="clearEditForm"
          title="编辑管理员"
          :visible.sync="editFormVisible">
        <el-form :model="editForm" :rules="editRules" ref="editForm">
          <el-form-item label="账号" label-width="150px" prop="adminId">
            <el-input v-model="editForm.adminId" disabled></el-input>
          </el-form-item>
          <el-form-item label="管理员姓名" label-width="150px" prop="adminName">
            <el-input v-model="editForm.adminName" placeholder="请输入管理员姓名"></el-input>
          </el-form-item>
          <el-form-item label="密码" label-width="150px" prop="adminPwd">
            <el-input v-model="editForm.adminPwd" placeholder="请输入密码" autocomplete="off" clearable
                      type="password"></el-input>
          </el-form-item>
          <el-form-item label="密码" label-width="150px" prop="adminPwd2">
            <el-input v-model="editForm.adminPwd2" placeholder="请再次输入相同密码" autocomplete="off" clearable
                      type="password"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelEdit">取 消</el-button>
          <el-button type="primary" @click="editAdmin">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>

import axios from "axios";
import service from "@/request";

export default {
  data() {
    let validateCheckPwd = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入相同密码'));
      } else if (value !== this.editForm.adminPwd) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      column: 1,
      editFormVisible: false,
      userInfo: {
        adminId: '',
        adminName: '',
        adminPwd: '',
        createTime: '',
      },
      editForm: {
        adminId: '',
        adminName: '',
        adminPwd: "",
        adminPwd2: "",
        createTime: ''
      },
      editRules: {
        adminName: [
          {required: true, message: "请输入管理员姓名", trigger: "blur"}, //非空验证
          {min: 2, max: 20, message: '姓名长度为2到20个字符', trigger: 'blur'}
        ],
        adminPwd: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
          {min: 6, max: 16, message: '密码长度为6到16个字符', trigger: 'blur'}
        ],
        adminPwd2: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
          {validator: validateCheckPwd, trigger: 'blur'}
        ],
      }
    }
  },

  created() {
    this.getAdminInfo()
  },
  methods: {
    getAdminInfo() {
      axios.get(service.baseURL + "/admin/getAdminById?adminId=" + this.$store.state.userInfo.adminId,
          {
            headers: {
              Authorization: this.$store.state.token
            }
          })
          .then((resp) => {
            if (resp.data.code == '20000') {
              this.userInfo = resp.data.data;
            } else {
              this.$message.error(resp.data.msg)
            }
          })
    },
    openEditAdmin() {
      this.editForm.adminId = this.userInfo.adminId
      this.editForm.adminName = this.userInfo.adminName
      this.editFormVisible = true
    },
    clearEditForm() {
      this.editForm = {}
      this.$set(this.editForm, 'adminName', '')
      this.$refs.editForm.clearValidate()
    },
    cancelEdit() {
      this.editFormVisible = false
      this.clearEditForm()
      this.$message.info("已取消编辑")
    },
    editAdmin() {
      this.$refs.editForm.validate((valid) => {
        if (valid) {//如果表单的填写合法,发送axios请求
          axios.put(service.baseURL + "/admin/editAdmin", this.editForm,
              {
                headers: {
                  Authorization: this.$store.state.token
                }
              })
              .then((resp) => {
                if (resp.data.code == '20000') {
                  this.editFormVisible = false;//编辑成功关闭表单
                  this.clearEditForm()
                  this.getAdminInfo();//刷新表格
                  this.$message.success(resp.data.msg);
                } else {
                  this.$message.error(resp.data.msg)
                }
              })
        } else {//表单验证失败
          return false;
        }
      })
    }
  }
}
</script>

<style scoped>
.admin-card {
  width: 100%;
  margin: 20px auto;
}

.el-input {
  width: 400px;
}

.header {
  font-size: 30px;
  /*font-weight: bold;*/
}
</style>

```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209201729014.png" alt="image-20231209201729014" style="zoom:67%;" />

### 学生管理

```vue
<template>
  <div>
    <div class="findAdd">
      <el-input autocomplete="off" placeholder="请输入学号" v-model="stuId" clearable style="width: 240px"></el-input>
      <el-input autocomplete="off" placeholder="请输入学生姓名" v-model="stuName" clearable style="width: 240px"></el-input>
      <el-input autocomplete="off" placeholder="请输入班级名称" v-model="className" clearable
                style="width: 240px"></el-input>
      <el-button type="primary" @click="searchStuList" icon="el-icon-search">搜索</el-button>
      <el-button @click="resetStuList" icon="el-icon-refresh" style="margin-left:0">刷新</el-button>
      <el-button style="margin-left: 45px" type="primary" @click="openAddStu" icon="el-icon-plus">新增学生</el-button>
    </div>
    <div class="stuTable">
      <el-table :data="stuList" stripe size="small" style="width: 100%">
        <el-table-column label="序号" width="80" align="center">
          <template slot-scope="scope">
            {{
              (pageNum - 1) * pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>
        <el-table-column prop="stuId" label="学号" sortable align="center" width="130"></el-table-column>
        <el-table-column prop="stuName" label="学生姓名" align="center" sortable width="150"></el-table-column>
        <el-table-column prop="majorName" sortable align="center" width="200" label="专业名称"></el-table-column>
        <el-table-column prop="className" align="center" width="220" label="班级名称" sortable></el-table-column>
        <el-table-column prop="teaName" sortable align="center" width="150" label="班主任姓名"></el-table-column>
        <el-table-column prop="handle" align="center" label="操作">
          <template slot-scope="scope">
            <el-button @click="openStuScore(scope.row)" type="primary" size="small">打分</el-button>
            <el-button @click="openEditStu(scope.row)" type="warning" size="small">编辑</el-button>
            <el-button @click="delStu(scope.row)" type="danger" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="page">
      <el-pagination style="text-align: center"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          :pager-count="11"
          background
          :current-page="pageNum"
          :page-size="pageSize"
          :page-sizes="[5, 10, 20, 50,100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
    <!--    添加学生表单-->
    <div class="addStuDialog">
      <el-dialog
          @close="clearAddForm"
          title="新增学生"
          :visible.sync="addFormVisible">
        <el-form :model="addStuForm" :rules="addRules" ref="addStuFormRef">
          <el-form-item label="入学年份" label-width="150px" prop="admissionYear">
            <el-select v-model="addStuForm.admissionYear" placeholder="请选择入学年份" clearable @change="add_Year(addStuForm)">
              <el-option
                  v-for="item in admissionYears"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="专业" label-width="150px" prop="majorName">
            <el-select v-model="addStuForm.majorName" placeholder="请选择专业" clearable @change="add_Major(addStuForm)">
              <el-option
                  v-for="item in majorNames"
                  :key="item.majorId"
                  :label="item.majorName"
                  :value="item.majorName">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="班号" label-width="150px" prop="classId">
            <el-select v-model="addStuForm.classId" placeholder="请选择班号" clearable id="selectClassId">
              <el-option
                  v-for="item in classNos"
                  :key="item.classId"
                  :label="item.classId.substring(6,7)"
                  :value="item.classId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="学生姓名" label-width="150px" prop="stuName">
            <el-input v-model="addStuForm.stuName" autocomplete="off" placeholder="请输入学生姓名" clearable></el-input>
          </el-form-item>
          <el-form-item label="身份证" label-width="150px" prop="stuIdCard">
            <el-input v-model="addStuForm.stuIdCard" autocomplete="off" placeholder="请输入身份证"
                      clearable></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelAdd">取 消</el-button>
          <el-button type="primary" @click="addStu">确 定</el-button>
        </div>
      </el-dialog>
    </div>
    <!--    编辑学生表单-->
    <div class="editStuDialog">
      <el-dialog
          @close="clearEditForm"
          title="编辑学生"
          :visible.sync="editFormVisible">
        <el-form :model="editStuForm" :rules="editRules" ref="editStuFormRef">
          <el-form-item label="学号" label-width="150px" prop="stuId">
            <el-input v-model="editStuForm.stuId" disabled></el-input>
          </el-form-item>
          <el-form-item label="入学年份" label-width="150px" prop="admissionYear">
            <el-input v-model="editStuForm.admissionYear" disabled></el-input>
          </el-form-item>
          <el-form-item label="专业" label-width="150px" prop="majorName">
            <el-input v-model="editStuForm.majorName" disabled></el-input>
          </el-form-item>
          <el-form-item label="班号" label-width="150px" prop="classId">
            <el-input v-model="editStuForm.classNo" disabled></el-input>
          </el-form-item>
          <el-form-item label="学生姓名" label-width="150px" prop="stuName">
            <el-input v-model="editStuForm.stuName" autocomplete="off" placeholder="请输入学生姓名" clearable></el-input>
          </el-form-item>
          <el-form-item label="身份证" label-width="150px" prop="stuIdCard">
            <el-input v-model="editStuForm.stuIdCard" placeholder="请输入身份证" disabled></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelEdit">取 消</el-button>
          <el-button type="primary" @click="editStu">确 定</el-button>
        </div>
      </el-dialog>
    </div>
    <!--        学生打分表单-->
    <div class="scoreStuDialog">
      <el-dialog
          @close="clearScoreForm"
          title="学生打分"
          :visible.sync="scoreFormVisible">

        <el-form :model="scoreStuForm" :rules="scoreRules" ref="scoreStuFormRef">

          <el-form-item label="学号" label-width="150px" prop="stuId">
            <el-input v-model="scoreStuForm.stuId" disabled></el-input>
          </el-form-item>

          <el-form-item label="学生姓名" label-width="150px" prop="stuName">
            <el-input v-model="scoreStuForm.stuName" disabled></el-input>
          </el-form-item>

          <el-form-item label="课程" label-width="150px" prop="courseName">
            <el-select v-model="scoreStuForm.courseName" clearable placeholder="请选择课程" @change="getScore">
              <el-option
                  v-for="item in courseNames"
                  :key="item.courseId"
                  :label="item.courseName"
                  :value="item.courseName">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="分数" label-width="150px" prop="score">
            <el-input v-model="scoreStuForm.score" clearable placeholder="请给予分数" type="number"></el-input>
          </el-form-item>

        </el-form>

        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelScore">取 消</el-button>
          <el-button type="primary" @click="scoreStu">确 定</el-button>
        </div>

      </el-dialog>
    </div>

    <el-backtop :visibility-height=200>
      <div
          style="{
        height: 100%;
        width: 100%;
        background-color: #f2f5f6;
        box-shadow: 0 0 6px rgba(0,0,0, .30);
        text-align: center;
        line-height: 40px;
        color: #1989fa;
      }">
        ↑
      </div>
    </el-backtop>
  </div>
</template>

<script>
import axios from "axios";
import service from "@/request";

export default {
  name: "StuManage",
  data() {
    let nowTime = new Date()
    let nowYear = nowTime.getFullYear();
    let maxYear = nowYear - 1//默认是当前年-1
    let startTime = new Date(nowYear + '-9-01 00:00:00')//今年9月份开学时间
    if (nowTime > startTime) {
      maxYear += 1
    }
    ;
    let checkStuIdCard = (rule, value, callback) => {
      let reg = /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|[xX])$/;
      if (!reg.test(value)) {
        return callback(new Error("身份证格式错误"));
      }
      callback();
    };
    let checkScore = (rule, value, callback) => {
      if (value < 0) {
        return callback(new Error("分数范围在0到100之间"));
      } else if (value > 100) {
        return callback(new Error("分数范围在0到100之间"));
      }
      callback();
    };
    return {
      addStuForm: {
        admissionYear: '',
        majorName: '',
        classId: '',
        stuName: '',
        stuIdCard: '',
      },
      editStuForm: {
        stuId: '',
        admissionYear: '',
        majorName: '',//改
        classNo: '',//改
        stuName: '',//改
        stuIdCard: '',
      },
      scoreStuForm: {
        score: '',
        courseName: ''
      },
      courseNames: [],

      pageNum: 1,
      pageSize: 100,
      stuId: '',
      stuName: '',
      className: '',
      total: 0,
      addFormVisible: false,
      editFormVisible: false,
      scoreFormVisible: false,
      stuList: [],
      admissionYears: [{value: '1', label: maxYear}, {value: '2', label: maxYear - 1},
        {value: '3', label: maxYear - 2}, {value: '4', label: maxYear - 3}],
      majorNames: [],
      classNos: [],
      addRules: {
        admissionYear: {required: true, message: "请选择入学年份", trigger: "blur"}, //非空验证,
        majorName: {required: true, message: "请选择专业", trigger: "blur"}, //非空验证
        classId: {required: true, message: "请选择班号", trigger: "blur"}, //非空验证
        stuName: [
          {required: true, message: "请输入学生姓名", trigger: "blur"}, //非空验证
          {min: 2, max: 20, message: "姓名长度为2到20个字符", trigger: "blur",}, //规则验证
        ],
        stuIdCard: [
          {required: true, message: "请输入身份证", trigger: "blur"}, //非空验证
          {validator: checkStuIdCard, trigger: "blur"},
        ]
      },
      editRules: {
        // majorName: {required: true, message: "请选择专业", trigger: "blur"},
        // classId: {required: true, message: "请选择班号", trigger: "blur"},
        stuName: [
          {required: true, message: "请输入学生姓名", trigger: "blur"},
          {min: 2, max: 20, message: "姓名长度为2到20个字符", trigger: "blur"},
        ]
      },
      scoreRules: {
        courseName: {required: true, message: "请选择课程", trigger: "blur"},
        score: [{required: true, message: "请给予分数", trigger: "blur"},
          {validator: checkScore, trigger: "blur"}]
      },
    }
  },
  created() {
    this.getStuList();
  },
  methods: {
    scoreStu() {
      this.$refs.scoreStuFormRef.validate((valid) => {
        if (valid) {//如果表单的填写合法,发送axios请求
          axios.put(service.baseURL+"/score/giveNewScore", this.scoreStuForm,
              {
                headers: {
                  Authorization: this.$store.state.token
                }
              })
              .then((resp) => {
                if (resp.data.code == '20000') {
                  this.scoreFormVisible = false;//打分成功关闭表单
                  this.clearScoreForm()
                  this.getStuList();//刷新表格
                  this.$message.success(resp.data.msg);
                } else {
                  this.$message.error(resp.data.msg)
                }
              })
        } else {//表单验证失败
          return false;
        }
      })
    },
    getScore() {
      axios.get(service.baseURL+"/score/getScoreByCourseName?courseName=" + this.scoreStuForm.courseName +
          "&stuId=" + this.scoreStuForm.stuId, {
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp) => {
        if (resp.data.code == '20000') {
          if (resp.data.data != null) {
            this.$message.info(this.scoreStuForm.courseName + "成绩为" + resp.data.data)
          }
        }
      })
    },
    add_Year(stuForm) {//新增学生时改专业重新选班级
      // setTimeout(()=>{
      //   this.addStuForm.classId=''
      // }, 100)
      this.$set(this.addStuForm,'classId','')
      if ((stuForm.majorName!=""&&stuForm.majorName!=undefined)&&(stuForm.admissionYear!=""&&stuForm.admissionYear!=undefined))
      this.getClasses(stuForm)
    },
    add_Major(stuForm) {
      // setTimeout(()=>{
      //   this.addStuForm.classId=''
      // }, 100)
      this.$set(this.addStuForm,'classId','')
      if ((stuForm.majorName!=""&&stuForm.majorName!=undefined)&&(stuForm.admissionYear!=""&&stuForm.admissionYear!=undefined))
      this.getClasses(stuForm)
    },
    getClasses(stuForm) {
      axios.get(service.baseURL+"/class/getClassesByMajorName?majorName=" + stuForm.majorName + "&classYear=" + stuForm.admissionYear,
          {
            headers: {
              Authorization: this.$store.state.token
            }
          })
          .then((resp) => {
            if (resp.code = "20000") {
              this.classNos = resp.data.data
            }
          })
    },
    resetStuList() {
      location.reload();
    },
    searchStuList() {
      this.pageNum = 1
      this.getStuList();
    },
    cancelEdit() {
      this.editFormVisible = false
      this.clearAddForm()
      this.$message.info("已取消编辑")
    },
    cancelAdd() {
      this.addFormVisible = false
      this.classNos=[]
      this.clearAddForm()
      this.$set(this.addStuForm,'classId','')
      this.$message.info("已取消新增")
    },
    cancelScore() {
      this.scoreFormVisible = false
      this.clearScoreForm()
      this.$message.info("已取消打分")
    },
    delStu(row) {
      this.$confirm(`确认删除学生：${row.stuName} ？`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      }).then(() => {
        axios.delete(service.baseURL+"/student/delete?stuId=" + row.stuId,
            {
              headers: {
                Authorization: this.$store.state.token
              }
            })
            .then((resp) => {
              if (resp.data.code == '20000') {
                this.getStuList();
                this.$message.success(resp.data.msg)
              } else {
                this.$message.error(resp.data.msg)
              }
            })
      }).catch(() => {
        this.$message({
          type: "info",
          message: "已取消删除",
        })
      })
    },
    openStuScore(row) {
      axios.get(service.baseURL+"/student/getByStuId?stuId=" + row.stuId,
          {
            headers: {
              Authorization: this.$store.state.token
            }
          })
          .then((resp) => {
            if (resp.data.code == '20000') {
              this.scoreFormVisible = true;
              this.scoreStuForm = resp.data.data;
              axios.get(service.baseURL+"/chooseCourse/getCanScoreCourse?stuId=" + row.stuId,
                  {
                    headers: {
                      Authorization: this.$store.state.token
                    }
                  })
                  .then((resp) => {
                    this.courseNames = resp.data.data;
                  })
            } else {
              this.$message.error(resp.data.msg)
            }
          })
    },
    openEditStu(row) {
      axios.get(service.baseURL+"/student/getByStuId?stuId=" + row.stuId,
          {
            headers: {
              Authorization: this.$store.state.token
            }
          })
          .then((resp) => {
            if (resp.data.code == '20000') {
              this.editFormVisible = true;
              this.editStuForm = resp.data.data//不能换专业！
              //   axios.get("http://localhost:8091/major/getAllMajorName",
              //       {
              //         headers: {
              //           Authorization: this.$store.state.token
              //         }
              //       })
              //       .then((resp) => {
              //         this.majorNames = resp.data.data;
              //         this.getClasses(this.editStuForm)
              //       })
              // } else {
              //   this.$message.error(resp.data.msg)
            }
          })
    },
    editStu() {
      this.$refs.editStuFormRef.validate((valid) => {
        if (valid) {//如果表单的填写合法,发送axios请求
          axios.put(service.baseURL+"/student/editByStuId", this.editStuForm,
              {
                headers: {
                  Authorization: this.$store.state.token
                }
              })
              .then((resp) => {
                if (resp.data.code == '20000') {
                  this.editFormVisible = false;//编辑成功关闭表单
                  this.clearEditForm()
                  this.getStuList();//刷新表格
                  this.$message.success(resp.data.msg);
                } else {
                  this.$message.error(resp.data.msg)
                }
              })
        } else {//表单验证失败
          return false;
        }
      })
    },
    openAddStu() {
      this.addFormVisible = true
      axios.get(service.baseURL+"/major/getAllMajorName",
          {
            headers: {
              Authorization: this.$store.state.token
            }
          })
          .then((resp) => {
            this.majorNames = resp.data.data;
          })
    },
    getStuList() {
      axios.get(service.baseURL+"/student/stuPageList?pageNum=" + this.pageNum +
          "&pageSize=" + this.pageSize + "&stuId=" + this.stuId +
          "&stuName=" + this.stuName + "&className=" + this.className,
          {
            headers: {
              Authorization: this.$store.state.token
            }
          }
      ).then((resp) => {
        if (resp.data.code == '20000') {
          this.stuList = resp.data.data.stuList
          this.total = resp.data.data.total
        } else {
          this.$message.error(resp.data.msg)
        }
      })
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.getStuList()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.pageNum = 1
      this.getStuList()
    },
    clearAddForm() {//每次打开弹框应该清空数据
      this.addStuForm = {}
      // setTimeout(()=>{
      //   this.addStuForm.classId=''
      // }, 100)
      this.$set(this.addStuForm,'classId','')
      this.$refs.addStuFormRef.clearValidate()
    },
    clearEditForm() {
      this.editStuForm = {}
      this.$refs.editStuFormRef.clearValidate()
    },
    clearScoreForm() {
      this.scoreStuForm = {}
      this.$refs.scoreStuFormRef.clearValidate()
    },
    addStu() {//添加学生表单确定触发
      console.log(this.addStuForm.classId)
      this.$refs.addStuFormRef.validate((valid) => {
        if (valid) {//如果表单的填写合法,发送axios请求
          axios.post(service.baseURL+"/student/add", {
                'admissionYear': this.addStuForm.admissionYear,
                'majorName': this.addStuForm.majorName,
                'classId': this.addStuForm.classId,
                'stuName': this.addStuForm.stuName,
                'stuIdCard': this.addStuForm.stuIdCard,
              },
              {
                headers: {
                  Authorization: this.$store.state.token
                }
              }
          ).then((resp) => {
            if (resp.data.code == '20000') {
              this.addFormVisible = false;//添加成功关闭表单
              this.clearAddForm()
              this.getStuList();//刷新表格
              this.$message.success(resp.data.msg);
            } else {
              this.$message.error(resp.data.msg)
            }
          })
        } else {//表单验证失败
          return false;
        }
      })
    }
  }
}
</script>

<style scoped>
.findAdd {
  margin: 20px auto;
}
.el-input {
  width: 400px;
  margin-right: 20px;
}

.el-button {
  margin-right: 20px;
}

</style>
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209201757132.png" alt="image-20231209201757132" style="zoom:67%;" />

### 老师管理

```vue
<template>
  <div>
    <div class="findAdd">
      <el-input autocomplete="off" placeholder="请输入班主任编号" v-model="teaId" clearable
                style="width: 240px"></el-input>
      <el-input autocomplete="off" placeholder="请输入班主任姓名" v-model="teaName" clearable
                style="width: 240px"></el-input>
      <el-input autocomplete="off" placeholder="请输入班级名称" v-model="className" clearable
                style="width: 240px"></el-input>
      <el-button type="primary" @click="searchTeaList" icon="el-icon-search">搜索</el-button>
      <el-button @click="resetTeaList" icon="el-icon-refresh" style="margin-left:0">刷新</el-button>
      <el-button style="margin-left: 40px" type="primary" @click="openAddTea" icon="el-icon-plus">新增班主任</el-button>
    </div>
    <div class="teaTable">
      <el-table :data="teaList" stripe size="small" style="width: 100%">
        <el-table-column label="序号" width="80" align="center">
          <template slot-scope="scope">
            {{
              (pageNum - 1) * pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>
        <el-table-column prop="teaId" label="班主任编号" sortable align="center" width="150"></el-table-column>
        <el-table-column prop="teaName" label="班主任姓名" align="center" sortable width="150"></el-table-column>
        <el-table-column prop="majorName" sortable align="center" width="200" label="专业名称"></el-table-column>
        <el-table-column prop="className" align="center" width="200" label="班级名称" sortable></el-table-column>
        <el-table-column prop="classYear" align="center" width="150" label="年级" sortable></el-table-column>
        <el-table-column prop="handle" align="center" label="操作">
          <template slot-scope="scope">
            <el-button @click="openEditTea(scope.row)" type="warning" size="small">编辑</el-button>
            <el-button @click="delTea(scope.row)" type="danger" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="page">
      <el-pagination style="text-align: center" @current-change="handleCurrentChange"
                     @size-change="handleSizeChange"
                     :pager-count="11"
                     background
                     :current-page="pageNum"
                     :page-size="pageSize"
                     :page-sizes="[5, 10, 20, 50,100]"
                     layout="total, sizes, prev, pager, next, jumper"
                     :total="total">
      </el-pagination>
    </div>
    <!--添加班主任表单-->
    <div class="addTeaDialog">
      <el-dialog
          @close="clearAddForm"
          title="新增班主任"
          :visible.sync="addFormVisible">
        <el-form :model="addTeaForm" :rules="addRules" ref="addTeaFormRef">
          <el-form-item label="年级" label-width="150px" prop="classYear">
            <el-select v-model="addTeaForm.classYear" placeholder="请选择年级" clearable
                       @change="getClasses(addTeaForm)">
              <el-option
                  v-for="item in classYears"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="专业" label-width="150px" prop="majorName">
            <el-select v-model="addTeaForm.majorName" placeholder="请选择专业" clearable
                       @change="getClasses(addTeaForm)">
              <el-option
                  v-for="item in majorNames"
                  :key="item.majorId"
                  :label="item.majorName"
                  :value="item.majorName">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="班号" label-width="150px" prop="classId">
            <el-select v-model="addTeaForm.classId" placeholder="请选择班号" clearable id="selectClassId">
              <el-option
                  v-for="item in classNos"
                  :key="item.classId"
                  :label="item.classId.substring(6,7)"
                  :value="item.classId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="班主任姓名" label-width="150px" prop="teaName">
            <el-input v-model="addTeaForm.teaName" autocomplete="off" placeholder="请输入班主任姓名"
                      clearable></el-input>
          </el-form-item>
          <el-form-item label="身份证" label-width="150px" prop="teaIdCard">
            <el-input v-model="addTeaForm.teaIdCard" autocomplete="off" placeholder="请输入身份证" clearable></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelAdd">取 消</el-button>
          <el-button type="primary" @click="addTea">确 定</el-button>
        </div>
      </el-dialog>

    </div>
    <!--编辑班主任表单-->
    <div class="editStuDialog">
      <el-dialog
          @close="clearEditForm"
          title="编辑班主任"
          :visible.sync="editFormVisible">
        <el-form :model="editTeaForm" :rules="editRules" ref="editTeaFormRef">
          <el-form-item label="年级" label-width="150px" prop="classYear" disabled>
            <el-input v-model="editTeaForm.classYear" autocomplete="off" placeholder="请输入专业" disabled></el-input>
          </el-form-item>
          <el-form-item label="专业" label-width="150px" prop="majorName">
            <el-input v-model="editTeaForm.majorName" autocomplete="off" placeholder="请输入专业" disabled></el-input>
          </el-form-item>
          <el-form-item label="班号" label-width="150px" prop="classNo">
            <el-input v-model="editTeaForm.classNo" autocomplete="off" placeholder="请输入班号" disabled></el-input>
          </el-form-item>
          <el-form-item label="班主任姓名" label-width="150px" prop="teaName">
            <el-input v-model="editTeaForm.teaName" autocomplete="off" placeholder="请输入班主任姓名"
                      clearable></el-input>
          </el-form-item>
          <el-form-item label="身份证" label-width="150px" prop="teaIdCard">
            <el-input v-model="editTeaForm.teaIdCard" autocomplete="off" placeholder="请输入身份证" disabled></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelEdit">取 消</el-button>
          <el-button type="primary" @click="editTea">确 定</el-button>
        </div>
      </el-dialog>
    </div>
    <el-backtop :visibility-height=200>
      <div
          style="{
        height: 100%;
        width: 100%;
        background-color: #f2f5f6;
        box-shadow: 0 0 6px rgba(0,0,0, .30);
        text-align: center;
        line-height: 40px;
        color: #1989fa;
      }">
        ↑
      </div>
    </el-backtop>
  </div>
</template>

<script>
import axios from "axios";
import service from "@/request";

export default {
  name: "AddTea",
  data() {
    let nowTime = new Date()
    let nowYear = nowTime.getFullYear();
    let maxYear = nowYear - 1//默认是当前年-1
    let startTime = new Date(nowYear + '-9-01 00:00:00')//今年9月份开学时间
    if (nowTime > startTime) {
      maxYear += 1
    }
    let checkTeaIdCard = (rule, value, callback) => {
      let reg = /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|[xX])$/;//未开发
      if (!reg.test(value)) {
        return callback(new Error("身份证格式错误"));
      }
      callback();
    };
    return {
      teaId: '',
      teaName: '',
      className: '',
      majorName: '',
      classNo: '',
      pageNum: 1,
      pageSize: 20,
      total: 0,
      teaList: [{
        teaId: '',
        teaName: '',
        majorName: '',
        classNo: ''
      }],

      addFormVisible: false,
      editFormVisible: false,
      addTeaForm: {
        teaIdCard: '',
        majorName: '',
        teaName: '',
        classId: ''
      },
      editTeaForm: {
        teaIdCard: '',
        majorName: '',
        teaName: ''
      },
      majorNames: [],
      classNos: [],
      addRules: {
        classYear: {required: true, message: "请选择年级", trigger: "blur"},
        majorName: {required: true, message: "请选择专业", trigger: "blur"}, //非空验证
        classId: {required: true, message: "请选择班号", trigger: "blur"}, //非空验证
        teaName: [
          {required: true, message: "请输入班主任姓名", trigger: "blur"}, //非空验证
          {min: 2, max: 20, message: "姓名长度为2到20个字符", trigger: "blur",}, //规则验证
        ],
        teaIdCard: [
          {required: true, message: "请输入身份证", trigger: "blur"}, //非空验证
          {validator: checkTeaIdCard, trigger: "blur"},
        ]
      },
      editRules: {
        teaName: [
          {required: true, message: "请输入班主任姓名", trigger: "blur"}, //非空验证
          {min: 2, max: 20, message: "姓名长度为2到20个字符", trigger: "blur",}, //规则验证
        ],
        // teaIdCard: [
        //   {required: true, message: "请输入身份证", trigger: "blur"}, //非空验证
        //   {validator: checkTeaIdCard, trigger: "blur"},
        // ]
      },
      classYears: [{value: '1', label: maxYear}, {value: '2', label: maxYear - 1},
        {value: '3', label: maxYear - 2}, {value: '4', label: maxYear - 3}],
    }
  },
  methods: {
    getClasses(teaForm) {
      this.$set(this.addTeaForm, 'classId', '')
      if ((teaForm.majorName != '' && teaForm.majorName != undefined) && (teaForm.classYear != '' && teaForm.classYear != undefined))
        axios.get(service.baseURL + "/class/getClassesByMajorName?majorName=" + teaForm.majorName + "&classYear=" + teaForm.classYear,
            {
              headers: {
                Authorization: this.$store.state.token
              }
            })
            .then((resp) => {
              if (resp.code = "20000") {
                this.classNos = resp.data.data
              }
            })
    },
    getTeaList() {//分页查询班主任表格
      axios.get(service.baseURL + "/teacher/teaPageList?pageNum=" + this.pageNum +
          "&pageSize=" + this.pageSize + "&teaId=" + this.teaId + "&teaName=" + this.teaName +
          "&className=" + this.className,
          {
            headers: {
              Authorization: this.$store.state.token
            }
          })
          .then((resp) => {
            if (resp.data.code == '20000') {
              this.teaList = resp.data.data.teaList
              this.total = resp.data.data.total
            } else {
              this.$message.error(resp.data.msg)
            }
          })
    },
    resetTeaList() {//刷新
      location.reload()
    },
    searchTeaList() {//待条件的分页查询
      this.pageNum = 1
      this.getTeaList();
    },
    openAddTea() {//显示班主任添加表单
      this.addFormVisible = true
      axios.get(service.baseURL + "/major/getAllMajorName",
          {
            headers: {
              Authorization: this.$store.state.token
            }
          })
          .then((resp) => {
            this.majorNames = resp.data.data;
          })
    },
    openEditTea(row) {//显示班主任编辑表单
      axios.get(service.baseURL + "/teacher/getTeaById?teaId=" + row.teaId,
          {
            headers: {
              Authorization: this.$store.state.token
            }
          })
          .then((resp) => {
            if (resp.data.code == '20000') {
              this.editFormVisible = true;
              this.editTeaForm = resp.data.data;
              // axios.get("http://localhost:8091/major/getAllMajorName",//不能换专业
              //     {
              //       headers: {
              //         Authorization: this.$store.state.token
              //       }
              //     })
              //     .then((resp) => {
              //       this.majorNames = resp.data.data;
              //     })
            } else {
              this.$message.error(resp.data.msg)
            }
          })
    },
    handleCurrentChange(pageNum) {//分页
      this.pageNum = pageNum
      this.getTeaList()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.pageNum = 1
      this.getTeaList()
    },
    clearAddForm() {//关闭表单时清空数据
      this.addTeaForm = {}
      this.$refs.addTeaFormRef.clearValidate()
    },
    clearEditForm() {
      this.editTeaForm = {}
      this.$refs.editTeaFormRef.clearValidate()
    },
    cancelAdd() {//取消增加
      this.addFormVisible = false
      this.classNos = []
      this.clearAddForm()
      this.$set(this.addTeaForm, 'classId', '')
      this.$message.info("已取消新增")
    },
    cancelEdit() {//取消编辑
      this.editFormVisible = false
      this.clearEditForm()
      this.$message.info("已取消编辑")
    },
    addTea() {//确定添加班主任
      this.$refs.addTeaFormRef.validate((valid) => {
        if (valid) {//如果表单的填写合法,发送axios请求
          axios.post(service.baseURL + "/teacher/add", {
                'classYear': this.addTeaForm.classYear,
                'majorName': this.addTeaForm.majorName,
                'classId': this.addTeaForm.classId,
                'teaName': this.addTeaForm.teaName,
                'teaIdCard': this.addTeaForm.teaIdCard,
              },
              {
                headers: {
                  Authorization: this.$store.state.token
                }
              }
          ).then((resp) => {
            if (resp.data.code == '20000') {
              this.addFormVisible = false
              this.clearAddForm()
              this.getTeaList()
              this.$message.success(resp.data.msg);
            } else {
              this.$message.error(resp.data.msg)
            }
          })
        } else {//表单验证失败
          return false;
        }
      })
    },
    delTea(row) {
      this.$confirm(`确认删除班主任：${row.teaName} ？`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      }).then(() => {
        axios.delete(service.baseURL + "/teacher/delTea?teaId=" + row.teaId,
            {
              headers: {
                Authorization: this.$store.state.token
              }
            })
            .then((resp) => {
              if (resp.data.code == '20000') {
                this.getTeaList();
                this.$message.success(resp.data.msg)
              } else {
                this.$message.error(resp.data.msg)
              }
            })
      }).catch(() => {
        this.$message({
          type: "info",
          message: "已取消删除",
        })
      })
    },
    editTea() {
      this.$refs.editTeaFormRef.validate((valid) => {
        if (valid) {//如果表单的填写合法,发送axios请求
          axios.put(service.baseURL + "/teacher/editById", this.editTeaForm,
              {
                headers: {
                  Authorization: this.$store.state.token
                }
              }).then((resp) => {
            if (resp.data.code == '20000') {
              this.editFormVisible = false;//添加成功关闭表单
              this.clearEditForm()
              this.getTeaList();//刷新表格
              this.$message.success(resp.data.msg);
            } else {
              this.$message.error(resp.data.msg)
            }
          })
        } else {//表单验证失败
          return false;
        }
      })
    }
  },
  created() {
    this.searchTeaList()
  }
}
</script>

<style scoped>
.findAdd {
  margin: 20px auto;
}

.el-input {
  width: 400px;
  margin-right: 20px;
}

.el-button {
  margin-right: 20px;
}
</style>
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/1702124549343.jpg" alt="1702124549343" style="zoom:67%;" />

### 班级管理

```vue
<template>
  <div>
    <div class="findAdd">
      <el-input autocomplete="off" placeholder="请输入班级名称" v-model="className" clearable
                style="width: 300px"></el-input>
      <el-input autocomplete="off" placeholder="请输入班主任姓名" v-model="teaName" clearable
                style="width: 300px"></el-input>
      <el-button type="primary" @click="searchClassList" icon="el-icon-search">搜索</el-button>
      <el-button @click="resetClassList" icon="el-icon-refresh" style="margin-left: 0">刷新</el-button>
      <el-button style="margin-left: 240px" type="primary" @click="openClassForm()" icon="el-icon-plus">新增班级
      </el-button>
    </div>
    <div class="classTable">
      <el-table :data="classList" stripe size="small" style="width: 100%">
        <el-table-column label="序号" width="80" align="center">
          <template slot-scope="scope">
            {{
              (pageNum - 1) * pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>
        <el-table-column prop="classId" label="班级编号" align="center" width="150" sortable></el-table-column>
        <el-table-column prop="className" label="班级名称" align="center" width="200" sortable></el-table-column>
        <el-table-column prop="majorName" label="所属专业" align="center" width="200" sortable></el-table-column>
        <el-table-column prop="classYear" label="年级" align="center" width="100" sortable></el-table-column>
        <el-table-column prop="stuTotal" label="学生数量" align="center" width="150" sortable></el-table-column>
        <el-table-column prop="teaName" label="班主任姓名" align="center" width="150" sortable></el-table-column>
        <el-table-column prop="handle" label="操作" align="center">
          <template slot-scope="scope">
            <el-button @click="delClass(scope.row)" type="danger" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="page">
      <el-pagination style="text-align: center"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          :pager-count="11"
          background
          :current-page="pageNum"
          :page-size="pageSize"
          :page-sizes="[5, 10, 20, 50,100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
    <!-- 添加班级表单-->
    <div class="classDialog">
      <el-dialog
          @close="clearForm"
          title="新增班级"
          :visible.sync="formVisible">
        <el-form :model="classForm" :rules="rules" ref="classFormRef">
          <el-form-item label="年级" label-width="150px" prop="classYear">
            <el-select v-model="classForm.classYear" placeholder="请选择年级" clearable>
              <el-option
                  v-for="item in classYears"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="专业" label-width="150px" prop="majorName">
            <el-select v-model="classForm.majorName" placeholder="请选择专业" clearable>
              <el-option
                  v-for="item in majorNames"
                  :key="item.majorId"
                  :label="item.majorName"
                  :value="item.majorName">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelForm()">取 消</el-button>
          <el-button type="primary" @click="saveClass()">确 定</el-button>
        </div>
      </el-dialog>
    </div>
    <el-backtop :visibility-height=200>
      <div
          style="{
        height: 100%;
        width: 100%;
        background-color: #f2f5f6;
        box-shadow: 0 0 6px rgba(0,0,0, .30);
        text-align: center;
        line-height: 40px;
        color: #1989fa;
      }">
        ↑
      </div>
    </el-backtop>
  </div>
</template>

<script>
import axios from "axios";
import service from "@/request";
export default {
  name: "ClassManage",
  data() {
    let nowTime = new Date()
    let nowYear = nowTime.getFullYear();
    let maxYear = nowYear - 1//默认是当前年-1
    let startTime = new Date(nowYear + '-9-01 00:00:00')//今年9月份开学时间
    if (nowTime > startTime) {
      maxYear += 1
    }
    return {
      className: '',
      teaName: '',
      classList: [{
        classId: '',
        className: '',
        majorName: '',
        stuTotal: 0,
        classYear: ''
      }],
      pageNum: 1,
      pageSize: 20,
      total: 0,
      formVisible: false,
      classForm: {
        majorName: '',
        classYear: ''
      },
      rules: {
        classYear: {required: true, message: "请选择年级", trigger: "blur"},
        majorName: {required: true, message: "请选择专业", trigger: "blur"}
      },
      majorNames: [],
      classYears: [{value: '1', label: maxYear}, {value: '2', label: maxYear - 1},
        {value: '3', label: maxYear - 2}, {value: '4', label: maxYear - 3}],
    }
  },
  methods: {
    getClassList() {
      axios.get(service.baseURL+"/class/classPageList?pageNum=" + this.pageNum +
          "&pageSize=" + this.pageSize + "&className=" + this.className + "&teaName=" + this.teaName, {
        headers: {
          Authorization: this.$store.state.token
        }
      })
          .then((resp) => {
            if (resp.data.code == '20000') {
              this.classList = resp.data.data.classList
              this.total = resp.data.data.total
            } else {
              this.$message.error(resp.data.msg)
            }
          })
    },
    searchClassList() {
      this.pageNum = 1
      this.getClassList();
    },
    resetClassList() {
      location.reload()
    },
    openClassForm() {
      this.formVisible = true
      axios.get(service.baseURL+"/major/getAllMajorName", {
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp) => {
        this.majorNames = resp.data.data;
      })
    },
    delClass(row) {
      //有班主任，无法删除
      if (row.stuTotal > 0) {
        this.$message.info("该班级还有学生，无法删除")
      }
      if (row.teaName != '' && row.teaName != null) {
        this.$message.info("该班级还有班主任，无法删除")
      } else
        this.$confirm(`确认删除班级：${row.className} ？`, "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          center: true,
        }).then(() => {
          axios.delete(service.baseURL+"/class/delClassById?classId=" + row.classId, {
            headers: {
              Authorization: this.$store.state.token
            }
          }).then((resp) => {
            if (resp.data.code == '20000') {
              this.$message.success(resp.data.msg)
              this.getClassList();
            } else {
              this.$message.error(resp.data.msg)
            }
          })
        }).catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          })
        });
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.getClassList()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.pageNum = 1
      this.getClassList()
    },
    clearForm() {
      this.classForm = {}
      this.$refs.classFormRef.clearValidate()
    },
    cancelForm() {
      this.formVisible = false
      this.clearForm()
      this.$message.info("已取消新增")
    },
    saveClass() {
      this.$refs.classFormRef.validate((valid) => {
        if (valid) {//如果表单的填写合法,发送axios请求
          axios.post(service.baseURL+"/class/add", {
                'majorName': this.classForm.majorName,
                'classYear': this.classForm.classYear
              }, {
                headers: {
                  Authorization: this.$store.state.token
                }
              }
          ).then((resp) => {
            if (resp.data.code == '20000') {
              this.formVisible = false
              this.clearForm()
              this.getClassList()
              this.$message.success(resp.data.msg);
            } else {
              this.$message.error(resp.data.msg)
            }
          })
        } else {//表单验证失败
          return false;
        }
      })
    }
  },
  created() {
    this.getClassList();
  }
}
</script>

<style scoped>
.findAdd {
  margin: 20px auto;
}

.el-input,.el-button{
  margin-right: 20px;
}
</style>
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209201929579.png" alt="image-20231209201929579" style="zoom:67%;" />

### 课程管理

```vue
<template>
  <div>
    <div class="findAdd">
      <el-input autocomplete="off" placeholder="请输入课程名" v-model="courseName" clearable
                style="width: 240px"></el-input>
      <el-input autocomplete="off" placeholder="请输入专业名" v-model="majorName" clearable
                style="width: 240px"></el-input>
      <el-select v-model="coursePeriod" placeholder="请选择开课时期" clearable>
        <el-option
            v-for="item in coursePeriods"
            :key="item.value"
            :label="item.label"
            :value="item.label">
        </el-option>
      </el-select>
      <el-button type="primary" @click="searchCourseList" icon="el-icon-search">搜索</el-button>
      <el-button @click="resetCourseList" icon="el-icon-refresh" style="margin-left: 0">刷新</el-button>
      <el-button type="primary" @click="openAddCourseForm()" icon="el-icon-plus" style="margin-left: 70px">新增课程
      </el-button>
    </div>
    <div class="classTable">
      <el-table :data="courseList" stripe size="small" style="width: 100%" :row-style="{ height:'40px'}">
        <el-table-column label="序号" width="80" align="center">
          <template slot-scope="scope">
            {{
              (pageNum - 1) * pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>
        <el-table-column prop="courseId" label="课程编号" align="center" width="130" sortable></el-table-column>
        <el-table-column prop="courseName" label="课程名" align="center" width="150" sortable></el-table-column>
        <el-table-column prop="majorName" label="所属专业" align="center" width="200" sortable></el-table-column>
        <el-table-column prop="ifDegree" label="课程类型" width="140" align="center" sortable>
          <template slot-scope="scope">
            <el-tag v-if="scope.row.ifDegree == 1">必修</el-tag>
            <el-tag v-else-if="scope.row.ifDegree == 0" type="success">选修</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="coursePeriod" label="开课时期" align="center" width="150" sortable></el-table-column>
        <el-table-column prop="stuChooseNum" label="选课人数" align="center" width="100" sortable></el-table-column>
        <el-table-column prop="handle" label="操作" align="center">
          <template slot-scope="scope">
            <el-button @click="openEditCourseForm(scope.row)" type="warning" size="small">编辑</el-button>
            <el-button @click="delCourse(scope.row)" type="danger" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="page">
      <el-pagination style="text-align: center"
                     @current-change="handleCurrentChange"
                     @size-change="handleSizeChange"
                     :pager-count="11"
                     background
                     :current-page="pageNum"
                     :page-size="pageSize"
                     :page-sizes="[5, 10, 20, 50,100]"
                     layout="total, sizes, prev, pager, next, jumper"
                     :total="total">
      </el-pagination>
    </div>
    <!-- 添加课程表单-->
    <div class="addCourseDialog">
      <el-dialog
          @close="clearAddForm"
          title="新增课程"
          :visible.sync="addFormVisible">
        <el-form :model="addCourseForm" :rules="addRules" ref="addFormRef">
          <el-form-item label="专业" label-width="150px" prop="majorName">
            <el-select v-model="addCourseForm.majorName" placeholder="请选择专业" clearable>
              <el-option
                  v-for="item in majorNames"
                  :key="item.majorId"
                  :label="item.majorName"
                  :value="item.majorName">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="开课时期" label-width="150px" prop="coursePeriod">
            <el-select v-model="addCourseForm.coursePeriod" placeholder="请选择开课时期" clearable>
              <el-option
                  v-for="item in coursePeriods"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="课程名" label-width="150px" prop="courseName">
            <el-input v-model="addCourseForm.courseName" autocomplete="off" placeholder="请输入课程名"
                      clearable></el-input>
          </el-form-item>
          <el-form-item label="是否必修" label-width="150px" prop="ifDegree">
            <el-switch v-model="addCourseForm.ifDegree" :active-value="1" :inactive-value="0"></el-switch>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelAddForm()">取 消</el-button>
          <el-button type="primary" @click="addCourse()">确 定</el-button>
        </div>
      </el-dialog>
    </div>
    <!-- 编辑课程表单-->
    <div class="editCourseDialog">
      <el-dialog
          @close="clearEditForm"
          title="编辑课程"
          :visible.sync="editFormVisible">
        <el-form :model="editCourseForm" :rules="editRules" ref="editFormRef">
          <el-form-item label="课程编号" label-width="150px" prop="courseId">
            <el-input v-model="editCourseForm.courseId" autocomplete="off" disabled></el-input>
          </el-form-item>
          <el-form-item label="专业" label-width="150px" prop="majorName">
            <el-input v-model="editCourseForm.majorName" autocomplete="off" placeholder="请选择专业"
                      disabled></el-input>
          </el-form-item>
          <el-form-item label="开课时期" label-width="150px" prop="coursePeriod">
            <el-select v-model="editCourseForm.coursePeriod" placeholder="请选择开课时期" clearable>
              <el-option
                  v-for="item in coursePeriods"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="课程名" label-width="150px" prop="courseName">
            <el-input v-model="editCourseForm.courseName" autocomplete="off" placeholder="请输入课程名"
                      clearable></el-input>
          </el-form-item>
          <el-form-item label="是否必修" label-width="150px" prop="ifDegree">
            <el-switch v-model="editCourseForm.ifDegree" :active-value="'1'" :inactive-value="'0'"></el-switch>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelEditForm()">取 消</el-button>
          <el-button type="primary" @click="editCourse()">确 定</el-button>
        </div>
      </el-dialog>
    </div>
    <el-backtop :visibility-height=200>
      <div
          style="{
        height: 100%;
        width: 100%;
        background-color: #f2f5f6;
        box-shadow: 0 0 6px rgba(0,0,0, .30);
        text-align: center;
        line-height: 40px;
        color: #1989fa;
      }">
        ↑
      </div>
    </el-backtop>
  </div>
</template>

<script>
import axios from "axios";
import service from "@/request";

export default {
  name: "CourseManage",
  data() {
    return {
      coursePeriod: '',
      courseName: '',
      majorName: '',
      pageNum: 1,
      pageSize: 50,
      total: 0,
      addFormVisible: false,
      editFormVisible: false,
      courseList: [{
        courseId: '',
        courseName: '',
        majorName: '',
        coursePeriod: '',
        stuChooseNum: 0,
        ifDegree: '',
      }],
      addCourseForm: {},
      editCourseForm: {},
      addRules: {
        majorName: {required: true, message: "请选择专业", trigger: "blur"},//非空验证,
        coursePeriod: {required: true, message: "请选择开课时期", trigger: "blur"},
        courseName: [{required: true, message: "请输入课程名", trigger: "blur"},
          {min: 2, max: 20, message: "课程长度为2到20个字符", trigger: "blur"}],
      },
      editRules: {
        coursePeriod: {required: true, message: "请选择开课时期", trigger: "blur"},
        courseName: [{required: true, message: "请输入课程名", trigger: "blur"},
          {min: 2, max: 20, message: "课程名长度为2到20个字符", trigger: "blur"}],
      },
      coursePeriods: [
        {value: '1', label: '大一上'},
        {value: '2', label: '大一下'},
        {value: '3', label: '大二上'},
        {value: '4', label: '大二下'},
        {value: '5', label: '大三上'},
        {value: '6', label: '大三下'},
        {value: '7', label: '大四上'},
        {value: '8', label: '大四下'},
      ],
      majorNames: []
    }
  },
  methods: {
    getCourseList() {
      axios.get(service.baseURL + "/course/coursePageList?pageNum=" + this.pageNum +
          "&pageSize=" + this.pageSize + "&courseName=" + this.courseName + "&majorName=" +
          this.majorName + "&coursePeriod=" + this.coursePeriod, {
            headers: {
              Authorization: this.$store.state.token
            }
          }
      ).then((resp) => {
        if (resp.data.code == '20000') {
          this.courseList = resp.data.data.courseList
          this.total = resp.data.data.total
        } else {
          this.$message.error(resp.data.msg)
        }
      })
    },
    searchCourseList() {
      this.pageNum = 1
      this.getCourseList();
    },
    resetCourseList() {
      location.reload()
    },
    openAddCourseForm() {
      this.addFormVisible = true
      axios.get(service.baseURL + "/major/getAllMajorName", {
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp) => {
        this.majorNames = resp.data.data;
      })
    },
    openEditCourseForm(row) {
      if (row.stuChooseNum > 0) {
        this.$message.info("还有学生有该课,无法编辑")
      } else
        axios.get(service.baseURL + "/course/getCourseById?courseId=" + row.courseId, {
          headers: {
            Authorization: this.$store.state.token
          }
        }).then((resp) => {
          if (resp.data.code = "20000") {
            this.editFormVisible = true
            // axios.get("http://localhost:8091/major/getAllMajorName", {//课程不能换专业
            //   headers: {
            //     Authorization: this.$store.state.token
            //   }
            // })//获取所有专业
            //     .then((resp) => {
            //       this.majorNames = resp.data.data;
            //     })
            this.editCourseForm = resp.data.data//渲染编辑表单
          } else {
            this.$message.error(resp.data.msg)
          }
        })
    },
    delCourse(row) {
      if (row.stuChooseNum > 0) {
        this.$message.info("还有学生有该课，无法删除")
      } else
        this.$confirm(`确认删除课程：${row.courseName} ？`, "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          center: true,
        }).then(() => {
          axios.delete(service.baseURL + "/course/delCourseById?courseId=" + row.courseId, {
            headers: {
              Authorization: this.$store.state.token
            }
          })
              .then((resp) => {
                if (resp.data.code == '20000') {
                  this.getCourseList()
                  this.$message.success(resp.data.msg)
                } else {
                  this.$message.error(resp.data.msg)
                }
              })
        }).catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          })
        });
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.getCourseList()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.pageNum = 1
      this.getCourseList()
    },
    clearAddForm() {
      this.addCourseForm = {}
      this.$refs.addFormRef.clearValidate()
    },
    clearEditForm() {
      this.addCourseForm = {}
      this.$refs.editFormRef.clearValidate()
    },

    cancelAddForm() {
      this.addFormVisible = false
      this.$message.info("已取消新增")
    },
    cancelEditForm() {
      this.editFormVisible = false
      this.$message.info("已取消编辑")
    },
    addCourse() {
      this.$refs.addFormRef.validate((valid) => {
        if (valid) {//如果表单的填写合法,发送axios请求
          axios.post(service.baseURL + "/course/add", {
                'coursePeriod': this.addCourseForm.coursePeriod,
                'majorName': this.addCourseForm.majorName,
                'courseName': this.addCourseForm.courseName,
                'ifDegree': this.addCourseForm.ifDegree
              }, {
                headers: {
                  Authorization: this.$store.state.token
                }
              }
          ).then((resp) => {
            if (resp.data.code == '20000') {
              this.addFormVisible = false;//添加成功关闭表单
              this.clearAddForm()
              this.getCourseList();//刷新表格
              this.$message.success(resp.data.msg);
            } else {
              this.$message.error(resp.data.msg)
            }
          })
        } else {//表单验证失败
          return false;
        }
      })
    },
    editCourse() {
      this.$refs.editFormRef.validate((valid) => {
        if (valid) {//如果表单的填写合法,发送axios请求
          axios.put(service.baseURL + "/course/edit", {
                'courseId': this.editCourseForm.courseId,
                'coursePeriod': this.editCourseForm.coursePeriod,
                'majorName': this.editCourseForm.majorName,
                'courseName': this.editCourseForm.courseName,
                'ifDegree': this.editCourseForm.ifDegree
              }, {
                headers: {
                  Authorization: this.$store.state.token
                }
              }
          ).then((resp) => {
            if (resp.data.code == '20000') {
              this.editFormVisible = false;//编辑成功关闭表单
              this.clearEditForm()
              this.getCourseList();//刷新表格
              this.$message.success(resp.data.msg);
            } else {
              this.$message.error(resp.data.msg)
            }
          })
        } else {//表单验证失败
          return false;
        }
      })
    }
  },
  created() {
    this.getCourseList()
  }
}
</script>

<style scoped>
.findAdd {
  margin: 20px auto;
}

.el-input, .el-button, .el-select {
  margin-right: 20px;
}
</style>
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/1702124467983.jpg" alt="1702124467983" style="zoom:67%;" />

### 专业管理

```vue
<template>
  <div>
    <div class="findAdd">
      <el-input autocomplete="off" placeholder="请输入专业名称" v-model="majorName" clearable
                style="width: 300px"></el-input>
      <el-button type="primary" @click="searchMajorList" icon="el-icon-search">搜索</el-button>
      <el-button @click="resetMajorList" icon="el-icon-refresh" style="margin-left: 0">刷新</el-button>
      <el-button style="margin-left: 560px" type="primary" @click="openMajorForm(null)" icon="el-icon-plus">新增专业
      </el-button>
    </div>
    <div class="majorTable">
      <el-table :data="majorList" stripe size="small" style="width: 100%">
        <el-table-column label="序号" width="80" align="center">
          <template slot-scope="scope">
            {{
              (pageNum - 1) * pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>
        <el-table-column prop="majorId" label="专业编号" align="center" width="150" sortable></el-table-column>
        <el-table-column prop="majorName" label="专业名称" align="center" width="200" sortable></el-table-column>
        <el-table-column prop="classTotal" label="班级数量" align="center" width="150" sortable></el-table-column>
        <el-table-column prop="courseTotal" label="课程数量" align="center" width="150" sortable></el-table-column>
        <el-table-column prop="stuTotal" label="学生数量" align="center" width="150" sortable></el-table-column>
        <el-table-column prop="teaTotal" label="班主任数量" align="center" width="150" sortable></el-table-column>
        <el-table-column prop="handle" label="操作" align="center">
          <template slot-scope="scope">
            <el-button @click="openMajorForm(scope.row)" type="warning" size="small">编辑</el-button>
            <el-button @click="delMajor(scope.row)" type="danger" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="page">
      <el-pagination style="text-align: center"
                     @current-change="handleCurrentChange"
                     @size-change="handleSizeChange"
                     :pager-count="11"
                     background
                     :current-page="pageNum"
                     :page-size="pageSize"
                     :page-sizes="[5, 10, 20, 50,100]"
                     layout="total, sizes, prev, pager, next, jumper"
                     :total="total">
      </el-pagination>
    </div>
    <!-- 添加或编辑专业表单-->
    <div class="MajorDialog">
      <el-dialog
          @close="clearForm"
          :title="title"
          :visible.sync="formVisible">
        <el-form :model="majorForm" :rules="rules" ref="majorFormRef">
          <el-form-item label="专业名" label-width="150px" prop="majorName">
            <el-input v-model="majorForm.majorName" autocomplete="off" placeholder="请输入专业名" clearable></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelForm(title)">取 消</el-button>
          <el-button type="primary" @click="saveMajor(title)">确 定</el-button>
        </div>
      </el-dialog>
    </div>
    <el-backtop :visibility-height=200>
      <div
          style="{
        height: 100%;
        width: 100%;
        background-color: #f2f5f6;
        box-shadow: 0 0 6px rgba(0,0,0, .30);
        text-align: center;
        line-height: 40px;
        color: #1989fa;
      }">
        ↑
      </div>
    </el-backtop>
  </div>
</template>

<script>
import axios from "axios";
import service from "@/request";

export default {
  name: "MajorManage",
  data() {
    return {
      majorName: '',
      majorList: [{
        majorId: '',
        majorName: '',
        classTotal: 0,
        courseTotal: 0,
      }],
      title: '',
      pageNum: 1,
      pageSize: 10,
      total: 0,
      formVisible: false,
      majorForm: {
        majorId: '',
        majorName: '',
      },
      rules: {
        majorName: [{required: true, message: "请输入专业", trigger: "blur"},//非空验证,
          {min: 2, max: 20, message: "专业名长度为2到20个字符", trigger: "blur"}],
      },
    }
  },
  methods: {
    getMajorList() {
      axios.get(service.baseURL + "/major/majorPageList?pageNum=" + this.pageNum +
          "&pageSize=" + this.pageSize + "&majorName=" + this.majorName, {
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp) => {
        if (resp.data.code == '20000') {
          this.majorList = resp.data.data.majorList
          this.total = resp.data.data.total
        } else {
          this.$message.error(resp.data.msg)
        }
      })
    },
    searchMajorList() {
      this.pageNum = 1
      this.getMajorList();
    }
    ,
    resetMajorList() {
      location.reload()
    },
    openMajorForm(row) {
      if (row == null) {
        this.formVisible = true
        this.title = '新增专业'
      } else {
        if (row.classTotal > 0 || row.courseTotal > 0) {
          this.$message.info("该专业还有班级或课程，无法编辑")
        } else {
          this.formVisible = true
          this.title = '编辑专业'
          axios.get(service.baseURL + "/major/getByMajorId?majorId=" + row.majorId, {
            headers: {
              Authorization: this.$store.state.token
            }
          })
              .then((resp) => {
                if (resp.data.code == '20000') {
                  this.majorForm = resp.data.data
                } else {
                  this.$message.error(resp.data.msg)
                }
              })
        }
      }
    },
    delMajor(row) {
      if (row.classTotal > 0 || row.courseTotal > 0) {
        this.$message.info("该专业还有班级或课程无法，无法删除")
      } else
        this.$confirm(`确认删除专业：${row.majorName} ？`, "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          center: true,
        }).then(() => {
          axios.delete(service.baseURL + "/major/delById?majorId=" + row.majorId, {
            headers: {
              Authorization: this.$store.state.token
            }
          })
              .then((resp) => {
                if (resp.data.code == '20000') {
                  this.getMajorList();
                  this.$message.success(resp.data.msg)
                } else {
                  this.$message.error(resp.data.msg)
                }
              })
        }).catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.getMajorList()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.pageNum = 1
      this.getMajorList()
    },
    clearForm() {
      this.majorForm = {}
      this.$refs.majorFormRef.clearValidate()
    },
    cancelForm(title) {
      this.formVisible = false
      this.clearForm()
      if (title == '新增专业') {
        this.$message.info("已取消新增")
      } else if (title == '编辑专业') {
        this.$message.info("已取消编辑")
      }
    },
    saveMajor(title) {
      this.$refs.majorFormRef.validate((valid) => {
        if (valid && title == '新增专业') {//如果表单的填写合法,发送axios请求
          axios.post(service.baseURL + "/major/add", {
                'majorName': this.majorForm.majorName,
              }, {
                headers: {
                  Authorization: this.$store.state.token
                }
              }
          ).then((resp) => {
            if (resp.data.code == '20000') {
              this.formVisible = false;//添加成功关闭表单
              this.clearForm()
              this.getMajorList();//刷新表格
              this.$message.success(resp.data.msg);
            } else {
              this.$message.error(resp.data.msg)
            }
          })
        } else if (valid && title == '编辑专业') {
          axios.put(service.baseURL + "/major/edit", {
            'majorName': this.majorForm.majorName,
            'majorId': this.majorForm.majorId
          }, {
            headers: {
              Authorization: this.$store.state.token
            }
          }).then((resp) => {
            if (resp.data.code == '20000') {
              this.formVisible = false;//添加成功关闭表单
              this.clearForm()
              this.getMajorList();//刷新表格
              this.$message.success(resp.data.msg);
            } else {
              this.$message.error(resp.data.msg)
            }
          })
        } else {//表单验证失败
          return false;
        }
      })
    }
  },
  created() {
    this.getMajorList();
  }
}
</script>

<style scoped>
.findAdd {
  margin: 20px auto;
}

.el-input, .el-button {
  margin-right: 20px;
}
</style>
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209202000791.png" alt="image-20231209202000791" style="zoom:67%;" />

### 成绩管理

```vue
<template>
  <div>
    <div class="findAdd">
      <el-input autocomplete="off" placeholder="请输入学生姓名" v-model="stuName" clearable style="width: 240px;margin-left: 0"></el-input>
      <el-input autocomplete="off" placeholder="请输入班号" v-model="className" clearable
                style="width: 240px"></el-input>
      <el-select placeholder="请选择年级（必填）" clearable v-model="admissionYear">
        <el-option
            v-for="item in classYears"
            :key="item.value"
            :label="item.label"
            :value="item.label">
        </el-option>
      </el-select>
      <el-select placeholder="请选择专业（必填）" clearable v-model="major">
        <el-option
            v-for="item in majors"
            :key="item.majorId"
            :label="item.majorName"
            :value="item.majorId">
        </el-option>
      </el-select>
      <el-button type="primary" @click="searchStuList" icon="el-icon-search">搜索</el-button>
      <el-button @click="resetStuList" icon="el-icon-refresh" style="margin-left: 0">刷新</el-button>
    </div>

    <div class="stuTable">
      <el-table :data="stuScoreList" stripe size="small" style="width: 100%">
        <el-table-column label="排名" width="40" align="center">
          <template slot-scope="scope">
            {{
              (pageNum - 1) * pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>
        <el-table-column prop="stuId" label="学号" sortable align="center" width="90"></el-table-column>
        <el-table-column prop="stuName" label="学生姓名" align="center" sortable width="100"></el-table-column>
        <el-table-column prop="className" align="center" width="180" label="班级名称" sortable></el-table-column>
        <el-table-column prop="" :label="item.courseName" v-for="(item,index) in courseList" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.courseScore[index] }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="scoreTotal" label="必修课总分" sortable align="center"></el-table-column>
      </el-table>
    </div>
    <div class="page">
      <el-pagination style="text-align: center"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          :pager-count="11"
          background
          :current-page="pageNum"
          :page-size="pageSize"
          :page-sizes="[5, 10, 20, 50,100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
    <el-backtop :visibility-height=200>
      <div
          style="{
        height: 100%;
        width: 100%;
        background-color: #f2f5f6;
        box-shadow: 0 0 6px rgba(0,0,0, .30);
        text-align: center;
        line-height: 40px;
        color: #1989fa;
      }">
        ↑
      </div>
    </el-backtop>
  </div>
</template>

<script>
import axios from "axios";
import service from "@/request";
export default {
  stuName: "MajorScore",
  data() {
    let nowTime = new Date()
    let nowYear = nowTime.getFullYear();
    let maxYear = nowYear - 1//默认是当前年-1
    let startTime = new Date(nowYear + '-9-01 00:00:00')//今年9月份开学时间
    if (nowTime > startTime) {
      maxYear += 1
    }
    return {
      majors:[],
      major:'',
      admissionYear:'',
      //查询本专业，本年级的所有课程
      courseList: [],
      stuScoreList: [
        {courseScore: []},
        {courseScore: []},
        {courseScore: []},
        {courseScore: []},
      ],

      pageNum: 1,
      pageSize: 50,
      stuId: '',
      stuName: '',
      className: '',
      total: 0,
      classYears: [{value: '1', label: maxYear}, {value: '2', label: maxYear - 1},
        {value: '3', label: maxYear - 2}, {value: '4', label: maxYear - 3}],

    }
  },
  created() {
    this.getMajors()
  },
  methods: {
    getStuScoreList() {
      axios.get(service.baseURL+"/score/getStuScoreList?admissionYearMajor=" + this.admissionYear +
          this.major + "&stuName=" + this.stuName+"&pageSize="+this.pageSize+"&pageNum="+this.pageNum
          +"&className="+this.className, {
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp => {
        if (resp.data.code == '20000') {
          this.stuScoreList = resp.data.data.stuScoreList
          this.total=resp.data.data.total
        }
      }))
    },
    getCourseList() {//渲染表头
      axios.get(service.baseURL+"/course/getScoreCourse?majorId=" + this.major +
          "&admissionYear=" + this.admissionYear, {
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp => {
        if (resp.data.code == '20000') {
          this.courseList = resp.data.data
        }
      }))
    },
    getMajors(){
      axios.get(service.baseURL+'/major/getAllMajorName',{
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp)=>{
        this.majors=resp.data.data
      })
    },
    resetStuList() {
      location.reload();
    },
    searchStuList() {
      if ((this.major!=''&&this.major!=null)&&(this.admissionYear!=''&&this.admissionYear!=null)){
        this.pageNum = 1
        this.getCourseList()
        this.getStuScoreList()
      }
    },

    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.getStuScoreList()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.pageNum = 1
      this.getStuScoreList()
    },
  }
}
</script>

<style scoped>
.findAdd {
  margin: 20px auto;
}

.el-input,.el-select,.el-button  {
  margin-right: 20px;
}
/*.el-select {*/
/*  margin-left: 15px;*/
/*}*/

/*.el-button {*/
/*  margin-left: 20px;*/
/*}*/

</style>
```


### 一键操作
```vue
<template>
  <div>
    <div class="adminChooseCourse">
      <font style="margin: 0 auto"></font>
      <el-tooltip class="item" effect="dark" content="给所有学生选好当前可选的必修课程" placement="top">
        <el-button @click="adminChooseCourse" type="primary">一键选课</el-button>
      </el-tooltip>
    </div>

    <div class="entertainment">
      <el-tooltip class="item" effect="dark" content="给所有学生的已选课程的未打分课程进行随机打分" placement="left">
        <el-button @click="adminScoreRandomly" round type="primary">一键打分</el-button>
      </el-tooltip>

      <el-tooltip class="item" effect="dark" content="清空所有学生的成绩" placement="bottom">
        <el-button @click="cancelAllScore" round type="primary">分数置空</el-button>
      </el-tooltip>

      <el-tooltip class="item" effect="dark" content="撤销所有学生的选课记录" placement="right">
        <el-button @click="cancelChooseCourse" round type="primary">一键退课</el-button>
      </el-tooltip>

    </div>
  </div>
</template>

<script>
import axios from "axios";
import service from "@/request";

export default {
  name: "OneClick",
  methods: {
    adminChooseCourse() {
      axios.get(service.baseURL + "/admin/adminChooseCourse",
          {
            headers: {
              Authorization: this.$store.state.token
            }
          }
      ).then((resp) => {
        if (resp.data.code == '20000') {
          this.$message.success(resp.data.msg)
        } else {
          this.$message.error(resp.data.msg)
        }
      })
    },


    adminScoreRandomly() {
      this.$confirm(`确认执行一键打分？`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      }).then(() => {
        axios.get(service.baseURL + "/admin/adminScoreRandomly",
            {
              headers: {
                Authorization: this.$store.state.token
              }
            }
        ).then((resp) => {
          if (resp.data.code == '20000') {
            this.$message.success(resp.data.msg)
          } else {
            this.$message.error(resp.data.msg)
          }
        })
      }).catch(() => {
        this.$message({
          type: "info",
          message: "已取消一键打分",
        })
      })
    },
    cancelAllScore() {
      this.$confirm(`确认执行分数置空？`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      }).then(() => {
        axios.delete(service.baseURL + "/score/cancelAllScore",
            {
              headers: {
                Authorization: this.$store.state.token
              }
            }
        ).then((resp) => {
          if (resp.data.code == '20000') {
            this.$message.success(resp.data.msg)
          } else {
            this.$message.error(resp.data.msg)
          }
        })
      }).catch(() => {
        this.$message({
          type: "info",
          message: "已取消分数置空",
        })
      })
    },
    cancelChooseCourse() {
      this.$confirm(`确认执行一键退课？`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      }).then(() => {
        axios.delete(service.baseURL + "/chooseCourse/cancelChooseCourse",
            {
              headers: {
                Authorization: this.$store.state.token
              }
            }
        ).then((resp) => {
          if (resp.data.code == '20000') {
            this.$message.success(resp.data.msg)
          } else {
            this.$message.error(resp.data.msg)
          }
        })
      }).catch(() => {
        this.$message({
          type: "info",
          message: "已取消一键退课",
        })
      })
    },
  }
}
</script>

<style scoped>
.adminChooseCourse {
  text-align: center;
  margin: 80px auto 0;
}

.entertainment {
  text-align: center;
  margin: 80px auto;
}


.adminChooseCourse .el-button {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  font-weight: bold;

}

.entertainment .el-button {
  width: 100px;
  height: 100px;
  margin: 0 30px;
  border-radius: 50%;
  background-color: red;
  font-weight: bold;
}
</style>
```

### 注销

```vue
<template>
  <div class="delAdmin">
    <font @click="delAdmin" class="delAdminContent">注销账号</font>
  </div>
</template>

<script>
import axios from "axios";
import service from "@/request";
export default {
  name: "AdminChooseCourse",
  methods: {
    delAdmin() {
      this.$confirm(`确认注销账号？`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      }).then(() => {
        axios.delete(service.baseURL+"/admin/delAdmin?adminId=" + this.$store.state.userInfo.adminId,
            {
              headers: {
                Authorization: this.$store.state.token
              }
            })
            .then((resp) => {
              if (resp.data.code == '20000') {
                this.$store.commit('REMOVE_INFO')
                this.$router.push('/login')
                this.$message.success(resp.data.msg)
              } else {
                this.$message.error(resp.data.msg)
              }
            })
      }).catch(() => {
        this.$message({
          type: "info",
          message: "已取消注销",
        })
      })
    }
  }
}

</script>

<style scoped>
.delAdmin {
  text-align: center;
  margin: 200px auto;
}
.delAdminContent:hover{
  color: red;
  cursor: pointer;
}
</style>
```



## 老师页面

```vue
<template>
  <el-container>

    <el-aside width="200px">
      <TeaLeftSide></TeaLeftSide>
    </el-aside>

    <el-container>

      <el-header style="padding: 0 10px">
        <Header></Header>
      </el-header>

      <el-main style="padding: 0 10px">
        <router-view></router-view>
      </el-main>

    </el-container>
  </el-container>
</template>

<script>
import Header from "@/components/Header";
import TeaLeftSide from "@/components/TeaLeftSide";
export default {
  name: "Teacher",
  components:{Header,TeaLeftSide}
}
</script>

<style scoped>
.el-main {
  background-color: #E9EEF3;
  /*text-align: center;*/
  /*line-height: 160px;*/
  width: 100%;
  /*min-height: 100%;*/
  /*position: absolute;*/
}
</style>
```

### 老师信息

```vue
<template>
  <div class="myInfo">
    <el-card class="admin-card">
      <div slot="header" align="center" class="header">
        <span>班主任个人资料</span>
      </div>
      <div class="content">
        <div class="edit">
          <el-button type="primary" round style="float: right" @click="openEditTea">编辑
          </el-button>
        </div>
        <el-descriptions title="" border :column="column">
          <el-descriptions-item label="身份">班主任</el-descriptions-item>
          <el-descriptions-item label="班主任编号">{{ userInfo.teaId }}</el-descriptions-item>
          <el-descriptions-item label="班主任姓名">{{ userInfo.teaName }}</el-descriptions-item>
          <el-descriptions-item label="密码">{{ userInfo.teaPwd }}</el-descriptions-item>
          <el-descriptions-item label="年级">{{ userInfo.classYear }}</el-descriptions-item>
          <el-descriptions-item label="专业">{{ userInfo.majorName }}</el-descriptions-item>
          <el-descriptions-item label="班级">{{ userInfo.className }}</el-descriptions-item>
          <el-descriptions-item label="身份证">{{ userInfo.teaIdCard }}</el-descriptions-item>
          <el-descriptions-item label="注册时间">{{ userInfo.createTime }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
    <!--    编辑班主任表单-->
    <div class="editDialog">
      <el-dialog
          @close="clearEditForm"
          title="编辑班主任"
          :visible.sync="editFormVisible">
        <el-form :model="editForm" :rules="editRules" ref="editRef">
          <el-form-item label="账号" label-width="150px" prop="teaId">
            <el-input v-model="editForm.teaId" disabled></el-input>
          </el-form-item>
          <el-form-item label="班主任姓名" label-width="150px" prop="teaName">
            <el-input v-model="editForm.teaName" placeholder="请输入班主任姓名"></el-input>
          </el-form-item>
          <el-form-item label="密码" label-width="150px" prop="teaPwd">
            <el-input v-model="editForm.teaPwd" placeholder="请输入密码" autocomplete="off" clearable
                      type="password"></el-input>
          </el-form-item>
          <el-form-item label="密码" label-width="150px" prop="teaPwd2">
            <el-input v-model="editForm.teaPwd2" placeholder="请再次输入相同密码" autocomplete="off" clearable
                      type="password"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelEdit">取 消</el-button>
          <el-button type="primary" @click="editTea">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>

import axios from "axios";
import service from "@/request";

export default {
  data() {
    let validateCheckPwd = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入相同密码'));
      } else if (value !== this.editForm.teaPwd) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      column: 2,
      editFormVisible: false,
      userInfo: {
        classYear: '',
        teaName: '',
        teaPwd: '',
        createTime: '',
      },
      editForm: {
        teaId: '',
        teaName: '',
        teaPwd: "",
        teaPwd2: "",
      },
      editRules: {
        teaName: [{required: true, message: "请输入班主任姓名", trigger: "blur"}, //非空验证
          {min: 2, max: 20, message: '姓名长度为2到20个字符', trigger: 'blur'}
        ],
        teaPwd: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
          {min: 6, max: 16, message: '密码长度为6到16个字符', trigger: 'blur'}
        ],
        teaPwd2: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
          {validator: validateCheckPwd, trigger: 'blur'}
        ],
      }
    }
  },

  created() {
    this.getTeaInfo()
  },
  methods: {
    getTeaInfo() {
      axios.get(service.baseURL + "/teacher/getTeaById?teaId=" + this.$store.state.userInfo.teaId,
          {
            headers: {
              Authorization: this.$store.state.token
            }
          })
          .then((resp) => {
            if (resp.data.code == '20000') {
              this.userInfo = resp.data.data;
            } else {
              this.$message.error(resp.data.msg)
            }
          })
    },
    openEditTea() {
      this.editForm.teaId = this.userInfo.teaId
      this.editForm.teaName = this.userInfo.teaName
      this.editFormVisible = true
    },
    clearEditForm() {
      this.editForm = {}
      this.$set(this.editForm, 'teaName', '')
      this.$refs.editRef.clearValidate()
    },
    cancelEdit() {
      this.editFormVisible = false
      this.clearEditForm()
      this.$message.info("已取消编辑")
    },
    editTea() {
      this.$refs.editRef.validate((valid) => {
        if (valid) {//如果表单的填写合法,发送axios请求
          axios.put(service.baseURL + "/teacher/editById", this.editForm,
              {
                headers: {
                  Authorization: this.$store.state.token
                }
              })
              .then((resp) => {
                if (resp.data.code == '20000') {
                  this.editFormVisible = false;//编辑成功关闭表单
                  this.clearEditForm()
                  this.getTeaInfo();//刷新表格
                  this.$message.success('修改个人信息成功');
                } else {
                  this.$message.error('修改个人信息失败')
                }
              })
        } else {//表单验证失败
          return false;
        }
      })
    }
  }
}
</script>

<style scoped>
.admin-card {
  width: 100%;
  margin: 20px auto;
}

.el-input {
  width: 400px;
}

.header {
  font-size: 30px;
}
</style>

```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209202603155.png" alt="image-20231209202603155" style="zoom:67%;" />

### 班级成绩管理

```vue
<template>
  <div>
    <div class="findAdd">
      <el-input autocomplete="off" placeholder="请输入学号" v-model="stuId" clearable style="width: 240px"></el-input>
      <el-input autocomplete="off" placeholder="请输入学生姓名" v-model="stuName" clearable
                style="width: 240px"></el-input>
      <el-button type="primary" @click="searchStuList" icon="el-icon-search">搜索</el-button>
      <el-button @click="resetStuList" icon="el-icon-refresh" style="margin-left: 0">刷新</el-button>
    </div>

    <div class="stuTable">
      <el-table :data="stuScoreList" stripe size="small" style="width: 100%">
        <el-table-column label="排名" width="40" align="center">
          <template slot-scope="scope">
            {{
              (pageNum - 1) * pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>
        <el-table-column prop="stuId" label="学号" sortable align="center" width="90"></el-table-column>
        <el-table-column prop="stuName" label="学生姓名" align="center" sortable width="100"></el-table-column>
        <el-table-column prop="className" align="center" width="180" label="班级名称" sortable></el-table-column>
        <el-table-column prop="" :label="item.courseName" v-for="(item,index) in courseList" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.courseScore[index] }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="scoreTotal" label="必修课总分" sortable align="center"></el-table-column>
        <el-table-column prop="handle" label="操作" align="center">
          <template slot-scope="scope">
            <el-button @click="openMessage(scope.row)" type="primary" size="mini">私信</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="page">
      <el-pagination style="text-align: center"          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          :pager-count="11"
          background
          :current-page="pageNum"
          :page-size="pageSize"
          :page-sizes="[5, 10, 20, 50,100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <div class="msgDialog">
      <el-dialog
          @close="clearMsgForm"
          title="给学生发消息"
          :visible.sync="msgFormVisible">
        <el-form :model="msgForm" :rules="msgRules" ref="msgRef">

          <el-form-item label="学生姓名" label-width="80px" prop="stuName">
            <el-input v-model="msgForm.stuName" disabled></el-input>
          </el-form-item>

          <el-form-item label="消息" label-width="80px" prop="msgContent">
            <el-input v-model="msgForm.msgContent" placeholder="请输入消息" autocomplete="off" clearable type="textarea"
                      rows="5"></el-input>
          </el-form-item>
        </el-form>

        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelMsg">取 消</el-button>
          <el-button type="primary" @click="msgToStu">确 定</el-button>
        </div>
      </el-dialog>
    </div>

    <el-backtop :visibility-height=200>
      <div
          style="{
        height: 100%;
        width: 100%;
        background-color: #f2f5f6;
        box-shadow: 0 0 6px rgba(0,0,0, .30);
        text-align: center;
        line-height: 40px;
        color: #1989fa;
      }">
        ↑
      </div>
    </el-backtop>
  </div>
</template>

<script>
import axios from "axios";
import service from "@/request";

export default {
  name: "ClassScore",
  data() {
    return {
      msgRules: {
        msgContent: [
          {required: true, message: "消息不能为空", trigger: "blur"},
          {min: 1, max: 255, message: "消息长度为1到255个字符", trigger: "blur"},
        ]
      },
      msgForm: {
        msgContent: ''
      },
      msgFormVisible: false,
      //查询本专业，本年级的所有课程
      courseList: [],
      stuScoreList: [
        {courseScore: []},
        {courseScore: []},
        {courseScore: []},
        {courseScore: []},
      ],

      pageNum: 1,
      pageSize: 20,
      stuId: '',
      stuName: '',
      className: '',
      total: 0,

    }
  },
  created() {
    this.getCourseList()
    this.getStuScoreList()
  },
  methods: {

    msgToStu(row) {
      this.$refs.msgRef.validate((valid) => {
        if (valid) {
          axios.post(service.baseURL + "/stuMessage/addMsg", {
            stuId: this.msgForm.stuId,
            stuName: this.msgForm.stuName,
            teaId: this.$store.state.userInfo.teaId,
            msgContent: this.msgForm.msgContent
          }, {
            headers: {
              Authorization: this.$store.state.token
            }
          })
          axios.post(service.baseURL + "/teaMessage/addMsg", {
            stuId: this.msgForm.stuId,
            stuName: this.msgForm.stuName,
            teaId: this.$store.state.userInfo.teaId,
            msgContent: this.msgForm.msgContent
          }, {
            headers: {
              Authorization: this.$store.state.token
            }
          }).then((resp) => {
            if (resp.data.code == '20000') {
              this.msgFormVisible = false;//编辑成功关闭表单
              this.clearMsgForm()
              this.$message.success(resp.data.msg)
            } else {
              this.$message.error(resp.data.msg)
            }
          })
        } else {
          return false
        }
      })
    },
    cancelMsg() {
      this.msgFormVisible = false
      this.clearMsgForm()
      this.$message.info("已取消发消息")
    },
    clearMsgForm() {
      this.msgForm = {}
      this.$set(this.msgForm, 'msgContent', '')
      this.$refs.msgRef.clearValidate()
    },
    openMessage(row) {
      this.msgFormVisible = true
      this.msgForm.stuId = row.stuId
      this.msgForm.stuName = row.stuName
    },
    getStuScoreList() {
      axios.get(service.baseURL + "/score/getStuScoreList?admissionYearMajor=" + this.$store.state.userInfo.classYear +
          this.$store.state.userInfo.majorId + "&stuName=" + this.stuName + "&pageSize=" + this.pageSize + "&pageNum=" + this.pageNum
          + "&stuId=" + this.stuId + "&className=" + this.$store.state.userInfo.classNo, {
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp => {
        if (resp.data.code == '20000') {
          this.stuScoreList = resp.data.data.stuScoreList
          this.total = resp.data.data.total
        }
      }))
    },
    getCourseList() {//渲染表头
      axios.get(service.baseURL + "/course/getScoreCourse?majorId=" + this.$store.state.userInfo.majorId +
          "&admissionYear=" + this.$store.state.userInfo.classYear, {
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp => {
        if (resp.data.code == '20000') {
          this.courseList = resp.data.data
        }
      }))
    },
    resetStuList() {
      location.reload();
    },
    searchStuList() {
      this.pageNum = 1
      this.getStuScoreList()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.getStuScoreList()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.pageNum = 1
      this.getStuScoreList()
    },
  }
}
</script>

<style scoped>
.findAdd {
  margin: 20px auto;
}

.el-input {
  width: 400px;
  margin-right: 20px;
}

.el-button {
  margin-right: 20px;
}

</style>
```

### 专业成绩管理

```vue
<template>
  <div>
    <div class="findAdd">
      <el-input autocomplete="off" placeholder="请输入学号" v-model="stuId" clearable style="width: 240px"></el-input>
      <el-input autocomplete="off" placeholder="请输入学生姓名" v-model="stuName" clearable style="width: 240px"></el-input>
      <el-input autocomplete="off" placeholder="请输入班号" v-model="className" clearable
                style="width: 240px"></el-input>
      <el-button type="primary" @click="searchStuList" icon="el-icon-search">搜索</el-button>
      <el-button @click="resetStuList" icon="el-icon-refresh" style="margin-left: 0">刷新</el-button>
    </div>

    <div class="stuTable">
      <el-table :data="stuScoreList" stripe size="small" style="width: 100%">
        <el-table-column label="排名" width="40" align="center">
          <template slot-scope="scope">
            {{
              (pageNum - 1) * pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>
        <el-table-column prop="stuId" label="学号" sortable align="center" width="90"></el-table-column>
        <el-table-column prop="stuName" label="学生姓名" align="center" sortable width="100"></el-table-column>
        <el-table-column prop="className" align="center" width="180" label="班级名称" sortable></el-table-column>
        <el-table-column prop="" :label="item.courseName" v-for="(item,index) in courseList" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.courseScore[index] }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="scoreTotal" label="必修课总分" sortable align="center"></el-table-column>
      </el-table>
    </div>
    <div class="page">
      <el-pagination style="text-align: center"          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          :pager-count="11"
          background
          :current-page="pageNum"
          :page-size="pageSize"
          :page-sizes="[5, 10, 20, 50,100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
    <el-backtop :visibility-height=200>
      <div
          style="{
        height: 100%;
        width: 100%;
        background-color: #f2f5f6;
        box-shadow: 0 0 6px rgba(0,0,0, .30);
        text-align: center;
        line-height: 40px;
        color: #1989fa;
      }">
        ↑
      </div>
    </el-backtop>
  </div>
</template>

<script>
import axios from "axios";
import service from "@/request";
export default {
  stuName: "MajorScore",
  data() {
    return {
      //查询本专业，本年级的所有课程
      courseList: [],
      stuScoreList: [
        {courseScore: []},
        {courseScore: []},
        {courseScore: []},
        {courseScore: []},
      ],

      pageNum: 1,
      pageSize: 50,
      stuId: '',
      stuName: '',
      className: '',
      total: 0,

    }
  },
  created() {
    this.getCourseList()
    this.getStuScoreList()
  },
  methods: {
    getStuScoreList() {
      axios.get(service.baseURL+"/score/getStuScoreList?admissionYearMajor=" + this.$store.state.userInfo.classYear +
          this.$store.state.userInfo.majorId + "&stuName=" + this.stuName+"&pageSize="+this.pageSize+"&pageNum="+this.pageNum
          +"&stuId="+this.stuId+"&className="+this.className, {
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp => {
        if (resp.data.code == '20000') {
          this.stuScoreList = resp.data.data.stuScoreList
          this.total=resp.data.data.total
        }
      }))
    },
    getCourseList() {//渲染表头
      axios.get(service.baseURL+"/course/getScoreCourse?majorId=" + this.$store.state.userInfo.majorId +
          "&admissionYear=" + this.$store.state.userInfo.classYear, {
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp => {
        if (resp.data.code == '20000') {
          this.courseList = resp.data.data
        }
      }))
    },

    resetStuList() {
      location.reload();
    },
    searchStuList() {
      this.pageNum = 1
      this.getStuScoreList()
    },


    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.getStuScoreList()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.pageNum = 1
      this.getStuScoreList()
    },
  }
}
</script>

<style scoped>
.findAdd {
  margin: 20px auto;
}

.el-input {
  width: 400px;
  margin-right: 20px;
}

.el-button {
  margin-right: 20px;
}

</style>
```

### 消息管理

```vue
<template>
  <div>
    <div style="margin-top: 20px">
      <el-input v-model="stuName" style="width: 240px" autocomplete="off" placeholder="请输入学生姓名" clearable></el-input>
      <el-button type="primary" @click="searchMsg" icon="el-icon-search">搜索</el-button>
      <el-button @click="resetMsgList" icon="el-icon-refresh">刷新</el-button>
    </div>
    <h1 v-if="msgList.length==0" style="text-align: center">消息为空</h1>
    <el-card v-for="(item,index) in msgList" header="" style="margin: 20px auto">
      <div style="margin: 10px auto;">内容：{{ item.msgContent }}</div>
      <div style="margin: 10px auto;">学生：{{ item.stuName }}</div>
      <div style="margin: 10px auto;">时间：{{ item.msgTime }}</div>
      <div>
        <el-button @click="delMsg(item)" round type="primary" style="float: right">删除</el-button>
      </div>
    </el-card>
    <el-backtop :visibility-height=200>
      <div
          style="{
        height: 100%;
        width: 100%;
        background-color: #f2f5f6;
        box-shadow: 0 0 6px rgba(0,0,0, .30);
        text-align: center;
        line-height: 40px;
        color: #1989fa;
      }">
        ↑
      </div>
    </el-backtop>
  </div>
</template>

<script>
import axios from "axios";
import service from "@/request";

export default {
  name: "HasSendMsg",
  data() {
    return {
      stuName:'',
      msgList: [
        {
          msgTime: '',
          msgId: ''
        }
      ]
    }
  },
  methods: {
    searchMsg(){
      this.getMsgList()
    },
    resetMsgList(){
      location.reload()
    },
    getMsgList() {
      axios.get(service.baseURL + "/teaMessage/getMsgsByTeaId?teaId=" +
          this.$store.state.userInfo.teaId+"&stuName="+this.stuName, {
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp => {
        if (resp.data.code == '20000') {
          this.msgList = resp.data.data
        }
      }))
    },
    delMsg(item) {
      this.$confirm(`确认删除该消息？`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      }).then(() => {
        axios.delete(service.baseURL + "/teaMessage/delById?msgId=" + item.msgId, {
          headers: {
            Authorization: this.$store.state.token
          }
        }).then((resp) => {
          if (resp.data.code == '20000') {
            this.getMsgList()
            this.$message.success(resp.data.msg)
          } else {
            this.$message.error(resp.data.msg)
          }
        }).catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          })
        })
      })
    }
  },
  created() {
    this.getMsgList()
  }
}
</script>

<style scoped>
.el-button{
  margin-left: 20px;
}
</style>
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209202632173.png" alt="image-20231209202632173" style="zoom:67%;" />

## 学生页面

```vue
<template>
  <el-container>

    <el-aside width="200px">
      <StuLeftSide></StuLeftSide>
    </el-aside>

    <el-container>

      <el-header style="padding: 0 10px">
        <Header></Header>
      </el-header>

      <el-main style="padding: 0 10px">
        <router-view></router-view>
      </el-main>

    </el-container>
  </el-container>
</template>

<script>
import Header from "@/components/Header";
import StuLeftSide from "@/components/StuLeftSide";
export default {
  name: "Student",
  components:{Header,StuLeftSide}
}
</script>

<style scoped>
.el-main {
  background-color: #E9EEF3;
  /*text-align: center;*/
  /*line-height: 160px;*/
  width: 100%;
  /*min-height: 100%;*/
  /*position: absolute;*/
}
</style>
```

### 学生信息

```vue
<template>
  <div class="myInfo">
    <el-card class="admin-card">
      <div slot="header" align="center" class="header">
        <span>学生个人资料</span>
      </div>
      <div class="content">
        <div class="edit">
          <el-button type="primary" round style="float: right" @click="openEditStu">编辑
          </el-button>
        </div>
        <el-descriptions title="" border :column="column">
          <el-descriptions-item label="身份">学生</el-descriptions-item>
          <el-descriptions-item label="学号">{{ userInfo.stuId }}</el-descriptions-item>
          <el-descriptions-item label="学生姓名">{{ userInfo.stuName }}</el-descriptions-item>
          <el-descriptions-item label="密码">{{ userInfo.stuPwd }}</el-descriptions-item>
          <el-descriptions-item label="入学年份">{{ userInfo.admissionYear }}</el-descriptions-item>
          <el-descriptions-item label="专业">{{ userInfo.majorName }}</el-descriptions-item>
          <el-descriptions-item label="班级">{{ userInfo.className }}</el-descriptions-item>
          <el-descriptions-item label="班主任姓名">{{ userInfo.teaName }}</el-descriptions-item>
          <el-descriptions-item label="身份证">{{ userInfo.stuIdCard }}</el-descriptions-item>
          <el-descriptions-item label="注册时间">{{ userInfo.createTime }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
    <!--    编辑学生表单-->
    <div class="editDialog">
      <el-dialog
          @close="clearEditForm"
          title="编辑学生"
          :visible.sync="editFormVisible">
        <el-form :model="editForm" :rules="editRules" ref="editForm">
          <el-form-item label="账号" label-width="150px" prop="stuId">
            <el-input v-model="editForm.stuId" disabled></el-input>
          </el-form-item>
          <el-form-item label="学生姓名" label-width="150px" prop="stuName">
            <el-input v-model="editForm.stuName" placeholder="请输入学生姓名"></el-input>
          </el-form-item>
          <el-form-item label="密码" label-width="150px" prop="stuPwd">
            <el-input v-model="editForm.stuPwd" placeholder="请输入密码" autocomplete="off" clearable
                      type="password"></el-input>
          </el-form-item>
          <el-form-item label="密码" label-width="150px" prop="stuPwd2">
            <el-input v-model="editForm.stuPwd2" placeholder="请再次输入相同密码" autocomplete="off" clearable
                      type="password"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelEdit">取 消</el-button>
          <el-button type="primary" @click="editStu">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>

import axios from "axios";
import service from "@/request";

export default {
  data() {
    let validateCheckPwd = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入相同密码'));
      } else if (value !== this.editForm.stuPwd) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      column: 2,
      editFormVisible: false,
      userInfo: {
        admissionYear: '',
        stuName: '',
        stuPwd: '',
        createTime: '',
      },
      editForm: {
        stuId: '',
        stuName: '',
        stuPwd: "",
        stuPwd2: "",
      },
      editRules: {
        stuName: [{required: true, message: "请输入学生姓名", trigger: "blur"}, //非空验证
          {min: 2, max: 20, message: '姓名长度为2到20个字符', trigger: 'blur'}
        ],
        stuPwd: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
          {min: 6, max: 16, message: '密码长度为6到16个字符', trigger: 'blur'}
        ],
        stuPwd2: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
          {validator: validateCheckPwd, trigger: 'blur'}
        ],
      }
    }
  },

  created() {
    this.getStuInfo()
  },
  methods: {
    getStuInfo() {
      axios.get(service.baseURL + "/student/getByStuId?stuId=" + this.$store.state.userInfo.stuId,
          {
            headers: {
              Authorization: this.$store.state.token
            }
          })
          .then((resp) => {
            if (resp.data.code == '20000') {
              this.userInfo = resp.data.data;
            } else {
              this.$message.error(resp.data.msg)
            }
          })
    },
    openEditStu() {
      this.editFormVisible = true
      this.editForm.stuId = this.userInfo.stuId
      this.editForm.stuName = this.userInfo.stuName
    },
    clearEditForm() {
      this.editForm = {}
      this.$set(this.editForm, 'stuName', '')
      this.$refs.editForm.clearValidate()
    },
    cancelEdit() {
      this.editFormVisible = false
      this.clearEditForm()
      this.$message.info("已取消编辑")
    },
    editStu() {
      this.$refs.editForm.validate((valid) => {
        if (valid) {//如果表单的填写合法,发送axios请求
          axios.put(service.baseURL + "/student/editByStuId", this.editForm,
              {
                headers: {
                  Authorization: this.$store.state.token
                }
              })
              .then((resp) => {
                if (resp.data.code == '20000') {
                  this.getStuInfo()
                  // location.reload()
                  this.$message.success('修改个人信息成功');
                  this.editFormVisible = false;//编辑成功关闭表单
                  this.clearEditForm()
                } else {
                  this.$message.error('修改个人信息失败')
                }
              })
        } else {//表单验证失败
          return false;
        }
      })
    }
  }
}
</script>

<style scoped>
.admin-card {
  width: 100%;
  margin: 20px auto;
}

.el-input {
  width: 400px;
}

.header {
  font-size: 30px;
}
</style>

```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209202418193.png" alt="image-20231209202418193" style="zoom:67%;" />

### 选课

```vue
<template>
  <div>
    <div class="findAdd">
      <el-input autocomplete="off" placeholder="请输入课程名" v-model="courseName" clearable style="width: 240px"></el-input>
      <el-select v-model="coursePeriod" placeholder="请选择开课时期" clearable>
        <el-option
            v-for="item in coursePeriods"
            :key="item.value"
            :label="item.label"
            :value="item.label">
        </el-option>
      </el-select>
      <el-button type="primary" @click="searchCourseList" icon="el-icon-search">搜索</el-button>
      <el-button @click="resetCourseList" icon="el-icon-refresh" style="margin-left: 0">刷新</el-button>
    </div>

    <div class="classTable">
      <el-table :data="courseList" stripe size="small" style="width: 100%">

        <el-table-column label="序号" width="80" align="center">
          <template slot-scope="scope">
            {{
              (pageNum - 1) * pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>

        <el-table-column prop="courseId" label="课程编号" align="center" width="130" sortable></el-table-column>
        <el-table-column prop="courseName" label="课程名" align="center" width="200" sortable></el-table-column>
        <el-table-column prop="majorName" label="所属专业" align="center" width="200" sortable></el-table-column>

        <el-table-column prop="ifDegree" label="课程类型" width="100" align="center" sortable>
          <template slot-scope="scope">
            <el-tag v-if="scope.row.ifDegree == 1">必修</el-tag>
            <el-tag v-else-if="scope.row.ifDegree == 0" type="success">选修</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="coursePeriod" label="开课时期" align="center" width="150" sortable></el-table-column>
        <el-table-column prop="stuChooseNum" label="选课人数" align="center" width="100" sortable></el-table-column>
        <el-table-column prop="state" label="状态" align="center" width="180" sortable>
          <template slot-scope="scope" >
            <el-tag type="success" v-if="scope.row.state==1">已选</el-tag>
            <el-tag type="primary" v-else-if="scope.row.state!=1">未选</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="handle" label="操作" align="center" >
          <template slot-scope="scope" v-if="scope.row.state!=1">
            <el-button @click="chooseCourse(scope.row)" type="primary" size="small">选课</el-button>
          </template>
        </el-table-column>

      </el-table>
    </div>

    <div class="page">
      <el-pagination style="text-align: center"
                     @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          :pager-count="11"
          background
          :current-page="pageNum"
          :page-size="pageSize"
          :background="pageBackground"
          :page-sizes="[5, 10, 20, 50,100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
    <el-backtop :visibility-height=200>
      <div
          style="{
        height: 100%;
        width: 100%;
        background-color: #f2f5f6;
        box-shadow: 0 0 6px rgba(0,0,0, .30);
        text-align: center;
        line-height: 40px;
        color: #1989fa;
      }">
        ↑
      </div>
    </el-backtop>
  </div>
</template>

<script>
import axios from "axios";
import service from "@/request";

export default {
  name: "ChooseCourse",
  data() {
    return {
      coursePeriod:'',
      pageBackground:true,
      courseName: '',
      pageNum: 1,
      pageSize: 20,
      total: 0,
      schoolPeriodNum: 0,
      courseList: [],
      coursePeriods: [
        {value: '1', label: '大一上'},
        {value: '2', label: '大一下'},
        {value: '3', label: '大二上'},
        {value: '4', label: '大二下'},
        {value: '5', label: '大三上'},
        {value: '6', label: '大三下'},
        {value: '7', label: '大四上'},
        {value: '8', label: '大四下'},
      ],
    }
  },
  methods: {
    refreshSchoolPeriodNum() {
      let MyAdmissionYear = this.$store.state.userInfo.admissionYear
      let myAdmissionTime = new Date(MyAdmissionYear + '-9-01 00:00:00')//今年9月份开学时间
      let nowTime = new Date()
      let schoolTime = nowTime - myAdmissionTime
      let schoolPeriodNum = Math.floor(schoolTime / 1000 / (3600 * 24) / 180.625)
      this.schoolPeriodNum = schoolPeriodNum
    },
    getCourseList() {
      axios.get(service.baseURL+"/student/coursePageList?pageNum=" + this.pageNum +
          "&pageSize=" + this.pageSize + "&courseName=" + this.courseName +
          "&majorName=" + this.$store.state.userInfo.majorName + "&schoolPeriodNum=" + this.schoolPeriodNum+
          "&coursePeriod="+this.coursePeriod, {
            headers: {
              Authorization: this.$store.state.token
            }
          }
      ).then((resp) => {
        if (resp.data.code == '20000') {
          this.courseList = resp.data.data.courseList
          this.total = resp.data.data.total
        } else {
          this.$message.error(resp.data.msg)
        }
      })
    },
    chooseCourse(row) {//选课
      axios.post(service.baseURL+"/chooseCourse/add", {
            "stuId":this.$store.state.userInfo.stuId,
            "courseId":row.courseId,
            "courseName":row.courseName,
            "stuName":this.$store.state.userInfo.stuName,
            "majorName":row.majorName,
            "ifDegree":row.ifDegree,
            "coursePeriod":row.coursePeriod,
          },
          {
            headers: {
              Authorization: this.$store.state.token
            }
          }).then((resp=>{
            if (resp.data.code=='20000'){
              this.getCourseList()
              this.$message.success(resp.data.msg)
            }
            else {
              this.$message.error(resp.data.msg)
            }
      }))
    },
    resetCourseList() {
      location.reload()
    },

    handleCurrentChange(pageNum) {//2
      this.pageNum = pageNum
      this.getCourseList()
    },
    handleSizeChange(pageSize) {//1
      this.pageSize = pageSize
      this.pageNum = 1
      this.getCourseList()
    },
    searchCourseList() {
      this.pageNum = 1
      this.getCourseList();
    }
  },
  created() {
    this.refreshSchoolPeriodNum()
    this.getCourseList()
  }
}
</script>

<style scoped>
.findAdd {
  margin: 20px auto;
}
.el-input,.el-button,.el-select{
  margin-right: 20px;
}
</style>
```

![image-20231209202442421](https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209202442421.png)

### 消息管理

```vue
<template>
  <div>
    <h1 v-if="msgList.length==0" style="text-align: center">消息为空</h1>
    <el-card v-for="(item,index) in msgList" header="" style="margin: 20px auto">
      <div style="margin: 10px auto;">内容：{{ item.msgContent }}</div>
      <div style="margin: 10px auto;">时间：{{ item.msgTime }}</div>
      <div><el-button @click="delMsg(item)" round type="primary" style="float: right">删除</el-button></div>
    </el-card>
    <el-backtop :visibility-height=200>
      <div
          style="{
        height: 100%;
        width: 100%;
        background-color: #f2f5f6;
        box-shadow: 0 0 6px rgba(0,0,0, .30);
        text-align: center;
        line-height: 40px;
        color: #1989fa;
      }">
        ↑
      </div>
    </el-backtop>
  </div>
</template>

<script>
import axios from "axios";
import service from "@/request";
export default {
  name: "StuMessage",
  data() {
    return {
      msgList: [
        {msgTime:''}
      ]
    }
  },
  methods: {
    getMsgList() {
      axios.get(service.baseURL+"/stuMessage/getMsgsByStuId?stuId=" + this.$store.state.userInfo.stuId, {
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp => {
        if (resp.data.code == '20000') {
          this.msgList = resp.data.data
        }
      }))
    },
    delMsg(item) {
      this.$confirm(`确认删除该消息？`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      }).then(()=>{
        axios.delete(service.baseURL+"/stuMessage/delById?msgId=" + item.msgId + "&stuId=" + item.stuId, {
          headers: {
            Authorization: this.$store.state.token
          }
        }).then((resp) => {
          if (resp.data.code == '20000') {
            this.getMsgList()
            this.$message.success(resp.data.msg)
          } else {
            this.$message.error(resp.data.msg)
          }
        }).catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          })
        })
      })
    }
  },
  created() {
    this.getMsgList()
  }
}
</script>

<style scoped>

</style>
```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209202515318.png" alt="image-20231209202515318" style="zoom:67%;" />

## X. 部署

```dockerfile
FROM nginx

# 映射 HTML
COPY ./dist ./usr/share/nginx/html
# Conf

COPY dist/conf/nginx.conf ./etc/nginx/conf.conf
COPY dist/conf/conf.d ./etc/nginx/conf.d

EXPOSE 8090


```

<img src="https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/image-20231209202728981.png" alt="image-20231209202728981" style="zoom:67%;" />

## X. 依赖  .package.json

```json
{
    "name": "stu-score-front",
    "version": "0.1.0",
    "private": true,
    "scripts": {
        "serve": "vue-cli-service serve --open",
        "build": "vue-cli-service build"
    },
    "dependencies": {
        "axios": "^1.3.4",
        "core-js": "^3.8.3",
        "element-ui": "^2.15.13",
        "vue": "^2.6.14",
        "vue-router": "^3.5.1",
        "vuex": "^3.6.2"
    },
    "devDependencies": {
        "@vue/cli-plugin-babel": "~5.0.0",
        "@vue/cli-plugin-router": "~5.0.0",
        "@vue/cli-plugin-vuex": "~5.0.0",
        "@vue/cli-service": "~5.0.0",
        "vue-template-compiler": "^2.6.14"
    },
    "browserslist": [
        "> 1%",
        "last 2 versions",
        "not dead"
    ]
}

```

