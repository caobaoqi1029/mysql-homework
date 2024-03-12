<template>
  <div style="width: 100vw;height: 100vh;overflow: hidden;display: flex">
    <div class="background-container">
      <el-image class="logo" :src="Bsrc"/>
      <el-image class="background" :src="Msrc"/>
    </div>

    <div class="welcome-info">
      <div style="font-size: 30px">欢迎来到晋中学院</div>
      <div style="margin-top: 5px">士不可以不弘毅，任重而道远。</div>
    </div>

    <div style="width: 400px;background-color: white;z-index: 1">
      <div style="text-align: center;margin-top: 150px">
        <div style="font-size: 25px;font-weight: bold">登录</div>
        <div style="font-size: 14px; color: grey;margin-top: 2px">进入系统前请先进行登录</div>
      </div>
      <div style="margin-left: 55px;margin-top: 5px">
        <el-form :model="loginForm" status-icon :rules="rules" ref="loginForm"
                 class="demo-ruleForm" style="text-align: center">
          <el-form-item prop="id">
            <el-input prefix-icon="el-icon-user" v-model="loginForm.id" autocomplete="off" placeholder="请输入学号 | 班主任编号 | 管理员编号"
                      clearable></el-input>
          </el-form-item>

          <el-form-item prop="password">
            <el-input prefix-icon="el-icon-lock" show-password v-model="loginForm.password" autocomplete="off"
                      placeholder="请输入密码(默认为身份证后 6 位)"
                      clearable></el-input>
          </el-form-item>

          <el-form-item prop="state">
            <el-select clearable placeholder="请选择身份" v-model="loginForm.state">
              <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button style="width:145px" type="success" @click="submitForm('loginForm')" round :disabled="ifDisabled">
              登录
            </el-button>
            <el-button style="width:145px" type="warning" @click="resetForm('loginForm')" round>重置表单</el-button>
          </el-form-item>
          <el-divider>
            <span style="color: grey;font-size: 13px">没有账号？</span>
          </el-divider>
          <el-form-item>
            <el-button style="width:300px" type="danger" round @click="toRegister">立即注册</el-button>
          </el-form-item>
        </el-form>
      </div>

    </div>
  </div>

</template>

<script>
import axios from "axios";
import service from "@/request";

export default {
  name: "Login",
  data() {
    return {
      Msrc: "https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/login-back.jpg",
      Bsrc: "https://jz-cbq-1311841992.cos.ap-beijing.myqcloud.com/images/jz-logo.png",
      loginForm: {
        id: "",
        password: "",
        state: ""
      },
      options: [{
        value: '1',
        label: '学生'
      }, {
        value: '2',
        label: '班主任'
      }, {
        value: '3',
        label: '管理员'
      }],
      rules: {
        id: [
          {required: true, message: '编号不能为空', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
          {min: 6, max: 16, message: '密码长度为 6 到 16 个字符', trigger: 'blur'}
        ],
        state: [
          {required: true, message: '请选择身份', trigger: 'blur'}
        ]
      },
      ifDisabled: null
    }

  },
  methods: {
    toRegister() {
      this.$router.push('/addAdmin');
    },
    submitForm(loginForm) {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          axios.post(service.baseURL + "/login?id=" + this.loginForm.id + "&password=" +
              this.loginForm.password + "&state=" + this.loginForm.state,)
              .then((resp) => {
                if (resp.data.code == '20000') {
                  this.$message.success(resp.data.msg);
                  this.$store.commit('SET_TOKEN', resp.data.data.token)
                  this.$store.commit('SET_USERINFO', resp.data.data.user)
                  if (resp.data.data.state == '管理员') {
                    this.ifDisabled = true
                    this.$router.push('/admin')
                  } else if (resp.data.data.state == '学生') {
                    this.ifDisabled = true
                    this.$router.push('/student')
                  } else {
                    this.ifDisabled = true
                    this.$router.push('/teacher')
                  }
                  this.$refs['loginForm'].resetFields();
                } else {
                  this.ifDisabled = false
                  this.$message.error(resp.data.msg)
                }
              }).catch(() => {
          })
        } else {
          return false;
        }
      })
    },
    resetForm(loginForm) {
      this.$refs.loginForm.resetFields();
    }
  }
}
</script>

<style scoped>
.background-container {
  flex: 1;
  position: relative;
}

.logo {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 1; /* 设置z-index属性将logo放在背景图上方 */
}

.background {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.welcome-info {
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: white;
  text-shadow: 0 0 10px black;
}

.login {
  background-image: url("../assets/img/login-back.jpg");
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