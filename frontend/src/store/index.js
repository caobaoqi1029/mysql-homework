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
