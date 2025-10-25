<template>
    <div class="bg">
      <div class="box">
        <div class="form" v-show="!registerVisible">
            <div class="c">Login</div>
            <input type="text" class="a" v-model="form.name" placeholder="用户名/手机号">
            <input type="password" class="a" v-model="form.password" placeholder="密码">
            <div class="verify" style="margin-top: 10px;" v-if="showCaptcha">
                <input type="text" class="a a2" style="width: 150px;" v-model="form.verifyCode" placeholder="请输入验证码" maxlength="5">
                <el-image class="Captcha" @click="refreshCaptcha" :src="verify_img" fit="contain" />
            </div>
            <div class="d">
                <input type="checkbox" style="width: 16px;margin-right: 4px;" v-model="isAdmin"><p class="f" @click="roleChange()">管理员登陆</p>
                <a href="#">忘记密码?</a>
            </div>
            <button class="b" @click="login()">登 录</button>
            <p class="e" @click="LoginChange()">还没有账号，去注册-></p>
        </div>
        <div class="form" v-show="registerVisible">
            <div class="c">Register</div>
            <input type="text" class="a a2" v-model="form.name" placeholder="用户名">
            <input type="text" class="a a2" v-model="form.phone" placeholder="手机号">
            <input type="password" class="a a2" v-model="form.password" placeholder="密码">
            <input type="password" class="a a2" v-model="pwd" placeholder="确认密码">
            <div class="verify">
                <input type="text" class="a a2" style="width: 180px;" v-model="form.code" placeholder="验证码" maxlength="6">
                <el-button 
                    @click="sendSmsCode"
                    :loading="loading"
                    type="primary"
                    class="codeBtn"
                >
                    {{ buttonText }}
                </el-button>
            </div>
            <button class="b" @click="register()" style="margin-top: 40px;">注 册</button>
            <p class="e" @click="LoginChange()">已有账号，去登录-></p>
        </div>
      </div>
    </div>
</template>
  
