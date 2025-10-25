<template>
  <el-aside :width="isCollapse">
        <el-scrollbar>
          <el-menu 
            :default-openeds="['1', '3']"
            :collapse="fold"
            :collapse-transition="false" 
            router
          >
            <div class="logobox">
              <img src="../../assets/img/logo2.0.png" alt="" class="logo">
              <p v-show="!fold">后台管理系统</p>
            </div>
            <el-sub-menu index="1" class="index">
              <template #title>
                  <el-icon><PieChart /></el-icon><p v-show="!fold">数据管理</p>
              </template>
            </el-sub-menu>
            <el-sub-menu index="2">
              <template #title>
                <el-icon><Menu /></el-icon><p v-show="!fold">账号管理</p>
              </template>
                <el-menu-item index="/system/user">用户管理</el-menu-item>
                <el-menu-item index="/system/admin">管理员管理</el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="3">
              <template #title>
                <el-icon><ChatDotRound /></el-icon><p v-show="!fold">内容管理</p>
              </template>
              <el-menu-item index="/system/post">贴子管理</el-menu-item>
              <el-menu-item index="3-2">评论管理</el-menu-item>
              <el-menu-item index="/system/tab">贴吧管理</el-menu-item>
              <el-menu-item index="/system/banner">轮播图管理</el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="4">
              <template #title>
                <el-icon><EditPen /></el-icon><p v-show="!fold">待审核</p>
              </template>
              <el-menu-item index="/system/peding">贴吧新增审核</el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-scrollbar>
      </el-aside>
</template>

<script lang="ts" setup>
import router from '@/router';
import axios from 'axios';
import { onMounted,ref } from 'vue';

    defineProps({
        fold: Boolean,
        isCollapse: String
    })
</script>

<style scoped>

  .logobox{
    height: 56px;
    padding-left: 18px;
    display: flex;
  }
  .logo{
    width: 30px;
    height: 30px;
    padding-top: 12px;
  }
  .logobox p{
    display: inline-block;
    height: 56px;
    margin-left: 10px;
    line-height: 56px;
    font-weight: bold;
  }
  .logobox p:hover{
    cursor:default;
  }
  :deep( .index .el-sub-menu__icon-arrow svg path) {
    display: none;
  }
  .el-menu-item.is-active {
    background-color: #7ac6e4;
    color: white !important;
    /* color: #000 !important; */
    box-shadow: 0 4px 15px rgba(26, 88, 188, 0.3);
  }
  .el-menu-item{
    /* background-color: #f4f4f4; */
  }
</style>