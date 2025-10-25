<template>
    <div class="bg">
        <div class="main">
            <img class="img" src="../../assets/img/logo1.1.png">
            <div class="text1">404 NotFound</div>
            <div class="text2">页面找不到了QAQ</div>
            <!-- 纯CSS黑白按钮 -->
            <button class="home-btn" @click="toIndex()">
                回首页
            </button>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { useRouter } from 'vue-router'

const router = useRouter()

function toIndex(){
    router.push('/index');
}
</script>

<style scoped>
    .bg{
        width: 100%;
        height: 100%;
        background-color: #ebebeb;
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .main{
        width: 500px;
        height: 500px;
        background-color: #fff;
        border-radius: 20px;
        padding: 20px 20px;
        display: flex;
        flex-direction: column;
        align-items: center;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }
    .img{
        height: 200px;
        margin-top: 30px;
    }
    .text1{
        margin-top: -30px;
        font-size: 50px;
        font-weight: bold;
        color: #333;
    }
    .text2{
        margin-top: 10px;
        font-size: 18px;
        color:#525252
    }
    .home-btn {
        margin-top: 100px;
        padding: 12px 32px;
        font-size: 16px;
        border: 2px solid #333;
        border-radius: 8px;
        background: white;
        color: #333;
        cursor: pointer;
        transition: all 0.3s ease;
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
        font-weight: 500;
    }
    
    .home-btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
        background: #333;
        color: white;
    }
    
    .home-btn:active {
        transform: translateY(0);
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    }
</style>