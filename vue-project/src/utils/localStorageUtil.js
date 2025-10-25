// 存储数据（带过期时间）
export const setItemWithExpire = (key, value, expireHours) => {
    const expireTime = Date.now() + expireHours * 60 * 60 * 1000;
    localStorage.setItem(key, JSON.stringify({
      value: value,
      expire: expireTime
    }));
  };
  
  // 获取数据（自动检查过期）
  export const getItemWithExpire = (key) => {
    const itemStr = localStorage.getItem(key);
    if (!itemStr) return null;
  
    try {
      const item = JSON.parse(itemStr);
      if (Date.now() > item.expire) {
        localStorage.removeItem(key);
        return null;
      }
      return item.value;
    } catch (e) {
      return null;
    }
  };