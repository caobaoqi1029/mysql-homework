<template>
  <div>
    <div style="margin-top: 20px">
      <el-input v-model="stuName" style="width: 240px" autocomplete="off" placeholder="请输入学生姓名" clearable></el-input>
      <el-button type="primary" @click="searchMsg" icon="el-icon-search">搜索</el-button>
      <el-button @click="resetMsgList" icon="el-icon-refresh">刷新</el-button>
    </div>
    <h1 v-if="msgList.length==0" style="text-align: center">消息为空</h1>
    <el-card v-for="(item,index) in msgList" header="" style="margin: 20px auto">
      <div style="margin: 10px auto;">内容：{{ item.msgContent }}</div>
      <div style="margin: 10px auto;">学生：{{ item.stuName }}</div>
      <div style="margin: 10px auto;">时间：{{ item.msgTime }}</div>
      <div>
        <el-button @click="delMsg(item)" round type="primary" style="float: right">删除</el-button>
      </div>
    </el-card>
    <el-backtop :visibility-height=200>
      <div
          style="{
        height: 100%;
        width: 100%;
        background-color: #f2f5f6;
        box-shadow: 0 0 6px rgba(0,0,0, .30);
        text-align: center;
        line-height: 40px;
        color: #1989fa;
      }">
        ↑
      </div>
    </el-backtop>
  </div>
</template>

<script>
import axios from "axios";
import service from "@/request";

export default {
  name: "HasSendMsg",
  data() {
    return {
      stuName:'',
      msgList: [
        {
          msgTime: '',
          msgId: ''
        }
      ]
    }
  },
  methods: {
    searchMsg(){
      this.getMsgList()
    },
    resetMsgList(){
      location.reload()
    },
    getMsgList() {
      axios.get(service.baseURL + "/teaMessage/getMsgsByTeaId?teaId=" +
          this.$store.state.userInfo.teaId+"&stuName="+this.stuName, {
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp => {
        if (resp.data.code == '20000') {
          this.msgList = resp.data.data
        }
      }))
    },
    delMsg(item) {
      this.$confirm(`确认删除该消息？`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      }).then(() => {
        axios.delete(service.baseURL + "/teaMessage/delById?msgId=" + item.msgId, {
          headers: {
            Authorization: this.$store.state.token
          }
        }).then((resp) => {
          if (resp.data.code == '20000') {
            this.getMsgList()
            this.$message.success(resp.data.msg)
          } else {
            this.$message.error(resp.data.msg)
          }
        }).catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          })
        })
      })
    }
  },
  created() {
    this.getMsgList()
  }
}
</script>

<style scoped>
.el-button{
  margin-left: 20px;
}
</style>