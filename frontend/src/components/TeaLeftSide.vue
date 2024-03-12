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