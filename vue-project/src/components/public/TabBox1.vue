<template>
  <div class="box1">
    <el-image class="img" :src="url" fit="cover" />
    <p class="text">fufu吧</p>
  </div>
</template>

<script lang="ts" setup>
    import { ref } from 'vue';

    const tabId = ref("")
    const url = ref("")
</script>

<style scoped>
    .box1{
        /* width: 80px; */
        /* height: 85px; */
        width: 80px;
        height: 102px;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        align-items: center;
        align-content: center;
    }
    .box1:hover{
        cursor: pointer;
    }
    .box1:hover .text{
        color: #e41065;
    }
    .img{
        width: 72px;
        height: 72px;
        border-radius: 5px;
    }
    .text{
        text-align: center;
        line-height: 20px;
        font-size: 14px;
        width: 72px;
        overflow: hidden;
        text-overflow:ellipsis; 
        white-space:nowrap;
    }
</style>