<script lang="ts" setup>
    import { ref,computed, reactive, watch, Ref, onMounted } from 'vue';
    import axios from '@/utils/request';
    import { ElMessage } from 'element-plus';
    import { getItemWithExpire, setItemWithExpire } from '@/utils/localStorageUtil';

    //不想改名了，code:短信验证码,verify:图形验证码
    let form = reactive({
        name:"",
        phone:"",
        password:"",
        role: "user",
        code:"",
        verifyCode:"",
        captchaId: "" 
    })

    const countdown = ref(0)
    const isCounting = ref(false)
    const loading = ref(false)
    const timer = ref(null)

    const pwd = ref("")

    // 图形验证码path
    const verify_img: Ref<string> = ref("")
    // 是否显示验证码
    const showCaptcha = ref(false)
    // 登录失败次数
    const loginFailCount = ref(0)

    // 是否显示注册页面
    const registerVisible = ref(false)

    // 管理员状态
    const isAdmin = ref(false)

    // 验证手机格式
    const isPhoneValid = computed(() => {
        const phoneRegex = /^1[3-9]\d{9}$/
        return phoneRegex.test(form.phone)
    })

    //验证码按钮文字
    const buttonText = computed(() => {
        if(!isPhoneValid.value) return '发送验证码'
        if(isCounting.value) return `${countdown.value}秒后重新发送`
        return '发送验证码'
    })

    // 监听角色状态
    watch(isAdmin, (newVal) => {
        form.role = newVal ? "admin" : "user"
    })

    // 管理员状态切换
    function roleChange(){
        isAdmin.value = !isAdmin.value
    }

    // 登录页面、注册页面转换
    function LoginChange(){
        form.name = ""
        form.password = ""
        pwd.value = ""
        form.code = ""
        form.verifyCode = ""
        form.captchaId = ""
        registerVisible.value = !registerVisible.value
    }

    // 发送短信验证码
    const sendSmsCode = async() => {
        if(!isPhoneValid.value) return ElMessage.warning("手机格式不正确")
        if(isCounting.value) return 

        try {
            loading.value = true

            // 使用 await 等待请求完成
            const response = await axios.post("/sms/send", {
                phone: form.phone 
            })
            
            const res = response.data
            console.log(res)
            
            if(res.code == 1){
                startCountdown()
                ElMessage.success('验证码发送成功,5分钟内有效')
            } else {
                ElMessage.error(res.msg)
            }
            
        } catch (error) {
            console.error('发送失败:', error)
            ElMessage.error('发送失败，请重试')
        } finally {
            loading.value = false
        }
    }

    // 短信验证码倒计时
    const startCountdown = () => {
        isCounting.value = true
        countdown.value = 60

        timer.value = setInterval(() => {
            countdown.value--
            if(countdown.value <= 0){
                isCounting.value = false
                countdown.value = 0
                clearInterval(timer.value)
            }
        },1000)
    }

    // 刷新图形登录验证码
    const refreshCaptcha = async () => {
        try {
            const response = await axios.get("/captcha/generate")
            const res = response.data
            if (res.code === 1) {
                verify_img.value = res.data.imgurl
                form.captchaId = res.data.captchaId
                form.verifyCode = ''
            } else {
                ElMessage.error('验证码获取失败')
            }
        } catch (error) {
            console.error('验证码刷新失败:', error)
            ElMessage.error('验证码获取失败')
        }
    }

    // 注册
    function register(){
        form.name = form.name.trim()
        form.password = form.password.trim()
        form.phone = form.phone.trim()

        if(form.name.length == 0){
            ElMessage.error("用户名不为空")
            return
        }

        if(form.name.length > 10){
            ElMessage.error("昵称不得超过10字符")
            return
        }

        if(form.phone.length === 0){
            ElMessage.error("手机号不为空")
            return
        }

        const phoneRegex = /^1[3-9]\d{9}$/
        if(!phoneRegex.test(form.phone)){
            ElMessage.error("手机号格式不正确")
            return
        }

        if(form.password !== pwd.value){
            ElMessage.error("密码不一致")
            return;
        }

        if(form.password.length < 6 || form.password.length > 20){
            ElMessage.error("密码长度6~20位")
            return;
        }

        if(form.code.length == 0){
            ElMessage.error("验证码不能为空")
            return;
        }

        if(form.code.length !== 6){
            ElMessage.error("验证码格式不正确")
            return;
        }

        axios.post("/user/register",form)
            .then(response => {
                var res = response.data
                console.log(res)
                if(res.code == 1){
                    ElMessage.success("注册成功!")
                    registerVisible.value = !registerVisible.value
                }else {
                    ElMessage.error(res.msg)
                }
        })
    }

    // 登录
    function login(){
        form.name = form.name.trim()
        form.password = form.password.trim()
        if(form.name.length == 0 || form.password.length == 0){
            ElMessage.error("用户名或密码不为空")
            return
        }
        // 如果显示验证码，检查验证码
        if (showCaptcha.value) {
            form.verifyCode = form.verifyCode.trim()
            if (!form.verifyCode || form.verifyCode.length !== 5) {
                ElMessage.error("请输入正确的验证码")
                return
            }
        }

        axios.post("/user/login",form)
            .then(response => {
                const res = response.data
                if(res.code == 1){
                    // 登录成功，重置失败次数
                    loginFailCount.value = 0
                    localStorage.removeItem('loginFailCount')

                    // 存储 Token，30天后过期
                    setItemWithExpire('token', res.data.token, 24 * 30);
                    setItemWithExpire('userInfo',res.data.user, 24 * 30);
                    setItemWithExpire('userRole',res.data.user.role, 24 * 30);

                    const role = res.data.user.role;

                    ElMessage.success("登录成功")
                    // 跳转
                    if (role === 'admin') {
                        window.location.href = '/system/user';
                    } else {
                        window.location.href = '/';
                    }
                }else {
                    // 登录失败，增加失败次数
                    loginFailCount.value++
                    setItemWithExpire('loginFailCount', loginFailCount.value.toString(), 24)
                    
                    // 如果失败次数大于3次，显示验证码
                    if (loginFailCount.value > 3) {
                        if(!showCaptcha.value){
                            showCaptcha.value = true
                            refreshCaptcha()
                            ElMessage.warning('登录失败次数过多，请输入验证码')
                            return;
                        }

                        refreshCaptcha()
                    } 
                    if(res.msg !== "验证码错误，请重试"){
                        form.password = ""
                    }

                    ElMessage.error(res.msg)
                }
            })

    }

    onMounted(() => {
        // 从localStorage获取登录失败次数
        const savedCount = getItemWithExpire('loginFailCount')
        if (savedCount) {
            loginFailCount.value = parseInt(savedCount)
            if (loginFailCount.value >= 3) {
                showCaptcha.value = true
                refreshCaptcha()
            }
        }
    })
</script>
  
