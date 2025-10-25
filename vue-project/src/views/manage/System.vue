<template>
    <el-container class="layout-container-demo" style="height: 100%">
      <Sidebar :fold="fold" :is-collapse="isCollapse"></Sidebar>
  
      <el-container direction="vertical">
        <Header :fold="fold" @isCollapse="collapse"></Header>
  
        <el-main>
          <router-view v-slot="{ Component }">
            <component :is="Component" />
          </router-view>
        </el-main>
      </el-container>
    </el-container>
  </template>
  
  <script lang="ts" setup>
    import { computed, ref } from 'vue'
    import { Menu as IconMenu, Message, Setting } from '@element-plus/icons-vue'
    import Sidebar from '@/components/system/Sidebar.vue';
    import Header from '@/components/system/Header.vue';

    var fold = ref(false);
    var isCollapse=computed(()=>{
      return fold.value?'64px':'200px';
    });
    
    
    // 折叠
    function collapse(){
      fold.value=!fold.value
    }

  </script>
  
  <style scoped>
    .layout-container-demo .el-header {
        position: relative;
        background-color: var(--el-color-primary-light-7);
        color: var(--el-text-color-primary);
    }
    .layout-container-demo .el-aside {
        color: var(--el-text-color-primary);
        background: var(--el-color-primary-light-8);
    }
    .layout-container-demo .el-menu {
        border-right: none;
    }
    .layout-container-demo .el-main {
        padding: 0;
    }

  </style>