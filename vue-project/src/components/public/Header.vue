<template>
  <div class="header">
    <div class="a" @click="toHome()">
        <img src="../../assets/img/logo2.0.png" class="logo">
        <span class="logo_text">足迹</span>
    </div>
    <div class="search">
      <input type="text" class="search-main" v-model="searchText" @keydown.enter="search()"/>
      <div class="search-aside" @click="search()">
        <el-icon size="large" style="padding: 5px"><Search /></el-icon>
      </div>
    </div>
    <div style="margin-left: 30px;margin-right: 10px;">
      <el-avatar :icon="UserFilled" v-show="avatar_url == ''"/>
      <el-avatar :src="avatar_url"  v-show="avatar_url != ''"/>
    </div>
    <div class="item">
        <div class="item_icon"><el-icon :size="size"><Message /></el-icon></div>
        <div class="item_text">消息</div>
    </div>
    <div class="item">
        <div class="item_icon"><el-icon :size="size"><Star /></el-icon></div>
        <div class="item_text">收藏</div>
    </div>
    <div class="item">
        <div class="item_icon"><el-icon :size="size"><Clock /></el-icon></div>
        <div class="item_text">历史</div>
    </div>
    <el-dropdown>
        <div class="item">
            <div class="item_icon"><el-icon :size="size"><Setting /></el-icon></div>
            <div class="item_text">设置</div>
        </div>
        <template #dropdown>
        <el-dropdown-menu>
            <el-dropdown-item>个人信息</el-dropdown-item>
            <el-dropdown-item divided @click="loginout()">退出登录</el-dropdown-item>
        </el-dropdown-menu>
        </template>
    </el-dropdown>
    <div class="btn addTab" @click="addTab()" v-show="addShow">
        <el-icon size="16px"><Edit /></el-icon>
        <p style="margin-left: 4px;font-size: 14px;">投稿</p>
    </div>
    <div class="btn toSystem" @click="toSystem()" v-if="role === 'admin'">
        <el-icon size="16px"><Operation /></el-icon>
        <p style="margin-left: 4px;font-size: 14px;">管理端</p>
    </div>
  </div>
</template>

<script lang="ts" setup>
    import router from '@/router';
    import axios from '@/utils/request';
    import { ElMessage } from 'element-plus';
    import { UserFilled } from '@element-plus/icons-vue'
    import { onMounted,ref,defineExpose } from 'vue';
import { getItemWithExpire } from '@/utils/localStorageUtil';

    // 搜索框内容
    const searchText = ref("")
    // 头像路径
    const avatar_url = ref("")
    // 用户id
    const userId = ref("")
    // 用户身份
    const role = ref("")
    // 图标大小
    const size = ref(20)
    // 投稿按钮显示
    let addShow = ref(true)

    // 跳转
    function toHome(){
        router.push("/")
    }

    // 搜索
    function search(){
        
    }

    // 投稿
    function addTab(){
        const routeUrl = router.resolve('/addTab')
        window.open(routeUrl.href,"_blank")
    }

    // 退出登录
    function loginout(){
        localStorage.removeItem("token")
        localStorage.removeItem("userInfo") 
        localStorage.removeItem("userRole") 
        // 跳转到登录页
        window.location.href = '/login';
    }

    // 回后台
    function toSystem(){
        window.location.href = '/system/user';
    }

    onMounted(() => {
        axios.get("/user/userinfo").then(response => {
            var res = response.data

            if(res.code != 1){
                ElMessage.error(res.msg)
                return;
            }

            console.log(res)
            avatar_url.value = res.data.avatarUrl
            role.value = res.data.role
        })
        if(router.currentRoute.value.fullPath == '/addTab'){
            addShow.value = false
        }
    })
</script>

<style scoped>
    .header{
        height: 60px;
        padding-left: 10px;
        display: flex;
        flex-direction: row;
        box-shadow: 0px 5px 5px #dddddd;
        align-items: center; 
    }
    .logo{
        height: 56px;
        padding-left: 20px;
    }
    .a{
        display: flex;
        flex-direction: row;
    }
    .a:hover{
        cursor: pointer;
    }
    .logo_text{
        line-height: 60px;
        margin-left: 10px;
        font-size: 30px;
        font-family: "siYuan Song","SimSun";
    }
    .search {
        width: 400px;
        height: 40px;
        margin-left: 430px;
        background-color: #eae9e9;
        border-radius: 15px;
    }
    .search::after{
        content: '';
        display: block;
        clear: both;
    }
    .search-main {
        margin-left: 10px;
        padding-left: 10px;
        width: 320px;
        height: 40px;
        border: none;
        font-size: 18px;
        color: #4a4a4a;
        background-color: #eae9e9;
        outline: none;
    }
    .search-aside {
        float: right;
        margin-top: 5px;
        margin-right: 10px;
        width: 30px;
        height: 30px;
        border-radius: 9px;
    }
    .search-aside:hover {
        background-color: #c4c2c2;
        cursor: pointer;
    }
    .item{
        height: 40px;
        width: 50px;
        margin-left: 10px;
        display: flex;
        flex-direction: column;
        padding: 10px 0px;
        color: #000;
    }
    .item_icon{
        height: 20px;
        align-self: center;
    }
    .item_text{
        height: 20px;
        align-self: center;
        font-size: 14px;
        line-height: 20px;
    }
    .item:hover{
        cursor: pointer;
    }
    .item:hover .item_icon {
        transform: rotate(360deg);
        color: #e41065;
        transition: transform 0.6s cubic-bezier(0.4, 0, 0.2, 1);
    }
    .item:hover .item_text{
        color: #e41065;
    }
    ::v-deep(.el-dropdown-menu__item:not(.is-disabled):hover) {
        background-color: #f5f5f5 !important; 
        color: #e41065 !important;        
    }
    .addTab{
        margin-left: 30px;
        background-color: #fd5c9c;
        color: #fff;
    }
    .toSystem{
        margin-left: 16px;
        background-color: #615cfd;
        color: #fff;
    }
    .btn {
        width: 80px;
        height: 36px;
        border-radius: 5px;
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .addTab:hover{
        cursor: pointer;
        background-color: #fd5c9ca9;
    }
    .toSystem:hover{
        cursor: pointer;
        background-color: #9379daa9;
    }
</style>