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