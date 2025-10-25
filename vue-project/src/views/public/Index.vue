<template>
  <!-- <div style="background-color: #f9f7f7;"> -->
  <div class="no-select" style="background-color: #f9f7f7;">
    <Header class="header"></Header>
    <div class="main">
      <div class="m1">
        <el-carousel class="banner" height="300px" motion-blur>
          <el-carousel-item v-for="item in ads" :key="item">
            <el-image style="width: 900px; height: 300px" :src="item" fit="cover" />
          </el-carousel-item>
        </el-carousel>
        <div class="box">
          <div class="box_title">
            <img src="../../assets/img/read_logo.svg" style="margin-right: 4px;width:20px">最近逛的吧
          </div>
          <!-- 如果最近游览为空 -->
          <!-- <el-empty  :image-size="80" description="无记录" v-if="recent_post.length == 0"/> -->
          <div class="box_content">
            <div v-for="item in 8" :key="item">
              <TabBox1></TabBox1>
            </div>
          </div>
        </div>
      </div>
      <div class="m2">
          <div class="m2_title">
            <img src="../../assets/img/hot_logo.svg" style="margin-right: 4px;width: 24px;">热门吧
          </div>
          <div class="m2_contant">
            <div v-for="item in 12" :key="item">
              <TabBox2></TabBox2>
            </div>
          </div>
      </div>
      <div class="m3">
        <div class="b1">

          <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
            <el-tab-pane 
              v-for="type in typeList" 
              :key="type" 
              :label="type" 
              :name="type"
            >
              <IndexBox :type="type"></IndexBox>
            </el-tab-pane>
          </el-tabs>
        </div>
        <!-- <el-affix :offset="80"> -->
          <div class="b2">
            <div class="b2_titile">
              <img src="../../assets/img/rank_logo.svg" style="margin-right: 5px;">热议帖子
            </div>
            <div class="b2_contant">
              <div v-for="r in rank" :key="r.index">
                <div class="rank" @click="toRank(r.post_id)">
                  {{ r.index }}&nbsp&nbsp<p class="rank_contant">{{ r.contant }}</p>
                </div>
              </div>
            </div>
          </div>
        <!-- </el-affix> -->
      </div>
    </div>
    <Footer class="footer"></Footer>
  </div>

  <el-backtop :bottom="100" :right="60">
    <div
      class="up"
      style="
        height: 100%;
        width: 100%;
        background-color: #fff;
        box-shadow: var(--el-box-shadow-lighter);
        text-align: center;
        line-height: 40px;
        color: #aaa;
      "
      
    >
      <el-icon :size="34"><ArrowUp /></el-icon>
    </div>
  </el-backtop>
</template>

<script lang="ts" setup>
  import Header from '@/components/public/Header.vue';
  import TabBox1 from '@/components/public/TabBox1.vue';
  import TabBox2 from '@/components/public/TabBox2.vue';
  import IndexBox from '@/components/public/IndexBox.vue';
  import Footer from '@/components/public/Footer.vue';
  import { onMounted, ref } from 'vue';
  import type { TabsPaneContext } from 'element-plus'
  import TypeBox from '@/components/public/TypeBox.vue';

  // 标签页
  const activeName = ref('热门')
  // 分类
  const typeList = ref(["热门","最近","游戏","动漫","小说","音乐","数码","娱乐","影视","科技"])
  // 广告列表
  const ads = ref(["https://tse3-mm.cn.bing.net/th/id/OIP-C.LeYhnkBLZGu678ndDsOFtwHaEy?w=280&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7","https://tse4-mm.cn.bing.net/th/id/OIP-C._7Ck7OlJluHJHhOXdMDm_QHaC_?w=304&h=141&c=7&r=0&o=5&dpr=1.3&pid=1.7","https://tse4-mm.cn.bing.net/th/id/OIP-C.u6AbTuaewYjBPTYEbYHIUQHaEn?w=310&h=193&c=7&r=0&o=5&dpr=1.3&pid=1.7"])
  // 最近游览的吧
  const recent_post = ref([])
  // 热榜前十
  // {
  //   index:
  //   post_id:
  //   contant:
  // }
  const rank = ref([{index:1,post_id:1,contant:"fufu天下第一可爱,论述fufu的卡哇伊之处"},{index:2,post_id:1,contant:"fufu天下第一可爱"},{index:3,post_id:1,contant:"fufu天下第一可爱"},{index:4,post_id:1,contant:"fufu天下第一可爱"},{index:5,post_id:1,contant:"fufu天下第一可爱"},{index:6,post_id:1,contant:"fufu天下第一可爱"},{index:7,post_id:1,contant:"fufu天下第一可爱"},{index:8,post_id:1,contant:"fufu天下第一可爱"},{index:9,post_id:1,contant:"fufu天下第一可爱"},{index:10,post_id:1,contant:"fufu天下第一可爱"}])

  function load(){
    // 获取广告列表、最近逛的吧、最近热门的吧、最近热议、热榜前十
  }

  // 跳转贴子
  function toRank(id){

  }

  const handleClick = (tab: TabsPaneContext, event: Event) => {
    console.log(tab, event)
  }
  
  onMounted(() => {
    load();
  })
