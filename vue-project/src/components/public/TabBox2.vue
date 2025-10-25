<template>
  <div class="box2">
    <el-image class="img" :src="url" fit="cover" />
    <div class="box2_contant">
        <div class="title">fufu</div>
        <div class="item">
            <img src="../../assets/img/people_logo.png" class="icon">
            123
        </div>
        <div class="item">
            <img src="../../assets/img/comment_logo.png" class="icon">
            123
        </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
    import { ref } from 'vue';

    const tabId = ref("")
    const url = ref("")
</script>
    
<style scoped>
    .box2{
        width: 200px;
        height: 72px;
        display: flex;
        flex-direction: row;
        border-radius: 5px;
    }
    .box2:hover{
        cursor: pointer;
    }
    .box2:hover .box2_contant .title{
        color: #e41065;
        animation: bounceText 0.6s cubic-bezier(0.68, -0.55, 0.265, 1.55) both;
    }
    .img{
        width: 72px;
        height: 72px;
        border-radius: 5px;
    }
    .box2_contant{
        width: 120px;
        height: 72px;
        margin-left: 10px;
        display: flex;
        flex-direction: column;
    }
    .title{
        height: 26px;
        font-size: 16px;
        line-height: 26px;
        margin-top: 2px;
        margin-bottom: 4px;
        width: 106px;
        overflow: hidden;
        text-overflow:ellipsis; 
        white-space:nowrap;
    }
    .item{
        height: 15px;
        font-size: 12px;
        line-height: 15px;
        color:rgb(133, 133, 133);
        display: flex;
        flex-direction: row;
        align-content: center;
        margin-bottom: 4px;
    }
    .icon{
        height: 12px;
        margin-right: 2px;
        margin-top: 1px;
    }
    /* 弹跳动画关键帧 */
    @keyframes bounceText {
        0% {
            transform: translateY(0);
        }
        50% {
            transform: translateY(-8px);
        }
        75% {
            transform: translateY(4px);
        }
        100% {
            transform: translateY(0);
        }
    }
</style>