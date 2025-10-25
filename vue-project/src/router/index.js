import { createRouter, createWebHistory } from "vue-router";
import { getItemWithExpire } from '@/utils/localStorageUtil';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/login",
      name: "login",
      component: () => import("../views/public/Login.vue")
    },
    {
      path: '/',
      redirect: "/index"
    },
    {
      path: "/index",
      name: "index",
      component: () => import("../views/public/Index.vue")
    },
    {
      path: "/addTab",
      name: "/addTab",
      component: () => import("../views/public/AddTab.vue")
    },
    {
      path: "/system",
      name: "system",
      component: () => import("../views/manage/System.vue"),
      children: [
        {
          path: "user",
          name: "user",
          component: () => import("../views/manage/User.vue"),
          meta: {
            title: '用户管理'
          }
        },
        {
          path: "admin",
          name: "admin",
          component: () => import("../views/manage/Admin.vue"),
          meta: {
            title: '管理员管理'
          }
        },
        {
          path: "post",
          name: "post",
          component: () => import("../views/manage/Post.vue"),
          meta: {
            title: '贴子管理'
          }
        },
        {
          path: "tab",
          name: "tab",
          component: () => import("../views/manage/Tab.vue"),
          meta: {
            title: '贴吧管理'
          }
        },
        {
          path: "banner",
          name: "banner",
          component: () => import("../views/manage/Banner.vue"),
          meta: {
            title: '轮播图管理'
          }
        },
        {
          path: "peding",
          name: "peding",
          component: () => import("../views/manage/Peding.vue"),
          meta: {
            title: '待审核贴吧'
          }
        }
      ]
    },
    {
      path: "/:pathMatch(.*)*",
      name: "notFound",
      component: () => import("../views/public/NotFound.vue")
    }
  ]
});


router.beforeEach((to, from, next) => {

  // 定义公开路径
  const publicPaths = ["/login"]

  if(publicPaths.includes(to.path)) {
    next();
    return
  } 
  
  const token = getItemWithExpire('token');
  if(!token || token === "null" || token === "" || token === "undefined") {
    next("/login");
    return;
  }

  if(to.path.startsWith("/system")) {
    const localRole = getItemWithExpire('userRole')

    if(localRole === 'admin'){
      next();
    }else {
      ElMessage?.warning('权限不足');
      next("/index")
    }
    return;
  }
  
  next();
});

export default router;
