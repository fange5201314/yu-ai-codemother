import { ref } from 'vue'
import { defineStore } from 'pinia'
import { getLoginUser } from '@/api/userController.ts'
import type { LoginUserVO } from '@/api/typings.d.ts'

/**
 * 登录用户信息状态管理
 */
export const useLoginUserStore = defineStore('loginUser', () => {
  // 登录用户信息
  const loginUser = ref<API.LoginUserVO>({
    userName: '未登录',
  })

  /**
   * 获取登录用户信息
   */
  async function fetchLoginUser(): Promise<void> {
    try {
      const res = await getLoginUser()
      if (res.data.code === 0 && res.data.data) {
        loginUser.value = res.data.data
      }
    } catch (error) {
      console.error('获取登录用户信息失败:', error)
    }
  }

  /**
   * 设置登录用户信息
   * @param newLoginUser 新的登录用户信息
   */
  function setLoginUser(newLoginUser: LoginUserVO): void {
    loginUser.value = newLoginUser
  }

  return { loginUser, fetchLoginUser, setLoginUser }
})
