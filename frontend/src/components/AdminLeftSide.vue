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