import axios from "axios"

export const RefreshTokenDB = async () => {
  const refreshToken = window.localStorage.getItem("refreshToken")
  if(!refreshToken){
    throw new Error('No refresh token')
  }
  const url = `${import.meta.env.VITE_SPRING_IP}auth/refresh`
  try {
    const res = await axios.post(
      url,
      {refreshToken: refreshToken},
      {headers: {'Content-Type': 'application/json'}}
    )
    console.log(res)
    return res
  } catch (error) {
    console.error("refresh token 요청 실패!!!", error)
    throw error 
  }
}