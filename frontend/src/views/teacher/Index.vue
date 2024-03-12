<template>
  <div class="myInfo">
    <el-card class="admin-card">
      <div slot="header" align="center" class="header">
        <span>班主任个人资料</span>
      </div>
      <div class="content">
        <div class="edit">
          <el-button type="primary" round style="float: right" @click="openEditTea">编辑
          </el-button>
        </div>
        <el-descriptions title="" border :column="column">
          <el-descriptions-item label="身份">班主任</el-descriptions-item>
          <el-descriptions-item label="班主任编号">{{ userInfo.teaId }}</el-descriptions-item>
          <el-descriptions-item label="班主任姓名">{{ userInfo.teaName }}</el-descriptions-item>
          <el-descriptions-item label="密码">{{ userInfo.teaPwd }}</el-descriptions-item>
          <el-descriptions-item label="年级">{{ userInfo.classYear }}</el-descriptions-item>
          <el-descriptions-item label="专业">{{ userInfo.majorName }}</el-descriptions-item>
          <el-descriptions-item label="班级">{{ userInfo.className }}</el-descriptions-item>
          <el-descriptions-item label="身份证">{{ userInfo.teaIdCard }}</el-descriptions-item>
          <el-descriptions-item label="注册时间">{{ userInfo.createTime }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
    <!--    编辑班主任表单-->
    <div class="editDialog">
      <el-dialog
          @close="clearEditForm"
          title="编辑班主任"
          :visible.sync="editFormVisible">
        <el-form :model="editForm" :rules="editRules" ref="editRef">
          <el-form-item label="账号" label-width="150px" prop="teaId">
            <el-input v-model="editForm.teaId" disabled></el-input>
          </el-form-item>
          <el-form-item label="班主任姓名" label-width="150px" prop="teaName">
            <el-input v-model="editForm.teaName" placeholder="请输入班主任姓名"></el-input>
          </el-form-item>
          <el-form-item label="密码" label-width="150px" prop="teaPwd">
            <el-input v-model="editForm.teaPwd" placeholder="请输入密码" autocomplete="off" clearable
                      type="password"></el-input>
          </el-form-item>
          <el-form-item label="密码" label-width="150px" prop="teaPwd2">
            <el-input v-model="editForm.teaPwd2" placeholder="请再次输入相同密码" autocomplete="off" clearable
                      type="password"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelEdit">取 消</el-button>
          <el-button type="primary" @click="editTea">确 定</el-button>
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
      } else if (value !== this.editForm.teaPwd) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      column: 2,
      editFormVisible: false,
      userInfo: {
        classYear: '',
        teaName: '',
        teaPwd: '',
        createTime: '',
      },
      editForm: {
        teaId: '',
        teaName: '',
        teaPwd: "",
        teaPwd2: "",
      },
      editRules: {
        teaName: [{required: true, message: "请输入班主任姓名", trigger: "blur"}, //非空验证
          {min: 2, max: 20, message: '姓名长度为2到20个字符', trigger: 'blur'}
        ],
        teaPwd: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
          {min: 6, max: 16, message: '密码长度为6到16个字符', trigger: 'blur'}
        ],
        teaPwd2: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
          {validator: validateCheckPwd, trigger: 'blur'}
        ],
      }
    }
  },

  created() {
    this.getTeaInfo()
  },
  methods: {
    getTeaInfo() {
      axios.get(service.baseURL + "/teacher/getTeaById?teaId=" + this.$store.state.userInfo.teaId,
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
    openEditTea() {
      this.editForm.teaId = this.userInfo.teaId
      this.editForm.teaName = this.userInfo.teaName
      this.editFormVisible = true
    },
    clearEditForm() {
      this.editForm = {}
      this.$set(this.editForm, 'teaName', '')
      this.$refs.editRef.clearValidate()
    },
    cancelEdit() {
      this.editFormVisible = false
      this.clearEditForm()
      this.$message.info("已取消编辑")
    },
    editTea() {
      this.$refs.editRef.validate((valid) => {
        if (valid) {//如果表单的填写合法,发送axios请求
          axios.put(service.baseURL + "/teacher/editById", this.editForm,
              {
                headers: {
                  Authorization: this.$store.state.token
                }
              })
              .then((resp) => {
                if (resp.data.code == '20000') {
                  this.editFormVisible = false;//编辑成功关闭表单
                  this.clearEditForm()
                  this.getTeaInfo();//刷新表格
                  this.$message.success('修改个人信息成功');
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
