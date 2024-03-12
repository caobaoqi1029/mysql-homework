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