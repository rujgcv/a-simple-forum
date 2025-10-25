<template>
  <el-header class="header fold" >
        <div class="toolbar" @click="$emit('isCollapse')">
        <el-icon class="icon"  v-show="!fold">
            <Fold />
        </el-icon>
        <el-icon class="icon" v-show="fold">
            <Expand />
        </el-icon>
        </div>
        <el-breadcrumb separator="/" class="breadcrumb">
            <el-breadcrumb-item 
                v-for="item in pathList" 
                :key="item.id"
            >
                {{ item.meta.title }}
            </el-breadcrumb-item>
        </el-breadcrumb>
        <div class="toolbar">
        <el-dropdown>
            <el-icon class="icon">
            <setting />
            </el-icon>
            <template #dropdown>
            <el-dropdown-menu>
                <el-dropdown-item @click="person()">个人信息</el-dropdown-item>
                <el-dropdown-item @click="toIndex()">去首页</el-dropdown-item>
                <el-dropdown-item @click="loginout()">退出</el-dropdown-item>
            </el-dropdown-menu>
            </template>
        </el-dropdown>
        <span style="margin-left: 4px;font-size: 14px;">
            你好，<span style="color:rgb(9, 112, 208);font-weight: 600;">{{username}}</span>
        </span>
        </div>
    </el-header>
</template>

<script lang="ts" setup>
    import { ref,onMounted,watch } from 'vue';
    import axios from '@/utils/request';
    import { ElMessage } from 'element-plus';
    import { useRoute } from 'vue-router'

    defineProps({
        fold: Boolean
    })

    var username = ref("")

    // 面包屑
    const route = useRoute()
    const pathList = ref()

    const getCurrentPath = () => {
    console.log(route.matched);
        pathList.value = route.matched.filter(item => item.meta && item.meta.title);
    }
    watch(route, (to, from) => {
        pathList.value = to.matched.filter(item => item.meta && item.meta.title);
        console.log(to);
        console.log(from);
    }, { immediate: true });


    onMounted(() => {
        axios.get("/user/userinfo").then(response => {
            var res = response.data

            if(res.code != 1){
                ElMessage.error(res.msg)
                return;
            }

            console.log(res)
            username.value = res.data.name
        })
        getCurrentPath();
    })

    // 个人信息
    function person(){

    }

    // 去首页
    function toIndex(){
        window.location.href = '/';
    }

    // 退出登录
    function loginout(){
        localStorage.removeItem("token")
        localStorage.removeItem("userInfo") 
        localStorage.removeItem("userRole") 
        // 跳转到登录页
        window.location.href = '/login';
    }
</script>

<style scoped>
.header{
    height: 50px;
    text-align: right;
    font-size: 12px;
    display: flex;
    flex-wrap: nowrap;
    justify-content: space-between;
}
.fold:hover{
    cursor: pointer;
}
.icon{
    margin-right: 8px;
    margin-top: 1px;
    font-size: 20px;
}
.toolbar {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    height: 100%;
    right: 20px;
}
.breadcrumb {
    margin-left: 10px; /* 与折叠按钮的间距 */
    flex-grow: 1; /* 占据剩余空间 */
    display: flex;
    justify-content:flex-start;
    align-items: center; /* 垂直居中 */
}
:deep(.el-breadcrumb:hover){
    cursor: default;
}
.breadcrumb :deep(.el-breadcrumb__inner) {
    color: #515151;
    font-size: 18px;
    font-weight: 800;
}
/* hover时保持黑色字体 */
.breadcrumb :deep(.el-breadcrumb__item):hover .el-breadcrumb__inner {
    color: #515151;
    font-size: 18px;
    font-weight: 800;
    cursor: default;
}

</style>