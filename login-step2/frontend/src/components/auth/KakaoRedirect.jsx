import axios from 'axios'
import React, { useEffect } from 'react'

const KakaoRedirect = () => {
  const code = new URL(window.location.href).searchParams.get('code')
  console.log(code)
  useEffect(()=>{
    const kakaoLogin = async() => {
      const response = await axios.post('http://localhost:8000/member/kakao/doLogin',{code: code})
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
