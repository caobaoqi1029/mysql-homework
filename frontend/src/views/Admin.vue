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