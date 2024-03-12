<template>

  <div class="register">

    <div class="content">
      <h1>MySQL-Homework</h1>
      <el-form :model="registerForm" status-icon :rules="rules" ref="registerForm" class="demo-ruleForm">
        <el-form-item prop="name">
          <el-input prefix-icon="el-icon-user" v-model="registerForm.name" autocomplete="off" placeholder="请输入姓名" clearable></el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" type="password" v-model="registerForm.password" autocomplete="off" placeholder="请输入密码"
                    clearable></el-input>
        </el-form-item>

        <el-form-item prop="checkPwd">
          <el-input prefix-icon="el-icon-lock" type="password" v-model="registerForm.checkPwd" autocomplete="off"
                    placeholder="请再次输入相同密码"
                    clearable></el-input>
        </el-form-item>

        <el-form-item prop="adminSecret">
          <el-input prefix-icon="el-icon-lock" v-model="registerForm.adminSecret" placeholder="请输入管理员密钥" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button style="width:145px"type="success" @click="submitForm('registerForm')" round>注册</el-button>
          <el-button style="width:145px" type="warning" @click="resetForm('registerForm')" round>重置</el-button>
        </el-form-item>
        <el-form-item>
          <el-button style="width:300px" round type="danger" @click="toLogin">返回登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import service from "@/request";

export default {
  name: "Register",
  data() {
    let validateCheckPwd = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入相同密码'));
      } else if (value !== this.registerForm.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      registerForm: {
        name: '',
        password: '',
        checkPwd: '',
        adminSecret: '',
      },
      rules: {
        name: [
          {required: true, message: '姓名不能为空', trigger: 'blur'},
          {min: 2, max: 20, message: '姓名长度为2到20个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
          {min: 6, max: 16, message: '密码长度为6到16个字符', trigger: 'blur'}
        ],
        checkPwd: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
          {validator: validateCheckPwd, trigger: 'blur'}
        ],
        adminSecret: [
          {required: true, message: '管理员密钥不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    toLogin() {
      this.$router.push('/login')
    },
    submitForm(registerForm) {
      this.$refs.registerForm.validate((valid) => {
        if (valid) {
          axios.post(service.baseURL+"/admin/add?adminSecret=" + this.registerForm.adminSecret, {
                'adminName': this.registerForm.name,
                'adminPwd': this.registerForm.password,
              },
          ).then((resp) => {
            if (resp.data.code == '20000') {
              this.$refs['registerForm'].resetFields();
              this.$message.success(resp.data.msg);
            } else {
              this.$message.error(resp.data.msg)
            }

          })
        } else {
          return false;
        }
      });
    },
    resetForm(registerForm) {
      this.$refs.registerForm.resetFields();
    }
  }
}
</script>

<style scoped>
.register {
  background-image: url("https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/jinzhong-02.jpg");
  background-size: 100%;
  height: 100%;
  width: 100%;
  position: absolute;
  display: flex;
  justify-content: center;
  align-items: center;
}

.content {
  text-align: center;
}

h1 {
  color: #a52a47;
}

.el-form {
  width: 300px;
}

.el-form-item .el-select {
  width: 300px;
}
</style>