<style scoped>
    .bg{
        width: 100%;
        height: 100%;
        background-image: url(../../assets/img/bg3.png);
        background-repeat: no-repeat;
        background-size: cover;
        background-position: 0 -30px;
        display: flex;
        justify-content: right;
        align-items: center;
    }
    .box{
        width: 30%;
        height: 100%;
        background-color: #fff;
        margin-right: 160px;
        display: flex;
        align-items: center;
        justify-content: center;
        box-shadow: 5px 5px 20px rgba(0,0,0,.5);
        min-width: 400px;
    }
    .form{
        height: 600px;
        width: 320px;
        display: flex;
        flex-direction: column;
    }
    .c{
        font-size: 50px;
        align-self: center;
        transition: transform 0.3s;
    }
    .c:hover {
        transform: scale(1.05);
    }
    .a{
        height: 40px;
        margin-top: 40px;
        padding: 0px 10px;
        border: 1px solid rgb(170, 170, 170);
        border-left: none;
        border-right: none;
        border-top: none;
        outline: none;
        font-size: 20px;
        background-color: #fff;
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    }
    .a2{
        margin-top: 30px;
    }
    .a:hover {
        border-color: #299ef8;
        box-shadow: 0 2px 4px rgba(41, 158, 248, 0.1);
    }
    .a:focus {
        border-color: #299ef8;
        box-shadow: 0 2px 8px rgba(41, 158, 248, 0.2);
    }
    a{
        display: inline-block;
        align-self: flex-end;
        text-decoration: none;
        color: #2a86cd;
        margin-left: auto;
        position: relative;
        transition: color 0.3s;
    }
    a::after {
        content: '';
        position: absolute;
        bottom: -2px;
        left: 0;
        width: 0;
        height: 1px;
        background: currentColor;
        transition: width 0.3s;
    }
    a:hover {
        color: #ff0565;
    }
    a:hover::after {
        width: 100%;
    }
    button{
        width: 140px;
        height: 40px;
        align-self: center;
        border-radius: 10px;
        border: 0px none ;
        color: #fff;
        font-size: 20px;
        background-color: #299ef8;
        cursor: pointer;
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        position: relative;
        overflow: hidden;
    }
    button:hover {
        background-color: #ff0565;
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(255, 5, 101, 0.3);
    }
    button::after {
        content: '';
        position: absolute;
        top: 50%;
        left: 50%;
        width: 0;
        height: 0;
        background: rgba(255, 255, 255, 0.2);
        border-radius: 50%;
        transform: translate(-50%, -50%);
        transition: width 0.3s, height 0.3s;
    }
    button:hover::after {
        width: 200px;
        height: 200px;
    }
    .e{
        align-self: center;
        font-size: 14px;
        color:#848484;
        margin-top: 10px;
        cursor: pointer;
        position: relative;
        transition: color 0.3s;
    }
    .e::after {
        content: '';
        position: absolute;
        bottom: -2px;
        left: 0;
        width: 0;
        height: 1px;
        background: currentColor;
        transition: width 0.3s;
    }
    .e:hover {
        color: #ff0565;
    }
    .e:hover::after {
        width: 100%;
    }
    .d{
        margin-top: 20px;
        margin-bottom: 100px;
        display: flex;
        justify-content: flex-start;
        align-items: center; 
        gap: 8px;
    }
    .f{
        color:#299ef8;
        cursor: pointer;
        margin-right: auto;
        user-select: none;
        transition: all 0.3s;
        position: relative;
    }
    .f:hover {
        color: #ff0565;
        transform: translateX(4px);
    }
    .f::before {
        position: absolute;
        left: -24px;
        opacity: 0;
        transition: opacity 0.3s;
    }
    .f:hover::before {
        opacity: 1;
    }
    input[type="checkbox"] {
        transition: all 0.3s;
        cursor: pointer;
    }
    input[type="checkbox"]:hover {
        transform: scale(1.1);
        filter: hue-rotate(45deg);
    }

    /* 涟漪动画 */
    @keyframes ripple {
        from {
            transform: scale(1);
            opacity: 1;
        }
        to {
            transform: scale(10);
            opacity: 0;
        }
    }
    .verify{
        display: flex;
    }
    .codeBtn{
        margin-top: 30px;
        font-size: 14px;
    }
    .Captcha{
        width: 140px;
        margin-top: 15px;
        margin-left: 10px;
    }
    .Captcha:hover{
        cursor: pointer;
    }
</style>