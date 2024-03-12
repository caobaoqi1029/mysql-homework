<template>
  <div class="myInfo">
    <el-card class="admin-card">
      <div slot="header" align="center" class="header">
        <span>管理员个人资料</span>
      </div>
      <div class="content">
        <div class="edit">
          <el-button type="primary" round style="float: right;margin-bottom: 15px" @click="openEditAdmin">编辑
          </el-button>
        </div>
        <el-descriptions title="" border :column="column">
          <el-descriptions-item label="身份">管理员</el-descriptions-item>
          <el-descriptions-item label="账号">{{ userInfo.adminId }}</el-descriptions-item>
          <el-descriptions-item label="管理员姓名">{{ userInfo.adminName }}</el-descriptions-item>
          <el-descriptions-item label="密码">{{ userInfo.adminPwd }}</el-descriptions-item>
          <el-descriptions-item label="注册时间">{{ userInfo.createTime }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
    <!--    编辑管理员表单-->
    <div class="editDialog">
      <el-dialog
          @close="clearEditForm"
          title="编辑管理员"
          :visible.sync="editFormVisible">
        <el-form :model="editForm" :rules="editRules" ref="editForm">
          <el-form-item label="账号" label-width="150px" prop="adminId">
            <el-input v-model="editForm.adminId" disabled></el-input>
          </el-form-item>
          <el-form-item label="管理员姓名" label-width="150px" prop="adminName">
            <el-input v-model="editForm.adminName" placeholder="请输入管理员姓名"></el-input>
          </el-form-item>
          <el-form-item label="密码" label-width="150px" prop="adminPwd">
            <el-input v-model="editForm.adminPwd" placeholder="请输入密码" autocomplete="off" clearable
                      type="password"></el-input>
          </el-form-item>
          <el-form-item label="密码" label-width="150px" prop="adminPwd2">
            <el-input v-model="editForm.adminPwd2" placeholder="请再次输入相同密码" autocomplete="off" clearable
                      type="password"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelEdit">取 消</el-button>
          <el-button type="primary" @click="editAdmin">确 定</el-button>
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
      } else if (value !== this.editForm.adminPwd) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      column: 1,
      editFormVisible: false,
      userInfo: {
        adminId: '',
        adminName: '',
        adminPwd: '',
        createTime: '',
      },
      editForm: {
        adminId: '',
        adminName: '',
        adminPwd: "",
        adminPwd2: "",
        createTime: ''
      },
      editRules: {
        adminName: [
          {required: true, message: "请输入管理员姓名", trigger: "blur"}, //非空验证
          {min: 2, max: 20, message: '姓名长度为2到20个字符', trigger: 'blur'}
        ],
        adminPwd: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
          {min: 6, max: 16, message: '密码长度为6到16个字符', trigger: 'blur'}
        ],
        adminPwd2: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
          {validator: validateCheckPwd, trigger: 'blur'}
        ],
      }
    }
  },

  created() {
    this.getAdminInfo()
  },
  methods: {
    getAdminInfo() {
      axios.get(service.baseURL + "/admin/getAdminById?adminId=" + this.$store.state.userInfo.adminId,
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
    openEditAdmin() {
      this.editForm.adminId = this.userInfo.adminId
      this.editForm.adminName = this.userInfo.adminName
      this.editFormVisible = true
    },
    clearEditForm() {
      this.editForm = {}
      this.$set(this.editForm, 'adminName', '')
      this.$refs.editForm.clearValidate()
    },
    cancelEdit() {
      this.editFormVisible = false
      this.clearEditForm()
      this.$message.info("已取消编辑")
    },
    editAdmin() {
      this.$refs.editForm.validate((valid) => {
        if (valid) {//如果表单的填写合法,发送axios请求
          axios.put(service.baseURL + "/admin/editAdmin", this.editForm,
              {
                headers: {
                  Authorization: this.$store.state.token
                }
              })
              .then((resp) => {
                if (resp.data.code == '20000') {
                  this.editFormVisible = false;//编辑成功关闭表单
                  this.clearEditForm()
                  this.getAdminInfo();//刷新表格
                  this.$message.success(resp.data.msg);
                } else {
                  this.$message.error(resp.data.msg)
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
  /*font-weight: bold;*/
}
</style>
