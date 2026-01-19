import React from 'react'
import { Navigate } from 'react-router-dom'

const AuthProvider = ({children}) => {
  console.log(children)//함수
  const token = window.localStorage.getItem('accessToken')
  //토큰이 없는거야? - 네
  if(!token){
    return <Navigate to="/" />
  }
  return children
}

export default AuthProvider
