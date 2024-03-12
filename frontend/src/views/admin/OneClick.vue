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