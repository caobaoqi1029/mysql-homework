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