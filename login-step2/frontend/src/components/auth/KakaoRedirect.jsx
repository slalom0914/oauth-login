import axios from 'axios'
import React, { useEffect } from 'react'

const KakaoRedirect = () => {
  const code = new URL(window.location.href).searchParams.get('code')
  console.log(code)
  useEffect(()=>{
    const kakaoLogin = async() => {
      const response = await axios.post(`${import.meta.env.VITE_SPRING_IP}member/kakao/doLogin`,{code: code})
      console.log(response)
    }
    kakaoLogin()
  },[])
  return (
    <>
      loading ...
    </>
  )
}

export default KakaoRedirect
