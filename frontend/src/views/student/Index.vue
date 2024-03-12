<template>
  <div class="myInfo">
    <el-card class="admin-card">
      <div slot="header" align="center" class="header">
        <span>学生个人资料</span>
      </div>
      <div class="content">
        <div class="edit">
          <el-button type="primary" round style="float: right" @click="openEditStu">编辑
          </el-button>
        </div>
        <el-descriptions title="" border :column="column">
          <el-descriptions-item label="身份">学生</el-descriptions-item>
          <el-descriptions-item label="学号">{{ userInfo.stuId }}</el-descriptions-item>
          <el-descriptions-item label="学生姓名">{{ userInfo.stuName }}</el-descriptions-item>
          <el-descriptions-item label="密码">{{ userInfo.stuPwd }}</el-descriptions-item>
          <el-descriptions-item label="入学年份">{{ userInfo.admissionYear }}</el-descriptions-item>
          <el-descriptions-item label="专业">{{ userInfo.majorName }}</el-descriptions-item>
          <el-descriptions-item label="班级">{{ userInfo.className }}</el-descriptions-item>
          <el-descriptions-item label="班主任姓名">{{ userInfo.teaName }}</el-descriptions-item>
          <el-descriptions-item label="身份证">{{ userInfo.stuIdCard }}</el-descriptions-item>
          <el-descriptions-item label="注册时间">{{ userInfo.createTime }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
    <!--    编辑学生表单-->
    <div class="editDialog">
      <el-dialog
          @close="clearEditForm"
          title="编辑学生"
          :visible.sync="editFormVisible">
        <el-form :model="editForm" :rules="editRules" ref="editForm">
          <el-form-item label="账号" label-width="150px" prop="stuId">
            <el-input v-model="editForm.stuId" disabled></el-input>
          </el-form-item>
          <el-form-item label="学生姓名" label-width="150px" prop="stuName">
            <el-input v-model="editForm.stuName" placeholder="请输入学生姓名"></el-input>
          </el-form-item>
          <el-form-item label="密码" label-width="150px" prop="stuPwd">
            <el-input v-model="editForm.stuPwd" placeholder="请输入密码" autocomplete="off" clearable
                      type="password"></el-input>
          </el-form-item>
          <el-form-item label="密码" label-width="150px" prop="stuPwd2">
            <el-input v-model="editForm.stuPwd2" placeholder="请再次输入相同密码" autocomplete="off" clearable
                      type="password"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelEdit">取 消</el-button>
          <el-button type="primary" @click="editStu">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>

import axios from "axios";
import service from "@/request";

export default {
  data() {
    let validateCheckPwd = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入相同密码'));
      } else if (value !== this.editForm.stuPwd) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      column: 2,
      editFormVisible: false,
      userInfo: {
        admissionYear: '',
        stuName: '',
        stuPwd: '',
        createTime: '',
      },
      editForm: {
        stuId: '',
        stuName: '',
        stuPwd: "",
        stuPwd2: "",
      },
      editRules: {
        stuName: [{required: true, message: "请输入学生姓名", trigger: "blur"}, //非空验证
          {min: 2, max: 20, message: '姓名长度为2到20个字符', trigger: 'blur'}
        ],
        stuPwd: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
          {min: 6, max: 16, message: '密码长度为6到16个字符', trigger: 'blur'}
        ],
        stuPwd2: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
          {validator: validateCheckPwd, trigger: 'blur'}
        ],
      }
    }
  },

  created() {
    this.getStuInfo()
  },
  methods: {
    getStuInfo() {
      axios.get(service.baseURL + "/student/getByStuId?stuId=" + this.$store.state.userInfo.stuId,
          {
            headers: {
              Authorization: this.$store.state.token
            }
          })
          .then((resp) => {
            if (resp.data.code == '20000') {
              this.userInfo = resp.data.data;
            } else {
              this.$message.error(resp.data.msg)
            }
          })
    },
    openEditStu() {
      this.editFormVisible = true
      this.editForm.stuId = this.userInfo.stuId
      this.editForm.stuName = this.userInfo.stuName
    },
    clearEditForm() {
      this.editForm = {}
      this.$set(this.editForm, 'stuName', '')
      this.$refs.editForm.clearValidate()
    },
    cancelEdit() {
      this.editFormVisible = false
      this.clearEditForm()
      this.$message.info("已取消编辑")
    },
    editStu() {
      this.$refs.editForm.validate((valid) => {
        if (valid) {//如果表单的填写合法,发送axios请求
          axios.put(service.baseURL + "/student/editByStuId", this.editForm,
              {
                headers: {
                  Authorization: this.$store.state.token
                }
              })
              .then((resp) => {
                if (resp.data.code == '20000') {
                  this.getStuInfo()
                  // location.reload()
                  this.$message.success('修改个人信息成功');
                  this.editFormVisible = false;//编辑成功关闭表单
                  this.clearEditForm()
                } else {
                  this.$message.error('修改个人信息失败')
                }
              })
        } else {//表单验证失败
          return false;
        }
      })
    }
  }
}
</script>

<style scoped>
.admin-card {
  width: 100%;
  margin: 20px auto;
}

.el-input {
  width: 400px;
}

.header {
  font-size: 30px;
}
</style>