</script>

<style scoped>
  .header{
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    z-index: 1000;   
    background: white;
  }
  .main{
    margin-top: 60px;
    /* width: 1000px; */
    width: 1300px;
    /* background-color: pink; */
    margin: 0 auto;
    display: flex;
    flex-direction: column;
  }
  .m1{
    width: 100%;
    margin-top: 70px;
    height: 300px;
    display: flex;
    flex-direction: row;
  }
  .banner{
    /* width: 700px; */
    width: 900px;
    background-color: #fff;
    border-radius: 10px;
  }
  .banner:hover{
    cursor: pointer;
  }
  .box{
    width: 380px;
    margin-left: 20px;
    background-color: #fff;
    border-radius: 10px;
  }
  .m2{
    margin-top: 20px;
    width: 1280px;
    height: 226px;
    background-color: #fff;
    border-radius: 10px;
    padding: 0px 10px;
  }
  .el-carousel__item h3 {
    color: #475669;
    opacity: 0.75;
    line-height: 240px;
    margin: 0;
    text-align: center;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n + 1) {
    background-color: #d3dce6;
  }
  .ad{
    background-size: cover;
  }
  .box_title{
    height: 40px;
    line-height: 40px;
    margin-left: 16px;
    display: flex;
    flex-direction: row;
    align-items: center;
    font-size: 17px;
    user-select: none;
  }
  .box_content{
    width: 360px;
    height: 260px;
    padding: 0px 10px;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
    align-content:space-around;
  }
  .m2_title{
    height: 40px;
    line-height: 40px;
    display: flex;
    flex-direction: row;
    align-items: center;
    font-size: 17px;
    user-select: none;
  }
  .m2_contant{
    width: 100%;
    height: 180px;
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: space-around;
    align-content: space-around;
  }
  .m3{
    margin-top: 20px;
    width: 1300px;
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    align-items: flex-start;
  }
  .b1{
    width: 960px;
    background-color: #fff;
    border-radius: 10px;
  }
  .b2{
    width: 320px;
    height: 450px;
    /* height: 300px; */
    margin-left: 20px;
    padding-left: 20px;
    background-color: #fff;
    border-radius: 10px;
    display: flex;
    flex-direction: column;

  }
  .b2_titile{
    height: 40px;
    line-height: 40px;
    display: flex;
    flex-direction: row;
    align-items: center;
    font-size: 17px;
    user-select: none;
  }
  .b2_contant{
    height: 400px; 
    display: flex;
    flex-direction: column;
  }
  .rank{
    height: 30px;
    padding: 5px 0px;
    display: flex;
    flex-direction: row;
    align-items: center;
  }
  .rank:hover{
    cursor: pointer;
    color: #e41065;
  }
  .rank_contant{
    width: 240px;
    overflow: hidden;
    text-overflow:ellipsis; 
    white-space:nowrap;
  }
  .demo-tabs{
    padding: 0px 20px;
    margin-top: 10px;
    user-select: none;
  }
  /* 修改标签项基础样式 */
  ::v-deep(.el-tabs__item) {
    font-size: 20px;     /* 字体大小 */
    color: #666;         /* 默认颜色 */
    transition: 0.2s;    /* 过渡效果 */
    /* padding: 0px 16.6px; */
  }

  ::v-deep(.el-tabs__item:last-child) {
    margin-right: 0 !important;
  }


  /* 悬停状态 */
  ::v-deep(.el-tabs__item:hover) {
    color: #e41065 !important;  /* 悬停颜色 */
  }

  /* 激活状态 */
  ::v-deep(.el-tabs__item.is-active) {
    color: #fa438c !important;  /* 激活颜色 */
    font-weight: 600;           /* 加粗 */
  }

  /* 下划线颜色 */
  ::v-deep(.el-tabs__active-bar) {
    background-color: #fa438c !important; 
    height: 2px;
  }

  ::v-deep(.el-tabs__nav-prev),
  ::v-deep(.el-tabs__nav-next) {
    display: none !important;
  }

  ::v-deep(.up) {
    transition: background-color 0.3s;
  }

  ::v-deep(.up:hover) {
    cursor: pointer;
    background-color: #dfdfdf !important;
  }
</style>