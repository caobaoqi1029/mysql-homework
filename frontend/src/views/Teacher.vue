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