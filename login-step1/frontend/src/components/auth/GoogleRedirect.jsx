import axios from 'axios'
import React, { useEffect } from 'react'

const GoogleRedirect = () => {
  // 구글에서 보내주는 인가코드 받기
  // 구글에서 5173번 서버로 응답을 보낼 때 ?code=12345678
  const code = new URL(window.location.href).searchParams.get('code')
  console.log(code) //12345678
  useEffect(() => {
    const googleLogin = async() => {
      const response = await axios.post(`${import.meta.env.VITE_SPRING_IP}/member/google/doLogin`,{code: code})
      const token = response.data.token 
      localStorage.setItem("token", token)
      //토큰이 생성되면 홈화면으로 이동

    }
    googleLogin()
  },[])
  return (
    <>
      loading ...
    </>
  )
}

export default GoogleRedirect