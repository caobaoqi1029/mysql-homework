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